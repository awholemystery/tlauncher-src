package org.apache.commons.compress.compressors.lz4;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;
import org.apache.commons.compress.compressors.lz77support.Parameters;
import org.apache.commons.compress.harmony.unpack200.IcTuple;
import org.apache.commons.compress.utils.ByteUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz4/BlockLZ4CompressorOutputStream.class */
public class BlockLZ4CompressorOutputStream extends CompressorOutputStream {
    private static final int MIN_BACK_REFERENCE_LENGTH = 4;
    private static final int MIN_OFFSET_OF_LAST_BACK_REFERENCE = 12;
    private final LZ77Compressor compressor;
    private final OutputStream os;
    private final byte[] oneByte;
    private boolean finished;
    private final Deque<Pair> pairs;
    private final Deque<byte[]> expandedBlocks;

    public BlockLZ4CompressorOutputStream(OutputStream os) {
        this(os, createParameterBuilder().build());
    }

    public BlockLZ4CompressorOutputStream(OutputStream os, Parameters params) {
        this.oneByte = new byte[1];
        this.pairs = new LinkedList();
        this.expandedBlocks = new LinkedList();
        this.os = os;
        this.compressor = new LZ77Compressor(params, block -> {
            switch (block.getType()) {
                case LITERAL:
                    addLiteralBlock((LZ77Compressor.LiteralBlock) block);
                    return;
                case BACK_REFERENCE:
                    addBackReference((LZ77Compressor.BackReference) block);
                    return;
                case EOD:
                    writeFinalLiteralBlock();
                    return;
                default:
                    return;
            }
        });
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.oneByte[0] = (byte) (b & 255);
        write(this.oneByte);
    }

