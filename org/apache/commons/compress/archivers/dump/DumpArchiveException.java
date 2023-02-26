package org.apache.commons.compress.archivers.dump;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/dump/DumpArchiveException.class */
public class DumpArchiveException extends IOException {
    private static final long serialVersionUID = 1;

    public DumpArchiveException() {
    }

    public DumpArchiveException(String msg) {
        super(msg);
    }

    public DumpArchiveException(Throwable cause) {
        initCause(cause);
    }

    public DumpArchiveException(String msg, Throwable cause) {
        super(msg);
        initCause(cause);
    }
}
