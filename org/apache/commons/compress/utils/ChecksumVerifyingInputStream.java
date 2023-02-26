package org.apache.commons.compress.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/ChecksumVerifyingInputStream.class */
public class ChecksumVerifyingInputStream extends InputStream {
    private final InputStream in;
    private long bytesRemaining;
    private final long expectedChecksum;
    private final Checksum checksum;

    public ChecksumVerifyingInputStream(Checksum checksum, InputStream in, long size, long expectedChecksum) {
        this.checksum = checksum;
        this.in = in;
        this.expectedChecksum = expectedChecksum;
        this.bytesRemaining = size;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    public long getBytesRemaining() {
        return this.bytesRemaining;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.bytesRemaining <= 0) {
            return -1;
        }
        int ret = this.in.read();
        if (ret >= 0) {
            this.checksum.update(ret);
            this.bytesRemaining--;
        }
        verify();
        return ret;
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
        int ret = this.in.read(b, off, len);
        if (ret >= 0) {
            this.checksum.update(b, off, ret);
            this.bytesRemaining -= ret;
        }
        verify();
        return ret;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        return read() >= 0 ? 1L : 0L;
    }

    private void verify() throws IOException {
        if (this.bytesRemaining <= 0 && this.expectedChecksum != this.checksum.getValue()) {
            throw new IOException("Checksum verification failed");
        }
    }
}
