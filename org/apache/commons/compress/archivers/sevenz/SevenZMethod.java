package org.apache.commons.compress.archivers.sevenz;

import java.util.Arrays;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/SevenZMethod.class */
public enum SevenZMethod {
    COPY(new byte[]{0}),
    LZMA(new byte[]{3, 1, 1}),
    LZMA2(new byte[]{33}),
    DEFLATE(new byte[]{4, 1, 8}),
    DEFLATE64(new byte[]{4, 1, 9}),
    BZIP2(new byte[]{4, 2, 2}),
    AES256SHA256(new byte[]{6, -15, 7, 1}),
    BCJ_X86_FILTER(new byte[]{3, 3, 1, 3}),
    BCJ_PPC_FILTER(new byte[]{3, 3, 2, 5}),
    BCJ_IA64_FILTER(new byte[]{3, 3, 4, 1}),
    BCJ_ARM_FILTER(new byte[]{3, 3, 5, 1}),
    BCJ_ARM_THUMB_FILTER(new byte[]{3, 3, 7, 1}),
    BCJ_SPARC_FILTER(new byte[]{3, 3, 8, 5}),
    DELTA_FILTER(new byte[]{3});
    
    private final byte[] id;

    SevenZMethod(byte[] id) {
        this.id = id;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getId() {
        int idLength = this.id.length;
        byte[] copy = new byte[idLength];
        System.arraycopy(this.id, 0, copy, 0, idLength);
        return copy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SevenZMethod byId(byte[] id) {
        SevenZMethod[] sevenZMethodArr;
        for (SevenZMethod m : (SevenZMethod[]) SevenZMethod.class.getEnumConstants()) {
            if (Arrays.equals(m.id, id)) {
                return m;
            }
        }
        return null;
    }
}
