package org.apache.commons.compress.archivers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/ArchiveOutputStream.class */
public abstract class ArchiveOutputStream extends OutputStream {
    private final byte[] oneByte = new byte[1];
    static final int BYTE_MASK = 255;
    private long bytesWritten;

    public abstract void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException;

    public abstract void closeArchiveEntry() throws IOException;

    public abstract void finish() throws IOException;

    public abstract ArchiveEntry createArchiveEntry(File file, String str) throws IOException;

    public ArchiveEntry createArchiveEntry(Path inputPath, String entryName, LinkOption... options) throws IOException {
        return createArchiveEntry(inputPath.toFile(), entryName);
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.oneByte[0] = (byte) (b & BYTE_MASK);
        write(this.oneByte, 0, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void count(int written) {
        count(written);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void count(long written) {
        if (written != -1) {
            this.bytesWritten += written;
        }
    }

    @Deprecated
    public int getCount() {
        return (int) this.bytesWritten;
    }

    public long getBytesWritten() {
        return this.bytesWritten;
    }

    public boolean canWriteEntryData(ArchiveEntry archiveEntry) {
        return true;
    }
}
