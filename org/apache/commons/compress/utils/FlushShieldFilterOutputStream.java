package org.apache.commons.compress.utils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/FlushShieldFilterOutputStream.class */
public class FlushShieldFilterOutputStream extends FilterOutputStream {
    public FlushShieldFilterOutputStream(OutputStream out) {
        super(out);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
    }
}
