package org.apache.commons.compress.archivers.dump;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.zip.UnixStat;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/dump/DumpArchiveEntry.class */
public class DumpArchiveEntry implements ArchiveEntry {
    private String name;
    private int mode;
    private long size;
    private long atime;
    private long mtime;
    private int uid;
    private int gid;
    private String simpleName;
    private String originalName;
    private int volume;
    private long offset;
    private int ino;
    private int nlink;
    private long ctime;
    private int generation;
    private boolean isDeleted;
    private TYPE type = TYPE.UNKNOWN;
    private Set<PERMISSION> permissions = Collections.emptySet();
    private final DumpArchiveSummary summary = null;
    private final TapeSegmentHeader header = new TapeSegmentHeader();

    public DumpArchiveEntry() {
    }

    public DumpArchiveEntry(String name, String simpleName) {
        setName(name);
        this.simpleName = simpleName;
    }

    protected DumpArchiveEntry(String name, String simpleName, int ino, TYPE type) {
        setType(type);
        setName(name);
        this.simpleName = simpleName;
        this.ino = ino;
        this.offset = 0L;
    }

    public String getSimpleName() {
        return this.simpleName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public int getIno() {
        return this.header.getIno();
    }

    public int getNlink() {
        return this.nlink;
    }

    public void setNlink(int nlink) {
        this.nlink = nlink;
    }

    public Date getCreationTime() {
        return new Date(this.ctime);
    }

    public void setCreationTime(Date ctime) {
        this.ctime = ctime.getTime();
    }

    public int getGeneration() {
        return this.generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getOffset() {
        return this.offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public DumpArchiveConstants.SEGMENT_TYPE getHeaderType() {
        return this.header.getType();
    }

    public int getHeaderCount() {
        return this.header.getCount();
    }

    public int getHeaderHoles() {
        return this.header.getHoles();
    }

    public boolean isSparseRecord(int idx) {
        return (this.header.getCdata(idx) & 1) == 0;
    }

    public int hashCode() {
        return this.ino;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !o.getClass().equals(getClass())) {
            return false;
        }
        DumpArchiveEntry rhs = (DumpArchiveEntry) o;
        if (this.ino != rhs.ino) {
            return false;
        }
        if (this.summary != null || rhs.summary == null) {
            if (this.summary != null && !this.summary.equals(rhs.summary)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        return getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DumpArchiveEntry parse(byte[] buffer) {
        DumpArchiveEntry entry = new DumpArchiveEntry();
        TapeSegmentHeader header = entry.header;
        header.type = DumpArchiveConstants.SEGMENT_TYPE.find(DumpArchiveUtil.convert32(buffer, 0));
        header.volume = DumpArchiveUtil.convert32(buffer, 12);
        entry.ino = header.ino = DumpArchiveUtil.convert32(buffer, 20);
        int m = DumpArchiveUtil.convert16(buffer, 32);
        entry.setType(TYPE.find((m >> 12) & 15));
        entry.setMode(m);
        entry.nlink = DumpArchiveUtil.convert16(buffer, 34);
        entry.setSize(DumpArchiveUtil.convert64(buffer, 40));
        long t = (1000 * DumpArchiveUtil.convert32(buffer, 48)) + (DumpArchiveUtil.convert32(buffer, 52) / 1000);
        entry.setAccessTime(new Date(t));
        long t2 = (1000 * DumpArchiveUtil.convert32(buffer, 56)) + (DumpArchiveUtil.convert32(buffer, 60) / 1000);
        entry.setLastModifiedDate(new Date(t2));
        long t3 = (1000 * DumpArchiveUtil.convert32(buffer, 64)) + (DumpArchiveUtil.convert32(buffer, 68) / 1000);
        entry.ctime = t3;
        entry.generation = DumpArchiveUtil.convert32(buffer, 140);
        entry.setUserId(DumpArchiveUtil.convert32(buffer, 144));
        entry.setGroupId(DumpArchiveUtil.convert32(buffer, TarConstants.CHKSUM_OFFSET));
        header.count = DumpArchiveUtil.convert32(buffer, 160);
        header.holes = 0;
        for (int i = 0; i < 512 && i < header.count; i++) {
            if (buffer[164 + i] == 0) {
                TapeSegmentHeader.access$408(header);
            }
        }
        System.arraycopy(buffer, 164, header.cdata, 0, 512);
        entry.volume = header.getVolume();
        return entry;
    }

    void update(byte[] buffer) {
        this.header.volume = DumpArchiveUtil.convert32(buffer, 16);
        this.header.count = DumpArchiveUtil.convert32(buffer, 160);
        this.header.holes = 0;
        for (int i = 0; i < 512 && i < this.header.count; i++) {
            if (buffer[164 + i] == 0) {
                TapeSegmentHeader.access$408(this.header);
            }
        }
        System.arraycopy(buffer, 164, this.header.cdata, 0, 512);
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/dump/DumpArchiveEntry$TapeSegmentHeader.class */
    static class TapeSegmentHeader {
        private DumpArchiveConstants.SEGMENT_TYPE type;
        private int volume;
        private int ino;
        private int count;
        private int holes;
        private final byte[] cdata = new byte[512];

        TapeSegmentHeader() {
        }

        static /* synthetic */ int access$408(TapeSegmentHeader x0) {
            int i = x0.holes;
            x0.holes = i + 1;
            return i;
        }

        public DumpArchiveConstants.SEGMENT_TYPE getType() {
            return this.type;
        }

        public int getVolume() {
            return this.volume;
        }

        public int getIno() {
            return this.ino;
        }

        void setIno(int ino) {
            this.ino = ino;
        }

        public int getCount() {
            return this.count;
        }

        public int getHoles() {
            return this.holes;
        }

        public int getCdata(int idx) {
            return this.cdata[idx];
        }
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public String getName() {
        return this.name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getOriginalName() {
        return this.originalName;
    }

    public final void setName(String name) {
        this.originalName = name;
        if (name != null) {
            if (isDirectory() && !name.endsWith("/")) {
                name = name + "/";
            }
            if (name.startsWith("./")) {
                name = name.substring(2);
            }
        }
        this.name = name;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public Date getLastModifiedDate() {
        return new Date(this.mtime);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public boolean isDirectory() {
        return this.type == TYPE.DIRECTORY;
    }

    public boolean isFile() {
        return this.type == TYPE.FILE;
    }

    public boolean isSocket() {
        return this.type == TYPE.SOCKET;
    }

    public boolean isChrDev() {
        return this.type == TYPE.CHRDEV;
    }

    public boolean isBlkDev() {
        return this.type == TYPE.BLKDEV;
    }

    public boolean isFifo() {
        return this.type == TYPE.FIFO;
    }

    public TYPE getType() {
        return this.type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode & UnixStat.PERM_MASK;
        this.permissions = PERMISSION.find(mode);
    }

    public Set<PERMISSION> getPermissions() {
        return this.permissions;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public long getSize() {
        if (isDirectory()) {
            return -1L;
        }
        return this.size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getEntrySize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setLastModifiedDate(Date mtime) {
        this.mtime = mtime.getTime();
    }

    public Date getAccessTime() {
        return new Date(this.atime);
    }

    public void setAccessTime(Date atime) {
        this.atime = atime.getTime();
    }

    public int getUserId() {
        return this.uid;
    }

    public void setUserId(int uid) {
        this.uid = uid;
    }

    public int getGroupId() {
        return this.gid;
    }

    public void setGroupId(int gid) {
        this.gid = gid;
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/dump/DumpArchiveEntry$TYPE.class */
    public enum TYPE {
        WHITEOUT(14),
        SOCKET(12),
        LINK(10),
        FILE(8),
        BLKDEV(6),
        DIRECTORY(4),
        CHRDEV(2),
        FIFO(1),
        UNKNOWN(15);
        
        private final int code;

        TYPE(int code) {
            this.code = code;
        }

        public static TYPE find(int code) {
            TYPE[] values;
            TYPE type = UNKNOWN;
            for (TYPE t : values()) {
                if (code == t.code) {
                    type = t;
                }
            }
            return type;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/dump/DumpArchiveEntry$PERMISSION.class */
    public enum PERMISSION {
        SETUID(2048),
        SETGUI(1024),
        STICKY(512),
        USER_READ(256),
        USER_WRITE(128),
        USER_EXEC(64),
        GROUP_READ(32),
        GROUP_WRITE(16),
        GROUP_EXEC(8),
        WORLD_READ(4),
        WORLD_WRITE(2),
        WORLD_EXEC(1);
        
        private final int code;

        PERMISSION(int code) {
            this.code = code;
        }

        public static Set<PERMISSION> find(int code) {
            PERMISSION[] values;
            Set<PERMISSION> set = new HashSet<>();
            for (PERMISSION p : values()) {
                if ((code & p.code) == p.code) {
                    set.add(p);
                }
            }
            if (set.isEmpty()) {
                return Collections.emptySet();
            }
            return EnumSet.copyOf((Collection) set);
        }
    }
}
