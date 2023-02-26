package org.apache.commons.compress.archivers.tar;

import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/tar/TarArchiveStructSparse.class */
public final class TarArchiveStructSparse {
    private final long offset;
    private final long numbytes;

    public TarArchiveStructSparse(long offset, long numbytes) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset must not be negative");
        }
        if (numbytes < 0) {
            throw new IllegalArgumentException("numbytes must not be negative");
        }
        this.offset = offset;
        this.numbytes = numbytes;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TarArchiveStructSparse that = (TarArchiveStructSparse) o;
        return this.offset == that.offset && this.numbytes == that.numbytes;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.offset), Long.valueOf(this.numbytes));
    }

    public String toString() {
        return "TarArchiveStructSparse{offset=" + this.offset + ", numbytes=" + this.numbytes + '}';
    }

    public long getOffset() {
        return this.offset;
    }

    public long getNumbytes() {
        return this.numbytes;
    }
}
