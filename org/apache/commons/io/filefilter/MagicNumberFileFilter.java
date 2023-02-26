package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/filefilter/MagicNumberFileFilter.class */
public class MagicNumberFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = -547733176983104172L;
    private final byte[] magicNumbers;
    private final long byteOffset;

    public MagicNumberFileFilter(byte[] magicNumber) {
        this(magicNumber, 0L);
    }

    public MagicNumberFileFilter(byte[] magicNumber, long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        }
        if (magicNumber.length == 0) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }
        this.magicNumbers = IOUtils.byteArray(magicNumber.length);
        System.arraycopy(magicNumber, 0, this.magicNumbers, 0, magicNumber.length);
        this.byteOffset = offset;
    }

    public MagicNumberFileFilter(String magicNumber) {
        this(magicNumber, 0L);
    }

    public MagicNumberFileFilter(String magicNumber, long offset) {
        if (magicNumber == null) {
            throw new IllegalArgumentException("The magic number cannot be null");
        }
        if (magicNumber.isEmpty()) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative");
        }
        this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset());
        this.byteOffset = offset;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        if (file != null && file.isFile() && file.canRead()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                byte[] fileBytes = IOUtils.byteArray(this.magicNumbers.length);
                randomAccessFile.seek(this.byteOffset);
                int read = randomAccessFile.read(fileBytes);
                if (read == this.magicNumbers.length) {
                    boolean equals = Arrays.equals(this.magicNumbers, fileBytes);
                    if (randomAccessFile != null) {
                        if (0 != 0) {
                            randomAccessFile.close();
                        } else {
                            randomAccessFile.close();
                        }
                    }
                    return equals;
                }
                if (randomAccessFile != null) {
                    if (0 != 0) {
                        randomAccessFile.close();
                    } else {
                        randomAccessFile.close();
                    }
                }
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path file, BasicFileAttributes attributes) {
        if (file != null && Files.isRegularFile(file, new LinkOption[0]) && Files.isReadable(file)) {
            try {
                FileChannel fileChannel = FileChannel.open(file, new OpenOption[0]);
                ByteBuffer byteBuffer = ByteBuffer.allocate(this.magicNumbers.length);
                int read = fileChannel.read(byteBuffer);
                if (read != this.magicNumbers.length) {
                    FileVisitResult fileVisitResult = FileVisitResult.TERMINATE;
                    if (fileChannel != null) {
                        if (0 != 0) {
                            fileChannel.close();
                        } else {
                            fileChannel.close();
                        }
                    }
                    return fileVisitResult;
                }
                FileVisitResult fileVisitResult2 = toFileVisitResult(Arrays.equals(this.magicNumbers, byteBuffer.array()), file);
                if (fileChannel != null) {
                    if (0 != 0) {
                        fileChannel.close();
                    } else {
                        fileChannel.close();
                    }
                }
                return fileVisitResult2;
            } catch (IOException e) {
            }
        }
        return FileVisitResult.TERMINATE;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        return super.toString() + "(" + new String(this.magicNumbers, Charset.defaultCharset()) + "," + this.byteOffset + ")";
    }
}
