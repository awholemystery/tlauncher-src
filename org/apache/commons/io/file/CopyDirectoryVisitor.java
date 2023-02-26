package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.file.Counters;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/file/CopyDirectoryVisitor.class */
public class CopyDirectoryVisitor extends CountingPathVisitor {
    private final CopyOption[] copyOptions;
    private final Path sourceDirectory;
    private final Path targetDirectory;

    public CopyDirectoryVisitor(Counters.PathCounters pathCounter, Path sourceDirectory, Path targetDirectory, CopyOption... copyOptions) {
        super(pathCounter);
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.copyOptions = copyOptions == null ? PathUtils.EMPTY_COPY_OPTIONS : (CopyOption[]) copyOptions.clone();
    }

    public CopyDirectoryVisitor(Counters.PathCounters pathCounter, PathFilter fileFilter, PathFilter dirFilter, Path sourceDirectory, Path targetDirectory, CopyOption... copyOptions) {
        super(pathCounter, fileFilter, dirFilter);
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.copyOptions = copyOptions == null ? PathUtils.EMPTY_COPY_OPTIONS : (CopyOption[]) copyOptions.clone();
    }

    protected void copy(Path sourceFile, Path targetFile) throws IOException {
        Files.copy(sourceFile, targetFile, this.copyOptions);
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        CopyDirectoryVisitor other = (CopyDirectoryVisitor) obj;
        return Arrays.equals(this.copyOptions, other.copyOptions) && Objects.equals(this.sourceDirectory, other.sourceDirectory) && Objects.equals(this.targetDirectory, other.targetDirectory);
    }

    public CopyOption[] getCopyOptions() {
        return (CopyOption[]) this.copyOptions.clone();
    }

    public Path getSourceDirectory() {
        return this.sourceDirectory;
    }

    public Path getTargetDirectory() {
        return this.targetDirectory;
    }

    @Override // org.apache.commons.io.file.CountingPathVisitor
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * result) + Arrays.hashCode(this.copyOptions))) + Objects.hash(this.sourceDirectory, this.targetDirectory);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.io.file.CountingPathVisitor, java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {
        Path newTargetDir = resolveRelativeAsString(directory);
        if (Files.notExists(newTargetDir, new LinkOption[0])) {
            Files.createDirectory(newTargetDir, new FileAttribute[0]);
        }
        return super.preVisitDirectory(directory, attributes);
    }

    private Path resolveRelativeAsString(Path directory) {
        return this.targetDirectory.resolve(this.sourceDirectory.relativize(directory).toString());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.io.file.CountingPathVisitor, java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attributes) throws IOException {
        Path targetFile = resolveRelativeAsString(sourceFile);
        copy(sourceFile, targetFile);
        return super.visitFile(targetFile, attributes);
    }
}
