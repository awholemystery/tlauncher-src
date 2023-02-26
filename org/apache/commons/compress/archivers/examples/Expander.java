package org.apache.commons.compress.archivers.examples;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Enumeration;
import java.util.Iterator;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/examples/Expander.class */
public class Expander {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/examples/Expander$ArchiveEntrySupplier.class */
    public interface ArchiveEntrySupplier {
        ArchiveEntry getNextReadableEntry() throws IOException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/examples/Expander$EntryWriter.class */
    public interface EntryWriter {
        void writeEntryDataTo(ArchiveEntry archiveEntry, OutputStream outputStream) throws IOException;
    }

    private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, Path targetDirectory) throws IOException {
        boolean nullTarget = targetDirectory == null;
        Path targetDirPath = nullTarget ? null : targetDirectory.normalize();
        ArchiveEntry nextReadableEntry = supplier.getNextReadableEntry();
        while (true) {
            ArchiveEntry nextEntry = nextReadableEntry;
            if (nextEntry != null) {
                Path targetPath = nullTarget ? null : targetDirectory.resolve(nextEntry.getName());
                if (!nullTarget && !targetPath.normalize().startsWith(targetDirPath) && !Files.isSameFile(targetDirectory, targetPath)) {
                    throw new IOException("Expanding " + nextEntry.getName() + " would create file outside of " + targetDirectory);
                }
                if (nextEntry.isDirectory()) {
                    if (!nullTarget && !Files.isDirectory(targetPath, new LinkOption[0]) && Files.createDirectories(targetPath, new FileAttribute[0]) == null) {
                        throw new IOException("Failed to create directory " + targetPath);
                    }
                } else {
                    Path parent = nullTarget ? null : targetPath.getParent();
                    if (!nullTarget && !Files.isDirectory(parent, new LinkOption[0]) && Files.createDirectories(parent, new FileAttribute[0]) == null) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    if (nullTarget) {
                        writer.writeEntryDataTo(nextEntry, null);
                    } else {
                        OutputStream outputStream = Files.newOutputStream(targetPath, new OpenOption[0]);
                        Throwable th = null;
                        try {
                            writer.writeEntryDataTo(nextEntry, outputStream);
                            if (outputStream != null) {
                                if (0 != 0) {
                                    try {
                                        outputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    outputStream.close();
                                }
                            }
                        } finally {
                        }
                    }
                }
                nextReadableEntry = supplier.getNextReadableEntry();
            } else {
                return;
            }
        }
    }

    public void expand(ArchiveInputStream archive, File targetDirectory) throws IOException {
        expand(archive, toPath(targetDirectory));
    }

    public void expand(ArchiveInputStream archive, Path targetDirectory) throws IOException {
        expand(() -> {
            ArchiveEntry next;
            ArchiveEntry nextEntry = archive.getNextEntry();
            while (true) {
                next = nextEntry;
                if (next == null || archive.canReadEntryData(next)) {
                    break;
                }
                nextEntry = archive.getNextEntry();
            }
            return next;
        }, entry, out -> {
            IOUtils.copy(archive, out);
        }, targetDirectory);
    }

    public void expand(File archive, File targetDirectory) throws IOException, ArchiveException {
        expand(archive.toPath(), toPath(targetDirectory));
    }

    @Deprecated
    public void expand(InputStream archive, File targetDirectory) throws IOException, ArchiveException {
        expand(archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
    }

    public void expand(InputStream archive, File targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            expand((ArchiveInputStream) c.track(ArchiveStreamFactory.DEFAULT.createArchiveInputStream(archive)), targetDirectory);
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

    public void expand(Path archive, Path targetDirectory) throws IOException, ArchiveException {
        InputStream inputStream = new BufferedInputStream(Files.newInputStream(archive, new OpenOption[0]));
        Throwable th = null;
        try {
            String format = ArchiveStreamFactory.detect(inputStream);
            if (inputStream != null) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    inputStream.close();
                }
            }
            expand(format, archive, targetDirectory);
        } finally {
        }
    }

    public void expand(SevenZFile archive, File targetDirectory) throws IOException {
        expand(archive, toPath(targetDirectory));
    }

    public void expand(SevenZFile archive, Path targetDirectory) throws IOException {
        archive.getClass();
        expand(this::getNextEntry, entry, out -> {
            byte[] buffer = new byte[8192];
            while (true) {
                int n = archive.read(buffer);
                if (-1 != n) {
                    if (out != null) {
                        out.write(buffer, 0, n);
                    }
                } else {
                    return;
                }
            }
        }, targetDirectory);
    }

    public void expand(String format, File archive, File targetDirectory) throws IOException, ArchiveException {
        expand(format, archive.toPath(), toPath(targetDirectory));
    }

    @Deprecated
    public void expand(String format, InputStream archive, File targetDirectory) throws IOException, ArchiveException {
        expand(format, archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
    }

    public void expand(String format, InputStream archive, File targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
        expand(format, archive, toPath(targetDirectory), closeableConsumer);
    }

    public void expand(String format, InputStream archive, Path targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            expand((ArchiveInputStream) c.track(ArchiveStreamFactory.DEFAULT.createArchiveInputStream(format, archive)), targetDirectory);
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

    public void expand(String format, Path archive, Path targetDirectory) throws IOException, ArchiveException {
        if (prefersSeekableByteChannel(format)) {
            SeekableByteChannel channel = FileChannel.open(archive, StandardOpenOption.READ);
            Throwable th = null;
            try {
                expand(format, channel, targetDirectory, CloseableConsumer.CLOSING_CONSUMER);
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
        InputStream inputStream = new BufferedInputStream(Files.newInputStream(archive, new OpenOption[0]));
        Throwable th6 = null;
        try {
            expand(format, inputStream, targetDirectory, CloseableConsumer.CLOSING_CONSUMER);
            if (inputStream != null) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                        return;
                    } catch (Throwable th7) {
                        th6.addSuppressed(th7);
                        return;
                    }
                }
                inputStream.close();
            }
        } catch (Throwable th8) {
            try {
                throw th8;
            } catch (Throwable th9) {
                if (inputStream != null) {
                    if (th8 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable th10) {
                            th8.addSuppressed(th10);
                        }
                    } else {
                        inputStream.close();
                    }
                }
                throw th9;
            }
        }
    }

    @Deprecated
    public void expand(String format, SeekableByteChannel archive, File targetDirectory) throws IOException, ArchiveException {
        expand(format, archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
    }

    public void expand(String format, SeekableByteChannel archive, File targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
        expand(format, archive, toPath(targetDirectory), closeableConsumer);
    }

    public void expand(String format, SeekableByteChannel archive, Path targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
        CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer);
        Throwable th = null;
        try {
            if (!prefersSeekableByteChannel(format)) {
                expand(format, (InputStream) c.track(Channels.newInputStream(archive)), targetDirectory, CloseableConsumer.NULL_CONSUMER);
            } else if (ArchiveStreamFactory.TAR.equalsIgnoreCase(format)) {
                expand((TarFile) c.track(new TarFile(archive)), targetDirectory);
            } else if (ArchiveStreamFactory.ZIP.equalsIgnoreCase(format)) {
                expand((ZipFile) c.track(new ZipFile(archive)), targetDirectory);
            } else if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format)) {
                expand((SevenZFile) c.track(new SevenZFile(archive)), targetDirectory);
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

    public void expand(TarFile archive, File targetDirectory) throws IOException {
        expand(archive, toPath(targetDirectory));
    }

    public void expand(TarFile archive, Path targetDirectory) throws IOException {
        Iterator<TarArchiveEntry> entryIterator = archive.getEntries().iterator();
        expand(() -> {
            if (entryIterator.hasNext()) {
                return (ArchiveEntry) entryIterator.next();
            }
            return null;
        }, entry, out -> {
            InputStream in = archive.getInputStream((TarArchiveEntry) entry);
            Throwable th = null;
            try {
                IOUtils.copy(in, out);
                if (in != null) {
                    if (0 != 0) {
                        try {
                            in.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    in.close();
                }
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    if (in != null) {
                        if (th3 != null) {
                            try {
                                in.close();
                            } catch (Throwable th5) {
                                th3.addSuppressed(th5);
                            }
                        } else {
                            in.close();
                        }
                    }
                    throw th4;
                }
            }
        }, targetDirectory);
    }

    public void expand(ZipFile archive, File targetDirectory) throws IOException {
        expand(archive, toPath(targetDirectory));
    }

    public void expand(ZipFile archive, Path targetDirectory) throws IOException {
        Enumeration<ZipArchiveEntry> entries = archive.getEntries();
        expand(() -> {
            ZipArchiveEntry next;
            ZipArchiveEntry zipArchiveEntry = entries.hasMoreElements() ? (ZipArchiveEntry) entries.nextElement() : null;
            while (true) {
                next = zipArchiveEntry;
                if (next == null || archive.canReadEntryData(next)) {
                    break;
                }
                zipArchiveEntry = entries.hasMoreElements() ? (ZipArchiveEntry) entries.nextElement() : null;
            }
            return next;
        }, entry, out -> {
            InputStream in = archive.getInputStream((ZipArchiveEntry) entry);
            Throwable th = null;
            try {
                IOUtils.copy(in, out);
                if (in != null) {
                    if (0 != 0) {
                        try {
                            in.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    in.close();
                }
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    if (in != null) {
                        if (th3 != null) {
                            try {
                                in.close();
                            } catch (Throwable th5) {
                                th3.addSuppressed(th5);
                            }
                        } else {
                            in.close();
                        }
                    }
                    throw th4;
                }
            }
        }, targetDirectory);
    }

    private boolean prefersSeekableByteChannel(String format) {
        return ArchiveStreamFactory.TAR.equalsIgnoreCase(format) || ArchiveStreamFactory.ZIP.equalsIgnoreCase(format) || ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format);
    }

    private Path toPath(File targetDirectory) {
        if (targetDirectory != null) {
            return targetDirectory.toPath();
        }
        return null;
    }
}
