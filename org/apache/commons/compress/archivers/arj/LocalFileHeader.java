package org.apache.commons.compress.archivers.arj;

import java.util.Arrays;
import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/arj/LocalFileHeader.class */
class LocalFileHeader {
    int archiverVersionNumber;
    int minVersionToExtract;
    int hostOS;
    int arjFlags;
    int method;
    int fileType;
    int reserved;
    int dateTimeModified;
    long compressedSize;
    long originalSize;
    long originalCrc32;
    int fileSpecPosition;
    int fileAccessMode;
    int firstChapter;
    int lastChapter;
    int extendedFilePosition;
    int dateTimeAccessed;
    int dateTimeCreated;
    int originalSizeEvenForVolumes;
    String name;
    String comment;
    byte[][] extendedHeaders;

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/arj/LocalFileHeader$Flags.class */
    static class Flags {
        static final int GARBLED = 1;
        static final int VOLUME = 4;
        static final int EXTFILE = 8;
        static final int PATHSYM = 16;
        static final int BACKUP = 32;

        Flags() {
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/arj/LocalFileHeader$FileTypes.class */
    static class FileTypes {
        static final int BINARY = 0;
        static final int SEVEN_BIT_TEXT = 1;
        static final int COMMENT_HEADER = 2;
        static final int DIRECTORY = 3;
        static final int VOLUME_LABEL = 4;
        static final int CHAPTER_LABEL = 5;

        FileTypes() {
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/arj/LocalFileHeader$Methods.class */
    static class Methods {
        static final int STORED = 0;
        static final int COMPRESSED_MOST = 1;
        static final int COMPRESSED = 2;
        static final int COMPRESSED_FASTER = 3;
        static final int COMPRESSED_FASTEST = 4;
        static final int NO_DATA_NO_CRC = 8;
        static final int NO_DATA = 9;

        Methods() {
        }
    }

    public String toString() {
        return "LocalFileHeader [archiverVersionNumber=" + this.archiverVersionNumber + ", minVersionToExtract=" + this.minVersionToExtract + ", hostOS=" + this.hostOS + ", arjFlags=" + this.arjFlags + ", method=" + this.method + ", fileType=" + this.fileType + ", reserved=" + this.reserved + ", dateTimeModified=" + this.dateTimeModified + ", compressedSize=" + this.compressedSize + ", originalSize=" + this.originalSize + ", originalCrc32=" + this.originalCrc32 + ", fileSpecPosition=" + this.fileSpecPosition + ", fileAccessMode=" + this.fileAccessMode + ", firstChapter=" + this.firstChapter + ", lastChapter=" + this.lastChapter + ", extendedFilePosition=" + this.extendedFilePosition + ", dateTimeAccessed=" + this.dateTimeAccessed + ", dateTimeCreated=" + this.dateTimeCreated + ", originalSizeEvenForVolumes=" + this.originalSizeEvenForVolumes + ", name=" + this.name + ", comment=" + this.comment + ", extendedHeaders=" + Arrays.toString(this.extendedHeaders) + "]";
    }

    public int hashCode() {
        if (this.name == null) {
            return 0;
        }
        return this.name.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LocalFileHeader other = (LocalFileHeader) obj;
        return this.archiverVersionNumber == other.archiverVersionNumber && this.minVersionToExtract == other.minVersionToExtract && this.hostOS == other.hostOS && this.arjFlags == other.arjFlags && this.method == other.method && this.fileType == other.fileType && this.reserved == other.reserved && this.dateTimeModified == other.dateTimeModified && this.compressedSize == other.compressedSize && this.originalSize == other.originalSize && this.originalCrc32 == other.originalCrc32 && this.fileSpecPosition == other.fileSpecPosition && this.fileAccessMode == other.fileAccessMode && this.firstChapter == other.firstChapter && this.lastChapter == other.lastChapter && this.extendedFilePosition == other.extendedFilePosition && this.dateTimeAccessed == other.dateTimeAccessed && this.dateTimeCreated == other.dateTimeCreated && this.originalSizeEvenForVolumes == other.originalSizeEvenForVolumes && Objects.equals(this.name, other.name) && Objects.equals(this.comment, other.comment) && Arrays.deepEquals(this.extendedHeaders, other.extendedHeaders);
    }
}
