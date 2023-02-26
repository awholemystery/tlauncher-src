package org.apache.commons.compress.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/BitInputStream.class */
public class BitInputStream implements Closeable {
    private static final int MAXIMUM_CACHE_SIZE = 63;
    private static final long[] MASKS = new long[64];
    private final CountingInputStream in;
    private final ByteOrder byteOrder;
    private long bitsCached;
    private int bitsCachedSize;

    static {
        for (int i = 1; i <= MAXIMUM_CACHE_SIZE; i++) {
            MASKS[i] = (MASKS[i - 1] << 1) + 1;
        }
    }

    public BitInputStream(InputStream in, ByteOrder byteOrder) {
        this.in = new CountingInputStream(in);
        this.byteOrder = byteOrder;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    public void clearBitCache() {
        this.bitsCached = 0L;
        this.bitsCachedSize = 0;
    }

    public long readBits(int count) throws IOException {
        if (count < 0 || count > MAXIMUM_CACHE_SIZE) {
            throw new IOException("count must not be negative or greater than 63");
        }
        if (ensureCache(count)) {
            return -1L;
        }
        if (this.bitsCachedSize < count) {
            return processBitsGreater57(count);
        }
        return readCachedBits(count);
    }

    public int bitsCached() {
        return this.bitsCachedSize;
    }

    public long bitsAvailable() throws IOException {
        return this.bitsCachedSize + (8 * this.in.available());
    }

    public void alignWithByteBoundary() {
        int toSkip = this.bitsCachedSize % 8;
        if (toSkip > 0) {
            readCachedBits(toSkip);
        }
    }

    public long getBytesRead() {
        return this.in.getBytesRead();
    }

    private long processBitsGreater57(int count) throws IOException {
        long overflow;
        int bitsToAddCount = count - this.bitsCachedSize;
        int overflowBits = 8 - bitsToAddCount;
        long nextByte = this.in.read();
        if (nextByte < 0) {
            return nextByte;
        }
        if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
            long bitsToAdd = nextByte & MASKS[bitsToAddCount];
            this.bitsCached |= bitsToAdd << this.bitsCachedSize;
            overflow = (nextByte >>> bitsToAddCount) & MASKS[overflowBits];
        } else {
            this.bitsCached <<= bitsToAddCount;
            long bitsToAdd2 = (nextByte >>> overflowBits) & MASKS[bitsToAddCount];
            this.bitsCached |= bitsToAdd2;
            overflow = nextByte & MASKS[overflowBits];
        }
        long bitsOut = this.bitsCached & MASKS[count];
        this.bitsCached = overflow;
        this.bitsCachedSize = overflowBits;
        return bitsOut;
    }

    private long readCachedBits(int count) {
        long bitsOut;
        if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
            bitsOut = this.bitsCached & MASKS[count];
            this.bitsCached >>>= count;
        } else {
            bitsOut = (this.bitsCached >> (this.bitsCachedSize - count)) & MASKS[count];
        }
        this.bitsCachedSize -= count;
        return bitsOut;
    }

    private boolean ensureCache(int count) throws IOException {
        while (this.bitsCachedSize < count && this.bitsCachedSize < 57) {
            long nextByte = this.in.read();
            if (nextByte < 0) {
                return true;
            }
            if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.bitsCached |= nextByte << this.bitsCachedSize;
            } else {
                this.bitsCached <<= 8;
                this.bitsCached |= nextByte;
            }
            this.bitsCachedSize += 8;
        }
        return false;
    }
}
