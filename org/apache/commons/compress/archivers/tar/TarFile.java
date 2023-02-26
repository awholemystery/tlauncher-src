package org.apache.commons.compress.archivers.tar;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.BoundedArchiveInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.BoundedSeekableByteChannelInputStream;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/tar/TarFile.class */
public class TarFile implements Closeable {
    private static final int SMALL_BUFFER_SIZE = 256;
    private final byte[] smallBuf;
    private final SeekableByteChannel archive;
    private final ZipEncoding zipEncoding;
    private final LinkedList<TarArchiveEntry> entries;
    private final int blockSize;
    private final boolean lenient;
    private final int recordSize;
    private final ByteBuffer recordBuffer;
    private final List<TarArchiveStructSparse> globalSparseHeaders;
    private boolean hasHitEOF;
    private TarArchiveEntry currEntry;
    private Map<String, String> globalPaxHeaders;
    private final Map<String, List<InputStream>> sparseInputStreams;

    public TarFile(byte[] content) throws IOException {
        this(new SeekableInMemoryByteChannel(content));
    }

    public TarFile(byte[] content, String encoding) throws IOException {
        this(new SeekableInMemoryByteChannel(content), TarConstants.DEFAULT_BLKSIZE, 512, encoding, false);
    }

    public TarFile(byte[] content, boolean lenient) throws IOException {
        this(new SeekableInMemoryByteChannel(content), TarConstants.DEFAULT_BLKSIZE, 512, null, lenient);
    }

    public TarFile(File archive) throws IOException {
        this(archive.toPath());
    }

    public TarFile(File archive, String encoding) throws IOException {
        this(archive.toPath(), encoding);
    }

    public TarFile(File archive, boolean lenient) throws IOException {
        this(archive.toPath(), lenient);
    }

    public TarFile(Path archivePath) throws IOException {
        this(Files.newByteChannel(archivePath, new OpenOption[0]), TarConstants.DEFAULT_BLKSIZE, 512, null, false);
    }

    public TarFile(Path archivePath, String encoding) throws IOException {
        this(Files.newByteChannel(archivePath, new OpenOption[0]), TarConstants.DEFAULT_BLKSIZE, 512, encoding, false);
    }

    public TarFile(Path archivePath, boolean lenient) throws IOException {
        this(Files.newByteChannel(archivePath, new OpenOption[0]), TarConstants.DEFAULT_BLKSIZE, 512, null, lenient);
    }

    public TarFile(SeekableByteChannel content) throws IOException {
        this(content, TarConstants.DEFAULT_BLKSIZE, 512, null, false);
    }

    public TarFile(SeekableByteChannel archive, int blockSize, int recordSize, String encoding, boolean lenient) throws IOException {
        this.smallBuf = new byte[256];
        this.entries = new LinkedList<>();
        this.globalSparseHeaders = new ArrayList();
        this.globalPaxHeaders = new HashMap();
        this.sparseInputStreams = new HashMap();
        this.archive = archive;
        this.hasHitEOF = false;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.recordSize = recordSize;
        this.recordBuffer = ByteBuffer.allocate(this.recordSize);
        this.blockSize = blockSize;
        this.lenient = lenient;
        while (true) {
            TarArchiveEntry entry = getNextTarEntry();
            if (entry != null) {
                this.entries.add(entry);
            } else {
                return;
            }
        }
    }

