package org.apache.commons.compress.archivers.dump;

import ch.qos.logback.classic.pattern.CallerDataConverter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/dump/DumpArchiveInputStream.class */
public class DumpArchiveInputStream extends ArchiveInputStream {
    private final DumpArchiveSummary summary;
    private DumpArchiveEntry active;
    private boolean isClosed;
    private boolean hasHitEOF;
    private long entrySize;
    private long entryOffset;
    private int readIdx;
    private final byte[] readBuf;
    private byte[] blockBuffer;
    private int recordOffset;
    private long filepos;
    protected TapeInputStream raw;
    private final Map<Integer, Dirent> names;
    private final Map<Integer, DumpArchiveEntry> pending;
    private final Queue<DumpArchiveEntry> queue;
    private final ZipEncoding zipEncoding;
    final String encoding;

    public DumpArchiveInputStream(InputStream is) throws ArchiveException {
        this(is, null);
    }

    public DumpArchiveInputStream(InputStream is, String encoding) throws ArchiveException {
        this.readBuf = new byte[1024];
        this.names = new HashMap();
        this.pending = new HashMap();
        this.raw = new TapeInputStream(is);
        this.hasHitEOF = false;
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        try {
            byte[] headerBytes = this.raw.readRecord();
            if (!DumpArchiveUtil.verify(headerBytes)) {
                throw new UnrecognizedFormatException();
            }
            this.summary = new DumpArchiveSummary(headerBytes, this.zipEncoding);
            this.raw.resetBlockSize(this.summary.getNTRec(), this.summary.isCompressed());
            this.blockBuffer = new byte[CpioConstants.C_ISFIFO];
            readCLRI();
            readBITS();
            Dirent root = new Dirent(2, 2, 4, ".");
            this.names.put(2, root);
            this.queue = new PriorityQueue(10, p, q -> {
                if (p.getOriginalName() == null || q.getOriginalName() == null) {
                    return Integer.MAX_VALUE;
                }
                return p.getOriginalName().compareTo(q.getOriginalName());
            });
        } catch (IOException ex) {
            throw new ArchiveException(ex.getMessage(), ex);
        }
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    @Deprecated
    public int getCount() {
        return (int) getBytesRead();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public long getBytesRead() {
        return this.raw.getBytesRead();
    }

    public DumpArchiveSummary getSummary() {
        return this.summary;
    }

    private void readCLRI() throws IOException {
        byte[] buffer = this.raw.readRecord();
        if (!DumpArchiveUtil.verify(buffer)) {
            throw new InvalidFormatException();
        }
        this.active = DumpArchiveEntry.parse(buffer);
        if (DumpArchiveConstants.SEGMENT_TYPE.CLRI != this.active.getHeaderType()) {
            throw new InvalidFormatException();
        }
        if (this.raw.skip(1024 * this.active.getHeaderCount()) == -1) {
            throw new EOFException();
        }
        this.readIdx = this.active.getHeaderCount();
    }

    private void readBITS() throws IOException {
        byte[] buffer = this.raw.readRecord();
        if (!DumpArchiveUtil.verify(buffer)) {
            throw new InvalidFormatException();
        }
        this.active = DumpArchiveEntry.parse(buffer);
        if (DumpArchiveConstants.SEGMENT_TYPE.BITS != this.active.getHeaderType()) {
            throw new InvalidFormatException();
        }
        if (this.raw.skip(1024 * this.active.getHeaderCount()) == -1) {
            throw new EOFException();
        }
        this.readIdx = this.active.getHeaderCount();
    }

    public DumpArchiveEntry getNextDumpEntry() throws IOException {
        return getNextEntry();
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public DumpArchiveEntry getNextEntry() throws IOException {
        DumpArchiveEntry entry = null;
        String path = null;
        if (!this.queue.isEmpty()) {
            return this.queue.remove();
        }
        while (entry == null) {
            if (this.hasHitEOF) {
                return null;
            }
            while (this.readIdx < this.active.getHeaderCount()) {
                DumpArchiveEntry dumpArchiveEntry = this.active;
                int i = this.readIdx;
                this.readIdx = i + 1;
                if (!dumpArchiveEntry.isSparseRecord(i) && this.raw.skip(1024L) == -1) {
                    throw new EOFException();
                }
            }
            this.readIdx = 0;
            this.filepos = this.raw.getBytesRead();
            byte[] headerBytes = this.raw.readRecord();
            if (!DumpArchiveUtil.verify(headerBytes)) {
                throw new InvalidFormatException();
            }
            this.active = DumpArchiveEntry.parse(headerBytes);
            while (DumpArchiveConstants.SEGMENT_TYPE.ADDR == this.active.getHeaderType()) {
                if (this.raw.skip(1024 * (this.active.getHeaderCount() - this.active.getHeaderHoles())) == -1) {
                    throw new EOFException();
                }
                this.filepos = this.raw.getBytesRead();
                byte[] headerBytes2 = this.raw.readRecord();
                if (!DumpArchiveUtil.verify(headerBytes2)) {
                    throw new InvalidFormatException();
                }
                this.active = DumpArchiveEntry.parse(headerBytes2);
            }
            if (DumpArchiveConstants.SEGMENT_TYPE.END == this.active.getHeaderType()) {
                this.hasHitEOF = true;
                return null;
            }
            entry = this.active;
            if (entry.isDirectory()) {
                readDirectoryEntry(this.active);
                this.entryOffset = 0L;
                this.entrySize = 0L;
                this.readIdx = this.active.getHeaderCount();
            } else {
                this.entryOffset = 0L;
                this.entrySize = this.active.getEntrySize();
                this.readIdx = 0;
            }
            this.recordOffset = this.readBuf.length;
            path = getPath(entry);
            if (path == null) {
                entry = null;
            }
        }
        entry.setName(path);
        entry.setSimpleName(this.names.get(Integer.valueOf(entry.getIno())).getName());
        entry.setOffset(this.filepos);
        return entry;
    }

    private void readDirectoryEntry(DumpArchiveEntry entry) throws IOException {
        long size = entry.getEntrySize();
        boolean first = true;
        while (true) {
            if (first || DumpArchiveConstants.SEGMENT_TYPE.ADDR == entry.getHeaderType()) {
                if (!first) {
                    this.raw.readRecord();
                }
                if (!this.names.containsKey(Integer.valueOf(entry.getIno())) && DumpArchiveConstants.SEGMENT_TYPE.INODE == entry.getHeaderType()) {
                    this.pending.put(Integer.valueOf(entry.getIno()), entry);
                }
                int datalen = 1024 * entry.getHeaderCount();
                if (this.blockBuffer.length < datalen) {
                    this.blockBuffer = IOUtils.readRange(this.raw, datalen);
                    if (this.blockBuffer.length != datalen) {
                        throw new EOFException();
                    }
                } else if (this.raw.read(this.blockBuffer, 0, datalen) != datalen) {
                    throw new EOFException();
                }
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= datalen - 8 || i2 >= size - 8) {
                        break;
                    }
                    int ino = DumpArchiveUtil.convert32(this.blockBuffer, i2);
                    int reclen = DumpArchiveUtil.convert16(this.blockBuffer, i2 + 4);
                    byte type = this.blockBuffer[i2 + 6];
                    String name = DumpArchiveUtil.decode(this.zipEncoding, this.blockBuffer, i2 + 8, this.blockBuffer[i2 + 7]);
                    if (!".".equals(name) && !CallerDataConverter.DEFAULT_RANGE_DELIMITER.equals(name)) {
                        Dirent d = new Dirent(ino, entry.getIno(), type, name);
                        this.names.put(Integer.valueOf(ino), d);
                        this.pending.forEach(k, v -> {
                            String path = getPath(v);
                            if (path != null) {
                                v.setName(path);
                                v.setSimpleName(this.names.get(k).getName());
                                this.queue.add(v);
                            }
                        });
                        this.queue.forEach(e -> {
                            this.pending.remove(Integer.valueOf(e.getIno()));
                        });
                    }
                    i = i2 + reclen;
                }
                byte[] peekBytes = this.raw.peek();
                if (!DumpArchiveUtil.verify(peekBytes)) {
                    throw new InvalidFormatException();
                }
                entry = DumpArchiveEntry.parse(peekBytes);
                first = false;
                size -= 1024;
            } else {
                return;
            }
        }
    }

    private String getPath(DumpArchiveEntry entry) {
        Stack<String> elements = new Stack<>();
        int ino = entry.getIno();
        while (true) {
            int i = ino;
            if (!this.names.containsKey(Integer.valueOf(i))) {
                elements.clear();
                break;
            }
            Dirent dirent = this.names.get(Integer.valueOf(i));
            elements.push(dirent.getName());
            if (dirent.getIno() == dirent.getParentIno()) {
                break;
            }
            ino = dirent.getParentIno();
        }
        if (elements.isEmpty()) {
            this.pending.put(Integer.valueOf(entry.getIno()), entry);
            return null;
        }
        StringBuilder sb = new StringBuilder(elements.pop());
        while (!elements.isEmpty()) {
            sb.append('/');
            sb.append(elements.pop());
        }
        return sb.toString();
    }

    @Override // java.io.InputStream
    public int read(byte[] buf, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int totalRead = 0;
        if (this.hasHitEOF || this.isClosed || this.entryOffset >= this.entrySize) {
            return -1;
        }
        if (this.active == null) {
            throw new IllegalStateException("No current dump entry");
        }
        if (len + this.entryOffset > this.entrySize) {
            len = (int) (this.entrySize - this.entryOffset);
        }
        while (len > 0) {
            int sz = Math.min(len, this.readBuf.length - this.recordOffset);
            if (this.recordOffset + sz <= this.readBuf.length) {
                System.arraycopy(this.readBuf, this.recordOffset, buf, off, sz);
                totalRead += sz;
                this.recordOffset += sz;
                len -= sz;
                off += sz;
            }
            if (len > 0) {
                if (this.readIdx >= 512) {
                    byte[] headerBytes = this.raw.readRecord();
                    if (!DumpArchiveUtil.verify(headerBytes)) {
                        throw new InvalidFormatException();
                    }
                    this.active = DumpArchiveEntry.parse(headerBytes);
                    this.readIdx = 0;
                }
                DumpArchiveEntry dumpArchiveEntry = this.active;
                int i = this.readIdx;
                this.readIdx = i + 1;
                if (!dumpArchiveEntry.isSparseRecord(i)) {
                    int r = this.raw.read(this.readBuf, 0, this.readBuf.length);
                    if (r != this.readBuf.length) {
                        throw new EOFException();
                    }
                } else {
                    Arrays.fill(this.readBuf, (byte) 0);
                }
                this.recordOffset = 0;
            }
        }
        this.entryOffset += totalRead;
        return totalRead;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.isClosed) {
            this.isClosed = true;
            this.raw.close();
        }
    }

    public static boolean matches(byte[] buffer, int length) {
        if (length < 32) {
            return false;
        }
        if (length >= 1024) {
            return DumpArchiveUtil.verify(buffer);
        }
        return 60012 == DumpArchiveUtil.convert32(buffer, 24);
    }
}
