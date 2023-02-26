package org.apache.commons.compress.archivers.zip;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ExtraFieldUtils.class */
public class ExtraFieldUtils {
    private static final int WORD = 4;
    private static final Map<ZipShort, Class<?>> IMPLEMENTATIONS = new ConcurrentHashMap();
    static final ZipExtraField[] EMPTY_ZIP_EXTRA_FIELD_ARRAY;

    static {
        register(AsiExtraField.class);
        register(X5455_ExtendedTimestamp.class);
        register(X7875_NewUnix.class);
        register(JarMarker.class);
        register(UnicodePathExtraField.class);
        register(UnicodeCommentExtraField.class);
        register(Zip64ExtendedInformationExtraField.class);
        register(X000A_NTFS.class);
        register(X0014_X509Certificates.class);
        register(X0015_CertificateIdForFile.class);
        register(X0016_CertificateIdForCentralDirectory.class);
        register(X0017_StrongEncryptionHeader.class);
        register(X0019_EncryptionRecipientCertificateList.class);
        register(ResourceAlignmentExtraField.class);
        EMPTY_ZIP_EXTRA_FIELD_ARRAY = new ZipExtraField[0];
    }

    public static void register(Class<?> c) {
        try {
            ZipExtraField ze = (ZipExtraField) c.newInstance();
            IMPLEMENTATIONS.put(ze.getHeaderId(), c);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(c + " doesn't implement ZipExtraField");
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException(c + "'s no-arg constructor is not public");
        } catch (InstantiationException e3) {
            throw new IllegalArgumentException(c + " is not a concrete class");
        }
    }

    public static ZipExtraField createExtraField(ZipShort headerId) throws InstantiationException, IllegalAccessException {
        ZipExtraField field = createExtraFieldNoDefault(headerId);
        if (field != null) {
            return field;
        }
        UnrecognizedExtraField u = new UnrecognizedExtraField();
        u.setHeaderId(headerId);
        return u;
    }

    public static ZipExtraField createExtraFieldNoDefault(ZipShort headerId) throws InstantiationException, IllegalAccessException {
        Class<?> c = IMPLEMENTATIONS.get(headerId);
        if (c != null) {
            return (ZipExtraField) c.newInstance();
        }
        return null;
    }

    public static ZipExtraField[] parse(byte[] data) throws ZipException {
        return parse(data, true, UnparseableExtraField.THROW);
    }

    public static ZipExtraField[] parse(byte[] data, boolean local) throws ZipException {
        return parse(data, local, UnparseableExtraField.THROW);
    }

    public static ZipExtraField[] parse(byte[] data, boolean local, final UnparseableExtraField onUnparseableData) throws ZipException {
        return parse(data, local, new ExtraFieldParsingBehavior() { // from class: org.apache.commons.compress.archivers.zip.ExtraFieldUtils.1
            @Override // org.apache.commons.compress.archivers.zip.UnparseableExtraFieldBehavior
            public ZipExtraField onUnparseableExtraField(byte[] data2, int off, int len, boolean local2, int claimedLength) throws ZipException {
                return UnparseableExtraField.this.onUnparseableExtraField(data2, off, len, local2, claimedLength);
            }

            @Override // org.apache.commons.compress.archivers.zip.ExtraFieldParsingBehavior
            public ZipExtraField createExtraField(ZipShort headerId) throws ZipException, InstantiationException, IllegalAccessException {
                return ExtraFieldUtils.createExtraField(headerId);
            }

            @Override // org.apache.commons.compress.archivers.zip.ExtraFieldParsingBehavior
            public ZipExtraField fill(ZipExtraField field, byte[] data2, int off, int len, boolean local2) throws ZipException {
                return ExtraFieldUtils.fillExtraField(field, data2, off, len, local2);
            }
        });
    }

    public static ZipExtraField[] parse(byte[] data, boolean local, ExtraFieldParsingBehavior parsingBehavior) throws ZipException {
        ArrayList arrayList = new ArrayList();
        int start = 0;
        int dataLength = data.length;
        while (true) {
            if (start > dataLength - 4) {
                break;
            }
            ZipShort headerId = new ZipShort(data, start);
            int length = new ZipShort(data, start + 2).getValue();
            if (start + 4 + length > dataLength) {
                ZipExtraField field = parsingBehavior.onUnparseableExtraField(data, start, dataLength - start, local, length);
                if (field != null) {
                    arrayList.add(field);
                }
            } else {
                try {
                    ZipExtraField ze = (ZipExtraField) Objects.requireNonNull(parsingBehavior.createExtraField(headerId), "createExtraField must not return null");
                    arrayList.add(Objects.requireNonNull(parsingBehavior.fill(ze, data, start + 4, length, local), "fill must not return null"));
                    start += length + 4;
                } catch (IllegalAccessException | InstantiationException ie) {
                    throw ((ZipException) new ZipException(ie.getMessage()).initCause(ie));
                }
            }
        }
        return (ZipExtraField[]) arrayList.toArray(EMPTY_ZIP_EXTRA_FIELD_ARRAY);
    }

