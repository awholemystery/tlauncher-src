package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import org.apache.commons.compress.utils.FileNameUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ZipSplitOutputStream.class */
class ZipSplitOutputStream extends OutputStream {
    private OutputStream outputStream;
    private Path zipFile;
    private final long splitSize;
    private int currentSplitSegmentIndex;
    private long currentSplitSegmentBytesWritten;
    private boolean finished;
    private final byte[] singleByte;
    private static final long ZIP_SEGMENT_MIN_SIZE = 65536;
    private static final long ZIP_SEGMENT_MAX_SIZE = 4294967295L;

    public ZipSplitOutputStream(File zipFile, long splitSize) throws IllegalArgumentException, IOException {
        this(zipFile.toPath(), splitSize);
    }

    public ZipSplitOutputStream(Path zipFile, long splitSize) throws IllegalArgumentException, IOException {
        this.singleByte = new byte[1];
        if (splitSize < ZIP_SEGMENT_MIN_SIZE || splitSize > ZIP_SEGMENT_MAX_SIZE) {
            throw new IllegalArgumentException("zip split segment size should between 64K and 4,294,967,295");
        }
        this.zipFile = zipFile;
        this.splitSize = splitSize;
        this.outputStream = Files.newOutputStream(zipFile, new OpenOption[0]);
        writeZipSplitSignature();
    }

    public void prepareToWriteUnsplittableContent(long unsplittableContentSize) throws IllegalArgumentException, IOException {
        if (unsplittableContentSize > this.splitSize) {
            throw new IllegalArgumentException("The unsplittable content size is bigger than the split segment size");
        }
        long bytesRemainingInThisSegment = this.splitSize - this.currentSplitSegmentBytesWritten;
        if (bytesRemainingInThisSegment < unsplittableContentSize) {
            openNewSplitSegment();
        }
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        this.singleByte[0] = (byte) (i & 255);
        write(this.singleByte);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        if (len <= 0) {
            return;
        }
        if (this.currentSplitSegmentBytesWritten >= this.splitSize) {
            openNewSplitSegment();
            write(b, off, len);
        } else if (this.currentSplitSegmentBytesWritten + len > this.splitSize) {
            int bytesToWriteForThisSegment = ((int) this.splitSize) - ((int) this.currentSplitSegmentBytesWritten);
            write(b, off, bytesToWriteForThisSegment);
            openNewSplitSegment();
            write(b, off + bytesToWriteForThisSegment, len - bytesToWriteForThisSegment);
        } else {
            this.outputStream.write(b, off, len);
            this.currentSplitSegmentBytesWritten += len;
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.finished) {
            finish();
        }
    }

    private void finish() throws IOException {
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        String zipFileBaseName = FileNameUtils.getBaseName(this.zipFile);
        this.outputStream.close();
        Files.move(this.zipFile, this.zipFile.resolveSibling(zipFileBaseName + ".zip"), StandardCopyOption.ATOMIC_MOVE);
        this.finished = true;
    }

    private void openNewSplitSegment() throws IOException {
        if (this.currentSplitSegmentIndex == 0) {
            this.outputStream.close();
            Files.move(this.zipFile, createNewSplitSegmentFile(1), StandardCopyOption.ATOMIC_MOVE);
        }
        Path newFile = createNewSplitSegmentFile(null);
        this.outputStream.close();
        this.outputStream = Files.newOutputStream(newFile, new OpenOption[0]);
        this.currentSplitSegmentBytesWritten = 0L;
        this.zipFile = newFile;
        this.currentSplitSegmentIndex++;
    }

    private void writeZipSplitSignature() throws IOException {
        this.outputStream.write(ZipArchiveOutputStream.DD_SIG);
        this.currentSplitSegmentBytesWritten += ZipArchiveOutputStream.DD_SIG.length;
    }

    private Path createNewSplitSegmentFile(Integer zipSplitSegmentSuffixIndex) throws IOException {
        int newZipSplitSegmentSuffixIndex = zipSplitSegmentSuffixIndex == null ? this.currentSplitSegmentIndex + 2 : zipSplitSegmentSuffixIndex.intValue();
        String baseName = FileNameUtils.getBaseName(this.zipFile);
        String extension = newZipSplitSegmentSuffixIndex <= 9 ? ".z0" + newZipSplitSegmentSuffixIndex : ".z" + newZipSplitSegmentSuffixIndex;
        Path parent = this.zipFile.getParent();
        String dir = Objects.nonNull(parent) ? parent.toAbsolutePath().toString() : ".";
        Path newFile = this.zipFile.getFileSystem().getPath(dir, baseName + extension);
        if (Files.exists(newFile, new LinkOption[0])) {
            throw new IOException("split zip segment " + baseName + extension + " already exists");
        }
        return newFile;
    }

    public int getCurrentSplitSegmentIndex() {
        return this.currentSplitSegmentIndex;
    }

    public long getCurrentSplitSegmentBytesWritten() {
        return this.currentSplitSegmentBytesWritten;
    }
}
