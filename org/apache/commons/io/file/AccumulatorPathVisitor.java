package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.file.Counters;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/file/AccumulatorPathVisitor.class */
public class AccumulatorPathVisitor extends CountingPathVisitor {
    private final List<Path> dirList;
    private final List<Path> fileList;

    public static AccumulatorPathVisitor withBigIntegerCounters() {
        return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters());
    }

    public static AccumulatorPathVisitor withBigIntegerCounters(PathFilter fileFilter, PathFilter dirFilter) {
        return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters(), fileFilter, dirFilter);
    }

    public static AccumulatorPathVisitor withLongCounters() {
        return new AccumulatorPathVisitor(Counters.longPathCounters());
    }

    public static AccumulatorPathVisitor withLongCounters(PathFilter fileFilter, PathFilter dirFilter) {
        return new AccumulatorPathVisitor(Counters.longPathCounters(), fileFilter, dirFilter);
    }

    public AccumulatorPathVisitor() {
        super(Counters.noopPathCounters());
        this.dirList = new ArrayList();
        this.fileList = new ArrayList();
    }

    public AccumulatorPathVisitor(Counters.PathCounters pathCounter) {
        super(pathCounter);
        this.dirList = new ArrayList();
        this.fileList = new ArrayList();
    }

    public AccumulatorPathVisitor(Counters.PathCounters pathCounter, PathFilter fileFilter, PathFilter dirFilter) {
        super(pathCounter, fileFilter, dirFilter);
        this.dirList = new ArrayList();
        this.fileList = new ArrayList();
    }

    private void add(List<Path> list, Path dir) {
        list.add(dir.normalize());
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof AccumulatorPathVisitor)) {
            return false;
        }
        AccumulatorPathVisitor other = (AccumulatorPathVisitor) obj;
        return Objects.equals(this.dirList, other.dirList) && Objects.equals(this.fileList, other.fileList);
    }

    public List<Path> getDirList() {
        return this.dirList;
    }

    public List<Path> getFileList() {
        return this.fileList;
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + Objects.hash(this.dirList, this.fileList);
    }

    public List<Path> relativizeDirectories(Path parent, boolean sort, Comparator<? super Path> comparator) {
        return PathUtils.relativize(getDirList(), parent, sort, comparator);
    }

    public List<Path> relativizeFiles(Path parent, boolean sort, Comparator<? super Path> comparator) {
        return PathUtils.relativize(getFileList(), parent, sort, comparator);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.io.file.CountingPathVisitor
    public void updateDirCounter(Path dir, IOException exc) {
        super.updateDirCounter(dir, exc);
        add(this.dirList, dir);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.io.file.CountingPathVisitor
    public void updateFileCounters(Path file, BasicFileAttributes attributes) {
        super.updateFileCounters(file, attributes);
        add(this.fileList, file);
    }
}
