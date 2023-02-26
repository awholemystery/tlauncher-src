package org.apache.commons.compress.archivers.zip;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.zip.ZipException;
import org.apache.commons.compress.utils.ByteUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/X7875_NewUnix.class */
public class X7875_NewUnix implements ZipExtraField, Cloneable, Serializable {
    private static final ZipShort HEADER_ID = new ZipShort(30837);
    private static final ZipShort ZERO = new ZipShort(0);
    private static final BigInteger ONE_THOUSAND = BigInteger.valueOf(1000);
    private static final long serialVersionUID = 1;
    private int version = 1;
    private BigInteger uid;
    private BigInteger gid;

    public X7875_NewUnix() {
        reset();
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    public long getUID() {
        return ZipUtil.bigToLong(this.uid);
    }

    public long getGID() {
        return ZipUtil.bigToLong(this.gid);
    }

    public void setUID(long l) {
        this.uid = ZipUtil.longToBig(l);
    }

    public void setGID(long l) {
        this.gid = ZipUtil.longToBig(l);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getLocalFileDataLength() {
        byte[] b = trimLeadingZeroesForceMinLength(this.uid.toByteArray());
        int uidSize = b == null ? 0 : b.length;
        byte[] b2 = trimLeadingZeroesForceMinLength(this.gid.toByteArray());
        int gidSize = b2 == null ? 0 : b2.length;
        return new ZipShort(3 + uidSize + gidSize);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getCentralDirectoryLength() {
        return ZERO;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public byte[] getLocalFileDataData() {
        byte[] uidBytes = this.uid.toByteArray();
        byte[] gidBytes = this.gid.toByteArray();
        byte[] uidBytes2 = trimLeadingZeroesForceMinLength(uidBytes);
        int uidBytesLen = uidBytes2 != null ? uidBytes2.length : 0;
        byte[] gidBytes2 = trimLeadingZeroesForceMinLength(gidBytes);
        int gidBytesLen = gidBytes2 != null ? gidBytes2.length : 0;
        byte[] data = new byte[3 + uidBytesLen + gidBytesLen];
        if (uidBytes2 != null) {
            ZipUtil.reverse(uidBytes2);
        }
        if (gidBytes2 != null) {
            ZipUtil.reverse(gidBytes2);
        }
        int pos = 0 + 1;
        data[0] = ZipUtil.unsignedIntToSignedByte(this.version);
        int pos2 = pos + 1;
        data[pos] = ZipUtil.unsignedIntToSignedByte(uidBytesLen);
        if (uidBytes2 != null) {
            System.arraycopy(uidBytes2, 0, data, pos2, uidBytesLen);
        }
        int pos3 = pos2 + uidBytesLen;
        int pos4 = pos3 + 1;
        data[pos3] = ZipUtil.unsignedIntToSignedByte(gidBytesLen);
        if (gidBytes2 != null) {
            System.arraycopy(gidBytes2, 0, data, pos4, gidBytesLen);
        }
        return data;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public byte[] getCentralDirectoryData() {
        return ByteUtils.EMPTY_BYTE_ARRAY;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
        reset();
        if (length < 3) {
            throw new ZipException("X7875_NewUnix length is too short, only " + length + " bytes");
        }
        int offset2 = offset + 1;
        this.version = ZipUtil.signedByteToUnsignedInt(data[offset]);
        int offset3 = offset2 + 1;
        int uidSize = ZipUtil.signedByteToUnsignedInt(data[offset2]);
        if (uidSize + 3 > length) {
            throw new ZipException("X7875_NewUnix invalid: uidSize " + uidSize + " doesn't fit into " + length + " bytes");
        }
        byte[] uidBytes = Arrays.copyOfRange(data, offset3, offset3 + uidSize);
        int offset4 = offset3 + uidSize;
        this.uid = new BigInteger(1, ZipUtil.reverse(uidBytes));
        int offset5 = offset4 + 1;
        int gidSize = ZipUtil.signedByteToUnsignedInt(data[offset4]);
        if (uidSize + 3 + gidSize > length) {
            throw new ZipException("X7875_NewUnix invalid: gidSize " + gidSize + " doesn't fit into " + length + " bytes");
        }
        byte[] gidBytes = Arrays.copyOfRange(data, offset5, offset5 + gidSize);
        this.gid = new BigInteger(1, ZipUtil.reverse(gidBytes));
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) throws ZipException {
    }

    private void reset() {
        this.uid = ONE_THOUSAND;
        this.gid = ONE_THOUSAND;
    }

    public String toString() {
        return "0x7875 Zip Extra Field: UID=" + this.uid + " GID=" + this.gid;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object o) {
        if (o instanceof X7875_NewUnix) {
            X7875_NewUnix xf = (X7875_NewUnix) o;
            return this.version == xf.version && this.uid.equals(xf.uid) && this.gid.equals(xf.gid);
        }
        return false;
    }

    public int hashCode() {
        int hc = (-1234567) * this.version;
        return (hc ^ Integer.rotateLeft(this.uid.hashCode(), 16)) ^ this.gid.hashCode();
    }

    static byte[] trimLeadingZeroesForceMinLength(byte[] array) {
        if (array == null) {
            return null;
        }
        int pos = 0;
        for (byte b : array) {
            if (b != 0) {
                break;
            }
            pos++;
        }
        byte[] trimmedArray = new byte[Math.max(1, array.length - pos)];
        int startPos = trimmedArray.length - (array.length - pos);
        System.arraycopy(array, pos, trimmedArray, startPos, trimmedArray.length - startPos);
        return trimmedArray;
    }
}
