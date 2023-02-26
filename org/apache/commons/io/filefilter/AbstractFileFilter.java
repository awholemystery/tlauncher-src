package org.apache.commons.io.filefilter;

import ch.qos.logback.core.joran.action.Action;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import org.apache.commons.io.file.PathVisitor;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/filefilter/AbstractFileFilter.class */
public abstract class AbstractFileFilter implements IOFileFilter, PathVisitor {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static FileVisitResult toFileVisitResult(boolean accept, Path path) {
        return accept ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        Objects.requireNonNull(file, Action.FILE_ATTRIBUTE);
        return accept(file.getParentFile(), file.getName());
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        Objects.requireNonNull(name, Action.NAME_ATTRIBUTE);
        return accept(new File(dir, name));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FileVisitResult handle(Throwable t) {
        return FileVisitResult.TERMINATE;
    }

    @Override // java.nio.file.FileVisitor
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override // java.nio.file.FileVisitor
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
        return accept(dir, attributes);
    }

    public String toString() {
        return getClass().getSimpleName();
    }

    @Override // java.nio.file.FileVisitor
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
        return accept(file, attributes);
    }

    @Override // java.nio.file.FileVisitor
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
