package org.apache.commons.compress.archivers.dump;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/dump/UnrecognizedFormatException.class */
public class UnrecognizedFormatException extends DumpArchiveException {
    private static final long serialVersionUID = 1;

    public UnrecognizedFormatException() {
        super("this is not a recognized format.");
    }
}
