package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ResourceAlignmentExtraField.class */
public class ResourceAlignmentExtraField implements ZipExtraField {
    public static final ZipShort ID = new ZipShort(41246);
    public static final int BASE_SIZE = 2;
    private static final int ALLOW_METHOD_MESSAGE_CHANGE_FLAG = 32768;
    private short alignment;
    private boolean allowMethodChange;
    private int padding;

    public ResourceAlignmentExtraField() {
    }

    public ResourceAlignmentExtraField(int alignment) {
        this(alignment, false);
    }

    public ResourceAlignmentExtraField(int alignment, boolean allowMethodChange) {
        this(alignment, allowMethodChange, 0);
    }

    public ResourceAlignmentExtraField(int alignment, boolean allowMethodChange, int padding) {
        if (alignment < 0 || alignment > 32767) {
            throw new IllegalArgumentException("Alignment must be between 0 and 0x7fff, was: " + alignment);
        }
        if (padding < 0) {
            throw new IllegalArgumentException("Padding must not be negative, was: " + padding);
        }
        this.alignment = (short) alignment;
        this.allowMethodChange = allowMethodChange;
        this.padding = padding;
    }

    public short getAlignment() {
        return this.alignment;
    }

    public boolean allowMethodChange() {
        return this.allowMethodChange;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return ID;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(2 + this.padding);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getCentralDirectoryLength() {
        return new ZipShort(2);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public byte[] getLocalFileDataData() {
        byte[] content = new byte[2 + this.padding];
        ZipShort.putShort(this.alignment | (this.allowMethodChange ? (short) 32768 : (short) 0), content, 0);
        return content;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public byte[] getCentralDirectoryData() {
        return ZipShort.getBytes(this.alignment | (this.allowMethodChange ? (short) 32768 : (short) 0));
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromLocalFileData(byte[] buffer, int offset, int length) throws ZipException {
        parseFromCentralDirectoryData(buffer, offset, length);
        this.padding = length - 2;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) throws ZipException {
        if (length < 2) {
            throw new ZipException("Too short content for ResourceAlignmentExtraField (0xa11e): " + length);
        }
        int alignmentValue = ZipShort.getValue(buffer, offset);
        this.alignment = (short) (alignmentValue & 32767);
        this.allowMethodChange = (alignmentValue & 32768) != 0;
    }
}
