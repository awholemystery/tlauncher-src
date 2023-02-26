package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/TeeReader.class */
public class TeeReader extends ProxyReader {
    private final Writer branch;
    private final boolean closeBranch;

    public TeeReader(Reader input, Writer branch) {
        this(input, branch, false);
    }

    public TeeReader(Reader input, Writer branch, boolean closeBranch) {
        super(input);
        this.branch = branch;
        this.closeBranch = closeBranch;
    }

    @Override // org.apache.commons.io.input.ProxyReader, java.io.FilterReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (this.closeBranch) {
                this.branch.close();
            }
        }
    }

    @Override // org.apache.commons.io.input.ProxyReader, java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int ch2 = super.read();
        if (ch2 != -1) {
            this.branch.write(ch2);
        }
        return ch2;
    }

    @Override // org.apache.commons.io.input.ProxyReader, java.io.Reader
    public int read(char[] chr) throws IOException {
        int n = super.read(chr);
        if (n != -1) {
            this.branch.write(chr, 0, n);
        }
        return n;
    }

    @Override // org.apache.commons.io.input.ProxyReader, java.io.FilterReader, java.io.Reader
    public int read(char[] chr, int st, int end) throws IOException {
        int n = super.read(chr, st, end);
        if (n != -1) {
            this.branch.write(chr, st, n);
        }
        return n;
    }

    @Override // org.apache.commons.io.input.ProxyReader, java.io.Reader, java.lang.Readable
    public int read(CharBuffer target) throws IOException {
        int originalPosition = target.position();
        int n = super.read(target);
        if (n != -1) {
            int newPosition = target.position();
            int newLimit = target.limit();
            try {
                target.position(originalPosition).limit(newPosition);
                this.branch.append((CharSequence) target);
                target.position(newPosition).limit(newLimit);
            } catch (Throwable th) {
                target.position(newPosition).limit(newLimit);
                throw th;
            }
        }
        return n;
    }
}
