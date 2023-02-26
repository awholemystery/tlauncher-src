package org.apache.commons.compress.archivers.sevenz;

import java.util.BitSet;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Archive.class */
class Archive {
    long packPos;
    BitSet packCrcsDefined;
    long[] packCrcs;
    SubStreamsInfo subStreamsInfo;
    StreamMap streamMap;
    long[] packSizes = new long[0];
    Folder[] folders = Folder.EMPTY_FOLDER_ARRAY;
    SevenZArchiveEntry[] files = SevenZArchiveEntry.EMPTY_SEVEN_Z_ARCHIVE_ENTRY_ARRAY;

    public String toString() {
        return "Archive with packed streams starting at offset " + this.packPos + ", " + lengthOf(this.packSizes) + " pack sizes, " + lengthOf(this.packCrcs) + " CRCs, " + lengthOf(this.folders) + " folders, " + lengthOf(this.files) + " files and " + this.streamMap;
    }

    private static String lengthOf(long[] a) {
        return a == null ? "(null)" : String.valueOf(a.length);
    }

    private static String lengthOf(Object[] a) {
        return a == null ? "(null)" : String.valueOf(a.length);
    }
}
