package org.apache.commons.io.filefilter;

import ch.qos.logback.core.joran.action.Action;
import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/filefilter/FileEqualsFileFilter.class */
public class FileEqualsFileFilter extends AbstractFileFilter {
    private final File file;
    private final Path path;

    public FileEqualsFileFilter(File file) {
        this.file = (File) Objects.requireNonNull(file, Action.FILE_ATTRIBUTE);
        this.path = file.toPath();
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return Objects.equals(this.file, file);
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        return toFileVisitResult(Objects.equals(this.path, path), path);
    }
}