    public static byte[] mergeLocalFileDataData(ZipExtraField[] data) {
        byte[] local;
        int dataLength = data.length;
        boolean lastIsUnparseableHolder = dataLength > 0 && (data[dataLength - 1] instanceof UnparseableExtraFieldData);
        int regularExtraFieldCount = lastIsUnparseableHolder ? dataLength - 1 : dataLength;
        int sum = 4 * regularExtraFieldCount;
        for (ZipExtraField element : data) {
            sum += element.getLocalFileDataLength().getValue();
        }
        byte[] result = new byte[sum];
        int start = 0;
        for (int i = 0; i < regularExtraFieldCount; i++) {
            System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
            System.arraycopy(data[i].getLocalFileDataLength().getBytes(), 0, result, start + 2, 2);
            start += 4;
            byte[] local2 = data[i].getLocalFileDataData();
            if (local2 != null) {
                System.arraycopy(local2, 0, result, start, local2.length);
                start += local2.length;
            }
        }
        if (lastIsUnparseableHolder && (local = data[dataLength - 1].getLocalFileDataData()) != null) {
            System.arraycopy(local, 0, result, start, local.length);
        }
        return result;
    }

    public static byte[] mergeCentralDirectoryData(ZipExtraField[] data) {
        byte[] central;
        int dataLength = data.length;
        boolean lastIsUnparseableHolder = dataLength > 0 && (data[dataLength - 1] instanceof UnparseableExtraFieldData);
        int regularExtraFieldCount = lastIsUnparseableHolder ? dataLength - 1 : dataLength;
        int sum = 4 * regularExtraFieldCount;
        for (ZipExtraField element : data) {
            sum += element.getCentralDirectoryLength().getValue();
        }
        byte[] result = new byte[sum];
        int start = 0;
        for (int i = 0; i < regularExtraFieldCount; i++) {
            System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
            System.arraycopy(data[i].getCentralDirectoryLength().getBytes(), 0, result, start + 2, 2);
            start += 4;
            byte[] central2 = data[i].getCentralDirectoryData();
            if (central2 != null) {
                System.arraycopy(central2, 0, result, start, central2.length);
                start += central2.length;
            }
        }
        if (lastIsUnparseableHolder && (central = data[dataLength - 1].getCentralDirectoryData()) != null) {
            System.arraycopy(central, 0, result, start, central.length);
        }
        return result;
    }

    public static ZipExtraField fillExtraField(ZipExtraField ze, byte[] data, int off, int len, boolean local) throws ZipException {
        try {
            if (local) {
                ze.parseFromLocalFileData(data, off, len);
            } else {
                ze.parseFromCentralDirectoryData(data, off, len);
            }
            return ze;
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw ((ZipException) new ZipException("Failed to parse corrupt ZIP extra field of type " + Integer.toHexString(ze.getHeaderId().getValue())).initCause(aiobe));
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ExtraFieldUtils$UnparseableExtraField.class */
    public static final class UnparseableExtraField implements UnparseableExtraFieldBehavior {
        public static final int THROW_KEY = 0;
        public static final int SKIP_KEY = 1;
        public static final int READ_KEY = 2;
        public static final UnparseableExtraField THROW = new UnparseableExtraField(0);
        public static final UnparseableExtraField SKIP = new UnparseableExtraField(1);
        public static final UnparseableExtraField READ = new UnparseableExtraField(2);
        private final int key;

        private UnparseableExtraField(int k) {
            this.key = k;
        }

        public int getKey() {
            return this.key;
        }

        @Override // org.apache.commons.compress.archivers.zip.UnparseableExtraFieldBehavior
        public ZipExtraField onUnparseableExtraField(byte[] data, int off, int len, boolean local, int claimedLength) throws ZipException {
            switch (this.key) {
                case 0:
                    throw new ZipException("Bad extra field starting at " + off + ".  Block length of " + claimedLength + " bytes exceeds remaining data of " + (len - 4) + " bytes.");
                case 1:
                    return null;
                case 2:
                    UnparseableExtraFieldData field = new UnparseableExtraFieldData();
                    if (local) {
                        field.parseFromLocalFileData(data, off, len);
                    } else {
                        field.parseFromCentralDirectoryData(data, off, len);
                    }
                    return field;
                default:
                    throw new ZipException("Unknown UnparseableExtraField key: " + this.key);
            }
        }
    }
}
