package com.sothawo.mapjfx.cache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:com/sothawo/mapjfx/cache/WriteCacheFileInputStream.class */
class WriteCacheFileInputStream extends FilterInputStream {
    private final OutputStream out;
    private Runnable notifyOnClose;

    /* JADX INFO: Access modifiers changed from: protected */
    public WriteCacheFileInputStream(InputStream in, OutputStream out) {
        super(in);
        this.out = out;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int numBytes = super.read(b, off, len);
        if (null != this.out && numBytes > 0) {
            this.out.write(b, off, numBytes);
        }
        return numBytes;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        if (null != this.out) {
            this.out.flush();
            this.out.close();
        }
        if (null != this.notifyOnClose) {
            this.notifyOnClose.run();
        }
    }

    public void onInputStreamClose(Runnable r) {
        this.notifyOnClose = r;
    }
}
