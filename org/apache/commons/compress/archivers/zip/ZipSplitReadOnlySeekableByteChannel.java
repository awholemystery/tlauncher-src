package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ZipSplitReadOnlySeekableByteChannel.class */
public class ZipSplitReadOnlySeekableByteChannel extends MultiReadOnlySeekableByteChannel {
    private static final Path[] EMPTY_PATH_ARRAY = new Path[0];
    private static final int ZIP_SPLIT_SIGNATURE_LENGTH = 4;
    private final ByteBuffer zipSplitSignatureByteBuffer;

    public ZipSplitReadOnlySeekableByteChannel(List<SeekableByteChannel> channels) throws IOException {
        super(channels);
        this.zipSplitSignatureByteBuffer = ByteBuffer.allocate(4);
        assertSplitSignature(channels);
    }

    private void assertSplitSignature(List<SeekableByteChannel> channels) throws IOException {
        SeekableByteChannel channel = channels.get(0);
        channel.position(0L);
        this.zipSplitSignatureByteBuffer.rewind();
        channel.read(this.zipSplitSignatureByteBuffer);
        ZipLong signature = new ZipLong(this.zipSplitSignatureByteBuffer.array());
        if (!signature.equals(ZipLong.DD_SIG)) {
            channel.position(0L);
            throw new IOException("The first zip split segment does not begin with split zip file signature");
        } else {
            channel.position(0L);
        }
    }

    public static SeekableByteChannel forOrderedSeekableByteChannels(SeekableByteChannel... channels) throws IOException {
        if (((SeekableByteChannel[]) Objects.requireNonNull(channels, "channels must not be null")).length == 1) {
            return channels[0];
        }
        return new ZipSplitReadOnlySeekableByteChannel(Arrays.asList(channels));
    }

    public static SeekableByteChannel forOrderedSeekableByteChannels(SeekableByteChannel lastSegmentChannel, Iterable<SeekableByteChannel> channels) throws IOException {
        Objects.requireNonNull(channels, "channels");
        Objects.requireNonNull(lastSegmentChannel, "lastSegmentChannel");
        List<SeekableByteChannel> channelsList = new ArrayList<>();
        channelsList.getClass();
        channels.forEach((v1) -> {
            r1.add(v1);
        });
        channelsList.add(lastSegmentChannel);
        return forOrderedSeekableByteChannels((SeekableByteChannel[]) channelsList.toArray(new SeekableByteChannel[0]));
    }

    public static SeekableByteChannel buildFromLastSplitSegment(File lastSegmentFile) throws IOException {
        return buildFromLastSplitSegment(lastSegmentFile.toPath());
    }

    public static SeekableByteChannel buildFromLastSplitSegment(Path lastSegmentPath) throws IOException {
        String extension = FileNameUtils.getExtension(lastSegmentPath);
        if (!extension.equalsIgnoreCase(ArchiveStreamFactory.ZIP)) {
            throw new IllegalArgumentException("The extension of last zip split segment should be .zip");
        }
        Path parent = Objects.nonNull(lastSegmentPath.getParent()) ? lastSegmentPath.getParent() : lastSegmentPath.getFileSystem().getPath(".", new String[0]);
        String fileBaseName = FileNameUtils.getBaseName(lastSegmentPath);
        Pattern pattern = Pattern.compile(Pattern.quote(fileBaseName) + ".[zZ][0-9]+");
        Stream<Path> walk = Files.walk(parent, 1, new FileVisitOption[0]);
        Throwable th = null;
        try {
            ArrayList<Path> splitZipSegments = (ArrayList) walk.filter(x$0 -> {
                return Files.isRegularFile(x$0, new LinkOption[0]);
            }).filter(path -> {
                return pattern.matcher(path.getFileName().toString()).matches();
            }).sorted(new ZipSplitSegmentComparator()).collect(Collectors.toCollection(ArrayList::new));
            if (walk != null) {
                if (0 != 0) {
                    try {
                        walk.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    walk.close();
                }
            }
            return forPaths(lastSegmentPath, splitZipSegments);
        } finally {
        }
    }

    public static SeekableByteChannel forFiles(File... files) throws IOException {
        File[] fileArr;
        List<Path> paths = new ArrayList<>();
        for (File f : (File[]) Objects.requireNonNull(files, "files must not be null")) {
            paths.add(f.toPath());
        }
        return forPaths((Path[]) paths.toArray(EMPTY_PATH_ARRAY));
    }

    public static SeekableByteChannel forPaths(Path... paths) throws IOException {
        Path[] pathArr;
        List<SeekableByteChannel> channels = new ArrayList<>();
        for (Path path : (Path[]) Objects.requireNonNull(paths, "paths must not be null")) {
            channels.add(Files.newByteChannel(path, StandardOpenOption.READ));
        }
        if (channels.size() == 1) {
            return channels.get(0);
        }
        return new ZipSplitReadOnlySeekableByteChannel(channels);
    }

    public static SeekableByteChannel forFiles(File lastSegmentFile, Iterable<File> files) throws IOException {
        Objects.requireNonNull(files, "files");
        Objects.requireNonNull(lastSegmentFile, "lastSegmentFile");
        List<Path> filesList = new ArrayList<>();
        files.forEach(f -> {
            filesList.add(f.toPath());
        });
        return forPaths(lastSegmentFile.toPath(), filesList);
    }

    public static SeekableByteChannel forPaths(Path lastSegmentPath, Iterable<Path> paths) throws IOException {
        Objects.requireNonNull(paths, "paths");
        Objects.requireNonNull(lastSegmentPath, "lastSegmentPath");
        List<Path> filesList = new ArrayList<>();
        filesList.getClass();
        paths.forEach((v1) -> {
            r1.add(v1);
        });
        filesList.add(lastSegmentPath);
        return forPaths((Path[]) filesList.toArray(EMPTY_PATH_ARRAY));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ZipSplitReadOnlySeekableByteChannel$ZipSplitSegmentComparator.class */
    public static class ZipSplitSegmentComparator implements Comparator<Path>, Serializable {
        private static final long serialVersionUID = 20200123;

        private ZipSplitSegmentComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Path file1, Path file2) {
            String extension1 = FileNameUtils.getExtension(file1);
            String extension2 = FileNameUtils.getExtension(file2);
            if (!extension1.startsWith(CompressorStreamFactory.Z)) {
                return -1;
            }
            if (!extension2.startsWith(CompressorStreamFactory.Z)) {
                return 1;
            }
            Integer splitSegmentNumber1 = Integer.valueOf(Integer.parseInt(extension1.substring(1)));
            Integer splitSegmentNumber2 = Integer.valueOf(Integer.parseInt(extension2.substring(1)));
            return splitSegmentNumber1.compareTo(splitSegmentNumber2);
        }
    }
}
