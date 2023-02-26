package org.apache.commons.compress.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/BoundedArchiveInputStream.class */
public abstract class BoundedArchiveInputStream extends InputStream {
    private final long end;
    private ByteBuffer singleByteBuffer;
    private long loc;

    protected abstract int read(long j, ByteBuffer byteBuffer) throws IOException;

    public BoundedArchiveInputStream(long start, long remaining) {
        this.end = start + remaining;
        if (this.end < start) {
            throw new IllegalArgumentException("Invalid length of stream at offset=" + start + ", length=" + remaining);
        }
        this.loc = start;
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        if (this.loc >= this.end) {
            return -1;
        }
        if (this.singleByteBuffer == null) {
            this.singleByteBuffer = ByteBuffer.allocate(1);
        } else {
            this.singleByteBuffer.rewind();
        }
        int read = read(this.loc, this.singleByteBuffer);
        if (read < 1) {
            return -1;
        }
        this.loc++;
        return this.singleByteBuffer.get() & 255;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        if (this.loc >= this.end) {
            return -1;
        }
        long maxLen = Math.min(len, this.end - this.loc);
        if (maxLen <= 0) {
            return 0;
        }
        if (off < 0 || off > b.length || maxLen > b.length - off) {
            throw new IndexOutOfBoundsException("offset or len are out of bounds");
        }
        ByteBuffer buf = ByteBuffer.wrap(b, off, (int) maxLen);
        int ret = read(this.loc, buf);
        if (ret > 0) {
            this.loc += ret;
        }
        return ret;
    }
}
