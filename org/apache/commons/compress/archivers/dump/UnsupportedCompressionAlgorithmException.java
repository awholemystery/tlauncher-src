package org.apache.commons.compress.archivers.dump;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/dump/UnsupportedCompressionAlgorithmException.class */
public class UnsupportedCompressionAlgorithmException extends DumpArchiveException {
    private static final long serialVersionUID = 1;

    public UnsupportedCompressionAlgorithmException() {
        super("this file uses an unsupported compression algorithm.");
    }

    public UnsupportedCompressionAlgorithmException(String alg) {
        super("this file uses an unsupported compression algorithm: " + alg + ".");
    }
}
