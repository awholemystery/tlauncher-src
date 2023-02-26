package org.apache.commons.compress.archivers;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/ArchiveException.class */
public class ArchiveException extends Exception {
    private static final long serialVersionUID = 2772690708123267100L;

    public ArchiveException(String message) {
        super(message);
    }

    public ArchiveException(String message, Exception cause) {
        super(message);
        initCause(cause);
    }
}
