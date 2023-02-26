package org.tlauncher.util.stream;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/stream/StringStream.class */
public class StringStream extends SafeOutputStream {
    protected final StringBuilder buffer = new StringBuilder();
    protected int caret;

    @Override // org.tlauncher.util.stream.SafeOutputStream, java.io.OutputStream
    public void write(int b) {
        write((char) b);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void write(char c) {
        this.buffer.append(c);
        this.caret++;
    }

    public void write(char[] c) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c.length == 0) {
            return;
        }
        for (char c2 : c) {
            write(c2);
        }
    }

    @Override // org.tlauncher.util.stream.SafeOutputStream, java.io.OutputStream, java.io.Flushable
    public synchronized void flush() {
        this.caret = 0;
        this.buffer.setLength(0);
    }
}
