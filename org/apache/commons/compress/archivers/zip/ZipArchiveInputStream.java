package org.apache.commons.compress.archivers.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.UnsupportedZipFeatureException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.InputStreamStatistics;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ZipArchiveInputStream.class */
public class ZipArchiveInputStream extends ArchiveInputStream implements InputStreamStatistics {
    private final ZipEncoding zipEncoding;
    final String encoding;
    private final boolean useUnicodeExtraFields;
    private final InputStream inputStream;
    private final Inflater inf;
    private final ByteBuffer buf;
    private CurrentEntry current;
    private boolean closed;
    private boolean hitCentralDirectory;
    private ByteArrayInputStream lastStoredEntry;
    private final boolean allowStoredEntriesWithDataDescriptor;
    private long uncompressedCount;
    private final boolean skipSplitSig;
    private static final int LFH_LEN = 30;
    private static final int CFH_LEN = 46;
    private static final long TWO_EXP_32 = 4294967296L;
    private final byte[] lfhBuf;
    private final byte[] skipBuf;
    private final byte[] shortBuf;
    private final byte[] wordBuf;
    private final byte[] twoDwordBuf;
    private int entriesRead;
    private static final String USE_ZIPFILE_INSTEAD_OF_STREAM_DISCLAIMER = " while reading a stored entry using data descriptor. Either the archive is broken or it can not be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See http://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile";
    private static final byte[] LFH = ZipLong.LFH_SIG.getBytes();
    private static final byte[] CFH = ZipLong.CFH_SIG.getBytes();
    private static final byte[] DD = ZipLong.DD_SIG.getBytes();
    private static final byte[] APK_SIGNING_BLOCK_MAGIC = {65, 80, 75, 32, 83, 105, 103, 32, 66, 108, 111, 99, 107, 32, 52, 50};
    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    public ZipArchiveInputStream(InputStream inputStream) {
        this(inputStream, "UTF8");
    }

    public ZipArchiveInputStream(InputStream inputStream, String encoding) {
        this(inputStream, encoding, true);
    }

    public ZipArchiveInputStream(InputStream inputStream, String encoding, boolean useUnicodeExtraFields) {
        this(inputStream, encoding, useUnicodeExtraFields, false);
    }

    public ZipArchiveInputStream(InputStream inputStream, String encoding, boolean useUnicodeExtraFields, boolean allowStoredEntriesWithDataDescriptor) {
        this(inputStream, encoding, useUnicodeExtraFields, allowStoredEntriesWithDataDescriptor, false);
    }

    public ZipArchiveInputStream(InputStream inputStream, String encoding, boolean useUnicodeExtraFields, boolean allowStoredEntriesWithDataDescriptor, boolean skipSplitSig) {
        this.inf = new Inflater(true);
        this.buf = ByteBuffer.allocate(512);
        this.lfhBuf = new byte[30];
        this.skipBuf = new byte[1024];
        this.shortBuf = new byte[2];
        this.wordBuf = new byte[4];
        this.twoDwordBuf = new byte[16];
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.useUnicodeExtraFields = useUnicodeExtraFields;
        this.inputStream = new PushbackInputStream(inputStream, this.buf.capacity());
        this.allowStoredEntriesWithDataDescriptor = allowStoredEntriesWithDataDescriptor;
        this.skipSplitSig = skipSplitSig;
        this.buf.limit(0);
    }

