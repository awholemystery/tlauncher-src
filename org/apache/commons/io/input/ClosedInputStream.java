package org.apache.commons.io.input;

import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/ClosedInputStream.class */
public class ClosedInputStream extends InputStream {
    public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

    @Override // java.io.InputStream
    public int read() {
        return -1;
    }
}
