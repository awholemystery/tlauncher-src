package org.apache.commons.compress.harmony.unpack200.bytecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.compress.harmony.unpack200.Segment;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/bytecode/ClassConstantPool.class */
public class ClassConstantPool {
    protected Map<ClassFileEntry, Integer> indexCache;
    private boolean resolved;
    protected HashSet<ClassFileEntry> entriesContainsSet = new HashSet<>();
    protected HashSet<ClassFileEntry> othersContainsSet = new HashSet<>();
    private final HashSet<ClassFileEntry> mustStartClassPool = new HashSet<>();
    private final List<ClassFileEntry> others = new ArrayList(500);
    private final List<ClassFileEntry> entries = new ArrayList(500);

    public ClassFileEntry add(ClassFileEntry entry) {
        if (entry instanceof ByteCode) {
            return null;
        }
        if (entry instanceof ConstantPoolEntry) {
            if (this.entriesContainsSet.add(entry)) {
                this.entries.add(entry);
            }
        } else if (this.othersContainsSet.add(entry)) {
            this.others.add(entry);
        }
        return entry;
    }

    public void addNestedEntries() {
        boolean added = true;
        List<ClassFileEntry> parents = new ArrayList<>(512);
        List<ClassFileEntry> children = new ArrayList<>(512);
        parents.addAll(this.entries);
        parents.addAll(this.others);
        while (true) {
            if (added || parents.size() > 0) {
                children.clear();
                int entriesOriginalSize = this.entries.size();
                int othersOriginalSize = this.others.size();
                for (int indexParents = 0; indexParents < parents.size(); indexParents++) {
                    ClassFileEntry entry = parents.get(indexParents);
                    ClassFileEntry[] entryChildren = entry.getNestedClassFileEntries();
                    children.addAll(Arrays.asList(entryChildren));
                    boolean isAtStart = (entry instanceof ByteCode) && ((ByteCode) entry).nestedMustStartClassPool();
                    if (isAtStart) {
                        this.mustStartClassPool.addAll(Arrays.asList(entryChildren));
                    }
                    add(entry);
                }
                added = (this.entries.size() == entriesOriginalSize && this.others.size() == othersOriginalSize) ? false : true;
                parents.clear();
                parents.addAll(children);
            } else {
                return;
            }
        }
    }

    public int indexOf(ClassFileEntry entry) {
        if (!this.resolved) {
            throw new IllegalStateException("Constant pool is not yet resolved; this does not make any sense");
        }
        if (null == this.indexCache) {
            throw new IllegalStateException("Index cache is not initialized!");
        }
        Integer entryIndex = this.indexCache.get(entry);
        if (entryIndex != null) {
            return entryIndex.intValue() + 1;
        }
        return -1;
    }

    public int size() {
        return this.entries.size();
    }

    public ClassFileEntry get(int i) {
        if (!this.resolved) {
            throw new IllegalStateException("Constant pool is not yet resolved; this does not make any sense");
        }
        return this.entries.get(i - 1);
    }

    public void resolve(Segment segment) {
        initialSort();
        sortClassPool();
        this.resolved = true;
        this.entries.forEach(entry -> {
            entry.resolve(this);
        });
        this.others.forEach(entry2 -> {
            entry2.resolve(this);
        });
    }

    private void initialSort() {
        TreeSet<ClassFileEntry> inCpAll = new TreeSet<>(Comparator.comparingInt(arg0 -> {
            return ((ConstantPoolEntry) arg0).getGlobalIndex();
        }));
        TreeSet<ClassFileEntry> cpUtf8sNotInCpAll = new TreeSet<>(Comparator.comparing(arg02 -> {
            return ((CPUTF8) arg02).underlyingString();
        }));
        TreeSet<ClassFileEntry> cpClassesNotInCpAll = new TreeSet<>(Comparator.comparing(arg03 -> {
            return ((CPClass) arg03).getName();
        }));
        for (ClassFileEntry entry2 : this.entries) {
            ConstantPoolEntry entry = (ConstantPoolEntry) entry2;
            if (entry.getGlobalIndex() == -1) {
                if (entry instanceof CPUTF8) {
                    cpUtf8sNotInCpAll.add(entry);
                } else if (entry instanceof CPClass) {
                    cpClassesNotInCpAll.add(entry);
                } else {
                    throw new Error("error");
                }
            } else {
                inCpAll.add(entry);
            }
        }
        this.entries.clear();
        this.entries.addAll(inCpAll);
        this.entries.addAll(cpUtf8sNotInCpAll);
        this.entries.addAll(cpClassesNotInCpAll);
    }

    public List<ClassFileEntry> entries() {
        return Collections.unmodifiableList(this.entries);
    }

    protected void sortClassPool() {
        List<ClassFileEntry> startOfPool = new ArrayList<>(this.entries.size());
        List<ClassFileEntry> finalSort = new ArrayList<>(this.entries.size());
        for (ClassFileEntry entry : this.entries) {
            if (this.mustStartClassPool.contains(entry)) {
                startOfPool.add(entry);
            } else {
                finalSort.add(entry);
            }
        }
        this.indexCache = new HashMap(this.entries.size());
        int index = 0;
        this.entries.clear();
        for (ClassFileEntry entry2 : startOfPool) {
            this.indexCache.put(entry2, Integer.valueOf(index));
            if ((entry2 instanceof CPLong) || (entry2 instanceof CPDouble)) {
                this.entries.add(entry2);
                this.entries.add(entry2);
                index += 2;
            } else {
                this.entries.add(entry2);
                index++;
            }
        }
        for (ClassFileEntry entry3 : finalSort) {
            this.indexCache.put(entry3, Integer.valueOf(index));
            if ((entry3 instanceof CPLong) || (entry3 instanceof CPDouble)) {
                this.entries.add(entry3);
                this.entries.add(entry3);
                index += 2;
            } else {
                this.entries.add(entry3);
                index++;
            }
        }
    }

    public ClassFileEntry addWithNestedEntries(ClassFileEntry entry) {
        ClassFileEntry[] nestedClassFileEntries;
        add(entry);
        for (ClassFileEntry nestedEntry : entry.getNestedClassFileEntries()) {
            addWithNestedEntries(nestedEntry);
        }
        return entry;
    }
}
