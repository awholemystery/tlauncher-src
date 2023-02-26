package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/output/BrokenWriter.class */
public class BrokenWriter extends Writer {
    private final IOException exception;

    public BrokenWriter(IOException exception) {
        this.exception = exception;
    }

    public BrokenWriter() {
        this(new IOException("Broken writer"));
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw this.exception;
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        throw this.exception;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        throw this.exception;
    }
}
