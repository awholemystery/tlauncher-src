package org.apache.commons.compress.archivers.tar;

import ch.qos.logback.core.CoreConstants;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.ExactMath;
import org.apache.commons.compress.utils.FixedLengthBlockOutputStream;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.protocol.HTTP;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/tar/TarArchiveOutputStream.class */
public class TarArchiveOutputStream extends ArchiveOutputStream {
    public static final int LONGFILE_ERROR = 0;
    public static final int LONGFILE_TRUNCATE = 1;
    public static final int LONGFILE_GNU = 2;
    public static final int LONGFILE_POSIX = 3;
    public static final int BIGNUMBER_ERROR = 0;
    public static final int BIGNUMBER_STAR = 1;
    public static final int BIGNUMBER_POSIX = 2;
    private static final int RECORD_SIZE = 512;
    private long currSize;
    private String currName;
    private long currBytes;
    private final byte[] recordBuf;
    private int longFileMode;
    private int bigNumberMode;
    private int recordsWritten;
    private final int recordsPerBlock;
    private boolean closed;
    private boolean haveUnclosedEntry;
    private boolean finished;
    private final FixedLengthBlockOutputStream out;
    private final CountingOutputStream countingOut;
    private final ZipEncoding zipEncoding;
    final String encoding;
    private boolean addPaxHeadersForNonAsciiNames;
    private static final ZipEncoding ASCII = ZipEncodingHelper.getZipEncoding(HTTP.ASCII);
    private static final int BLOCK_SIZE_UNSPECIFIED = -511;

    public TarArchiveOutputStream(OutputStream os) {
        this(os, (int) BLOCK_SIZE_UNSPECIFIED);
    }

    public TarArchiveOutputStream(OutputStream os, String encoding) {
        this(os, (int) BLOCK_SIZE_UNSPECIFIED, encoding);
    }

    public TarArchiveOutputStream(OutputStream os, int blockSize) {
        this(os, blockSize, (String) null);
    }

