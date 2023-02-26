package org.apache.commons.compress.harmony.pack200;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Codec.class */
public abstract class Codec {
    public static final BHSDCodec BCI5 = new BHSDCodec(5, 4);
    public static final BHSDCodec BRANCH5 = new BHSDCodec(5, 4, 2);
    public static final BHSDCodec BYTE1 = new BHSDCodec(1, 256);
    public static final BHSDCodec CHAR3 = new BHSDCodec(3, 128);
    public static final BHSDCodec DELTA5 = new BHSDCodec(5, 64, 1, 1);
    public static final BHSDCodec MDELTA5 = new BHSDCodec(5, 64, 2, 1);
    public static final BHSDCodec SIGNED5 = new BHSDCodec(5, 64, 1);
    public static final BHSDCodec UDELTA5 = new BHSDCodec(5, 64, 0, 1);
    public static final BHSDCodec UNSIGNED5 = new BHSDCodec(5, 64);
    public int lastBandLength;

    public abstract int decode(InputStream inputStream) throws IOException, Pack200Exception;

    public abstract byte[] encode(int i, int i2) throws Pack200Exception;

    public abstract byte[] encode(int i) throws Pack200Exception;

    public abstract int decode(InputStream inputStream, long j) throws IOException, Pack200Exception;

    public int[] decodeInts(int n, InputStream in) throws IOException, Pack200Exception {
        this.lastBandLength = 0;
        int[] result = new int[n];
        int last = 0;
        for (int i = 0; i < n; i++) {
            int decode = decode(in, last);
            last = decode;
            result[i] = decode;
        }
        return result;
    }

    public int[] decodeInts(int n, InputStream in, int firstValue) throws IOException, Pack200Exception {
        int[] result = new int[n + 1];
        result[0] = firstValue;
        int last = firstValue;
        for (int i = 1; i < n + 1; i++) {
            int decode = decode(in, last);
            last = decode;
            result[i] = decode;
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public byte[] encode(int[] ints) throws Pack200Exception {
        int total = 0;
        byte[] bArr = new byte[ints.length];
        int i = 0;
        while (i < ints.length) {
            bArr[i] = encode(ints[i], i > 0 ? ints[i - 1] : 0);
            total += bArr[i].length;
            i++;
        }
        byte[] encoded = new byte[total];
        int index = 0;
        for (Object[] objArr : bArr) {
            System.arraycopy(objArr, 0, encoded, index, objArr.length);
            index += objArr.length;
        }
        return encoded;
    }
}
