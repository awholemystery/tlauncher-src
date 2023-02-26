package org.apache.commons.compress.archivers.sevenz;

import ch.qos.logback.core.spi.AbstractComponentTracker;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.TimeZone;
import org.apache.commons.compress.archivers.ArchiveEntry;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/SevenZArchiveEntry.class */
public class SevenZArchiveEntry implements ArchiveEntry {
    private String name;
    private boolean hasStream;
    private boolean isDirectory;
    private boolean isAntiItem;
    private boolean hasCreationDate;
    private boolean hasLastModifiedDate;
    private boolean hasAccessDate;
    private long creationDate;
    private long lastModifiedDate;
    private long accessDate;
    private boolean hasWindowsAttributes;
    private int windowsAttributes;
    private boolean hasCrc;
    private long crc;
    private long compressedCrc;
    private long size;
    private long compressedSize;
    private Iterable<? extends SevenZMethodConfiguration> contentMethods;
    static final SevenZArchiveEntry[] EMPTY_SEVEN_Z_ARCHIVE_ENTRY_ARRAY = new SevenZArchiveEntry[0];

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasStream() {
        return this.hasStream;
    }

    public void setHasStream(boolean hasStream) {
        this.hasStream = hasStream;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public boolean isDirectory() {
        return this.isDirectory;
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public boolean isAntiItem() {
        return this.isAntiItem;
    }

    public void setAntiItem(boolean isAntiItem) {
        this.isAntiItem = isAntiItem;
    }

    public boolean getHasCreationDate() {
        return this.hasCreationDate;
    }

    public void setHasCreationDate(boolean hasCreationDate) {
        this.hasCreationDate = hasCreationDate;
    }

    public Date getCreationDate() {
        if (this.hasCreationDate) {
            return ntfsTimeToJavaTime(this.creationDate);
        }
        throw new UnsupportedOperationException("The entry doesn't have this timestamp");
    }

    public void setCreationDate(long ntfsCreationDate) {
        this.creationDate = ntfsCreationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.hasCreationDate = creationDate != null;
        if (this.hasCreationDate) {
            this.creationDate = javaTimeToNtfsTime(creationDate);
        }
    }

    public boolean getHasLastModifiedDate() {
        return this.hasLastModifiedDate;
    }

    public void setHasLastModifiedDate(boolean hasLastModifiedDate) {
        this.hasLastModifiedDate = hasLastModifiedDate;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public Date getLastModifiedDate() {
        if (this.hasLastModifiedDate) {
            return ntfsTimeToJavaTime(this.lastModifiedDate);
        }
        throw new UnsupportedOperationException("The entry doesn't have this timestamp");
    }

    public void setLastModifiedDate(long ntfsLastModifiedDate) {
        this.lastModifiedDate = ntfsLastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.hasLastModifiedDate = lastModifiedDate != null;
        if (this.hasLastModifiedDate) {
            this.lastModifiedDate = javaTimeToNtfsTime(lastModifiedDate);
        }
    }

    public boolean getHasAccessDate() {
        return this.hasAccessDate;
    }

    public void setHasAccessDate(boolean hasAcessDate) {
        this.hasAccessDate = hasAcessDate;
    }

    public Date getAccessDate() {
        if (this.hasAccessDate) {
            return ntfsTimeToJavaTime(this.accessDate);
        }
        throw new UnsupportedOperationException("The entry doesn't have this timestamp");
    }

    public void setAccessDate(long ntfsAccessDate) {
        this.accessDate = ntfsAccessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.hasAccessDate = accessDate != null;
        if (this.hasAccessDate) {
            this.accessDate = javaTimeToNtfsTime(accessDate);
        }
    }

    public boolean getHasWindowsAttributes() {
        return this.hasWindowsAttributes;
    }

    public void setHasWindowsAttributes(boolean hasWindowsAttributes) {
        this.hasWindowsAttributes = hasWindowsAttributes;
    }

    public int getWindowsAttributes() {
        return this.windowsAttributes;
    }

    public void setWindowsAttributes(int windowsAttributes) {
        this.windowsAttributes = windowsAttributes;
    }

    public boolean getHasCrc() {
        return this.hasCrc;
    }

    public void setHasCrc(boolean hasCrc) {
        this.hasCrc = hasCrc;
    }

    @Deprecated
    public int getCrc() {
        return (int) this.crc;
    }

    @Deprecated
    public void setCrc(int crc) {
        this.crc = crc;
    }

    public long getCrcValue() {
        return this.crc;
    }

    public void setCrcValue(long crc) {
        this.crc = crc;
    }

    @Deprecated
    int getCompressedCrc() {
        return (int) this.compressedCrc;
    }

    @Deprecated
    void setCompressedCrc(int crc) {
        this.compressedCrc = crc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCompressedCrcValue() {
        return this.compressedCrc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCompressedCrcValue(long crc) {
        this.compressedCrc = crc;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveEntry
    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCompressedSize() {
        return this.compressedSize;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCompressedSize(long size) {
        this.compressedSize = size;
    }

    public void setContentMethods(Iterable<? extends SevenZMethodConfiguration> methods) {
        if (methods != null) {
            LinkedList<SevenZMethodConfiguration> l = new LinkedList<>();
            l.getClass();
            methods.forEach((v1) -> {
                r1.addLast(v1);
            });
            this.contentMethods = Collections.unmodifiableList(l);
            return;
        }
        this.contentMethods = null;
    }

    public void setContentMethods(SevenZMethodConfiguration... methods) {
        setContentMethods(Arrays.asList(methods));
    }

    public Iterable<? extends SevenZMethodConfiguration> getContentMethods() {
        return this.contentMethods;
    }

    public int hashCode() {
        String n = getName();
        if (n == null) {
            return 0;
        }
        return n.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SevenZArchiveEntry other = (SevenZArchiveEntry) obj;
        return Objects.equals(this.name, other.name) && this.hasStream == other.hasStream && this.isDirectory == other.isDirectory && this.isAntiItem == other.isAntiItem && this.hasCreationDate == other.hasCreationDate && this.hasLastModifiedDate == other.hasLastModifiedDate && this.hasAccessDate == other.hasAccessDate && this.creationDate == other.creationDate && this.lastModifiedDate == other.lastModifiedDate && this.accessDate == other.accessDate && this.hasWindowsAttributes == other.hasWindowsAttributes && this.windowsAttributes == other.windowsAttributes && this.hasCrc == other.hasCrc && this.crc == other.crc && this.compressedCrc == other.compressedCrc && this.size == other.size && this.compressedSize == other.compressedSize && equalSevenZMethods(this.contentMethods, other.contentMethods);
    }

    public static Date ntfsTimeToJavaTime(long ntfsTime) {
        Calendar ntfsEpoch = Calendar.getInstance();
        ntfsEpoch.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        ntfsEpoch.set(1601, 0, 1, 0, 0, 0);
        ntfsEpoch.set(14, 0);
        long realTime = ntfsEpoch.getTimeInMillis() + (ntfsTime / AbstractComponentTracker.LINGERING_TIMEOUT);
        return new Date(realTime);
    }

    public static long javaTimeToNtfsTime(Date date) {
        Calendar ntfsEpoch = Calendar.getInstance();
        ntfsEpoch.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        ntfsEpoch.set(1601, 0, 1, 0, 0, 0);
        ntfsEpoch.set(14, 0);
        return (date.getTime() - ntfsEpoch.getTimeInMillis()) * 1000 * 10;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean equalSevenZMethods(java.lang.Iterable<? extends org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration> r4, java.lang.Iterable<? extends org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration> r5) {
        /*
            r3 = this;
            r0 = r4
            if (r0 != 0) goto Le
            r0 = r5
            if (r0 != 0) goto Lc
            r0 = 1
            goto Ld
        Lc:
            r0 = 0
        Ld:
            return r0
        Le:
            r0 = r5
            if (r0 != 0) goto L14
            r0 = 0
            return r0
        L14:
            r0 = r5
            java.util.Iterator r0 = r0.iterator()
            r6 = r0
            r0 = r4
            java.util.Iterator r0 = r0.iterator()
            r7 = r0
        L23:
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L57
            r0 = r7
            java.lang.Object r0 = r0.next()
            org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration r0 = (org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration) r0
            r8 = r0
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L44
            r0 = 0
            return r0
        L44:
            r0 = r8
            r1 = r6
            java.lang.Object r1 = r1.next()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L54
            r0 = 0
            return r0
        L54:
            goto L23
        L57:
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L64
            r0 = 1
            goto L65
        L64:
            r0 = 0
        L65:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry.equalSevenZMethods(java.lang.Iterable, java.lang.Iterable):boolean");
    }
}
