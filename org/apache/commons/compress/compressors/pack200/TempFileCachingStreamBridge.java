package org.apache.commons.compress.compressors.pack200;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/pack200/TempFileCachingStreamBridge.class */
class TempFileCachingStreamBridge extends StreamBridge {
    private final File f = File.createTempFile("commons-compress", "packtemp");

    /* JADX INFO: Access modifiers changed from: package-private */
    public TempFileCachingStreamBridge() throws IOException {
        this.f.deleteOnExit();
        this.out = Files.newOutputStream(this.f.toPath(), new OpenOption[0]);
    }

    @Override // org.apache.commons.compress.compressors.pack200.StreamBridge
    InputStream getInputView() throws IOException {
        this.out.close();
        return new FilterInputStream(Files.newInputStream(this.f.toPath(), new OpenOption[0])) { // from class: org.apache.commons.compress.compressors.pack200.TempFileCachingStreamBridge.1
            @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                try {
                    super.close();
                } finally {
                    TempFileCachingStreamBridge.this.f.delete();
                }
            }
        };
    }
}
