package org.tlauncher.util.stream;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/stream/SafeOutputStream.class */
public abstract class SafeOutputStream extends OutputStream {
    @Override // java.io.OutputStream
    public abstract void write(int i);

    @Override // java.io.OutputStream
    public void write(byte[] b) {
        try {
            super.write(b);
        } catch (IOException e) {
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) {
        try {
            super.write(b, off, len);
        } catch (IOException e) {
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() {
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
