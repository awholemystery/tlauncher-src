package org.apache.commons.compress.archivers.zip;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ScatterStatistics.class */
public class ScatterStatistics {
    private final long compressionElapsed;
    private final long mergingElapsed;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ScatterStatistics(long compressionElapsed, long mergingElapsed) {
        this.compressionElapsed = compressionElapsed;
        this.mergingElapsed = mergingElapsed;
    }

    public long getCompressionElapsed() {
        return this.compressionElapsed;
    }

    public long getMergingElapsed() {
        return this.mergingElapsed;
    }

    public String toString() {
        return "compressionElapsed=" + this.compressionElapsed + "ms, mergingElapsed=" + this.mergingElapsed + "ms";
    }
}
