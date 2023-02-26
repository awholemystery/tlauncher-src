package org.apache.commons.compress.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/ByteUtils.class */
public final class ByteUtils {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/ByteUtils$ByteConsumer.class */
    public interface ByteConsumer {
        void accept(int i) throws IOException;
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/ByteUtils$ByteSupplier.class */
    public interface ByteSupplier {
        int getAsByte() throws IOException;
    }

    private ByteUtils() {
    }

    public static long fromLittleEndian(byte[] bytes) {
        return fromLittleEndian(bytes, 0, bytes.length);
    }

    public static long fromLittleEndian(byte[] bytes, int off, int length) {
        checkReadLength(length);
        long l = 0;
        for (int i = 0; i < length; i++) {
            l |= (bytes[off + i] & 255) << (8 * i);
        }
        return l;
    }

    public static long fromLittleEndian(InputStream in, int length) throws IOException {
        checkReadLength(length);
        long l = 0;
        for (int i = 0; i < length; i++) {
            long b = in.read();
            if (b == -1) {
                throw new IOException("Premature end of data");
            }
            l |= b << (i * 8);
        }
        return l;
    }

    public static long fromLittleEndian(ByteSupplier supplier, int length) throws IOException {
        checkReadLength(length);
        long l = 0;
        for (int i = 0; i < length; i++) {
            long b = supplier.getAsByte();
            if (b == -1) {
                throw new IOException("Premature end of data");
            }
            l |= b << (i * 8);
        }
        return l;
    }

    public static long fromLittleEndian(DataInput in, int length) throws IOException {
        checkReadLength(length);
        long l = 0;
        for (int i = 0; i < length; i++) {
            long b = in.readUnsignedByte();
            l |= b << (i * 8);
        }
        return l;
    }

    public static void toLittleEndian(byte[] b, long value, int off, int length) {
        long num = value;
        for (int i = 0; i < length; i++) {
            b[off + i] = (byte) (num & 255);
            num >>= 8;
        }
    }

    public static void toLittleEndian(OutputStream out, long value, int length) throws IOException {
        long num = value;
        for (int i = 0; i < length; i++) {
            out.write((int) (num & 255));
            num >>= 8;
        }
    }

    public static void toLittleEndian(ByteConsumer consumer, long value, int length) throws IOException {
        long num = value;
        for (int i = 0; i < length; i++) {
            consumer.accept((int) (num & 255));
            num >>= 8;
        }
    }

    public static void toLittleEndian(DataOutput out, long value, int length) throws IOException {
        long num = value;
        for (int i = 0; i < length; i++) {
            out.write((int) (num & 255));
            num >>= 8;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/ByteUtils$InputStreamByteSupplier.class */
    public static class InputStreamByteSupplier implements ByteSupplier {
        private final InputStream is;

        public InputStreamByteSupplier(InputStream is) {
            this.is = is;
        }

        @Override // org.apache.commons.compress.utils.ByteUtils.ByteSupplier
        public int getAsByte() throws IOException {
            return this.is.read();
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/ByteUtils$OutputStreamByteConsumer.class */
    public static class OutputStreamByteConsumer implements ByteConsumer {
        private final OutputStream os;

        public OutputStreamByteConsumer(OutputStream os) {
            this.os = os;
        }

        @Override // org.apache.commons.compress.utils.ByteUtils.ByteConsumer
        public void accept(int b) throws IOException {
            this.os.write(b);
        }
    }

    private static void checkReadLength(int length) {
        if (length > 8) {
            throw new IllegalArgumentException("Can't read more than eight bytes into a long value");
        }
    }
}
