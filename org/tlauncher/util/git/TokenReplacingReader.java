package org.tlauncher.util.git;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/git/TokenReplacingReader.class */
public class TokenReplacingReader extends Reader {
    protected PushbackReader pushbackReader;
    protected ITokenResolver tokenResolver;
    protected StringBuilder tokenNameBuffer = new StringBuilder();
    protected String tokenValue = null;
    protected int tokenValueIndex = 0;

    public TokenReplacingReader(Reader source, ITokenResolver resolver) {
        this.pushbackReader = null;
        this.tokenResolver = null;
        this.pushbackReader = new PushbackReader(source, 2);
        this.tokenResolver = resolver;
    }

    @Override // java.io.Reader, java.lang.Readable
    public int read(CharBuffer target) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        if (this.tokenValue != null) {
            if (this.tokenValueIndex < this.tokenValue.length()) {
                String str = this.tokenValue;
                int i = this.tokenValueIndex;
                this.tokenValueIndex = i + 1;
                return str.charAt(i);
            } else if (this.tokenValueIndex == this.tokenValue.length()) {
                this.tokenValue = null;
                this.tokenValueIndex = 0;
            }
        }
        int data = this.pushbackReader.read();
        if (data != 36) {
            return data;
        }
        int data2 = this.pushbackReader.read();
        if (data2 != 123) {
            this.pushbackReader.unread(data2);
            return 36;
        }
        this.tokenNameBuffer.delete(0, this.tokenNameBuffer.length());
        int read = this.pushbackReader.read();
        while (true) {
            int data3 = read;
            if (data3 == 125) {
                break;
            }
            this.tokenNameBuffer.append((char) data3);
            read = this.pushbackReader.read();
        }
        this.tokenValue = this.tokenResolver.resolveToken(this.tokenNameBuffer.toString());
        if (this.tokenValue == null) {
            this.tokenValue = "${" + this.tokenNameBuffer.toString() + "}";
        }
        if (this.tokenValue.length() == 0) {
            return read();
        }
        String str2 = this.tokenValue;
        int i2 = this.tokenValueIndex;
        this.tokenValueIndex = i2 + 1;
        return str2.charAt(i2);
    }

    @Override // java.io.Reader
    public int read(char[] cbuf) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    @Override // java.io.Reader
    public int read(char[] cbuf, int off, int len) throws IOException {
        int charsRead = 0;
        int i = 0;
        while (true) {
            if (i >= len) {
                break;
            }
            int nextChar = read();
            if (nextChar == -1) {
                if (charsRead == 0) {
                    charsRead = -1;
                }
            } else {
                charsRead = i + 1;
                cbuf[off + i] = (char) nextChar;
                i++;
            }
        }
        return charsRead;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.pushbackReader.close();
    }

    @Override // java.io.Reader
    public long skip(long n) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        return this.pushbackReader.ready();
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.Reader
    public void mark(int readAheadLimit) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }
}
