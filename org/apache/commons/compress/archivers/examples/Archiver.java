package org.apache.commons.compress.archivers.examples;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Objects;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/examples/Archiver.class */
public class Archiver {
    public static final EnumSet<FileVisitOption> EMPTY_FileVisitOption = EnumSet.noneOf(FileVisitOption.class);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/examples/Archiver$ArchiverFileVisitor.class */
    public static class ArchiverFileVisitor extends SimpleFileVisitor<Path> {
        private final ArchiveOutputStream target;
        private final Path directory;
        private final LinkOption[] linkOptions;

        private ArchiverFileVisitor(ArchiveOutputStream target, Path directory, LinkOption... linkOptions) {
            this.target = target;
            this.directory = directory;
            this.linkOptions = linkOptions == null ? IOUtils.EMPTY_LINK_OPTIONS : (LinkOption[]) linkOptions.clone();
        }

        @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return visit(dir, attrs, false);
        }

        protected FileVisitResult visit(Path path, BasicFileAttributes attrs, boolean isFile) throws IOException {
            Objects.requireNonNull(path);
            Objects.requireNonNull(attrs);
            String name = this.directory.relativize(path).toString().replace('\\', '/');
            if (!name.isEmpty()) {
                ArchiveEntry archiveEntry = this.target.createArchiveEntry(path, (isFile || name.endsWith("/")) ? name : name + "/", this.linkOptions);
                this.target.putArchiveEntry(archiveEntry);
                if (isFile) {
                    Files.copy(path, this.target);
                }
                this.target.closeArchiveEntry();
            }
            return FileVisitResult.CONTINUE;
        }

