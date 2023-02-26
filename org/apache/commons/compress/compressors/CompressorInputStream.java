package org.apache.commons.compress.compressors;

import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/CompressorInputStream.class */
public abstract class CompressorInputStream extends InputStream {
    private long bytesRead;

    /* JADX INFO: Access modifiers changed from: protected */
    public void count(int read) {
        count(read);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void count(long read) {
        if (read != -1) {
            this.bytesRead += read;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void pushedBackBytes(long pushedBack) {
        this.bytesRead -= pushedBack;
    }

    @Deprecated
    public int getCount() {
        return (int) this.bytesRead;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }

    public long getUncompressedCount() {
        return getBytesRead();
    }
}
