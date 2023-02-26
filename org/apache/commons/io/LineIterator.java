package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/LineIterator.class */
public class LineIterator implements Iterator<String>, Closeable {
    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished;

    public LineIterator(Reader reader) throws IllegalArgumentException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }
        if (reader instanceof BufferedReader) {
            this.bufferedReader = (BufferedReader) reader;
        } else {
            this.bufferedReader = new BufferedReader(reader);
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.cachedLine != null) {
            return true;
        }
        if (this.finished) {
            return false;
        }
        while (true) {
            try {
                String line = this.bufferedReader.readLine();
                if (line == null) {
                    this.finished = true;
                    return false;
                } else if (isValidLine(line)) {
                    this.cachedLine = line;
                    return true;
                }
            } catch (IOException ioe) {
                ioe.getClass();
                IOUtils.closeQuietly(this, (v1) -> {
                    r1.addSuppressed(v1);
                });
                throw new IllegalStateException(ioe);
            }
        }
    }

    protected boolean isValidLine(String line) {
        return true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public String next() {
        return nextLine();
    }

    public String nextLine() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        String currentLine = this.cachedLine;
        this.cachedLine = null;
        return currentLine;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.finished = true;
        this.cachedLine = null;
        IOUtils.close(this.bufferedReader);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }

    @Deprecated
    public static void closeQuietly(LineIterator iterator) {
        IOUtils.closeQuietly(iterator);
    }
}
