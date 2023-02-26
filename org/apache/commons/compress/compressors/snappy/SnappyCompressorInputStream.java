package org.apache.commons.compress.compressors.snappy;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.lz77support.AbstractLZ77CompressorInputStream;
import org.apache.commons.compress.utils.ByteUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/snappy/SnappyCompressorInputStream.class */
public class SnappyCompressorInputStream extends AbstractLZ77CompressorInputStream {
    private static final int TAG_MASK = 3;
    public static final int DEFAULT_BLOCK_SIZE = 32768;
    private final int size;
    private int uncompressedBytesRemaining;
    private State state;
    private boolean endReached;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/snappy/SnappyCompressorInputStream$State.class */
    public enum State {
        NO_BLOCK,
        IN_LITERAL,
        IN_BACK_REFERENCE
    }

    public SnappyCompressorInputStream(InputStream is) throws IOException {
        this(is, 32768);
    }

    public SnappyCompressorInputStream(InputStream is, int blockSize) throws IOException {
        super(is, blockSize);
        this.state = State.NO_BLOCK;
        int readSize = (int) readSize();
        this.size = readSize;
        this.uncompressedBytesRemaining = readSize;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        if (this.endReached) {
            return -1;
        }
        switch (this.state) {
            case NO_BLOCK:
                fill();
                return read(b, off, len);
            case IN_LITERAL:
                int litLen = readLiteral(b, off, len);
                if (!hasMoreDataInBlock()) {
                    this.state = State.NO_BLOCK;
                }
                return litLen > 0 ? litLen : read(b, off, len);
            case IN_BACK_REFERENCE:
                int backReferenceLen = readBackReference(b, off, len);
                if (!hasMoreDataInBlock()) {
                    this.state = State.NO_BLOCK;
                }
                return backReferenceLen > 0 ? backReferenceLen : read(b, off, len);
            default:
                throw new IOException("Unknown stream state " + this.state);
        }
    }

    private void fill() throws IOException {
        if (this.uncompressedBytesRemaining == 0) {
            this.endReached = true;
            return;
        }
        int b = readOneByte();
        if (b == -1) {
            throw new IOException("Premature end of stream reading block start");
        }
        switch (b & 3) {
            case 0:
                int length = readLiteralLength(b);
                if (length < 0) {
                    throw new IOException("Illegal block with a negative literal size found");
                }
                this.uncompressedBytesRemaining -= length;
                startLiteral(length);
                this.state = State.IN_LITERAL;
                return;
            case 1:
                int length2 = 4 + ((b >> 2) & 7);
                this.uncompressedBytesRemaining -= length2;
                int offset = (b & 224) << 3;
                int b2 = readOneByte();
                if (b2 == -1) {
                    throw new IOException("Premature end of stream reading back-reference length");
                }
                try {
                    startBackReference(offset | b2, length2);
                    this.state = State.IN_BACK_REFERENCE;
                    return;
                } catch (IllegalArgumentException ex) {
                    throw new IOException("Illegal block with bad offset found", ex);
                }
            case 2:
                int length3 = (b >> 2) + 1;
                if (length3 < 0) {
                    throw new IOException("Illegal block with a negative match length found");
                }
                this.uncompressedBytesRemaining -= length3;
                int offset2 = (int) ByteUtils.fromLittleEndian(this.supplier, 2);
                try {
                    startBackReference(offset2, length3);
                    this.state = State.IN_BACK_REFERENCE;
                    return;
                } catch (IllegalArgumentException ex2) {
                    throw new IOException("Illegal block with bad offset found", ex2);
                }
            case 3:
                int length4 = (b >> 2) + 1;
                if (length4 < 0) {
                    throw new IOException("Illegal block with a negative match length found");
                }
                this.uncompressedBytesRemaining -= length4;
                int offset3 = ((int) ByteUtils.fromLittleEndian(this.supplier, 4)) & Integer.MAX_VALUE;
                try {
                    startBackReference(offset3, length4);
                    this.state = State.IN_BACK_REFERENCE;
                    return;
                } catch (IllegalArgumentException ex3) {
                    throw new IOException("Illegal block with bad offset found", ex3);
                }
            default:
                return;
        }
    }

    private int readLiteralLength(int b) throws IOException {
        int length;
        switch (b >> 2) {
            case 60:
                length = readOneByte();
                if (length == -1) {
                    throw new IOException("Premature end of stream reading literal length");
                }
                break;
            case 61:
                length = (int) ByteUtils.fromLittleEndian(this.supplier, 2);
                break;
            case 62:
                length = (int) ByteUtils.fromLittleEndian(this.supplier, 3);
                break;
            case 63:
                length = (int) ByteUtils.fromLittleEndian(this.supplier, 4);
                break;
            default:
                length = b >> 2;
                break;
        }
        return length + 1;
    }

    private long readSize() throws IOException {
        int b;
        int index = 0;
        long sz = 0;
        do {
            b = readOneByte();
            if (b == -1) {
                throw new IOException("Premature end of stream reading size");
            }
            int i = index;
            index++;
            sz |= (b & 127) << (i * 7);
        } while (0 != (b & 128));
        return sz;
    }

    @Override // org.apache.commons.compress.compressors.lz77support.AbstractLZ77CompressorInputStream
    public int getSize() {
        return this.size;
    }
}
