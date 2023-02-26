package org.apache.commons.io.output;

import java.io.Writer;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/output/CloseShieldWriter.class */
public class CloseShieldWriter extends ProxyWriter {
    public static CloseShieldWriter wrap(Writer writer) {
        return new CloseShieldWriter(writer);
    }

    @Deprecated
    public CloseShieldWriter(Writer writer) {
        super(writer);
    }

    @Override // org.apache.commons.io.output.ProxyWriter, java.io.FilterWriter, java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.out = ClosedWriter.CLOSED_WRITER;
    }
}
