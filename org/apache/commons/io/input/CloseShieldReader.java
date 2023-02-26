package org.apache.commons.io.input;

import java.io.Reader;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/CloseShieldReader.class */
public class CloseShieldReader extends ProxyReader {
    public static CloseShieldReader wrap(Reader reader) {
        return new CloseShieldReader(reader);
    }

    @Deprecated
    public CloseShieldReader(Reader reader) {
        super(reader);
    }

    @Override // org.apache.commons.io.input.ProxyReader, java.io.FilterReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.in = ClosedReader.CLOSED_READER;
    }
}
