package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Folder.class */
class Folder {
    Coder[] coders;
    long totalInputStreams;
    long totalOutputStreams;
    BindPair[] bindPairs;
    long[] packedStreams;
    long[] unpackSizes;
    boolean hasCrc;
    long crc;
    int numUnpackSubStreams;
    static final Folder[] EMPTY_FOLDER_ARRAY = new Folder[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    public Iterable<Coder> getOrderedCoders() throws IOException {
        if (this.packedStreams == null || this.coders == null || this.packedStreams.length == 0 || this.coders.length == 0) {
            return Collections.emptyList();
        }
        LinkedList<Coder> l = new LinkedList<>();
        int i = (int) this.packedStreams[0];
        while (true) {
            int current = i;
            if (current < 0 || current >= this.coders.length) {
                break;
            } else if (l.contains(this.coders[current])) {
                throw new IOException("folder uses the same coder more than once in coder chain");
            } else {
                l.addLast(this.coders[current]);
                int pair = findBindPairForOutStream(current);
                i = pair != -1 ? (int) this.bindPairs[pair].inIndex : -1;
            }
        }
        return l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int findBindPairForInStream(int index) {
        if (this.bindPairs != null) {
            for (int i = 0; i < this.bindPairs.length; i++) {
                if (this.bindPairs[i].inIndex == index) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    int findBindPairForOutStream(int index) {
        if (this.bindPairs != null) {
            for (int i = 0; i < this.bindPairs.length; i++) {
                if (this.bindPairs[i].outIndex == index) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getUnpackSize() {
        if (this.totalOutputStreams == 0) {
            return 0L;
        }
        for (int i = ((int) this.totalOutputStreams) - 1; i >= 0; i--) {
            if (findBindPairForOutStream(i) < 0) {
                return this.unpackSizes[i];
            }
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getUnpackSizeForCoder(Coder coder) {
        if (this.coders != null) {
            for (int i = 0; i < this.coders.length; i++) {
                if (this.coders[i] == coder) {
                    return this.unpackSizes[i];
                }
            }
            return 0L;
        }
        return 0L;
    }

    public String toString() {
        return "Folder with " + this.coders.length + " coders, " + this.totalInputStreams + " input streams, " + this.totalOutputStreams + " output streams, " + this.bindPairs.length + " bind pairs, " + this.packedStreams.length + " packed streams, " + this.unpackSizes.length + " unpack sizes, " + (this.hasCrc ? "with CRC " + this.crc : "without CRC") + " and " + this.numUnpackSubStreams + " unpack streams";
    }
}