    @Deprecated
    public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize) {
        this(os, blockSize, recordSize, null);
    }

    @Deprecated
    public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize, String encoding) {
        this(os, blockSize, encoding);
        if (recordSize != 512) {
            throw new IllegalArgumentException("Tar record size must always be 512 bytes. Attempt to set size of " + recordSize);
        }
    }

    public TarArchiveOutputStream(OutputStream os, int blockSize, String encoding) {
        int realBlockSize;
        this.longFileMode = 0;
        this.bigNumberMode = 0;
        if (BLOCK_SIZE_UNSPECIFIED == blockSize) {
            realBlockSize = 512;
        } else {
            realBlockSize = blockSize;
        }
        if (realBlockSize <= 0 || realBlockSize % 512 != 0) {
            throw new IllegalArgumentException("Block size must be a multiple of 512 bytes. Attempt to use set size of " + blockSize);
        }
        CountingOutputStream countingOutputStream = new CountingOutputStream(os);
        this.countingOut = countingOutputStream;
        this.out = new FixedLengthBlockOutputStream(countingOutputStream, 512);
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.recordBuf = new byte[512];
        this.recordsPerBlock = realBlockSize / 512;
    }

    public void setLongFileMode(int longFileMode) {
        this.longFileMode = longFileMode;
    }

    public void setBigNumberMode(int bigNumberMode) {
        this.bigNumberMode = bigNumberMode;
    }

    public void setAddPaxHeadersForNonAsciiNames(boolean b) {
        this.addPaxHeadersForNonAsciiNames = b;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    @Deprecated
    public int getCount() {
        return (int) getBytesWritten();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public long getBytesWritten() {
        return this.countingOut.getBytesWritten();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public void finish() throws IOException {
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        if (this.haveUnclosedEntry) {
            throw new IOException("This archive contains unclosed entries.");
        }
        writeEOFRecord();
        writeEOFRecord();
        padAsNeeded();
        this.out.flush();
        this.finished = true;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            if (!this.finished) {
                finish();
            }
        } finally {
            if (!this.closed) {
                this.out.close();
                this.closed = true;
            }
        }
    }

    @Deprecated
    public int getRecordSize() {
        return 512;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        TarArchiveEntry entry = (TarArchiveEntry) archiveEntry;
        if (entry.isGlobalPaxHeader()) {
            byte[] data = encodeExtendedPaxHeadersContents(entry.getExtraPaxHeaders());
            entry.setSize(data.length);
            entry.writeEntryHeader(this.recordBuf, this.zipEncoding, this.bigNumberMode == 1);
            writeRecord(this.recordBuf);
            this.currSize = entry.getSize();
            this.currBytes = 0L;
            this.haveUnclosedEntry = true;
            write(data);
            closeArchiveEntry();
            return;
        }
        Map<String, String> paxHeaders = new HashMap<>();
        String entryName = entry.getName();
        boolean paxHeaderContainsPath = handleLongName(entry, entryName, paxHeaders, ClientCookie.PATH_ATTR, (byte) 76, "file name");
        String linkName = entry.getLinkName();
        boolean paxHeaderContainsLinkPath = (linkName == null || linkName.isEmpty() || !handleLongName(entry, linkName, paxHeaders, "linkpath", (byte) 75, "link name")) ? false : true;
        if (this.bigNumberMode == 2) {
            addPaxHeadersForBigNumbers(paxHeaders, entry);
        } else if (this.bigNumberMode != 1) {
            failForBigNumbers(entry);
        }
        if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsPath && !ASCII.canEncode(entryName)) {
            paxHeaders.put(ClientCookie.PATH_ATTR, entryName);
        }
        if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsLinkPath && ((entry.isLink() || entry.isSymbolicLink()) && !ASCII.canEncode(linkName))) {
            paxHeaders.put("linkpath", linkName);
        }
        paxHeaders.putAll(entry.getExtraPaxHeaders());
        if (!paxHeaders.isEmpty()) {
            writePaxHeaders(entry, entryName, paxHeaders);
        }
        entry.writeEntryHeader(this.recordBuf, this.zipEncoding, this.bigNumberMode == 1);
        writeRecord(this.recordBuf);
        this.currBytes = 0L;
        if (entry.isDirectory()) {
            this.currSize = 0L;
        } else {
            this.currSize = entry.getSize();
        }
        this.currName = entryName;
        this.haveUnclosedEntry = true;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public void closeArchiveEntry() throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (!this.haveUnclosedEntry) {
            throw new IOException("No current entry to close");
        }
        this.out.flushBlock();
        if (this.currBytes < this.currSize) {
            throw new IOException("Entry '" + this.currName + "' closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
        }
        this.recordsWritten = ExactMath.add(this.recordsWritten, this.currSize / 512);
        if (0 != this.currSize % 512) {
            this.recordsWritten++;
        }
        this.haveUnclosedEntry = false;
    }

    @Override // java.io.OutputStream
    public void write(byte[] wBuf, int wOffset, int numToWrite) throws IOException {
        if (!this.haveUnclosedEntry) {
            throw new IllegalStateException("No current tar entry");
        }
        if (this.currBytes + numToWrite > this.currSize) {
            throw new IOException("Request to write '" + numToWrite + "' bytes exceeds size in header of '" + this.currSize + "' bytes for entry '" + this.currName + "'");
        }
        this.out.write(wBuf, wOffset, numToWrite);
        this.currBytes += numToWrite;
    }

    void writePaxHeaders(TarArchiveEntry entry, String entryName, Map<String, String> headers) throws IOException {
        String name = "./PaxHeaders.X/" + stripTo7Bits(entryName);
        if (name.length() >= 100) {
            name = name.substring(0, 99);
        }
        TarArchiveEntry pex = new TarArchiveEntry(name, (byte) 120);
        transferModTime(entry, pex);
        byte[] data = encodeExtendedPaxHeadersContents(headers);
        pex.setSize(data.length);
        putArchiveEntry(pex);
        write(data);
        closeArchiveEntry();
    }

    private byte[] encodeExtendedPaxHeadersContents(Map<String, String> headers) {
        StringWriter w = new StringWriter();
        headers.forEach(k, v -> {
            int len = k.length() + v.length() + 3 + 2;
            String line = len + " " + k + "=" + v + "\n";
            int length = line.getBytes(StandardCharsets.UTF_8).length;
            while (true) {
                int actualLength = length;
                if (len != actualLength) {
                    len = actualLength;
                    line = len + " " + k + "=" + v + "\n";
                    length = line.getBytes(StandardCharsets.UTF_8).length;
                } else {
                    w.write(line);
                    return;
                }
            }
        });
        return w.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String stripTo7Bits(String name) {
        int length = name.length();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char stripped = (char) (name.charAt(i) & 127);
            if (shouldBeReplaced(stripped)) {
                result.append("_");
            } else {
                result.append(stripped);
            }
        }
        return result.toString();
    }

    private boolean shouldBeReplaced(char c) {
        return c == 0 || c == '/' || c == '\\';
    }

    private void writeEOFRecord() throws IOException {
        Arrays.fill(this.recordBuf, (byte) 0);
        writeRecord(this.recordBuf);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        return new TarArchiveEntry(inputFile, entryName);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveOutputStream
    public ArchiveEntry createArchiveEntry(Path inputPath, String entryName, LinkOption... options) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        return new TarArchiveEntry(inputPath, entryName, options);
    }

    private void writeRecord(byte[] record) throws IOException {
        if (record.length != 512) {
            throw new IOException("Record to write has length '" + record.length + "' which is not the record size of '512'");
        }
        this.out.write(record);
        this.recordsWritten++;
    }

    private void padAsNeeded() throws IOException {
        int start = this.recordsWritten % this.recordsPerBlock;
        if (start != 0) {
            for (int i = start; i < this.recordsPerBlock; i++) {
                writeEOFRecord();
            }
        }
    }

    private void addPaxHeadersForBigNumbers(Map<String, String> paxHeaders, TarArchiveEntry entry) {
        addPaxHeaderForBigNumber(paxHeaders, "size", entry.getSize(), TarConstants.MAXSIZE);
        addPaxHeaderForBigNumber(paxHeaders, "gid", entry.getLongGroupId(), TarConstants.MAXID);
        addFileTimePaxHeaderForBigNumber(paxHeaders, "mtime", entry.getLastModifiedTime(), TarConstants.MAXSIZE);
        addFileTimePaxHeader(paxHeaders, "atime", entry.getLastAccessTime());
        if (entry.getStatusChangeTime() != null) {
            addFileTimePaxHeader(paxHeaders, "ctime", entry.getStatusChangeTime());
        } else {
            addFileTimePaxHeader(paxHeaders, "ctime", entry.getCreationTime());
        }
        addPaxHeaderForBigNumber(paxHeaders, "uid", entry.getLongUserId(), TarConstants.MAXID);
        addFileTimePaxHeader(paxHeaders, "LIBARCHIVE.creationtime", entry.getCreationTime());
        addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devmajor", entry.getDevMajor(), TarConstants.MAXID);
        addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devminor", entry.getDevMinor(), TarConstants.MAXID);
        failForBigNumber("mode", entry.getMode(), TarConstants.MAXID);
    }

    private void addPaxHeaderForBigNumber(Map<String, String> paxHeaders, String header, long value, long maxValue) {
        if (value < 0 || value > maxValue) {
            paxHeaders.put(header, String.valueOf(value));
        }
    }

    private void addFileTimePaxHeaderForBigNumber(Map<String, String> paxHeaders, String header, FileTime value, long maxValue) {
        if (value != null) {
            Instant instant = value.toInstant();
            long seconds = instant.getEpochSecond();
            int nanos = instant.getNano();
            if (nanos == 0) {
                addPaxHeaderForBigNumber(paxHeaders, header, seconds, maxValue);
            } else {
                addInstantPaxHeader(paxHeaders, header, seconds, nanos);
            }
        }
    }

    private void addFileTimePaxHeader(Map<String, String> paxHeaders, String header, FileTime value) {
        if (value != null) {
            Instant instant = value.toInstant();
            long seconds = instant.getEpochSecond();
            int nanos = instant.getNano();
            if (nanos == 0) {
                paxHeaders.put(header, String.valueOf(seconds));
            } else {
                addInstantPaxHeader(paxHeaders, header, seconds, nanos);
            }
        }
    }

    private void addInstantPaxHeader(Map<String, String> paxHeaders, String header, long seconds, int nanos) {
        BigDecimal bdSeconds = BigDecimal.valueOf(seconds);
        BigDecimal bdNanos = BigDecimal.valueOf(nanos).movePointLeft(9).setScale(7, RoundingMode.DOWN);
        BigDecimal timestamp = bdSeconds.add(bdNanos);
        paxHeaders.put(header, timestamp.toPlainString());
    }

    private void failForBigNumbers(TarArchiveEntry entry) {
        failForBigNumber("entry size", entry.getSize(), TarConstants.MAXSIZE);
        failForBigNumberWithPosixMessage("group id", entry.getLongGroupId(), TarConstants.MAXID);
        failForBigNumber("last modification time", entry.getLastModifiedTime().to(TimeUnit.SECONDS), TarConstants.MAXSIZE);
        failForBigNumber("user id", entry.getLongUserId(), TarConstants.MAXID);
        failForBigNumber("mode", entry.getMode(), TarConstants.MAXID);
        failForBigNumber("major device number", entry.getDevMajor(), TarConstants.MAXID);
        failForBigNumber("minor device number", entry.getDevMinor(), TarConstants.MAXID);
    }

    private void failForBigNumber(String field, long value, long maxValue) {
        failForBigNumber(field, value, maxValue, CoreConstants.EMPTY_STRING);
    }

    private void failForBigNumberWithPosixMessage(String field, long value, long maxValue) {
        failForBigNumber(field, value, maxValue, " Use STAR or POSIX extensions to overcome this limit");
    }

    private void failForBigNumber(String field, long value, long maxValue, String additionalMsg) {
        if (value < 0 || value > maxValue) {
            throw new IllegalArgumentException(field + " '" + value + "' is too big ( > " + maxValue + " )." + additionalMsg);
        }
    }

    private boolean handleLongName(TarArchiveEntry entry, String name, Map<String, String> paxHeaders, String paxHeaderName, byte linkType, String fieldName) throws IOException {
        ByteBuffer encodedName = this.zipEncoding.encode(name);
        int len = encodedName.limit() - encodedName.position();
        if (len >= 100) {
            if (this.longFileMode == 3) {
                paxHeaders.put(paxHeaderName, name);
                return true;
            } else if (this.longFileMode != 2) {
                if (this.longFileMode != 1) {
                    throw new IllegalArgumentException(fieldName + " '" + name + "' is too long ( > 100 bytes)");
                }
                return false;
            } else {
                TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, linkType);
                longLinkEntry.setSize(len + 1);
                transferModTime(entry, longLinkEntry);
                putArchiveEntry(longLinkEntry);
                write(encodedName.array(), encodedName.arrayOffset(), len);
                write(0);
                closeArchiveEntry();
                return false;
            }
        }
        return false;
    }

    private void transferModTime(TarArchiveEntry from, TarArchiveEntry to) {
        long fromModTimeSeconds = from.getLastModifiedTime().to(TimeUnit.SECONDS);
        if (fromModTimeSeconds < 0 || fromModTimeSeconds > TarConstants.MAXSIZE) {
            fromModTimeSeconds = 0;
        }
        to.setLastModifiedTime(FileTime.from(fromModTimeSeconds, TimeUnit.SECONDS));
    }
}
