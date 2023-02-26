package org.apache.commons.compress.compressors.lz4;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.lz77support.AbstractLZ77CompressorInputStream;
import org.apache.commons.compress.utils.ByteUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz4/BlockLZ4CompressorInputStream.class */
public class BlockLZ4CompressorInputStream extends AbstractLZ77CompressorInputStream {
    static final int WINDOW_SIZE = 65536;
    static final int SIZE_BITS = 4;
    static final int BACK_REFERENCE_SIZE_MASK = 15;
    static final int LITERAL_SIZE_MASK = 240;
    private int nextBackReferenceSize;
    private State state;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz4/BlockLZ4CompressorInputStream$State.class */
    public enum State {
        NO_BLOCK,
        IN_LITERAL,
        LOOKING_FOR_BACK_REFERENCE,
        IN_BACK_REFERENCE,
        EOF
    }

    public BlockLZ4CompressorInputStream(InputStream is) {
        super(is, 65536);
        this.state = State.NO_BLOCK;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0094  */
    @Override // java.io.InputStream
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int read(byte[] r6, int r7, int r8) throws java.io.IOException {
        /*
            r5 = this;
            r0 = r8
            if (r0 != 0) goto L6
            r0 = 0
            return r0
        L6:
            int[] r0 = org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream.AnonymousClass1.$SwitchMap$org$apache$commons$compress$compressors$lz4$BlockLZ4CompressorInputStream$State
            r1 = r5
            org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream$State r1 = r1.state
            int r1 = r1.ordinal()
            r0 = r0[r1]
            switch(r0) {
                case 1: goto L34;
                case 2: goto L36;
                case 3: goto L3a;
                case 4: goto L63;
                case 5: goto L73;
                default: goto L9c;
            }
        L34:
            r0 = -1
            return r0
        L36:
            r0 = r5
            r0.readSizes()
        L3a:
            r0 = r5
            r1 = r6
            r2 = r7
            r3 = r8
            int r0 = r0.readLiteral(r1, r2, r3)
            r9 = r0
            r0 = r5
            boolean r0 = r0.hasMoreDataInBlock()
            if (r0 != 0) goto L51
            r0 = r5
            org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream$State r1 = org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream.State.LOOKING_FOR_BACK_REFERENCE
            r0.state = r1
        L51:
            r0 = r9
            if (r0 <= 0) goto L5b
            r0 = r9
            goto L62
        L5b:
            r0 = r5
            r1 = r6
            r2 = r7
            r3 = r8
            int r0 = r0.read(r1, r2, r3)
        L62:
            return r0
        L63:
            r0 = r5
            boolean r0 = r0.initializeBackReference()
            if (r0 != 0) goto L73
            r0 = r5
            org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream$State r1 = org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream.State.EOF
            r0.state = r1
            r0 = -1
            return r0
        L73:
            r0 = r5
            r1 = r6
            r2 = r7
            r3 = r8
            int r0 = r0.readBackReference(r1, r2, r3)
            r10 = r0
            r0 = r5
            boolean r0 = r0.hasMoreDataInBlock()
            if (r0 != 0) goto L8a
            r0 = r5
            org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream$State r1 = org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream.State.NO_BLOCK
            r0.state = r1
        L8a:
            r0 = r10
            if (r0 <= 0) goto L94
            r0 = r10
            goto L9b
        L94:
            r0 = r5
            r1 = r6
            r2 = r7
            r3 = r8
            int r0 = r0.read(r1, r2, r3)
        L9b:
            return r0
        L9c:
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Unknown stream state "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r5
            org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream$State r3 = r3.state
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream.read(byte[], int, int):int");
    }

    private void readSizes() throws IOException {
        int nextBlock = readOneByte();
        if (nextBlock == -1) {
            throw new IOException("Premature end of stream while looking for next block");
        }
        this.nextBackReferenceSize = nextBlock & 15;
        long literalSizePart = (nextBlock & LITERAL_SIZE_MASK) >> 4;
        if (literalSizePart == 15) {
            literalSizePart += readSizeBytes();
        }
        if (literalSizePart < 0) {
            throw new IOException("Illegal block with a negative literal size found");
        }
        startLiteral(literalSizePart);
        this.state = State.IN_LITERAL;
    }

    private long readSizeBytes() throws IOException {
        int nextByte;
        long accum = 0;
        do {
            nextByte = readOneByte();
            if (nextByte == -1) {
                throw new IOException("Premature end of stream while parsing length");
            }
            accum += nextByte;
        } while (nextByte == 255);
        return accum;
    }

    private boolean initializeBackReference() throws IOException {
        try {
            int backReferenceOffset = (int) ByteUtils.fromLittleEndian(this.supplier, 2);
            long backReferenceSize = this.nextBackReferenceSize;
            if (this.nextBackReferenceSize == 15) {
                backReferenceSize += readSizeBytes();
            }
            if (backReferenceSize < 0) {
                throw new IOException("Illegal block with a negative match length found");
            }
            try {
                startBackReference(backReferenceOffset, backReferenceSize + 4);
                this.state = State.IN_BACK_REFERENCE;
                return true;
            } catch (IllegalArgumentException ex) {
                throw new IOException("Illegal block with bad offset found", ex);
            }
        } catch (IOException ex2) {
            if (this.nextBackReferenceSize == 0) {
                return false;
            }
            throw ex2;
        }
    }
}
