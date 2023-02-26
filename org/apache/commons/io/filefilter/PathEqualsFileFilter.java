package org.apache.commons.io.filefilter;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/filefilter/PathEqualsFileFilter.class */
public class PathEqualsFileFilter extends AbstractFileFilter {
    private final Path path;

    public PathEqualsFileFilter(Path file) {
        this.path = file;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return Objects.equals(this.path, file.toPath());
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        return toFileVisitResult(Objects.equals(this.path, path), path);
    }
}
