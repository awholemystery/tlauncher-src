package org.apache.commons.compress.compressors.snappy;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/snappy/FramedSnappyDialect.class */
public enum FramedSnappyDialect {
    STANDARD(true, true),
    IWORK_ARCHIVE(false, false);
    
    private final boolean streamIdentifier;
    private final boolean checksumWithCompressedChunks;

    FramedSnappyDialect(boolean hasStreamIdentifier, boolean usesChecksumWithCompressedChunks) {
        this.streamIdentifier = hasStreamIdentifier;
        this.checksumWithCompressedChunks = usesChecksumWithCompressedChunks;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasStreamIdentifier() {
        return this.streamIdentifier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean usesChecksumWithCompressedChunks() {
        return this.checksumWithCompressedChunks;
    }
}