    private TarArchiveEntry getNextTarEntry() throws IOException {
        if (isAtEOF()) {
            return null;
        }
        if (this.currEntry != null) {
            repositionForwardTo(this.currEntry.getDataOffset() + this.currEntry.getSize());
            throwExceptionIfPositionIsNotInArchive();
            skipRecordPadding();
        }
        ByteBuffer headerBuf = getRecord();
        if (null == headerBuf) {
            this.currEntry = null;
            return null;
        }
        try {
            long position = this.archive.position();
            this.currEntry = new TarArchiveEntry(this.globalPaxHeaders, headerBuf.array(), this.zipEncoding, this.lenient, position);
            if (this.currEntry.isGNULongLinkEntry()) {
                byte[] longLinkData = getLongNameData();
                if (longLinkData == null) {
                    return null;
                }
                this.currEntry.setLinkName(this.zipEncoding.decode(longLinkData));
            }
            if (this.currEntry.isGNULongNameEntry()) {
                byte[] longNameData = getLongNameData();
                if (longNameData == null) {
                    return null;
                }
                String name = this.zipEncoding.decode(longNameData);
                this.currEntry.setName(name);
                if (this.currEntry.isDirectory() && !name.endsWith("/")) {
                    this.currEntry.setName(name + "/");
                }
            }
            if (this.currEntry.isGlobalPaxHeader()) {
                readGlobalPaxHeaders();
            }
            try {
                if (this.currEntry.isPaxHeader()) {
                    paxHeaders();
                } else if (!this.globalPaxHeaders.isEmpty()) {
                    applyPaxHeadersToCurrentEntry(this.globalPaxHeaders, this.globalSparseHeaders);
                }
                if (this.currEntry.isOldGNUSparse()) {
                    readOldGNUSparse();
                }
                return this.currEntry;
            } catch (NumberFormatException e) {
                throw new IOException("Error detected parsing the pax header", e);
            }
        } catch (IllegalArgumentException e2) {
            throw new IOException("Error detected parsing the header", e2);
        }
    }

    private void readOldGNUSparse() throws IOException {
        TarArchiveSparseEntry entry;
        if (this.currEntry.isExtended()) {
            do {
                ByteBuffer headerBuf = getRecord();
                if (headerBuf == null) {
                    throw new IOException("premature end of tar archive. Didn't find extended_header after header with extended flag.");
                }
                entry = new TarArchiveSparseEntry(headerBuf.array());
                this.currEntry.getSparseHeaders().addAll(entry.getSparseHeaders());
                this.currEntry.setDataOffset(this.currEntry.getDataOffset() + this.recordSize);
            } while (entry.isExtended());
            buildSparseInputStreams();
        }
        buildSparseInputStreams();
    }

    private void buildSparseInputStreams() throws IOException {
        List<InputStream> streams = new ArrayList<>();
        List<TarArchiveStructSparse> sparseHeaders = this.currEntry.getOrderedSparseHeaders();
        InputStream zeroInputStream = new TarArchiveSparseZeroInputStream();
        long offset = 0;
        long numberOfZeroBytesInSparseEntry = 0;
        for (TarArchiveStructSparse sparseHeader : sparseHeaders) {
            long zeroBlockSize = sparseHeader.getOffset() - offset;
            if (zeroBlockSize < 0) {
                throw new IOException("Corrupted struct sparse detected");
            }
            if (zeroBlockSize > 0) {
                streams.add(new BoundedInputStream(zeroInputStream, zeroBlockSize));
                numberOfZeroBytesInSparseEntry += zeroBlockSize;
            }
            if (sparseHeader.getNumbytes() > 0) {
                long start = (this.currEntry.getDataOffset() + sparseHeader.getOffset()) - numberOfZeroBytesInSparseEntry;
                if (start + sparseHeader.getNumbytes() < start) {
                    throw new IOException("Unreadable TAR archive, sparse block offset or length too big");
                }
                streams.add(new BoundedSeekableByteChannelInputStream(start, sparseHeader.getNumbytes(), this.archive));
            }
            offset = sparseHeader.getOffset() + sparseHeader.getNumbytes();
        }
        this.sparseInputStreams.put(this.currEntry.getName(), streams);
    }

    private void applyPaxHeadersToCurrentEntry(Map<String, String> headers, List<TarArchiveStructSparse> sparseHeaders) throws IOException {
        this.currEntry.updateEntryFromPaxHeaders(headers);
        this.currEntry.setSparseHeaders(sparseHeaders);
    }

