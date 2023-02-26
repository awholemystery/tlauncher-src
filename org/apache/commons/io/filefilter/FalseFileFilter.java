package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/filefilter/FalseFileFilter.class */
public class FalseFileFilter implements IOFileFilter, Serializable {
    private static final String TO_STRING = Boolean.FALSE.toString();
    public static final IOFileFilter FALSE = new FalseFileFilter();
    public static final IOFileFilter INSTANCE = FALSE;
    private static final long serialVersionUID = 6210271677940926200L;

    protected FalseFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return false;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return false;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
        return FileVisitResult.TERMINATE;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter
    public IOFileFilter negate() {
        return TrueFileFilter.INSTANCE;
    }

    public String toString() {
        return TO_STRING;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter
    public IOFileFilter and(IOFileFilter fileFilter) {
        return INSTANCE;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter
    public IOFileFilter or(IOFileFilter fileFilter) {
        return fileFilter;
    }
}
