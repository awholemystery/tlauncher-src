package org.apache.commons.compress.archivers.jar;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.JarMarker;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/jar/JarArchiveOutputStream.class */
public class JarArchiveOutputStream extends ZipArchiveOutputStream {
    private boolean jarMarkerAdded;

    public JarArchiveOutputStream(OutputStream out) {
        super(out);
    }

    public JarArchiveOutputStream(OutputStream out, String encoding) {
        super(out);
        setEncoding(encoding);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream, org.apache.commons.compress.archivers.ArchiveOutputStream
    public void putArchiveEntry(ArchiveEntry ze) throws IOException {
        if (!this.jarMarkerAdded) {
            ((ZipArchiveEntry) ze).addAsFirstExtraField(JarMarker.getInstance());
            this.jarMarkerAdded = true;
        }
        super.putArchiveEntry(ze);
    }
}