        @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            return visit(file, attrs, true);
        }
    }

    public void create(ArchiveOutputStream target, File directory) throws IOException {
        create(target, directory.toPath(), EMPTY_FileVisitOption, new LinkOption[0]);
    }

    public void create(ArchiveOutputStream target, Path directory, EnumSet<FileVisitOption> fileVisitOptions, LinkOption... linkOptions) throws IOException {
        Files.walkFileTree(directory, fileVisitOptions, Integer.MAX_VALUE, new ArchiverFileVisitor(target, directory, linkOptions));
        target.finish();
    }

    public void create(ArchiveOutputStream target, Path directory) throws IOException {
        create(target, directory, EMPTY_FileVisitOption, new LinkOption[0]);
    }

    public void create(SevenZOutputFile target, File directory) throws IOException {
        create(target, directory.toPath());
    }

    public void create(final SevenZOutputFile target, final Path directory) throws IOException {
        Files.walkFileTree(directory, new ArchiverFileVisitor(null, directory, new LinkOption[0]) { // from class: org.apache.commons.compress.archivers.examples.Archiver.1
            @Override // org.apache.commons.compress.archivers.examples.Archiver.ArchiverFileVisitor
            protected FileVisitResult visit(Path path, BasicFileAttributes attrs, boolean isFile) throws IOException {
                Objects.requireNonNull(path);
                Objects.requireNonNull(attrs);
                String name = directory.relativize(path).toString().replace('\\', '/');
                if (!name.isEmpty()) {
                    ArchiveEntry archiveEntry = target.createArchiveEntry(path, (isFile || name.endsWith("/")) ? name : name + "/", new LinkOption[0]);
                    target.putArchiveEntry(archiveEntry);
                    if (isFile) {
                        target.write(path, new OpenOption[0]);
                    }
                    target.closeArchiveEntry();
                }
                return FileVisitResult.CONTINUE;
            }
        });
        target.finish();
    }

    public void create(String format, File target, File directory) throws IOException, ArchiveException {
        create(format, target.toPath(), directory.toPath());
    }

    @Deprecated
    public void create(String format, OutputStream target, File directory) throws IOException, ArchiveException {
        create(format, target, directory, CloseableConsumer.NULL_CONSUMER);
    }

    public void create(String format, OutputStream target, File directory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            create((ArchiveOutputStream) c.track(ArchiveStreamFactory.DEFAULT.createArchiveOutputStream(format, target)), directory);
            if (c != null) {
                if (0 != 0) {
                    try {
                        c.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                c.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (c != null) {
                    if (th3 != null) {
                        try {
                            c.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        c.close();
                    }
                }
                throw th4;
            }
        }
    }

    public void create(String format, Path target, Path directory) throws IOException, ArchiveException {
        if (prefersSeekableByteChannel(format)) {
            SeekableByteChannel channel = FileChannel.open(target, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Throwable th = null;
            try {
                create(format, channel, directory);
                if (channel != null) {
                    if (0 != 0) {
                        try {
                            channel.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    channel.close();
                    return;
                }
                return;
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    if (channel != null) {
                        if (th3 != null) {
                            try {
                                channel.close();
                            } catch (Throwable th5) {
                                th3.addSuppressed(th5);
                            }
                        } else {
                            channel.close();
                        }
                    }
                    throw th4;
                }
            }
        }
        ArchiveOutputStream outputStream = ArchiveStreamFactory.DEFAULT.createArchiveOutputStream(format, Files.newOutputStream(target, new OpenOption[0]));
        Throwable th6 = null;
        try {
            create(outputStream, directory, EMPTY_FileVisitOption, new LinkOption[0]);
            if (outputStream != null) {
                if (0 != 0) {
                    try {
                        outputStream.close();
                        return;
                    } catch (Throwable th7) {
                        th6.addSuppressed(th7);
                        return;
                    }
                }
                outputStream.close();
            }
        } catch (Throwable th8) {
            try {
                throw th8;
            } catch (Throwable th9) {
                if (outputStream != null) {
                    if (th8 != null) {
                        try {
                            outputStream.close();
                        } catch (Throwable th10) {
                            th8.addSuppressed(th10);
                        }
                    } else {
                        outputStream.close();
                    }
                }
                throw th9;
            }
        }
    }

    @Deprecated
    public void create(String format, SeekableByteChannel target, File directory) throws IOException, ArchiveException {
        create(format, target, directory, CloseableConsumer.NULL_CONSUMER);
    }

    public void create(String format, SeekableByteChannel target, File directory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            if (!prefersSeekableByteChannel(format)) {
                create(format, (OutputStream) c.track(Channels.newOutputStream(target)), directory);
            } else if (ArchiveStreamFactory.ZIP.equalsIgnoreCase(format)) {
                create((ArchiveOutputStream) c.track(new ZipArchiveOutputStream(target)), directory);
            } else if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format)) {
                create((SevenZOutputFile) c.track(new SevenZOutputFile(target)), directory);
            } else {
                throw new ArchiveException("Don't know how to handle format " + format);
            }
            if (c != null) {
                if (0 != 0) {
                    try {
                        c.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                c.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (c != null) {
                    if (th3 != null) {
                        try {
                            c.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        c.close();
                    }
                }
                throw th4;
            }
        }
    }

    public void create(String format, SeekableByteChannel target, Path directory) throws IOException {
        if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format)) {
            SevenZOutputFile sevenZFile = new SevenZOutputFile(target);
            Throwable th = null;
            try {
                create(sevenZFile, directory);
                if (sevenZFile != null) {
                    if (0 != 0) {
                        try {
                            sevenZFile.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    sevenZFile.close();
                }
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    if (sevenZFile != null) {
                        if (th3 != null) {
                            try {
                                sevenZFile.close();
                            } catch (Throwable th5) {
                                th3.addSuppressed(th5);
                            }
                        } else {
                            sevenZFile.close();
                        }
                    }
                    throw th4;
                }
            }
        } else if (ArchiveStreamFactory.ZIP.equalsIgnoreCase(format)) {
            ArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(target);
            Throwable th6 = null;
            try {
                create(archiveOutputStream, directory, EMPTY_FileVisitOption, new LinkOption[0]);
                if (archiveOutputStream != null) {
                    if (0 != 0) {
                        try {
                            archiveOutputStream.close();
                            return;
                        } catch (Throwable th7) {
                            th6.addSuppressed(th7);
                            return;
                        }
                    }
                    archiveOutputStream.close();
                }
            } catch (Throwable th8) {
                try {
                    throw th8;
                } catch (Throwable th9) {
                    if (archiveOutputStream != null) {
                        if (th8 != null) {
                            try {
                                archiveOutputStream.close();
                            } catch (Throwable th10) {
                                th8.addSuppressed(th10);
                            }
                        } else {
                            archiveOutputStream.close();
                        }
                    }
                    throw th9;
                }
            }
        } else {
            throw new IllegalStateException(format);
        }
    }

    private boolean prefersSeekableByteChannel(String format) {
        return ArchiveStreamFactory.ZIP.equalsIgnoreCase(format) || ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format);
    }
}
