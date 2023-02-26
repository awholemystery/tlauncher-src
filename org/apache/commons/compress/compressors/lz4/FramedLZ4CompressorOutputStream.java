package org.apache.commons.compress.compressors.lz4;

import ch.qos.logback.core.net.SyslogConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.harmony.unpack200.IcTuple;
import org.apache.commons.compress.utils.ByteUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz4/FramedLZ4CompressorOutputStream.class */
public class FramedLZ4CompressorOutputStream extends CompressorOutputStream {
    private static final byte[] END_MARK = new byte[4];
    private final byte[] oneByte;
    private final byte[] blockData;
    private final OutputStream out;
    private final Parameters params;
    private boolean finished;
    private int currentIndex;
    private final XXHash32 contentHash;
    private final XXHash32 blockHash;
    private final byte[] blockDependencyBuffer;
    private int collectedBlockDependencyBytes;

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz4/FramedLZ4CompressorOutputStream$BlockSize.class */
    public enum BlockSize {
        K64(IcTuple.NESTED_CLASS_FLAG, 4),
        K256(262144, 5),
        M1(1048576, 6),
        M4(4194304, 7);
        
        private final int size;
        private final int index;

        BlockSize(int size, int index) {
            this.size = size;
            this.index = index;
        }

        int getSize() {
            return this.size;
        }

        int getIndex() {
            return this.index;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz4/FramedLZ4CompressorOutputStream$Parameters.class */
    public static class Parameters {
        private final BlockSize blockSize;
        private final boolean withContentChecksum;
        private final boolean withBlockChecksum;
        private final boolean withBlockDependency;
        private final org.apache.commons.compress.compressors.lz77support.Parameters lz77params;
        public static final Parameters DEFAULT = new Parameters(BlockSize.M4, true, false, false);

        public Parameters(BlockSize blockSize) {
            this(blockSize, true, false, false);
        }

        public Parameters(BlockSize blockSize, org.apache.commons.compress.compressors.lz77support.Parameters lz77params) {
            this(blockSize, true, false, false, lz77params);
        }

        public Parameters(BlockSize blockSize, boolean withContentChecksum, boolean withBlockChecksum, boolean withBlockDependency) {
            this(blockSize, withContentChecksum, withBlockChecksum, withBlockDependency, BlockLZ4CompressorOutputStream.createParameterBuilder().build());
        }

        public Parameters(BlockSize blockSize, boolean withContentChecksum, boolean withBlockChecksum, boolean withBlockDependency, org.apache.commons.compress.compressors.lz77support.Parameters lz77params) {
            this.blockSize = blockSize;
            this.withContentChecksum = withContentChecksum;
            this.withBlockChecksum = withBlockChecksum;
            this.withBlockDependency = withBlockDependency;
            this.lz77params = lz77params;
        }

        public String toString() {
            return "LZ4 Parameters with BlockSize " + this.blockSize + ", withContentChecksum " + this.withContentChecksum + ", withBlockChecksum " + this.withBlockChecksum + ", withBlockDependency " + this.withBlockDependency;
        }
    }

    public FramedLZ4CompressorOutputStream(OutputStream out) throws IOException {
        this(out, Parameters.DEFAULT);
    }

    public FramedLZ4CompressorOutputStream(OutputStream out, Parameters params) throws IOException {
        this.oneByte = new byte[1];
        this.contentHash = new XXHash32();
        this.params = params;
        this.blockData = new byte[params.blockSize.getSize()];
        this.out = out;
        this.blockHash = params.withBlockChecksum ? new XXHash32() : null;
        out.write(FramedLZ4CompressorInputStream.LZ4_SIGNATURE);
        writeFrameDescriptor();
        this.blockDependencyBuffer = params.withBlockDependency ? new byte[IcTuple.NESTED_CLASS_FLAG] : null;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.oneByte[0] = (byte) (b & 255);
        write(this.oneByte);
    }

    @Override // java.io.OutputStream
    public void write(byte[] data, int off, int len) throws IOException {
        if (this.params.withContentChecksum) {
            this.contentHash.update(data, off, len);
        }
        int blockDataLength = this.blockData.length;
        if (this.currentIndex + len > blockDataLength) {
            flushBlock();
            while (len > blockDataLength) {
                System.arraycopy(data, off, this.blockData, 0, blockDataLength);
                off += blockDataLength;
                len -= blockDataLength;
                this.currentIndex = blockDataLength;
                flushBlock();
            }
        }
        System.arraycopy(data, off, this.blockData, this.currentIndex, len);
        this.currentIndex += len;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            finish();
        } finally {
            this.out.close();
        }
    }