    private void paxHeaders() throws IOException {
        List<TarArchiveStructSparse> sparseHeaders = new ArrayList<>();
        InputStream input = getInputStream(this.currEntry);
        Throwable th = null;
        try {
            Map<String, String> headers = TarUtils.parsePaxHeaders(input, sparseHeaders, this.globalPaxHeaders, this.currEntry.getSize());
            if (input != null) {
                if (0 != 0) {
                    try {
                        input.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    input.close();
                }
            }
            if (headers.containsKey("GNU.sparse.map")) {
                sparseHeaders = new ArrayList<>(TarUtils.parseFromPAX01SparseHeaders(headers.get("GNU.sparse.map")));
            }
            getNextTarEntry();
            if (this.currEntry == null) {
                throw new IOException("premature end of tar archive. Didn't find any entry after PAX header.");
            }
            applyPaxHeadersToCurrentEntry(headers, sparseHeaders);
            if (this.currEntry.isPaxGNU1XSparse()) {
                input = getInputStream(this.currEntry);
                Throwable th3 = null;
                try {
                    List<TarArchiveStructSparse> sparseHeaders2 = TarUtils.parsePAX1XSparseHeaders(input, this.recordSize);
                    if (input != null) {
                        if (0 != 0) {
                            try {
                                input.close();
                            } catch (Throwable th4) {
                                th3.addSuppressed(th4);
                            }
                        } else {
                            input.close();
                        }
                    }
                    this.currEntry.setSparseHeaders(sparseHeaders2);
                    this.currEntry.setDataOffset(this.currEntry.getDataOffset() + this.recordSize);
                } finally {
                    try {
                    } finally {
                    }
                }
            }
            buildSparseInputStreams();
        } finally {
        }
    }

    private void readGlobalPaxHeaders() throws IOException {
        InputStream input = getInputStream(this.currEntry);
        Throwable th = null;
        try {
            this.globalPaxHeaders = TarUtils.parsePaxHeaders(input, this.globalSparseHeaders, this.globalPaxHeaders, this.currEntry.getSize());
            if (input != null) {
                if (0 != 0) {
                    try {
                        input.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    input.close();
                }
            }
            getNextTarEntry();
            if (this.currEntry == null) {
                throw new IOException("Error detected parsing the pax header");
            }
        } finally {
        }
    }

    private byte[] getLongNameData() throws IOException {
        ByteArrayOutputStream longName = new ByteArrayOutputStream();
        InputStream in = getInputStream(this.currEntry);
        Throwable th = null;
        while (true) {
            try {
                int length = in.read(this.smallBuf);
                if (length < 0) {
                    break;
                }
                longName.write(this.smallBuf, 0, length);
            } finally {
            }
        }
        if (in != null) {
            if (0 != 0) {
                try {
                    in.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            } else {
                in.close();
            }
        }
        getNextTarEntry();
        if (this.currEntry == null) {
            return null;
        }
        byte[] longNameData = longName.toByteArray();
        int length2 = longNameData.length;
        while (length2 > 0 && longNameData[length2 - 1] == 0) {
            length2--;
        }
        if (length2 != longNameData.length) {
            byte[] l = new byte[length2];
            System.arraycopy(longNameData, 0, l, 0, length2);
            longNameData = l;
        }
        return longNameData;
    }

    private void skipRecordPadding() throws IOException {
        if (!isDirectory() && this.currEntry.getSize() > 0 && this.currEntry.getSize() % this.recordSize != 0) {
            long numRecords = (this.currEntry.getSize() / this.recordSize) + 1;
            long padding = (numRecords * this.recordSize) - this.currEntry.getSize();
            repositionForwardBy(padding);
            throwExceptionIfPositionIsNotInArchive();
        }
    }

    private void repositionForwardTo(long newPosition) throws IOException {
        long currPosition = this.archive.position();
        if (newPosition < currPosition) {
            throw new IOException("trying to move backwards inside of the archive");
        }
        this.archive.position(newPosition);
    }

    private void repositionForwardBy(long offset) throws IOException {
        repositionForwardTo(this.archive.position() + offset);
    }

    private void throwExceptionIfPositionIsNotInArchive() throws IOException {
        if (this.archive.size() < this.archive.position()) {
            throw new IOException("Truncated TAR archive");
        }
    }

    private ByteBuffer getRecord() throws IOException {
        ByteBuffer headerBuf = readRecord();
        setAtEOF(isEOFRecord(headerBuf));
        if (isAtEOF() && headerBuf != null) {
            tryToConsumeSecondEOFRecord();
            consumeRemainderOfLastBlock();
            headerBuf = null;
        }
        return headerBuf;
    }

    private void tryToConsumeSecondEOFRecord() throws IOException {
        boolean shouldReset = true;
        try {
            shouldReset = !isEOFRecord(readRecord());
        } finally {
            if (shouldReset) {
                this.archive.position(this.archive.position() - this.recordSize);
            }
        }
    }

    private void consumeRemainderOfLastBlock() throws IOException {
        long bytesReadOfLastBlock = this.archive.position() % this.blockSize;
        if (bytesReadOfLastBlock > 0) {
            repositionForwardBy(this.blockSize - bytesReadOfLastBlock);
        }
    }

    private ByteBuffer readRecord() throws IOException {
        this.recordBuffer.rewind();
        int readNow = this.archive.read(this.recordBuffer);
        if (readNow != this.recordSize) {
            return null;
        }
        return this.recordBuffer;
    }

    public List<TarArchiveEntry> getEntries() {
        return new ArrayList(this.entries);
    }

    private boolean isEOFRecord(ByteBuffer headerBuf) {
        return headerBuf == null || ArchiveUtils.isArrayZero(headerBuf.array(), this.recordSize);
    }

    protected final boolean isAtEOF() {
        return this.hasHitEOF;
    }

    protected final void setAtEOF(boolean b) {
        this.hasHitEOF = b;
    }

    private boolean isDirectory() {
        return this.currEntry != null && this.currEntry.isDirectory();
    }

    public InputStream getInputStream(TarArchiveEntry entry) throws IOException {
        try {
            return new BoundedTarEntryInputStream(entry, this.archive);
        } catch (RuntimeException ex) {
            throw new IOException("Corrupted TAR archive. Can't read entry", ex);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.archive.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/tar/TarFile$BoundedTarEntryInputStream.class */
    public final class BoundedTarEntryInputStream extends BoundedArchiveInputStream {
        private final SeekableByteChannel channel;
        private final TarArchiveEntry entry;
        private long entryOffset;
        private int currentSparseInputStreamIndex;

        BoundedTarEntryInputStream(TarArchiveEntry entry, SeekableByteChannel channel) throws IOException {
            super(entry.getDataOffset(), entry.getRealSize());
            if (channel.size() - entry.getSize() < entry.getDataOffset()) {
                throw new IOException("entry size exceeds archive size");
            }
            this.entry = entry;
            this.channel = channel;
        }

        @Override // org.apache.commons.compress.utils.BoundedArchiveInputStream
        protected int read(long pos, ByteBuffer buf) throws IOException {
            int totalRead;
            if (this.entryOffset >= this.entry.getRealSize()) {
                return -1;
            }
            if (this.entry.isSparse()) {
                totalRead = readSparse(this.entryOffset, buf, buf.limit());
            } else {
                totalRead = readArchive(pos, buf);
            }
            if (totalRead == -1) {
                if (buf.array().length > 0) {
                    throw new IOException("Truncated TAR archive");
                }
                TarFile.this.setAtEOF(true);
            } else {
                this.entryOffset += totalRead;
                buf.flip();
            }
            return totalRead;
        }

        private int readSparse(long pos, ByteBuffer buf, int numToRead) throws IOException {
            List<InputStream> entrySparseInputStreams = (List) TarFile.this.sparseInputStreams.get(this.entry.getName());
            if (entrySparseInputStreams == null || entrySparseInputStreams.isEmpty()) {
                return readArchive(this.entry.getDataOffset() + pos, buf);
            }
            if (this.currentSparseInputStreamIndex >= entrySparseInputStreams.size()) {
                return -1;
            }
            InputStream currentInputStream = entrySparseInputStreams.get(this.currentSparseInputStreamIndex);
            byte[] bufArray = new byte[numToRead];
            int readLen = currentInputStream.read(bufArray);
            if (readLen != -1) {
                buf.put(bufArray, 0, readLen);
            }
            if (this.currentSparseInputStreamIndex == entrySparseInputStreams.size() - 1) {
                return readLen;
            }
            if (readLen == -1) {
                this.currentSparseInputStreamIndex++;
                return readSparse(pos, buf, numToRead);
            } else if (readLen < numToRead) {
                this.currentSparseInputStreamIndex++;
                int readLenOfNext = readSparse(pos + readLen, buf, numToRead - readLen);
                if (readLenOfNext == -1) {
                    return readLen;
                }
                return readLen + readLenOfNext;
            } else {
                return readLen;
            }
        }

        private int readArchive(long pos, ByteBuffer buf) throws IOException {
            this.channel.position(pos);
            return this.channel.read(buf);
        }
    }
}