    public ZipArchiveEntry getNextZipEntry() throws IOException {
        int off;
        this.uncompressedCount = 0L;
        boolean firstEntry = true;
        if (this.closed || this.hitCentralDirectory) {
            return null;
        }
        if (this.current != null) {
            closeEntry();
            firstEntry = false;
        }
        long currentHeaderOffset = getBytesRead();
        try {
            if (firstEntry) {
                readFirstLocalFileHeader();
            } else {
                readFully(this.lfhBuf);
            }
            ZipLong sig = new ZipLong(this.lfhBuf);
            if (!sig.equals(ZipLong.LFH_SIG)) {
                if (sig.equals(ZipLong.CFH_SIG) || sig.equals(ZipLong.AED_SIG) || isApkSigningBlock(this.lfhBuf)) {
                    this.hitCentralDirectory = true;
                    skipRemainderOfArchive();
                    return null;
                }
                throw new ZipException(String.format("Unexpected record signature: 0x%x", Long.valueOf(sig.getValue())));
            }
            this.current = new CurrentEntry();
            int versionMadeBy = ZipShort.getValue(this.lfhBuf, 4);
            int off2 = 4 + 2;
            this.current.entry.setPlatform((versionMadeBy >> 8) & 15);
            GeneralPurposeBit gpFlag = GeneralPurposeBit.parse(this.lfhBuf, off2);
            boolean hasUTF8Flag = gpFlag.usesUTF8ForNames();
            ZipEncoding entryEncoding = hasUTF8Flag ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
            this.current.hasDataDescriptor = gpFlag.usesDataDescriptor();
            this.current.entry.setGeneralPurposeBit(gpFlag);
            int off3 = off2 + 2;
            this.current.entry.setMethod(ZipShort.getValue(this.lfhBuf, off3));
            int off4 = off3 + 2;
            long time = ZipUtil.dosToJavaTime(ZipLong.getValue(this.lfhBuf, off4));
            this.current.entry.setTime(time);
            int off5 = off4 + 4;
            ZipLong size = null;
            ZipLong cSize = null;
            if (this.current.hasDataDescriptor) {
                off = off5 + 12;
            } else {
                this.current.entry.setCrc(ZipLong.getValue(this.lfhBuf, off5));
                int off6 = off5 + 4;
                cSize = new ZipLong(this.lfhBuf, off6);
                int off7 = off6 + 4;
                size = new ZipLong(this.lfhBuf, off7);
                off = off7 + 4;
            }
            int fileNameLen = ZipShort.getValue(this.lfhBuf, off);
            int off8 = off + 2;
            int extraLen = ZipShort.getValue(this.lfhBuf, off8);
            int i = off8 + 2;
            byte[] fileName = readRange(fileNameLen);
            this.current.entry.setName(entryEncoding.decode(fileName), fileName);
            if (hasUTF8Flag) {
                this.current.entry.setNameSource(ZipArchiveEntry.NameSource.NAME_WITH_EFS_FLAG);
            }
            byte[] extraData = readRange(extraLen);
            try {
                this.current.entry.setExtra(extraData);
                if (!hasUTF8Flag && this.useUnicodeExtraFields) {
                    ZipUtil.setNameAndCommentFromExtraFields(this.current.entry, fileName, null);
                }
                processZip64Extra(size, cSize);
                this.current.entry.setLocalHeaderOffset(currentHeaderOffset);
                this.current.entry.setDataOffset(getBytesRead());
                this.current.entry.setStreamContiguous(true);
                ZipMethod m = ZipMethod.getMethodByCode(this.current.entry.getMethod());
                if (this.current.entry.getCompressedSize() == -1) {
                    if (m == ZipMethod.ENHANCED_DEFLATED) {
                        this.current.inputStream = new Deflate64CompressorInputStream(this.inputStream);
                    }
                } else if (ZipUtil.canHandleEntryData(this.current.entry) && m != ZipMethod.STORED && m != ZipMethod.DEFLATED) {
                    InputStream bis = new BoundedInputStream(this.inputStream, this.current.entry.getCompressedSize());
                    switch (m) {
                        case UNSHRINKING:
                            this.current.inputStream = new UnshrinkingInputStream(bis);
                            break;
                        case IMPLODING:
                            try {
                                this.current.inputStream = new ExplodingInputStream(this.current.entry.getGeneralPurposeBit().getSlidingDictionarySize(), this.current.entry.getGeneralPurposeBit().getNumberOfShannonFanoTrees(), bis);
                                break;
                            } catch (IllegalArgumentException ex) {
                                throw new IOException("bad IMPLODE data", ex);
                            }
                        case BZIP2:
                            this.current.inputStream = new BZip2CompressorInputStream(bis);
                            break;
                        case ENHANCED_DEFLATED:
                            this.current.inputStream = new Deflate64CompressorInputStream(bis);
                            break;
                    }
                }
                this.entriesRead++;
                return this.current.entry;
            } catch (RuntimeException ex2) {
                ZipException z = new ZipException("Invalid extra data in entry " + this.current.entry.getName());
                z.initCause(ex2);
                throw z;
            }
        } catch (EOFException e) {
            return null;
        }
    }

