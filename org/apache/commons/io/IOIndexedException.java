package org.apache.commons.io;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/IOIndexedException.class */
public class IOIndexedException extends IOException {
    private static final long serialVersionUID = 1;
    private final int index;

    public IOIndexedException(int index, Throwable cause) {
        super(toMessage(index, cause), cause);
        this.index = index;
    }

    protected static String toMessage(int index, Throwable cause) {
        String name = cause == null ? "Null" : cause.getClass().getSimpleName();
        String msg = cause == null ? "Null" : cause.getMessage();
        return String.format("%s #%,d: %s", name, Integer.valueOf(index), msg);
    }

    public int getIndex() {
        return this.index;
    }
}
