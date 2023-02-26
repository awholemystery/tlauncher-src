package org.apache.commons.io.input;

import ch.qos.logback.core.joran.action.Action;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/RandomAccessFileInputStream.class */
public class RandomAccessFileInputStream extends InputStream {
    private final boolean closeOnClose;
    private final RandomAccessFile randomAccessFile;

    public RandomAccessFileInputStream(RandomAccessFile file) {
        this(file, false);
    }

    public RandomAccessFileInputStream(RandomAccessFile file, boolean closeOnClose) {
        this.randomAccessFile = (RandomAccessFile) Objects.requireNonNull(file, Action.FILE_ATTRIBUTE);
        this.closeOnClose = closeOnClose;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        long avail = availableLong();
        if (avail > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) avail;
    }

    public long availableLong() throws IOException {
        return this.randomAccessFile.length() - this.randomAccessFile.getFilePointer();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        if (this.closeOnClose) {
            this.randomAccessFile.close();
        }
    }

    public RandomAccessFile getRandomAccessFile() {
        return this.randomAccessFile;
    }

    public boolean isCloseOnClose() {
        return this.closeOnClose;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.randomAccessFile.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes) throws IOException {
        return this.randomAccessFile.read(bytes);
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes, int offset, int length) throws IOException {
        return this.randomAccessFile.read(bytes, offset, length);
    }

    private void seek(long position) throws IOException {
        this.randomAccessFile.seek(position);
    }

    @Override // java.io.InputStream
    public long skip(long skipCount) throws IOException {
        if (skipCount <= 0) {
            return 0L;
        }
        long filePointer = this.randomAccessFile.getFilePointer();
        long fileLength = this.randomAccessFile.length();
        if (filePointer >= fileLength) {
            return 0L;
        }
        long targetPos = filePointer + skipCount;
        long newPos = targetPos > fileLength ? fileLength - 1 : targetPos;
        if (newPos > 0) {
            seek(newPos);
        }
        return this.randomAccessFile.getFilePointer() - filePointer;
    }
}