    public void finish() throws IOException {
        if (!this.finished) {
            if (this.currentIndex > 0) {
                flushBlock();
            }
            writeTrailer();
            this.finished = true;
        }
    }

    private void writeFrameDescriptor() throws IOException {
        int flags = 64;
        if (!this.params.withBlockDependency) {
            flags = 64 | 32;
        }
        if (this.params.withContentChecksum) {
            flags |= 4;
        }
        if (this.params.withBlockChecksum) {
            flags |= 16;
        }
        this.out.write(flags);
        this.contentHash.update(flags);
        int bd = (this.params.blockSize.getIndex() << 4) & SyslogConstants.LOG_ALERT;
        this.out.write(bd);
        this.contentHash.update(bd);
        this.out.write((int) ((this.contentHash.getValue() >> 8) & 255));
        this.contentHash.reset();
    }

    private void flushBlock() throws IOException {
        boolean withBlockDependency = this.params.withBlockDependency;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BlockLZ4CompressorOutputStream o = new BlockLZ4CompressorOutputStream(baos, this.params.lz77params);
        Throwable th = null;
        if (withBlockDependency) {
            try {
                o.prefill(this.blockDependencyBuffer, this.blockDependencyBuffer.length - this.collectedBlockDependencyBytes, this.collectedBlockDependencyBytes);
            } finally {
            }
        }
        o.write(this.blockData, 0, this.currentIndex);
        if (o != null) {
            if (0 != 0) {
                try {
                    o.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            } else {
                o.close();
            }
        }
        if (withBlockDependency) {
            appendToBlockDependencyBuffer(this.blockData, 0, this.currentIndex);
        }
        byte[] b = baos.toByteArray();
        if (b.length > this.currentIndex) {
            ByteUtils.toLittleEndian(this.out, this.currentIndex | Integer.MIN_VALUE, 4);
            this.out.write(this.blockData, 0, this.currentIndex);
            if (this.params.withBlockChecksum) {
                this.blockHash.update(this.blockData, 0, this.currentIndex);
            }
        } else {
            ByteUtils.toLittleEndian(this.out, b.length, 4);
            this.out.write(b);
            if (this.params.withBlockChecksum) {
                this.blockHash.update(b, 0, b.length);
            }
        }
        if (this.params.withBlockChecksum) {
            ByteUtils.toLittleEndian(this.out, this.blockHash.getValue(), 4);
            this.blockHash.reset();
        }
        this.currentIndex = 0;
    }

    private void writeTrailer() throws IOException {
        this.out.write(END_MARK);
        if (this.params.withContentChecksum) {
            ByteUtils.toLittleEndian(this.out, this.contentHash.getValue(), 4);
        }
    }

    private void appendToBlockDependencyBuffer(byte[] b, int off, int len) {
        int len2 = Math.min(len, this.blockDependencyBuffer.length);
        if (len2 > 0) {
            int keep = this.blockDependencyBuffer.length - len2;
            if (keep > 0) {
                System.arraycopy(this.blockDependencyBuffer, len2, this.blockDependencyBuffer, 0, keep);
            }
            System.arraycopy(b, off, this.blockDependencyBuffer, keep, len2);
            this.collectedBlockDependencyBytes = Math.min(this.collectedBlockDependencyBytes + len2, this.blockDependencyBuffer.length);
        }
    }
}
