package org.apache.commons.compress.changes;

import java.io.InputStream;
import java.util.Objects;
import org.apache.commons.compress.archivers.ArchiveEntry;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/changes/Change.class */
class Change {
    private final String targetFile;
    private final ArchiveEntry entry;
    private final InputStream input;
    private final boolean replaceMode;
    private final int type;
    static final int TYPE_DELETE = 1;
    static final int TYPE_ADD = 2;
    static final int TYPE_MOVE = 3;
    static final int TYPE_DELETE_DIR = 4;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Change(String fileName, int type) {
        Objects.requireNonNull(fileName, "fileName");
        this.targetFile = fileName;
        this.type = type;
        this.input = null;
        this.entry = null;
        this.replaceMode = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Change(ArchiveEntry archiveEntry, InputStream inputStream, boolean replace) {
        Objects.requireNonNull(archiveEntry, "archiveEntry");
        Objects.requireNonNull(inputStream, "inputStream");
        this.entry = archiveEntry;
        this.input = inputStream;
        this.type = 2;
        this.targetFile = null;
        this.replaceMode = replace;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArchiveEntry getEntry() {
        return this.entry;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputStream getInput() {
        return this.input;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String targetFile() {
        return this.targetFile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int type() {
        return this.type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReplaceMode() {
        return this.replaceMode;
    }
}
