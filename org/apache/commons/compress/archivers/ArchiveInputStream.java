package org.apache.commons.compress.archivers;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/ArchiveInputStream.class */
public abstract class ArchiveInputStream extends InputStream {
    private final byte[] single = new byte[1];
    private static final int BYTE_MASK = 255;
    private long bytesRead;

    public abstract ArchiveEntry getNextEntry() throws IOException;

    @Override // java.io.InputStream
    public int read() throws IOException {
        int num = read(this.single, 0, 1);
        if (num == -1) {
            return -1;
        }
        return this.single[0] & BYTE_MASK;
    }

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

    public boolean canReadEntryData(ArchiveEntry archiveEntry) {
        return true;
    }
}
