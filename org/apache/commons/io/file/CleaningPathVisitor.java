package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.file.Counters;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/file/CleaningPathVisitor.class */
public class CleaningPathVisitor extends CountingPathVisitor {
    private final String[] skip;
    private final boolean overrideReadOnly;

    public static CountingPathVisitor withBigIntegerCounters() {
        return new CleaningPathVisitor(Counters.bigIntegerPathCounters(), new String[0]);
    }

    public static CountingPathVisitor withLongCounters() {
        return new CleaningPathVisitor(Counters.longPathCounters(), new String[0]);
    }

    public CleaningPathVisitor(Counters.PathCounters pathCounter, DeleteOption[] deleteOption, String... skip) {
        super(pathCounter);
        String[] temp = skip != null ? (String[]) skip.clone() : EMPTY_STRING_ARRAY;
        Arrays.sort(temp);
        this.skip = temp;
        this.overrideReadOnly = StandardDeleteOption.overrideReadOnly(deleteOption);
    }

    public CleaningPathVisitor(Counters.PathCounters pathCounter, String... skip) {
        this(pathCounter, PathUtils.EMPTY_DELETE_OPTION_ARRAY, skip);
    }

    private boolean accept(Path path) {
        return Arrays.binarySearch(this.skip, Objects.toString(path.getFileName(), null)) < 0;
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        CleaningPathVisitor other = (CleaningPathVisitor) obj;
        return this.overrideReadOnly == other.overrideReadOnly && Arrays.equals(this.skip, other.skip);
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * result) + Arrays.hashCode(this.skip))) + Objects.hash(Boolean.valueOf(this.overrideReadOnly));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.io.file.CountingPathVisitor, java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
        super.preVisitDirectory(dir, attributes);
        return accept(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.io.file.CountingPathVisitor, java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
        if (accept(file) && Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
            if (this.overrideReadOnly) {
                PathUtils.setReadOnly(file, false, LinkOption.NOFOLLOW_LINKS);
            }
            Files.deleteIfExists(file);
        }
        updateFileCounters(file, attributes);
        return FileVisitResult.CONTINUE;
    }
}
