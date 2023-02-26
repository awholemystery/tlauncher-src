package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.compress.utils.BitInputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/BitStream.class */
class BitStream extends BitInputStream {
    /* JADX INFO: Access modifiers changed from: package-private */
    public BitStream(InputStream in) {
        super(in, ByteOrder.LITTLE_ENDIAN);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int nextBit() throws IOException {
        return (int) readBits(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long nextBits(int n) throws IOException {
        if (n < 0 || n > 8) {
            throw new IOException("Trying to read " + n + " bits, at most 8 are allowed");
        }
        return readBits(n);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int nextByte() throws IOException {
        return (int) readBits(8);
    }
}
