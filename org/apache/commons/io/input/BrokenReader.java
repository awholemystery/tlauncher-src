package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/BrokenReader.class */
public class BrokenReader extends Reader {
    private final IOException exception;

    public BrokenReader(IOException exception) {
        this.exception = exception;
    }

    public BrokenReader() {
        this(new IOException("Broken reader"));
    }

    @Override // java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        throw this.exception;
    }

    @Override // java.io.Reader
    public long skip(long n) throws IOException {
        throw this.exception;
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        throw this.exception;
    }

    @Override // java.io.Reader
    public void mark(int readAheadLimit) throws IOException {
        throw this.exception;
    }

    @Override // java.io.Reader
    public synchronized void reset() throws IOException {
        throw this.exception;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw this.exception;
    }
}
