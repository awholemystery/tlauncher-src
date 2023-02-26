package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.lang.Appendable;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/output/AppendableWriter.class */
public class AppendableWriter<T extends Appendable> extends Writer {
    private final T appendable;

    public AppendableWriter(T appendable) {
        this.appendable = appendable;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) throws IOException {
        this.appendable.append(c);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq) throws IOException {
        this.appendable.append(csq);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        this.appendable.append(csq, start, end);
        return this;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
    }

    public T getAppendable() {
        return this.appendable;
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        Objects.requireNonNull(cbuf, "Character array is missing");
        if (len < 0 || off + len > cbuf.length) {
            throw new IndexOutOfBoundsException("Array Size=" + cbuf.length + ", offset=" + off + ", length=" + len);
        }
        for (int i = 0; i < len; i++) {
            this.appendable.append(cbuf[off + i]);
        }
    }

    @Override // java.io.Writer
    public void write(int c) throws IOException {
        this.appendable.append((char) c);
    }

    @Override // java.io.Writer
    public void write(String str, int off, int len) throws IOException {
        Objects.requireNonNull(str, "String is missing");
        this.appendable.append(str, off, off + len);
    }
}
