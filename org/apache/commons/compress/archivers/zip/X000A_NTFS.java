package org.apache.commons.compress.archivers.zip;

import ch.qos.logback.core.spi.AbstractComponentTracker;
import java.util.Date;
import java.util.Objects;
import java.util.zip.ZipException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/X000A_NTFS.class */
public class X000A_NTFS implements ZipExtraField {
    private static final ZipShort HEADER_ID = new ZipShort(10);
    private static final ZipShort TIME_ATTR_TAG = new ZipShort(1);
    private static final ZipShort TIME_ATTR_SIZE = new ZipShort(24);
    private ZipEightByteInteger modifyTime = ZipEightByteInteger.ZERO;
    private ZipEightByteInteger accessTime = ZipEightByteInteger.ZERO;
    private ZipEightByteInteger createTime = ZipEightByteInteger.ZERO;
    private static final long EPOCH_OFFSET = -116444736000000000L;

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(32);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getCentralDirectoryLength() {
        return getLocalFileDataLength();
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public byte[] getLocalFileDataData() {
        byte[] data = new byte[getLocalFileDataLength().getValue()];
        System.arraycopy(TIME_ATTR_TAG.getBytes(), 0, data, 4, 2);
        int pos = 4 + 2;
        System.arraycopy(TIME_ATTR_SIZE.getBytes(), 0, data, pos, 2);
        int pos2 = pos + 2;
        System.arraycopy(this.modifyTime.getBytes(), 0, data, pos2, 8);
        int pos3 = pos2 + 8;
        System.arraycopy(this.accessTime.getBytes(), 0, data, pos3, 8);
        System.arraycopy(this.createTime.getBytes(), 0, data, pos3 + 8, 8);
        return data;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public byte[] getCentralDirectoryData() {
        return getLocalFileDataData();
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
        int len = offset + length;
        int offset2 = offset + 4;
        while (offset2 + 4 <= len) {
            ZipShort tag = new ZipShort(data, offset2);
            int offset3 = offset2 + 2;
            if (tag.equals(TIME_ATTR_TAG)) {
                readTimeAttr(data, offset3, len - offset3);
                return;
            } else {
                ZipShort size = new ZipShort(data, offset3);
                offset2 = offset3 + 2 + size.getValue();
            }
        }
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) throws ZipException {
        reset();
        parseFromLocalFileData(buffer, offset, length);
    }

    public ZipEightByteInteger getModifyTime() {
        return this.modifyTime;
    }

    public ZipEightByteInteger getAccessTime() {
        return this.accessTime;
    }

    public ZipEightByteInteger getCreateTime() {
        return this.createTime;
    }

    public Date getModifyJavaTime() {
        return zipToDate(this.modifyTime);
    }

    public Date getAccessJavaTime() {
        return zipToDate(this.accessTime);
    }

    public Date getCreateJavaTime() {
        return zipToDate(this.createTime);
    }

    public void setModifyTime(ZipEightByteInteger t) {
        this.modifyTime = t == null ? ZipEightByteInteger.ZERO : t;
    }

    public void setAccessTime(ZipEightByteInteger t) {
        this.accessTime = t == null ? ZipEightByteInteger.ZERO : t;
    }

    public void setCreateTime(ZipEightByteInteger t) {
        this.createTime = t == null ? ZipEightByteInteger.ZERO : t;
    }

    public void setModifyJavaTime(Date d) {
        setModifyTime(dateToZip(d));
    }

    public void setAccessJavaTime(Date d) {
        setAccessTime(dateToZip(d));
    }

    public void setCreateJavaTime(Date d) {
        setCreateTime(dateToZip(d));
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("0x000A Zip Extra Field:").append(" Modify:[").append(getModifyJavaTime()).append("] ").append(" Access:[").append(getAccessJavaTime()).append("] ").append(" Create:[").append(getCreateJavaTime()).append("] ");
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof X000A_NTFS) {
            X000A_NTFS xf = (X000A_NTFS) o;
            return Objects.equals(this.modifyTime, xf.modifyTime) && Objects.equals(this.accessTime, xf.accessTime) && Objects.equals(this.createTime, xf.createTime);
        }
        return false;
    }

    public int hashCode() {
        int hc = -123;
        if (this.modifyTime != null) {
            hc = (-123) ^ this.modifyTime.hashCode();
        }
        if (this.accessTime != null) {
            hc ^= Integer.rotateLeft(this.accessTime.hashCode(), 11);
        }
        if (this.createTime != null) {
            hc ^= Integer.rotateLeft(this.createTime.hashCode(), 22);
        }
        return hc;
    }

    private void reset() {
        this.modifyTime = ZipEightByteInteger.ZERO;
        this.accessTime = ZipEightByteInteger.ZERO;
        this.createTime = ZipEightByteInteger.ZERO;
    }

    private void readTimeAttr(byte[] data, int offset, int length) {
        if (length >= 26) {
            ZipShort tagValueLength = new ZipShort(data, offset);
            if (TIME_ATTR_SIZE.equals(tagValueLength)) {
                int offset2 = offset + 2;
                this.modifyTime = new ZipEightByteInteger(data, offset2);
                int offset3 = offset2 + 8;
                this.accessTime = new ZipEightByteInteger(data, offset3);
                this.createTime = new ZipEightByteInteger(data, offset3 + 8);
            }
        }
    }

    private static ZipEightByteInteger dateToZip(Date d) {
        if (d == null) {
            return null;
        }
        return new ZipEightByteInteger((d.getTime() * AbstractComponentTracker.LINGERING_TIMEOUT) - EPOCH_OFFSET);
    }

    private static Date zipToDate(ZipEightByteInteger z) {
        if (z == null || ZipEightByteInteger.ZERO.equals(z)) {
            return null;
        }
        long l = (z.getLongValue() + EPOCH_OFFSET) / AbstractComponentTracker.LINGERING_TIMEOUT;
        return new Date(l);
    }
}
