package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/entity/DeflateInputStream.class */
public class DeflateInputStream extends InputStream {
    private final InputStream sourceStream;

    public DeflateInputStream(InputStream wrapped) throws IOException {
        PushbackInputStream pushback = new PushbackInputStream(wrapped, 2);
        int i1 = pushback.read();
        int i2 = pushback.read();
        if (i1 == -1 || i2 == -1) {
            throw new ZipException("Unexpected end of stream");
        }
        pushback.unread(i2);
        pushback.unread(i1);
        boolean nowrap = true;
        int b1 = i1 & 255;
        int compressionMethod = b1 & 15;
        int compressionInfo = (b1 >> 4) & 15;
        int b2 = i2 & 255;
        if (compressionMethod == 8 && compressionInfo <= 7 && ((b1 << 8) | b2) % 31 == 0) {
            nowrap = false;
        }
        this.sourceStream = new DeflateStream(pushback, new Inflater(nowrap));
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.sourceStream.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return this.sourceStream.read(b);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        return this.sourceStream.read(b, off, len);
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        return this.sourceStream.skip(n);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.sourceStream.available();
    }

    @Override // java.io.InputStream
    public void mark(int readLimit) {
        this.sourceStream.mark(readLimit);
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        this.sourceStream.reset();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.sourceStream.markSupported();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.sourceStream.close();
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/http/client/entity/DeflateInputStream$DeflateStream.class */
    static class DeflateStream extends InflaterInputStream {
        private boolean closed;

        public DeflateStream(InputStream in, Inflater inflater) {
            super(in, inflater);
            this.closed = false;
        }

        @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.inf.end();
            super.close();
        }
    }
}
