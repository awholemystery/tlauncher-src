package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.io.file.PathFilter;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/filefilter/IOFileFilter.class */
public interface IOFileFilter extends FileFilter, FilenameFilter, PathFilter {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    @Override // java.io.FileFilter
    boolean accept(File file);

    boolean accept(File file, String str);

    default FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        return AbstractFileFilter.toFileVisitResult(accept(path.toFile()), path);
    }

    default IOFileFilter and(IOFileFilter fileFilter) {
        return new AndFileFilter(this, fileFilter);
    }

    default IOFileFilter negate() {
        return new NotFileFilter(this);
    }

    default IOFileFilter or(IOFileFilter fileFilter) {
        return new OrFileFilter(this, fileFilter);
    }
}
