package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/BoundedReader.class */
public class BoundedReader extends Reader {
    private static final int INVALID = -1;
    private final Reader target;
    private int charsRead;
    private int markedAt = -1;
    private int readAheadLimit;
    private final int maxCharsFromTargetReader;

    public BoundedReader(Reader target, int maxCharsFromTargetReader) {
        this.target = target;
        this.maxCharsFromTargetReader = maxCharsFromTargetReader;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.target.close();
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        this.charsRead = this.markedAt;
        this.target.reset();
    }

    @Override // java.io.Reader
    public void mark(int readAheadLimit) throws IOException {
        this.readAheadLimit = readAheadLimit - this.charsRead;
        this.markedAt = this.charsRead;
        this.target.mark(readAheadLimit);
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        if (this.charsRead >= this.maxCharsFromTargetReader) {
            return -1;
        }
        if (this.markedAt >= 0 && this.charsRead - this.markedAt >= this.readAheadLimit) {
            return -1;
        }
        this.charsRead++;
        return this.target.read();
    }

    @Override // java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            int c = read();
            if (c == -1) {
                if (i == 0) {
                    return -1;
                }
                return i;
            }
            cbuf[off + i] = (char) c;
        }
        return len;
    }
}