    private void readFirstLocalFileHeader() throws IOException {
        readFully(this.lfhBuf);
        ZipLong sig = new ZipLong(this.lfhBuf);
        if (!this.skipSplitSig && sig.equals(ZipLong.DD_SIG)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.SPLITTING);
        }
        if (sig.equals(ZipLong.SINGLE_SEGMENT_SPLIT_MARKER) || sig.equals(ZipLong.DD_SIG)) {
            byte[] missedLfhBytes = new byte[4];
            readFully(missedLfhBytes);
            System.arraycopy(this.lfhBuf, 4, this.lfhBuf, 0, 26);
            System.arraycopy(missedLfhBytes, 0, this.lfhBuf, 26, 4);
        }
    }

    private void processZip64Extra(ZipLong size, ZipLong cSize) throws ZipException {
        ZipExtraField extra = this.current.entry.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        if (extra != null && !(extra instanceof Zip64ExtendedInformationExtraField)) {
            throw new ZipException("archive contains unparseable zip64 extra field");
        }
        Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField) extra;
        this.current.usesZip64 = z64 != null;
        if (!this.current.hasDataDescriptor) {
            if (z64 == null || !(ZipLong.ZIP64_MAGIC.equals(cSize) || ZipLong.ZIP64_MAGIC.equals(size))) {
                if (cSize != null && size != null) {
                    if (cSize.getValue() < 0) {
                        throw new ZipException("broken archive, entry with negative compressed size");
                    }
                    this.current.entry.setCompressedSize(cSize.getValue());
                    if (size.getValue() < 0) {
                        throw new ZipException("broken archive, entry with negative size");
                    }
                    this.current.entry.setSize(size.getValue());
                }
            } else if (z64.getCompressedSize() == null || z64.getSize() == null) {
                throw new ZipException("archive contains corrupted zip64 extra field");
            } else {
                long s = z64.getCompressedSize().getLongValue();
                if (s < 0) {
                    throw new ZipException("broken archive, entry with negative compressed size");
                }
                this.current.entry.setCompressedSize(s);
                long s2 = z64.getSize().getLongValue();
                if (s2 < 0) {
                    throw new ZipException("broken archive, entry with negative size");
                }
                this.current.entry.setSize(s2);
            }
        }
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public ArchiveEntry getNextEntry() throws IOException {
        return getNextZipEntry();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public boolean canReadEntryData(ArchiveEntry ae) {
        if (ae instanceof ZipArchiveEntry) {
            ZipArchiveEntry ze = (ZipArchiveEntry) ae;
            return ZipUtil.canHandleEntryData(ze) && supportsDataDescriptorFor(ze) && supportsCompressedSizeFor(ze);
        }
        return false;
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int read;
        if (length == 0) {
            return 0;
        }
        if (this.closed) {
            throw new IOException("The stream is closed");
        }
        if (this.current == null) {
            return -1;
        }
        if (offset > buffer.length || length < 0 || offset < 0 || buffer.length - offset < length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        ZipUtil.checkRequestedFeatures(this.current.entry);
        if (!supportsDataDescriptorFor(this.current.entry)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.DATA_DESCRIPTOR, this.current.entry);
        }
        if (!supportsCompressedSizeFor(this.current.entry)) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.UNKNOWN_COMPRESSED_SIZE, this.current.entry);
        }
        if (this.current.entry.getMethod() == 0) {
            read = readStored(buffer, offset, length);
        } else if (this.current.entry.getMethod() == 8) {
            read = readDeflated(buffer, offset, length);
        } else if (this.current.entry.getMethod() != ZipMethod.UNSHRINKING.getCode() && this.current.entry.getMethod() != ZipMethod.IMPLODING.getCode() && this.current.entry.getMethod() != ZipMethod.ENHANCED_DEFLATED.getCode() && this.current.entry.getMethod() != ZipMethod.BZIP2.getCode()) {
            throw new UnsupportedZipFeatureException(ZipMethod.getMethodByCode(this.current.entry.getMethod()), this.current.entry);
        } else {
            read = this.current.inputStream.read(buffer, offset, length);
        }
        if (read >= 0) {
            this.current.crc.update(buffer, offset, read);
            this.uncompressedCount += read;
        }
        return read;
    }

    @Override // org.apache.commons.compress.utils.InputStreamStatistics
    public long getCompressedCount() {
        int method = this.current.entry.getMethod();
        if (method != 0) {
            if (method == 8) {
                return getBytesInflated();
            }
            if (method != ZipMethod.UNSHRINKING.getCode() && method != ZipMethod.IMPLODING.getCode() && method != ZipMethod.ENHANCED_DEFLATED.getCode() && method != ZipMethod.BZIP2.getCode()) {
                return -1L;
            }
            return ((InputStreamStatistics) this.current.checkInputStream()).getCompressedCount();
        }
        return this.current.bytesRead;
    }

    @Override // org.apache.commons.compress.utils.InputStreamStatistics
    public long getUncompressedCount() {
        return this.uncompressedCount;
    }

    private int readStored(byte[] buffer, int offset, int length) throws IOException {
        if (this.current.hasDataDescriptor) {
            if (this.lastStoredEntry == null) {
                readStoredEntry();
            }
            return this.lastStoredEntry.read(buffer, offset, length);
        }
        long csize = this.current.entry.getSize();
        if (this.current.bytesRead >= csize) {
            return -1;
        }
        if (this.buf.position() >= this.buf.limit()) {
            this.buf.position(0);
            int l = this.inputStream.read(this.buf.array());
            if (l == -1) {
                this.buf.limit(0);
                throw new IOException("Truncated ZIP file");
            }
            this.buf.limit(l);
            count(l);
            this.current.bytesReadFromStream += l;
        }
        int toRead = Math.min(this.buf.remaining(), length);
        if (csize - this.current.bytesRead < toRead) {
            toRead = (int) (csize - this.current.bytesRead);
        }
        this.buf.get(buffer, offset, toRead);
        this.current.bytesRead += toRead;
        return toRead;
    }

    private int readDeflated(byte[] buffer, int offset, int length) throws IOException {
        int read = readFromInflater(buffer, offset, length);
        if (read <= 0) {
            if (this.inf.finished()) {
                return -1;
            }
            if (this.inf.needsDictionary()) {
                throw new ZipException("This archive needs a preset dictionary which is not supported by Commons Compress.");
            }
            if (read == -1) {
                throw new IOException("Truncated ZIP file");
            }
        }
        return read;
    }

    private int readFromInflater(byte[] buffer, int offset, int length) throws IOException {
        int read = 0;
        while (true) {
            if (this.inf.needsInput()) {
                int l = fill();
                if (l > 0) {
                    this.current.bytesReadFromStream += this.buf.limit();
                } else if (l == -1) {
                    return -1;
                }
            }
            try {
                read = this.inf.inflate(buffer, offset, length);
                if (read == 0) {
                    if (!this.inf.needsInput()) {
                        break;
                    }
                } else {
                    break;
                }
            } catch (DataFormatException e) {
                throw ((IOException) new ZipException(e.getMessage()).initCause(e));
            }
        }
        return read;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            try {
                this.inputStream.close();
            } finally {
                this.inf.end();
            }
        }
    }

    @Override // java.io.InputStream
    public long skip(long value) throws IOException {
        if (value >= 0) {
            long j = 0;
            while (true) {
                long skipped = j;
                if (skipped < value) {
                    long rem = value - skipped;
                    int x = read(this.skipBuf, 0, (int) (((long) this.skipBuf.length) > rem ? rem : this.skipBuf.length));
                    if (x == -1) {
                        return skipped;
                    }
                    j = skipped + x;
                } else {
                    return skipped;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean matches(byte[] signature, int length) {
        if (length < ZipArchiveOutputStream.LFH_SIG.length) {
            return false;
        }
        return checksig(signature, ZipArchiveOutputStream.LFH_SIG) || checksig(signature, ZipArchiveOutputStream.EOCD_SIG) || checksig(signature, ZipArchiveOutputStream.DD_SIG) || checksig(signature, ZipLong.SINGLE_SEGMENT_SPLIT_MARKER.getBytes());
    }

    private static boolean checksig(byte[] signature, byte[] expected) {
        for (int i = 0; i < expected.length; i++) {
            if (signature[i] != expected[i]) {
                return false;
            }
        }
        return true;
    }

    private void closeEntry() throws IOException {
        long j;
        if (this.closed) {
            throw new IOException("The stream is closed");
        }
        if (this.current == null) {
            return;
        }
        if (currentEntryHasOutstandingBytes()) {
            drainCurrentEntryData();
        } else {
            skip(Long.MAX_VALUE);
            if (this.current.entry.getMethod() == 8) {
                j = getBytesInflated();
            } else {
                j = this.current.bytesRead;
            }
            long inB = j;
            int diff = (int) (this.current.bytesReadFromStream - inB);
            if (diff > 0) {
                pushback(this.buf.array(), this.buf.limit() - diff, diff);
                this.current.bytesReadFromStream -= diff;
            }
            if (currentEntryHasOutstandingBytes()) {
                drainCurrentEntryData();
            }
        }
        if (this.lastStoredEntry == null && this.current.hasDataDescriptor) {
            readDataDescriptor();
        }
        this.inf.reset();
        this.buf.clear().flip();
        this.current = null;
        this.lastStoredEntry = null;
    }

    private boolean currentEntryHasOutstandingBytes() {
        return this.current.bytesReadFromStream <= this.current.entry.getCompressedSize() && !this.current.hasDataDescriptor;
    }

    private void drainCurrentEntryData() throws IOException {
        long compressedSize = this.current.entry.getCompressedSize();
        long j = this.current.bytesReadFromStream;
        while (true) {
            long remaining = compressedSize - j;
            if (remaining > 0) {
                long n = this.inputStream.read(this.buf.array(), 0, (int) Math.min(this.buf.capacity(), remaining));
                if (n < 0) {
                    throw new EOFException("Truncated ZIP entry: " + ArchiveUtils.sanitize(this.current.entry.getName()));
                }
                count(n);
                compressedSize = remaining;
                j = n;
            } else {
                return;
            }
        }
    }

    private long getBytesInflated() {
        long inB = this.inf.getBytesRead();
        if (this.current.bytesReadFromStream >= TWO_EXP_32) {
            while (inB + TWO_EXP_32 <= this.current.bytesReadFromStream) {
                inB += TWO_EXP_32;
            }
        }
        return inB;
    }

    private int fill() throws IOException {
        if (this.closed) {
            throw new IOException("The stream is closed");
        }
        int length = this.inputStream.read(this.buf.array());
        if (length > 0) {
            this.buf.limit(length);
            count(this.buf.limit());
            this.inf.setInput(this.buf.array(), 0, this.buf.limit());
        }
        return length;
    }

    private void readFully(byte[] b) throws IOException {
        readFully(b, 0);
    }

    private void readFully(byte[] b, int off) throws IOException {
        int len = b.length - off;
        int count = IOUtils.readFully(this.inputStream, b, off, len);
        count(count);
        if (count < len) {
            throw new EOFException();
        }
    }

    private byte[] readRange(int len) throws IOException {
        byte[] ret = IOUtils.readRange(this.inputStream, len);
        count(ret.length);
        if (ret.length < len) {
            throw new EOFException();
        }
        return ret;
    }

    private void readDataDescriptor() throws IOException {
        readFully(this.wordBuf);
        ZipLong val = new ZipLong(this.wordBuf);
        if (ZipLong.DD_SIG.equals(val)) {
            readFully(this.wordBuf);
            val = new ZipLong(this.wordBuf);
        }
        this.current.entry.setCrc(val.getValue());
        readFully(this.twoDwordBuf);
        ZipLong potentialSig = new ZipLong(this.twoDwordBuf, 8);
        if (potentialSig.equals(ZipLong.CFH_SIG) || potentialSig.equals(ZipLong.LFH_SIG)) {
            pushback(this.twoDwordBuf, 8, 8);
            long size = ZipLong.getValue(this.twoDwordBuf);
            if (size < 0) {
                throw new ZipException("broken archive, entry with negative compressed size");
            }
            this.current.entry.setCompressedSize(size);
            long size2 = ZipLong.getValue(this.twoDwordBuf, 4);
            if (size2 < 0) {
                throw new ZipException("broken archive, entry with negative size");
            }
            this.current.entry.setSize(size2);
            return;
        }
        long size3 = ZipEightByteInteger.getLongValue(this.twoDwordBuf);
        if (size3 < 0) {
            throw new ZipException("broken archive, entry with negative compressed size");
        }
        this.current.entry.setCompressedSize(size3);
        long size4 = ZipEightByteInteger.getLongValue(this.twoDwordBuf, 8);
        if (size4 < 0) {
            throw new ZipException("broken archive, entry with negative size");
        }
        this.current.entry.setSize(size4);
    }

    private boolean supportsDataDescriptorFor(ZipArchiveEntry entry) {
        return !entry.getGeneralPurposeBit().usesDataDescriptor() || (this.allowStoredEntriesWithDataDescriptor && entry.getMethod() == 0) || entry.getMethod() == 8 || entry.getMethod() == ZipMethod.ENHANCED_DEFLATED.getCode();
    }

    private boolean supportsCompressedSizeFor(ZipArchiveEntry entry) {
        return entry.getCompressedSize() != -1 || entry.getMethod() == 8 || entry.getMethod() == ZipMethod.ENHANCED_DEFLATED.getCode() || (entry.getGeneralPurposeBit().usesDataDescriptor() && this.allowStoredEntriesWithDataDescriptor && entry.getMethod() == 0);
    }

    private void readStoredEntry() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int off = 0;
        boolean done = false;
        int ddLen = this.current.usesZip64 ? 20 : 12;
        while (!done) {
            int r = this.inputStream.read(this.buf.array(), off, 512 - off);
            if (r <= 0) {
                throw new IOException("Truncated ZIP file");
            }
            if (r + off < 4) {
                off += r;
            } else {
                done = bufferContainsSignature(bos, off, r, ddLen);
                if (!done) {
                    off = cacheBytesRead(bos, off, r, ddLen);
                }
            }
        }
        if (this.current.entry.getCompressedSize() != this.current.entry.getSize()) {
            throw new ZipException("compressed and uncompressed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it can not be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See http://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile");
        }
        byte[] b = bos.toByteArray();
        if (b.length != this.current.entry.getSize()) {
            throw new ZipException("actual and claimed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it can not be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See http://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile");
        }
        this.lastStoredEntry = new ByteArrayInputStream(b);
    }

    private boolean bufferContainsSignature(ByteArrayOutputStream bos, int offset, int lastRead, int expectedDDLen) throws IOException {
        boolean done = false;
        for (int i = 0; !done && i < (offset + lastRead) - 4; i++) {
            if (this.buf.array()[i] == LFH[0] && this.buf.array()[i + 1] == LFH[1]) {
                int expectDDPos = i;
                if ((i >= expectedDDLen && this.buf.array()[i + 2] == LFH[2] && this.buf.array()[i + 3] == LFH[3]) || (this.buf.array()[i + 2] == CFH[2] && this.buf.array()[i + 3] == CFH[3])) {
                    expectDDPos = i - expectedDDLen;
                    done = true;
                } else if (this.buf.array()[i + 2] == DD[2] && this.buf.array()[i + 3] == DD[3]) {
                    done = true;
                }
                if (done) {
                    pushback(this.buf.array(), expectDDPos, (offset + lastRead) - expectDDPos);
                    bos.write(this.buf.array(), 0, expectDDPos);
                    readDataDescriptor();
                }
            }
        }
        return done;
    }

    private int cacheBytesRead(ByteArrayOutputStream bos, int offset, int lastRead, int expecteDDLen) {
        int offset2;
        int cacheable = ((offset + lastRead) - expecteDDLen) - 3;
        if (cacheable > 0) {
            bos.write(this.buf.array(), 0, cacheable);
            System.arraycopy(this.buf.array(), cacheable, this.buf.array(), 0, expecteDDLen + 3);
            offset2 = expecteDDLen + 3;
        } else {
            offset2 = offset + lastRead;
        }
        return offset2;
    }

    private void pushback(byte[] buf, int offset, int length) throws IOException {
        ((PushbackInputStream) this.inputStream).unread(buf, offset, length);
        pushedBackBytes(length);
    }

    private void skipRemainderOfArchive() throws IOException {
        if (this.entriesRead > 0) {
            realSkip((this.entriesRead * 46) - 30);
            boolean foundEocd = findEocdRecord();
            if (foundEocd) {
                realSkip(16L);
                readFully(this.shortBuf);
                int commentLen = ZipShort.getValue(this.shortBuf);
                if (commentLen >= 0) {
                    realSkip(commentLen);
                    return;
                }
            }
        }
        throw new IOException("Truncated ZIP file");
    }

    private boolean findEocdRecord() throws IOException {
        int currentByte = -1;
        boolean skipReadCall = false;
        while (true) {
            if (!skipReadCall) {
                int readOneByte = readOneByte();
                currentByte = readOneByte;
                if (readOneByte <= -1) {
                    return false;
                }
            }
            skipReadCall = false;
            if (isFirstByteOfEocdSig(currentByte)) {
                currentByte = readOneByte();
                if (currentByte != ZipArchiveOutputStream.EOCD_SIG[1]) {
                    if (currentByte != -1) {
                        skipReadCall = isFirstByteOfEocdSig(currentByte);
                    } else {
                        return false;
                    }
                } else {
                    currentByte = readOneByte();
                    if (currentByte != ZipArchiveOutputStream.EOCD_SIG[2]) {
                        if (currentByte != -1) {
                            skipReadCall = isFirstByteOfEocdSig(currentByte);
                        } else {
                            return false;
                        }
                    } else {
                        currentByte = readOneByte();
                        if (currentByte != -1) {
                            if (currentByte == ZipArchiveOutputStream.EOCD_SIG[3]) {
                                return true;
                            }
                            skipReadCall = isFirstByteOfEocdSig(currentByte);
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }

    private void realSkip(long value) throws IOException {
        if (value >= 0) {
            long j = 0;
            while (true) {
                long skipped = j;
                if (skipped < value) {
                    long rem = value - skipped;
                    int x = this.inputStream.read(this.skipBuf, 0, (int) (((long) this.skipBuf.length) > rem ? rem : this.skipBuf.length));
                    if (x == -1) {
                        return;
                    }
                    count(x);
                    j = skipped + x;
                } else {
                    return;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private int readOneByte() throws IOException {
        int b = this.inputStream.read();
        if (b != -1) {
            count(1);
        }
        return b;
    }

    private boolean isFirstByteOfEocdSig(int b) {
        return b == ZipArchiveOutputStream.EOCD_SIG[0];
    }

    private boolean isApkSigningBlock(byte[] suspectLocalFileHeader) throws IOException {
        BigInteger len = ZipEightByteInteger.getValue(suspectLocalFileHeader);
        BigInteger toSkip = len.add(BigInteger.valueOf((8 - suspectLocalFileHeader.length) - APK_SIGNING_BLOCK_MAGIC.length));
        byte[] magic = new byte[APK_SIGNING_BLOCK_MAGIC.length];
        try {
            if (toSkip.signum() < 0) {
                int off = suspectLocalFileHeader.length + toSkip.intValue();
                if (off < 8) {
                    return false;
                }
                int bytesInBuffer = Math.abs(toSkip.intValue());
                System.arraycopy(suspectLocalFileHeader, off, magic, 0, Math.min(bytesInBuffer, magic.length));
                if (bytesInBuffer < magic.length) {
                    readFully(magic, bytesInBuffer);
                }
            } else {
                while (toSkip.compareTo(LONG_MAX) > 0) {
                    realSkip(Long.MAX_VALUE);
                    toSkip = toSkip.add(LONG_MAX.negate());
                }
                realSkip(toSkip.longValue());
                readFully(magic);
            }
            return Arrays.equals(magic, APK_SIGNING_BLOCK_MAGIC);
        } catch (EOFException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ZipArchiveInputStream$CurrentEntry.class */
    public static final class CurrentEntry {
        private final ZipArchiveEntry entry;
        private boolean hasDataDescriptor;
        private boolean usesZip64;
        private long bytesRead;
        private long bytesReadFromStream;
        private final CRC32 crc;
        private InputStream inputStream;

        private CurrentEntry() {
            this.entry = new ZipArchiveEntry();
            this.crc = new CRC32();
        }

        static /* synthetic */ long access$808(CurrentEntry x0) {
            long j = x0.bytesReadFromStream;
            x0.bytesReadFromStream = j + 1;
            return j;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public <T extends InputStream> T checkInputStream() {
            return (T) Objects.requireNonNull(this.inputStream, "inputStream");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ZipArchiveInputStream$BoundedInputStream.class */
    public class BoundedInputStream extends InputStream {
        private final InputStream in;
        private final long max;
        private long pos;

        public BoundedInputStream(InputStream in, long size) {
            this.max = size;
            this.in = in;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.max >= 0 && this.pos >= this.max) {
                return -1;
            }
            int result = this.in.read();
            this.pos++;
            ZipArchiveInputStream.this.count(1);
            CurrentEntry.access$808(ZipArchiveInputStream.this.current);
            return result;
        }

        @Override // java.io.InputStream
        public int read(byte[] b) throws IOException {
            return read(b, 0, b.length);
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            if (len == 0) {
                return 0;
            }
            if (this.max >= 0 && this.pos >= this.max) {
                return -1;
            }
            long maxRead = this.max >= 0 ? Math.min(len, this.max - this.pos) : len;
            int bytesRead = this.in.read(b, off, (int) maxRead);
            if (bytesRead == -1) {
                return -1;
            }
            this.pos += bytesRead;
            ZipArchiveInputStream.this.count(bytesRead);
            ZipArchiveInputStream.this.current.bytesReadFromStream += bytesRead;
            return bytesRead;
        }

        @Override // java.io.InputStream
        public long skip(long n) throws IOException {
            long toSkip = this.max >= 0 ? Math.min(n, this.max - this.pos) : n;
            long skippedBytes = IOUtils.skip(this.in, toSkip);
            this.pos += skippedBytes;
            return skippedBytes;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            if (this.max >= 0 && this.pos >= this.max) {
                return 0;
            }
            return this.in.available();
        }
    }
}
