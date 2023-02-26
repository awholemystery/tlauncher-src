package org.apache.commons.compress.archivers.zip;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/BinaryTree.class */
class BinaryTree {
    private static final int UNDEFINED = -1;
    private static final int NODE = -2;
    private final int[] tree;

    public BinaryTree(int depth) {
        if (depth < 0 || depth > 30) {
            throw new IllegalArgumentException("depth must be bigger than 0 and not bigger than 30 but is " + depth);
        }
        this.tree = new int[(int) ((1 << (depth + 1)) - 1)];
        Arrays.fill(this.tree, -1);
    }

    public void addLeaf(int node, int path, int depth, int value) {
        if (depth == 0) {
            if (this.tree[node] != -1) {
                throw new IllegalArgumentException("Tree value at index " + node + " has already been assigned (" + this.tree[node] + ")");
            }
            this.tree[node] = value;
            return;
        }
        this.tree[node] = -2;
        int nextChild = (2 * node) + 1 + (path & 1);
        addLeaf(nextChild, path >>> 1, depth - 1, value);
    }

    public int read(BitStream stream) throws IOException {
        int i = 0;
        while (true) {
            int currentIndex = i;
            int bit = stream.nextBit();
            if (bit == -1) {
                return -1;
            }
            int childIndex = (2 * currentIndex) + 1 + bit;
            int value = this.tree[childIndex];
            if (value == -2) {
                i = childIndex;
            } else if (value != -1) {
                return value;
            } else {
                throw new IOException("The child " + bit + " of node at index " + currentIndex + " is not defined");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BinaryTree decode(InputStream inputStream, int totalNumberOfValues) throws IOException {
        if (totalNumberOfValues < 0) {
            throw new IllegalArgumentException("totalNumberOfValues must be bigger than 0, is " + totalNumberOfValues);
        }
        int size = inputStream.read() + 1;
        if (size == 0) {
            throw new IOException("Cannot read the size of the encoded tree, unexpected end of stream");
        }
        byte[] encodedTree = IOUtils.readRange(inputStream, size);
        if (encodedTree.length != size) {
            throw new EOFException();
        }
        int maxLength = 0;
        int[] originalBitLengths = new int[totalNumberOfValues];
        int pos = 0;
        for (byte b : encodedTree) {
            int numberOfValues = ((b & 240) >> 4) + 1;
            if (pos + numberOfValues > totalNumberOfValues) {
                throw new IOException("Number of values exceeds given total number of values");
            }
            int bitLength = (b & 15) + 1;
            for (int j = 0; j < numberOfValues; j++) {
                int i = pos;
                pos++;
                originalBitLengths[i] = bitLength;
            }
            maxLength = Math.max(maxLength, bitLength);
        }
        int oBitLengths = originalBitLengths.length;
        int[] permutation = new int[oBitLengths];
        for (int k = 0; k < permutation.length; k++) {
            permutation[k] = k;
        }
        int c = 0;
        int[] sortedBitLengths = new int[oBitLengths];
        for (int k2 = 0; k2 < oBitLengths; k2++) {
            for (int l = 0; l < oBitLengths; l++) {
                if (originalBitLengths[l] == k2) {
                    sortedBitLengths[c] = k2;
                    permutation[c] = l;
                    c++;
                }
            }
        }
        int code = 0;
        int codeIncrement = 0;
        int lastBitLength = 0;
        int[] codes = new int[totalNumberOfValues];
        for (int i2 = totalNumberOfValues - 1; i2 >= 0; i2--) {
            code += codeIncrement;
            if (sortedBitLengths[i2] != lastBitLength) {
                lastBitLength = sortedBitLengths[i2];
                codeIncrement = 1 << (16 - lastBitLength);
            }
            codes[permutation[i2]] = code;
        }
        BinaryTree tree = new BinaryTree(maxLength);
        for (int k3 = 0; k3 < codes.length; k3++) {
            int bitLength2 = originalBitLengths[k3];
            if (bitLength2 > 0) {
                tree.addLeaf(0, Integer.reverse(codes[k3] << 16), bitLength2, k3);
            }
        }
        return tree;
    }
}
