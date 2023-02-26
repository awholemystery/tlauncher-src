package org.apache.commons.compress.archivers.zip;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/PKWareExtraHeader.class */
public abstract class PKWareExtraHeader implements ZipExtraField {
    private final ZipShort headerId;
    private byte[] localData;
    private byte[] centralData;

    /* JADX INFO: Access modifiers changed from: protected */
    public PKWareExtraHeader(ZipShort headerId) {
        this.headerId = headerId;
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getHeaderId() {
        return this.headerId;
    }

    public void setLocalFileDataData(byte[] data) {
        this.localData = ZipUtil.copy(data);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(this.localData != null ? this.localData.length : 0);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public byte[] getLocalFileDataData() {
        return ZipUtil.copy(this.localData);
    }

    public void setCentralDirectoryData(byte[] data) {
        this.centralData = ZipUtil.copy(data);
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public ZipShort getCentralDirectoryLength() {
        if (this.centralData != null) {
            return new ZipShort(this.centralData.length);
        }
        return getLocalFileDataLength();
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public byte[] getCentralDirectoryData() {
        if (this.centralData != null) {
            return ZipUtil.copy(this.centralData);
        }
        return getLocalFileDataData();
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
        setLocalFileDataData(Arrays.copyOfRange(data, offset, offset + length));
    }

    @Override // org.apache.commons.compress.archivers.zip.ZipExtraField
    public void parseFromCentralDirectoryData(byte[] data, int offset, int length) throws ZipException {
        byte[] tmp = Arrays.copyOfRange(data, offset, offset + length);
        setCentralDirectoryData(tmp);
        if (this.localData == null) {
            setLocalFileDataData(tmp);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void assertMinimalLength(int minimum, int length) throws ZipException {
        if (length < minimum) {
            throw new ZipException(getClass().getName() + " is too short, only " + length + " bytes, expected at least " + minimum);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/PKWareExtraHeader$EncryptionAlgorithm.class */
    public enum EncryptionAlgorithm {
        DES(26113),
        RC2pre52(26114),
        TripleDES168(26115),
        TripleDES192(26121),
        AES128(26126),
        AES192(26127),
        AES256(26128),
        RC2(26370),
        RC4(26625),
        UNKNOWN(65535);
        
        private final int code;
        private static final Map<Integer, EncryptionAlgorithm> codeToEnum;

        static {
            EncryptionAlgorithm[] values;
            Map<Integer, EncryptionAlgorithm> cte = new HashMap<>();
            for (EncryptionAlgorithm method : values()) {
                cte.put(Integer.valueOf(method.getCode()), method);
            }
            codeToEnum = Collections.unmodifiableMap(cte);
        }

        EncryptionAlgorithm(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static EncryptionAlgorithm getAlgorithmByCode(int code) {
            return codeToEnum.get(Integer.valueOf(code));
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/PKWareExtraHeader$HashAlgorithm.class */
    public enum HashAlgorithm {
        NONE(0),
        CRC32(1),
        MD5(32771),
        SHA1(32772),
        RIPEND160(32775),
        SHA256(32780),
        SHA384(32781),
        SHA512(32782);
        
        private final int code;
        private static final Map<Integer, HashAlgorithm> codeToEnum;

        static {
            HashAlgorithm[] values;
            Map<Integer, HashAlgorithm> cte = new HashMap<>();
            for (HashAlgorithm method : values()) {
                cte.put(Integer.valueOf(method.getCode()), method);
            }
            codeToEnum = Collections.unmodifiableMap(cte);
        }

        HashAlgorithm(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static HashAlgorithm getAlgorithmByCode(int code) {
            return codeToEnum.get(Integer.valueOf(code));
        }
    }
}
