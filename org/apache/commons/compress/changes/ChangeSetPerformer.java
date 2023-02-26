package org.apache.commons.compress.changes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/changes/ChangeSetPerformer.class */
public class ChangeSetPerformer {
    private final Set<Change> changes;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/changes/ChangeSetPerformer$ArchiveEntryIterator.class */
    public interface ArchiveEntryIterator {
        boolean hasNext() throws IOException;

        ArchiveEntry next();

        InputStream getInputStream() throws IOException;
    }

    public ChangeSetPerformer(ChangeSet changeSet) {
        this.changes = changeSet.getChanges();
    }

    public ChangeSetResults perform(ArchiveInputStream in, ArchiveOutputStream out) throws IOException {
        return perform(new ArchiveInputStreamIterator(in), out);
    }

    public ChangeSetResults perform(ZipFile in, ArchiveOutputStream out) throws IOException {
        return perform(new ZipFileIterator(in), out);
    }

    private ChangeSetResults perform(ArchiveEntryIterator entryIterator, ArchiveOutputStream out) throws IOException {
        ChangeSetResults results = new ChangeSetResults();
        Set<Change> workingSet = new LinkedHashSet<>(this.changes);
        Iterator<Change> it = workingSet.iterator();
        while (it.hasNext()) {
            Change change = it.next();
            if (change.type() == 2 && change.isReplaceMode()) {
                copyStream(change.getInput(), out, change.getEntry());
                it.remove();
                results.addedFromChangeSet(change.getEntry().getName());
            }
        }
        while (entryIterator.hasNext()) {
            ArchiveEntry entry = entryIterator.next();
            boolean copy = true;
            Iterator<Change> it2 = workingSet.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Change change2 = it2.next();
                int type = change2.type();
                String name = entry.getName();
                if (type == 1 && name != null) {
                    if (name.equals(change2.targetFile())) {
                        copy = false;
                        it2.remove();
                        results.deleted(name);
                        break;
                    }
                } else if (type == 4 && name != null && name.startsWith(change2.targetFile() + "/")) {
                    copy = false;
                    results.deleted(name);
                    break;
                }
            }
            if (copy && !isDeletedLater(workingSet, entry) && !results.hasBeenAdded(entry.getName())) {
                copyStream(entryIterator.getInputStream(), out, entry);
                results.addedFromStream(entry.getName());
            }
        }
        Iterator<Change> it3 = workingSet.iterator();
        while (it3.hasNext()) {
            Change change3 = it3.next();
            if (change3.type() == 2 && !change3.isReplaceMode() && !results.hasBeenAdded(change3.getEntry().getName())) {
                copyStream(change3.getInput(), out, change3.getEntry());
                it3.remove();
                results.addedFromChangeSet(change3.getEntry().getName());
            }
        }
        out.finish();
        return results;
    }

    private boolean isDeletedLater(Set<Change> workingSet, ArchiveEntry entry) {
        String source = entry.getName();
        if (!workingSet.isEmpty()) {
            for (Change change : workingSet) {
                int type = change.type();
                String target = change.targetFile();
                if (type == 1 && source.equals(target)) {
                    return true;
                }
                if (type == 4 && source.startsWith(target + "/")) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void copyStream(InputStream in, ArchiveOutputStream out, ArchiveEntry entry) throws IOException {
        out.putArchiveEntry(entry);
        IOUtils.copy(in, out);
        out.closeArchiveEntry();
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/changes/ChangeSetPerformer$ArchiveInputStreamIterator.class */
    private static class ArchiveInputStreamIterator implements ArchiveEntryIterator {
        private final ArchiveInputStream in;
        private ArchiveEntry next;

        ArchiveInputStreamIterator(ArchiveInputStream in) {
            this.in = in;
        }

        @Override // org.apache.commons.compress.changes.ChangeSetPerformer.ArchiveEntryIterator
        public boolean hasNext() throws IOException {
            ArchiveEntry nextEntry = this.in.getNextEntry();
            this.next = nextEntry;
            return nextEntry != null;
        }

        @Override // org.apache.commons.compress.changes.ChangeSetPerformer.ArchiveEntryIterator
        public ArchiveEntry next() {
            return this.next;
        }

        @Override // org.apache.commons.compress.changes.ChangeSetPerformer.ArchiveEntryIterator
        public InputStream getInputStream() {
            return this.in;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/changes/ChangeSetPerformer$ZipFileIterator.class */
    private static class ZipFileIterator implements ArchiveEntryIterator {
        private final ZipFile in;
        private final Enumeration<ZipArchiveEntry> nestedEnum;
        private ZipArchiveEntry current;

        ZipFileIterator(ZipFile in) {
            this.in = in;
            this.nestedEnum = in.getEntriesInPhysicalOrder();
        }

        @Override // org.apache.commons.compress.changes.ChangeSetPerformer.ArchiveEntryIterator
        public boolean hasNext() {
            return this.nestedEnum.hasMoreElements();
        }

        @Override // org.apache.commons.compress.changes.ChangeSetPerformer.ArchiveEntryIterator
        public ArchiveEntry next() {
            this.current = this.nestedEnum.nextElement();
            return this.current;
        }

        @Override // org.apache.commons.compress.changes.ChangeSetPerformer.ArchiveEntryIterator
        public InputStream getInputStream() throws IOException {
            return this.in.getInputStream(this.current);
        }
    }
}
