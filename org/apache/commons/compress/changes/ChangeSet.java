package org.apache.commons.compress.changes;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/changes/ChangeSet.class */
public final class ChangeSet {
    private final Set<Change> changes = new LinkedHashSet();

    public void delete(String fileName) {
        addDeletion(new Change(fileName, 1));
    }

    public void deleteDir(String dirName) {
        addDeletion(new Change(dirName, 4));
    }

    public void add(ArchiveEntry pEntry, InputStream pInput) {
        add(pEntry, pInput, true);
    }

    public void add(ArchiveEntry pEntry, InputStream pInput, boolean replace) {
        addAddition(new Change(pEntry, pInput, replace));
    }

    private void addAddition(Change pChange) {
        if (2 != pChange.type() || pChange.getInput() == null) {
            return;
        }
        if (!this.changes.isEmpty()) {
            Iterator<Change> it = this.changes.iterator();
            while (it.hasNext()) {
                Change change = it.next();
                if (change.type() == 2 && change.getEntry() != null) {
                    ArchiveEntry entry = change.getEntry();
                    if (entry.equals(pChange.getEntry())) {
                        if (pChange.isReplaceMode()) {
                            it.remove();
                            this.changes.add(pChange);
                            return;
                        }
                        return;
                    }
                }
            }
        }
        this.changes.add(pChange);
    }

    private void addDeletion(Change pChange) {
        String target;
        if ((1 != pChange.type() && 4 != pChange.type()) || pChange.targetFile() == null) {
            return;
        }
        String source = pChange.targetFile();
        if (source != null && !this.changes.isEmpty()) {
            Iterator<Change> it = this.changes.iterator();
            while (it.hasNext()) {
                Change change = it.next();
                if (change.type() == 2 && change.getEntry() != null && (target = change.getEntry().getName()) != null && ((1 == pChange.type() && source.equals(target)) || (4 == pChange.type() && target.matches(source + "/.*")))) {
                    it.remove();
                }
            }
        }
        this.changes.add(pChange);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<Change> getChanges() {
        return new LinkedHashSet(this.changes);
    }
}
