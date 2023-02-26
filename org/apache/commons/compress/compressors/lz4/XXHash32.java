package org.apache.commons.compress.compressors.lz4;

import java.util.zip.Checksum;
import org.apache.commons.compress.utils.ByteUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz4/XXHash32.class */
public class XXHash32 implements Checksum {
    private static final int BUF_SIZE = 16;
    private static final int ROTATE_BITS = 13;
    private static final int PRIME1 = -1640531535;
    private static final int PRIME2 = -2048144777;
    private static final int PRIME3 = -1028477379;
    private static final int PRIME4 = 668265263;
    private static final int PRIME5 = 374761393;
    private final byte[] oneByte;
    private final int[] state;
    private final byte[] buffer;
    private final int seed;
    private int totalLen;
    private int pos;

    public XXHash32() {
        this(0);
    }

    public XXHash32(int seed) {
        this.oneByte = new byte[1];
        this.state = new int[4];
        this.buffer = new byte[16];
        this.seed = seed;
        initializeState();
    }

    @Override // java.util.zip.Checksum
    public void reset() {
        initializeState();
        this.totalLen = 0;
        this.pos = 0;
    }

    @Override // java.util.zip.Checksum
    public void update(int b) {
        this.oneByte[0] = (byte) (b & 255);
        update(this.oneByte, 0, 1);
    }

    @Override // java.util.zip.Checksum
    public void update(byte[] b, int off, int len) {
        if (len <= 0) {
            return;
        }
        this.totalLen += len;
        int end = off + len;
        if (this.pos + len < 16) {
            System.arraycopy(b, off, this.buffer, this.pos, len);
            this.pos += len;
            return;
        }
        if (this.pos > 0) {
            int size = 16 - this.pos;
            System.arraycopy(b, off, this.buffer, this.pos, size);
            process(this.buffer, 0);
            off += size;
        }
        int limit = end - 16;
        while (off <= limit) {
            process(b, off);
            off += 16;
        }
        if (off < end) {
            this.pos = end - off;
            System.arraycopy(b, off, this.buffer, 0, this.pos);
        }
    }

    @Override // java.util.zip.Checksum
    public long getValue() {
        int hash;
        if (this.totalLen > 16) {
            hash = Integer.rotateLeft(this.state[0], 1) + Integer.rotateLeft(this.state[1], 7) + Integer.rotateLeft(this.state[2], 12) + Integer.rotateLeft(this.state[3], 18);
        } else {
            hash = this.state[2] + PRIME5;
        }
        int hash2 = hash + this.totalLen;
        int idx = 0;
        int limit = this.pos - 4;
        while (idx <= limit) {
            hash2 = Integer.rotateLeft(hash2 + (getInt(this.buffer, idx) * PRIME3), 17) * PRIME4;
            idx += 4;
        }
        while (idx < this.pos) {
            int i = idx;
            idx++;
            hash2 = Integer.rotateLeft(hash2 + ((this.buffer[i] & 255) * PRIME5), 11) * PRIME1;
        }
        int hash3 = (hash2 ^ (hash2 >>> 15)) * PRIME2;
        int hash4 = (hash3 ^ (hash3 >>> 13)) * PRIME3;
        return (hash4 ^ (hash4 >>> 16)) & 4294967295L;
    }

    private static int getInt(byte[] buffer, int idx) {
        return (int) (ByteUtils.fromLittleEndian(buffer, idx, 4) & 4294967295L);
    }

    private void initializeState() {
        this.state[0] = this.seed + PRIME1 + PRIME2;
        this.state[1] = this.seed + PRIME2;
        this.state[2] = this.seed;
        this.state[3] = this.seed - PRIME1;
    }

    private void process(byte[] b, int offset) {
        int s0 = this.state[0];
        int s1 = this.state[1];
        int s2 = this.state[2];
        int s3 = this.state[3];
        int s02 = Integer.rotateLeft(s0 + (getInt(b, offset) * PRIME2), 13) * PRIME1;
        int s12 = Integer.rotateLeft(s1 + (getInt(b, offset + 4) * PRIME2), 13) * PRIME1;
        int s22 = Integer.rotateLeft(s2 + (getInt(b, offset + 8) * PRIME2), 13) * PRIME1;
        int s32 = Integer.rotateLeft(s3 + (getInt(b, offset + 12) * PRIME2), 13) * PRIME1;
        this.state[0] = s02;
        this.state[1] = s12;
        this.state[2] = s22;
        this.state[3] = s32;
        this.pos = 0;
    }
}
