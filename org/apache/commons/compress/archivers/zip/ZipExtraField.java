package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ZipExtraField.class */
public interface ZipExtraField {
    public static final int EXTRAFIELD_HEADER_SIZE = 4;

    ZipShort getHeaderId();

    ZipShort getLocalFileDataLength();

    ZipShort getCentralDirectoryLength();

    byte[] getLocalFileDataData();

    byte[] getCentralDirectoryData();

    void parseFromLocalFileData(byte[] bArr, int i, int i2) throws ZipException;

    void parseFromCentralDirectoryData(byte[] bArr, int i, int i2) throws ZipException;
}
