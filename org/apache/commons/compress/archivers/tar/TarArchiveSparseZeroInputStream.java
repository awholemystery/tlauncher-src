package org.apache.commons.compress.archivers.tar;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/tar/TarArchiveSparseZeroInputStream.class */
class TarArchiveSparseZeroInputStream extends InputStream {
    @Override // java.io.InputStream
    public int read() throws IOException {
        return 0;
    }

    @Override // java.io.InputStream
    public long skip(long n) {
        return n;
    }
}
