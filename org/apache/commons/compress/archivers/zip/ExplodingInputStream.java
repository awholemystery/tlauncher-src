package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.utils.CloseShieldFilterInputStream;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.ExactMath;
import org.apache.commons.compress.utils.InputStreamStatistics;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ExplodingInputStream.class */
class ExplodingInputStream extends InputStream implements InputStreamStatistics {
    private final InputStream in;
    private BitStream bits;
    private final int dictionarySize;
    private final int numberOfTrees;
    private final int minimumMatchLength;
    private BinaryTree literalTree;
    private BinaryTree lengthTree;
    private BinaryTree distanceTree;
    private final CircularBuffer buffer = new CircularBuffer(32768);
    private long uncompressedCount;
    private long treeSizes;

    public ExplodingInputStream(int dictionarySize, int numberOfTrees, InputStream in) {
        if (dictionarySize != 4096 && dictionarySize != 8192) {
            throw new IllegalArgumentException("The dictionary size must be 4096 or 8192");
        }
        if (numberOfTrees != 2 && numberOfTrees != 3) {
            throw new IllegalArgumentException("The number of trees must be 2 or 3");
        }
        this.dictionarySize = dictionarySize;
        this.numberOfTrees = numberOfTrees;
        this.minimumMatchLength = numberOfTrees;
        this.in = in;
    }

    private void init() throws IOException {
        if (this.bits == null) {
            CountingInputStream i = new CountingInputStream(new CloseShieldFilterInputStream(this.in));
            Throwable th = null;
            try {
                if (this.numberOfTrees == 3) {
                    this.literalTree = BinaryTree.decode(i, 256);
                }
                this.lengthTree = BinaryTree.decode(i, 64);
                this.distanceTree = BinaryTree.decode(i, 64);
                this.treeSizes += i.getBytesRead();
                if (i != null) {
                    if (0 != 0) {
                        try {
                            i.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        i.close();
                    }
                }
                this.bits = new BitStream(this.in);
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    if (i != null) {
                        if (th3 != null) {
                            try {
                                i.close();
                            } catch (Throwable th5) {
                                th3.addSuppressed(th5);
                            }
                        } else {
                            i.close();
                        }
                    }
                    throw th4;
                }
            }
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (!this.buffer.available()) {
            try {
                fillBuffer();
            } catch (IllegalArgumentException ex) {
                throw new IOException("bad IMPLODE stream", ex);
            }
        }
        int ret = this.buffer.get();
        if (ret > -1) {
            this.uncompressedCount++;
        }
        return ret;
    }

    @Override // org.apache.commons.compress.utils.InputStreamStatistics
    public long getCompressedCount() {
        return this.bits.getBytesRead() + this.treeSizes;
    }

    @Override // org.apache.commons.compress.utils.InputStreamStatistics
    public long getUncompressedCount() {
        return this.uncompressedCount;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    private void fillBuffer() throws IOException {
        int literal;
        init();
        int bit = this.bits.nextBit();
        if (bit == -1) {
            return;
        }
        if (bit == 1) {
            if (this.literalTree != null) {
                literal = this.literalTree.read(this.bits);
            } else {
                literal = this.bits.nextByte();
            }
            if (literal == -1) {
                return;
            }
            this.buffer.put(literal);
            return;
        }
        int distanceLowSize = this.dictionarySize == 4096 ? 6 : 7;
        int distanceLow = (int) this.bits.nextBits(distanceLowSize);
        int distanceHigh = this.distanceTree.read(this.bits);
        if (distanceHigh == -1 && distanceLow <= 0) {
            return;
        }
        int distance = (distanceHigh << distanceLowSize) | distanceLow;
        int length = this.lengthTree.read(this.bits);
        if (length == 63) {
            long nextByte = this.bits.nextBits(8);
            if (nextByte == -1) {
                return;
            }
            length = ExactMath.add(length, nextByte);
        }
        this.buffer.copy(distance + 1, length + this.minimumMatchLength);
    }
}
