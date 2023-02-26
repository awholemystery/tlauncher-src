package org.apache.commons.compress.archivers.sevenz;

import ch.qos.logback.core.FileAppender;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/BoundedSeekableByteChannelInputStream.class */
class BoundedSeekableByteChannelInputStream extends InputStream {
    private static final int MAX_BUF_LEN = 8192;
    private final ByteBuffer buffer;
    private final SeekableByteChannel channel;
    private long bytesRemaining;

    public BoundedSeekableByteChannelInputStream(SeekableByteChannel channel, long size) {
        this.channel = channel;
        this.bytesRemaining = size;
        if (size < FileAppender.DEFAULT_BUFFER_SIZE && size > 0) {
            this.buffer = ByteBuffer.allocate((int) size);
        } else {
            this.buffer = ByteBuffer.allocate(8192);
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.bytesRemaining > 0) {
            this.bytesRemaining--;
            int read = read(1);
            if (read < 0) {
                return read;
            }
            return this.buffer.get() & 255;
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        ByteBuffer buf;
        int bytesRead;
        if (len == 0) {
            return 0;
        }
        if (this.bytesRemaining <= 0) {
            return -1;
        }
        int bytesToRead = len;
        if (bytesToRead > this.bytesRemaining) {
            bytesToRead = (int) this.bytesRemaining;
        }
        if (bytesToRead <= this.buffer.capacity()) {
            buf = this.buffer;
            bytesRead = read(bytesToRead);
        } else {
            buf = ByteBuffer.allocate(bytesToRead);
            bytesRead = this.channel.read(buf);
            buf.flip();
        }
        if (bytesRead >= 0) {
            buf.get(b, off, bytesRead);
            this.bytesRemaining -= bytesRead;
        }
        return bytesRead;
    }

    private int read(int len) throws IOException {
        this.buffer.rewind().limit(len);
        int read = this.channel.read(this.buffer);
        this.buffer.flip();
        return read;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
