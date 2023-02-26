package org.apache.commons.compress.parallel;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/parallel/ScatterGatherBackingStore.class */
public interface ScatterGatherBackingStore extends Closeable {
    InputStream getInputStream() throws IOException;

    void writeOut(byte[] bArr, int i, int i2) throws IOException;

    void closeForWriting() throws IOException;
}
