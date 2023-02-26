package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.zip.PKWareExtraHeader;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/X0015_CertificateIdForFile.class */
public class X0015_CertificateIdForFile extends PKWareExtraHeader {
    private int rcount;
    private PKWareExtraHeader.HashAlgorithm hashAlg;

    public X0015_CertificateIdForFile() {
        super(new ZipShort(21));
    }

    public int getRecordCount() {
        return this.rcount;
    }

    public PKWareExtraHeader.HashAlgorithm getHashAlgorithm() {
        return this.hashAlg;
    }

    @Override // org.apache.commons.compress.archivers.zip.PKWareExtraHeader, org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromCentralDirectoryData(byte[] data, int offset, int length) throws ZipException {
        assertMinimalLength(4, length);
        super.parseFromCentralDirectoryData(data, offset, length);
        this.rcount = ZipShort.getValue(data, offset);
        this.hashAlg = PKWareExtraHeader.HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    }
}
