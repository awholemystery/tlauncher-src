package org.apache.commons.compress.archivers.zip;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/UnicodePathExtraField.class */
public class UnicodePathExtraField extends AbstractUnicodeExtraField {
    public static final ZipShort UPATH_ID = new ZipShort(28789);

    public UnicodePathExtraField() {
    }

    public UnicodePathExtraField(String text, byte[] bytes, int off, int len) {
        super(text, bytes, off, len);
    }

    public UnicodePathExtraField(String name, byte[] bytes) {
        super(name, bytes);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return UPATH_ID;
    }
}