    @Override // java.io.OutputStream
    public void write(byte[] data, int off, int len) throws IOException {
        this.compressor.compress(data, off, len);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            finish();
        } finally {
            this.os.close();
        }
    }

    public void finish() throws IOException {
        if (!this.finished) {
            this.compressor.finish();
            this.finished = true;
        }
    }

    public void prefill(byte[] data, int off, int len) {
        if (len > 0) {
            byte[] b = Arrays.copyOfRange(data, off, off + len);
            this.compressor.prefill(b);
            recordLiteral(b);
        }
    }

    private void addLiteralBlock(LZ77Compressor.LiteralBlock block) throws IOException {
        Pair last = writeBlocksAndReturnUnfinishedPair(block.getLength());
        recordLiteral(last.addLiteral(block));
        clearUnusedBlocksAndPairs();
    }

    private void addBackReference(LZ77Compressor.BackReference block) throws IOException {
        Pair last = writeBlocksAndReturnUnfinishedPair(block.getLength());
        last.setBackReference(block);
        recordBackReference(block);
        clearUnusedBlocksAndPairs();
    }

    private Pair writeBlocksAndReturnUnfinishedPair(int length) throws IOException {
        writeWritablePairs(length);
        Pair last = this.pairs.peekLast();
        if (last == null || last.hasBackReference()) {
            last = new Pair();
            this.pairs.addLast(last);
        }
        return last;
    }

    private void recordLiteral(byte[] b) {
        this.expandedBlocks.addFirst(b);
    }

    private void clearUnusedBlocksAndPairs() {
        clearUnusedBlocks();
        clearUnusedPairs();
    }

    private void clearUnusedBlocks() {
        int blockLengths = 0;
        int blocksToKeep = 0;
        for (byte[] b : this.expandedBlocks) {
            blocksToKeep++;
            blockLengths += b.length;
            if (blockLengths >= 65536) {
                break;
            }
        }
        int size = this.expandedBlocks.size();
        for (int i = blocksToKeep; i < size; i++) {
            this.expandedBlocks.removeLast();
        }
    }

    private void recordBackReference(LZ77Compressor.BackReference block) {
        this.expandedBlocks.addFirst(expand(block.getOffset(), block.getLength()));
    }

    private byte[] expand(int offset, int length) {
        byte[] expanded = new byte[length];
        if (offset == 1) {
            byte[] block = this.expandedBlocks.peekFirst();
            byte b = block[block.length - 1];
            if (b != 0) {
                Arrays.fill(expanded, b);
            }
        } else {
            expandFromList(expanded, offset, length);
        }
        return expanded;
    }

    private void expandFromList(byte[] expanded, int offset, int length) {
        int copyOffset;
        int min;
        int offsetRemaining = offset;
        int lengthRemaining = length;
        int i = 0;
        while (true) {
            int writeOffset = i;
            if (lengthRemaining > 0) {
                byte[] block = null;
                if (offsetRemaining > 0) {
                    int blockOffset = 0;
                    Iterator<byte[]> it = this.expandedBlocks.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        byte[] b = it.next();
                        if (b.length + blockOffset >= offsetRemaining) {
                            block = b;
                            break;
                        }
                        blockOffset += b.length;
                    }
                    if (block == null) {
                        throw new IllegalStateException("Failed to find a block containing offset " + offset);
                    }
                    copyOffset = (blockOffset + block.length) - offsetRemaining;
                    min = Math.min(lengthRemaining, block.length - copyOffset);
                } else {
                    block = expanded;
                    copyOffset = -offsetRemaining;
                    min = Math.min(lengthRemaining, writeOffset + offsetRemaining);
                }
                int copyLen = min;
                System.arraycopy(block, copyOffset, expanded, writeOffset, copyLen);
                offsetRemaining -= copyLen;
                lengthRemaining -= copyLen;
                i = writeOffset + copyLen;
            } else {
                return;
            }
        }
    }

    private void clearUnusedPairs() {
        int pairLengths = 0;
        int pairsToKeep = 0;
        Iterator<Pair> it = this.pairs.descendingIterator();
        while (it.hasNext()) {
            Pair p = it.next();
            pairsToKeep++;
            pairLengths += p.length();
            if (pairLengths >= 65536) {
                break;
            }
        }
        int size = this.pairs.size();
        for (int i = pairsToKeep; i < size; i++) {
            Pair p2 = this.pairs.peekFirst();
            if (p2.hasBeenWritten()) {
                this.pairs.removeFirst();
            } else {
                return;
            }
        }
    }

    private void writeFinalLiteralBlock() throws IOException {
        rewriteLastPairs();
        for (Pair p : this.pairs) {
            if (!p.hasBeenWritten()) {
                p.writeTo(this.os);
            }
        }
        this.pairs.clear();
    }

    private void writeWritablePairs(int lengthOfBlocksAfterLastPair) throws IOException {
        int unwrittenLength = lengthOfBlocksAfterLastPair;
        Iterator<Pair> it = this.pairs.descendingIterator();
        while (it.hasNext()) {
            Pair p = it.next();
            if (p.hasBeenWritten()) {
                break;
            }
            unwrittenLength += p.length();
        }
        for (Pair p2 : this.pairs) {
            if (!p2.hasBeenWritten()) {
                unwrittenLength -= p2.length();
                if (p2.canBeWritten(unwrittenLength)) {
                    p2.writeTo(this.os);
                } else {
                    return;
                }
            }
        }
    }

    private void rewriteLastPairs() {
        LinkedList<Pair> lastPairs = new LinkedList<>();
        LinkedList<Integer> pairLength = new LinkedList<>();
        int offset = 0;
        Iterator<Pair> it = this.pairs.descendingIterator();
        while (it.hasNext()) {
            Pair p = it.next();
            if (p.hasBeenWritten()) {
                break;
            }
            int len = p.length();
            pairLength.addFirst(Integer.valueOf(len));
            lastPairs.addFirst(p);
            offset += len;
            if (offset >= 12) {
                break;
            }
        }
        Deque<Pair> deque = this.pairs;
        deque.getClass();
        lastPairs.forEach((v1) -> {
            r1.remove(v1);
        });
        int lastPairsSize = lastPairs.size();
        int toExpand = 0;
        for (int i = 1; i < lastPairsSize; i++) {
            toExpand += pairLength.get(i).intValue();
        }
        Pair replacement = new Pair();
        if (toExpand > 0) {
            replacement.prependLiteral(expand(toExpand, toExpand));
        }
        Pair splitCandidate = lastPairs.get(0);
        int stillNeeded = 12 - toExpand;
        int brLen = splitCandidate.hasBackReference() ? splitCandidate.backReferenceLength() : 0;
        if (!splitCandidate.hasBackReference() || brLen < 4 + stillNeeded) {
            if (splitCandidate.hasBackReference()) {
                replacement.prependLiteral(expand(toExpand + brLen, brLen));
            }
            splitCandidate.prependTo(replacement);
        } else {
            replacement.prependLiteral(expand(toExpand + stillNeeded, stillNeeded));
            this.pairs.add(splitCandidate.splitWithNewBackReferenceLengthOf(brLen - stillNeeded));
        }
        this.pairs.add(replacement);
    }

    public static Parameters.Builder createParameterBuilder() {
        return Parameters.builder(IcTuple.NESTED_CLASS_FLAG).withMinBackReferenceLength(4).withMaxBackReferenceLength(65535).withMaxOffset(65535).withMaxLiteralLength(65535);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz4/BlockLZ4CompressorOutputStream$Pair.class */
    public static final class Pair {
        private final Deque<byte[]> literals = new LinkedList();
        private int brOffset;
        private int brLength;
        private boolean written;

        Pair() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void prependLiteral(byte[] data) {
            this.literals.addFirst(data);
        }

        byte[] addLiteral(LZ77Compressor.LiteralBlock block) {
            byte[] copy = Arrays.copyOfRange(block.getData(), block.getOffset(), block.getOffset() + block.getLength());
            this.literals.add(copy);
            return copy;
        }

        void setBackReference(LZ77Compressor.BackReference block) {
            if (hasBackReference()) {
                throw new IllegalStateException();
            }
            this.brOffset = block.getOffset();
            this.brLength = block.getLength();
        }

        boolean hasBackReference() {
            return this.brOffset > 0;
        }

        boolean canBeWritten(int lengthOfBlocksAfterThisPair) {
            return hasBackReference() && lengthOfBlocksAfterThisPair >= 16;
        }

        int length() {
            return literalLength() + this.brLength;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasBeenWritten() {
            return this.written;
        }

        void writeTo(OutputStream out) throws IOException {
            int litLength = literalLength();
            out.write(lengths(litLength, this.brLength));
            if (litLength >= 15) {
                writeLength(litLength - 15, out);
            }
            for (byte[] b : this.literals) {
                out.write(b);
            }
            if (hasBackReference()) {
                ByteUtils.toLittleEndian(out, this.brOffset, 2);
                if (this.brLength - 4 >= 15) {
                    writeLength((this.brLength - 4) - 15, out);
                }
            }
            this.written = true;
        }

        private int literalLength() {
            return this.literals.stream().mapToInt(b -> {
                return b.length;
            }).sum();
        }

        private static int lengths(int litLength, int brLength) {
            int l = Math.min(litLength, 15);
            int br = brLength < 4 ? 0 : brLength < 19 ? brLength - 4 : 15;
            return (l << 4) | br;
        }

        private static void writeLength(int length, OutputStream out) throws IOException {
            while (length >= 255) {
                out.write(255);
                length -= 255;
            }
            out.write(length);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int backReferenceLength() {
            return this.brLength;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void prependTo(Pair other) {
            Iterator<byte[]> listBackwards = this.literals.descendingIterator();
            while (listBackwards.hasNext()) {
                other.prependLiteral(listBackwards.next());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Pair splitWithNewBackReferenceLengthOf(int newBackReferenceLength) {
            Pair p = new Pair();
            p.literals.addAll(this.literals);
            p.brOffset = this.brOffset;
            p.brLength = newBackReferenceLength;
            return p;
        }
    }
}
