package org.apache.commons.compress.compressors.bzip2;

import ch.qos.logback.core.net.SyslogConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.apache.commons.compress.compressors.CompressorOutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/bzip2/BZip2CompressorOutputStream.class */
public class BZip2CompressorOutputStream extends CompressorOutputStream implements BZip2Constants {
    public static final int MIN_BLOCKSIZE = 1;
    public static final int MAX_BLOCKSIZE = 9;
    private static final int GREATER_ICOST = 15;
    private static final int LESSER_ICOST = 0;
    private int last;
    private final int blockSize100k;
    private int bsBuff;
    private int bsLive;
    private final CRC crc;
    private int nInUse;
    private int nMTF;
    private int currentChar;
    private int runLength;
    private int blockCRC;
    private int combinedCRC;
    private final int allowableBlockSize;
    private Data data;
    private BlockSort blockSorter;
    private OutputStream out;
    private volatile boolean closed;

    private static void hbMakeCodeLengths(byte[] len, int[] freq, Data dat, int alphaSize, int maxLen) {
        int[] heap = dat.heap;
        int[] weight = dat.weight;
        int[] parent = dat.parent;
        int i = alphaSize;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            weight[i + 1] = (freq[i] == 0 ? 1 : freq[i]) << 8;
        }
        boolean tooLong = true;
        while (tooLong) {
            tooLong = false;
            int nNodes = alphaSize;
            int nHeap = 0;
            heap[0] = 0;
            weight[0] = 0;
            parent[0] = -2;
            for (int i2 = 1; i2 <= alphaSize; i2++) {
                parent[i2] = -1;
                nHeap++;
                heap[nHeap] = i2;
                int zz = nHeap;
                int tmp = heap[zz];
                while (weight[tmp] < weight[heap[zz >> 1]]) {
                    heap[zz] = heap[zz >> 1];
                    zz >>= 1;
                }
                heap[zz] = tmp;
            }
            while (nHeap > 1) {
                int n1 = heap[1];
                heap[1] = heap[nHeap];
                int nHeap2 = nHeap - 1;
                int zz2 = 1;
                int tmp2 = heap[1];
                while (true) {
                    int yy = zz2 << 1;
                    if (yy > nHeap2) {
                        break;
                    }
                    if (yy < nHeap2 && weight[heap[yy + 1]] < weight[heap[yy]]) {
                        yy++;
                    }
                    if (weight[tmp2] < weight[heap[yy]]) {
                        break;
                    }
                    heap[zz2] = heap[yy];
                    zz2 = yy;
                }
                heap[zz2] = tmp2;
                int n2 = heap[1];
                heap[1] = heap[nHeap2];
                int nHeap3 = nHeap2 - 1;
                int zz3 = 1;
                int tmp3 = heap[1];
                while (true) {
                    int yy2 = zz3 << 1;
                    if (yy2 > nHeap3) {
                        break;
                    }
                    if (yy2 < nHeap3 && weight[heap[yy2 + 1]] < weight[heap[yy2]]) {
                        yy2++;
                    }
                    if (weight[tmp3] < weight[heap[yy2]]) {
                        break;
                    }
                    heap[zz3] = heap[yy2];
                    zz3 = yy2;
                }
                heap[zz3] = tmp3;
                nNodes++;
                parent[n2] = nNodes;
                parent[n1] = nNodes;
                int weight_n1 = weight[n1];
                int weight_n2 = weight[n2];
                weight[nNodes] = ((weight_n1 & (-256)) + (weight_n2 & (-256))) | (1 + Math.max(weight_n1 & 255, weight_n2 & 255));
                parent[nNodes] = -1;
                nHeap = nHeap3 + 1;
                heap[nHeap] = nNodes;
                int zz4 = nHeap;
                int tmp4 = heap[zz4];
                int weight_tmp = weight[tmp4];
                while (weight_tmp < weight[heap[zz4 >> 1]]) {
                    heap[zz4] = heap[zz4 >> 1];
                    zz4 >>= 1;
                }
                heap[zz4] = tmp4;
            }
            for (int i3 = 1; i3 <= alphaSize; i3++) {
                int j = 0;
                int k = i3;
                while (true) {
                    int parent_k = parent[k];
                    if (parent_k < 0) {
                        break;
                    }
                    k = parent_k;
                    j++;
                }
                len[i3 - 1] = (byte) j;
                if (j > maxLen) {
                    tooLong = true;
                }
            }
            if (tooLong) {
                for (int i4 = 1; i4 < alphaSize; i4++) {
                    int j2 = weight[i4] >> 8;
                    weight[i4] = (1 + (j2 >> 1)) << 8;
                }
            }
        }
    }

    public static int chooseBlockSize(long inputLength) {
        if (inputLength > 0) {
            return (int) Math.min((inputLength / 132000) + 1, 9L);
        }
        return 9;
    }

    public BZip2CompressorOutputStream(OutputStream out) throws IOException {
        this(out, 9);
    }

    public BZip2CompressorOutputStream(OutputStream out, int blockSize) throws IOException {
        this.crc = new CRC();
        this.currentChar = -1;
        if (blockSize < 1) {
            throw new IllegalArgumentException("blockSize(" + blockSize + ") < 1");
        }
        if (blockSize > 9) {
            throw new IllegalArgumentException("blockSize(" + blockSize + ") > 9");
        }
        this.blockSize100k = blockSize;
        this.out = out;
        this.allowableBlockSize = (this.blockSize100k * BZip2Constants.BASEBLOCKSIZE) - 20;
        init();
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Closed");
        }
        write0(b);
    }

    private void writeRun() throws IOException {
        int lastShadow = this.last;
        if (lastShadow < this.allowableBlockSize) {
            int currentCharShadow = this.currentChar;
            Data dataShadow = this.data;
            dataShadow.inUse[currentCharShadow] = true;
            byte ch2 = (byte) currentCharShadow;
            int runLengthShadow = this.runLength;
            this.crc.updateCRC(currentCharShadow, runLengthShadow);
            switch (runLengthShadow) {
                case 1:
                    dataShadow.block[lastShadow + 2] = ch2;
                    this.last = lastShadow + 1;
                    return;
                case 2:
                    dataShadow.block[lastShadow + 2] = ch2;
                    dataShadow.block[lastShadow + 3] = ch2;
                    this.last = lastShadow + 2;
                    return;
                case 3:
                    byte[] block = dataShadow.block;
                    block[lastShadow + 2] = ch2;
                    block[lastShadow + 3] = ch2;
                    block[lastShadow + 4] = ch2;
                    this.last = lastShadow + 3;
                    return;
                default:
                    int runLengthShadow2 = runLengthShadow - 4;
                    dataShadow.inUse[runLengthShadow2] = true;
                    byte[] block2 = dataShadow.block;
                    block2[lastShadow + 2] = ch2;
                    block2[lastShadow + 3] = ch2;
                    block2[lastShadow + 4] = ch2;
                    block2[lastShadow + 5] = ch2;
                    block2[lastShadow + 6] = (byte) runLengthShadow2;
                    this.last = lastShadow + 5;
                    return;
            }
        }
        endBlock();
        initBlock();
        writeRun();
    }

    protected void finalize() throws Throwable {
        if (!this.closed) {
            System.err.println("Unclosed BZip2CompressorOutputStream detected, will *not* close it");
        }
        super.finalize();
    }

    public void finish() throws IOException {
        if (!this.closed) {
            this.closed = true;
            try {
                if (this.runLength > 0) {
                    writeRun();
                }
                this.currentChar = -1;
                endBlock();
                endCompression();
            } finally {
                this.out = null;
                this.blockSorter = null;
                this.data = null;
            }
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            OutputStream outShadow = this.out;
            Throwable th = null;
            try {
                finish();
                if (outShadow != null) {
                    if (0 != 0) {
                        try {
                            outShadow.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    outShadow.close();
                }
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    if (outShadow != null) {
                        if (th3 != null) {
                            try {
                                outShadow.close();
                            } catch (Throwable th5) {
                                th3.addSuppressed(th5);
                            }
                        } else {
                            outShadow.close();
                        }
                    }
                    throw th4;
                }
            }
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        OutputStream outShadow = this.out;
        if (outShadow != null) {
            outShadow.flush();
        }
    }

    private void init() throws IOException {
        bsPutUByte(66);
        bsPutUByte(90);
        this.data = new Data(this.blockSize100k);
        this.blockSorter = new BlockSort(this.data);
        bsPutUByte(SyslogConstants.LOG_AUDIT);
        bsPutUByte(48 + this.blockSize100k);
        this.combinedCRC = 0;
        initBlock();
    }

    private void initBlock() {
        this.crc.initializeCRC();
        this.last = -1;
        boolean[] inUse = this.data.inUse;
        int i = 256;
        while (true) {
            i--;
            if (i >= 0) {
                inUse[i] = false;
            } else {
                return;
            }
        }
    }

    private void endBlock() throws IOException {
        this.blockCRC = this.crc.getFinalCRC();
        this.combinedCRC = (this.combinedCRC << 1) | (this.combinedCRC >>> 31);
        this.combinedCRC ^= this.blockCRC;
        if (this.last == -1) {
            return;
        }
        blockSort();
        bsPutUByte(49);
        bsPutUByte(65);
        bsPutUByte(89);
        bsPutUByte(38);
        bsPutUByte(83);
        bsPutUByte(89);
        bsPutInt(this.blockCRC);
        bsW(1, 0);
        moveToFrontCodeAndSend();
    }

    private void endCompression() throws IOException {
        bsPutUByte(23);
        bsPutUByte(114);
        bsPutUByte(69);
        bsPutUByte(56);
        bsPutUByte(80);
        bsPutUByte(144);
        bsPutInt(this.combinedCRC);
        bsFinishedWithStream();
    }

    public final int getBlockSize() {
        return this.blockSize100k;
    }

    @Override // java.io.OutputStream
    public void write(byte[] buf, int offs, int len) throws IOException {
        if (offs < 0) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
        }
        if (offs + len > buf.length) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > buf.length(" + buf.length + ").");
        }
        if (this.closed) {
            throw new IOException("Stream closed");
        }
        int hi = offs + len;
        while (offs < hi) {
            int i = offs;
            offs++;
            write0(buf[i]);
        }
    }

    private void write0(int b) throws IOException {
        if (this.currentChar != -1) {
            int b2 = b & 255;
            if (this.currentChar == b2) {
                int i = this.runLength + 1;
                this.runLength = i;
                if (i > 254) {
                    writeRun();
                    this.currentChar = -1;
                    this.runLength = 0;
                    return;
                }
                return;
            }
            writeRun();
            this.runLength = 1;
            this.currentChar = b2;
            return;
        }
        this.currentChar = b & 255;
        this.runLength++;
    }

    private static void hbAssignCodes(int[] code, byte[] length, int minLen, int maxLen, int alphaSize) {
        int vec = 0;
        for (int n = minLen; n <= maxLen; n++) {
            for (int i = 0; i < alphaSize; i++) {
                if ((length[i] & 255) == n) {
                    code[i] = vec;
                    vec++;
                }
            }
            vec <<= 1;
        }
    }

    private void bsFinishedWithStream() throws IOException {
        while (this.bsLive > 0) {
            int ch2 = this.bsBuff >> 24;
            this.out.write(ch2);
            this.bsBuff <<= 8;
            this.bsLive -= 8;
        }
    }

    private void bsW(int n, int v) throws IOException {
        OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        while (bsLiveShadow >= 8) {
            outShadow.write(bsBuffShadow >> 24);
            bsBuffShadow <<= 8;
            bsLiveShadow -= 8;
        }
        this.bsBuff = bsBuffShadow | (v << ((32 - bsLiveShadow) - n));
        this.bsLive = bsLiveShadow + n;
    }

    private void bsPutUByte(int c) throws IOException {
        bsW(8, c);
    }

    private void bsPutInt(int u) throws IOException {
        bsW(8, (u >> 24) & 255);
        bsW(8, (u >> 16) & 255);
        bsW(8, (u >> 8) & 255);
        bsW(8, u & 255);
    }

    private void sendMTFValues() throws IOException {
        byte[][] len = this.data.sendMTFValues_len;
        int alphaSize = this.nInUse + 2;
        int t = 6;
        while (true) {
            t--;
            if (t < 0) {
                break;
            }
            byte[] len_t = len[t];
            int v = alphaSize;
            while (true) {
                v--;
                if (v >= 0) {
                    len_t[v] = 15;
                }
            }
        }
        int nGroups = this.nMTF < 200 ? 2 : this.nMTF < 600 ? 3 : this.nMTF < 1200 ? 4 : this.nMTF < 2400 ? 5 : 6;
        sendMTFValues0(nGroups, alphaSize);
        int nSelectors = sendMTFValues1(nGroups, alphaSize);
        sendMTFValues2(nGroups, nSelectors);
        sendMTFValues3(nGroups, alphaSize);
        sendMTFValues4();
        sendMTFValues5(nGroups, nSelectors);
        sendMTFValues6(nGroups, alphaSize);
        sendMTFValues7();
    }

    private void sendMTFValues0(int nGroups, int alphaSize) {
        byte[][] len = this.data.sendMTFValues_len;
        int[] mtfFreq = this.data.mtfFreq;
        int remF = this.nMTF;
        int gs = 0;
        for (int nPart = nGroups; nPart > 0; nPart--) {
            int tFreq = remF / nPart;
            int ge = gs - 1;
            int aFreq = 0;
            int a = alphaSize - 1;
            while (aFreq < tFreq && ge < a) {
                ge++;
                aFreq += mtfFreq[ge];
            }
            if (ge > gs && nPart != nGroups && nPart != 1 && ((nGroups - nPart) & 1) != 0) {
                int i = ge;
                ge--;
                aFreq -= mtfFreq[i];
            }
            byte[] len_np = len[nPart - 1];
            int v = alphaSize;
            while (true) {
                v--;
                if (v >= 0) {
                    if (v >= gs && v <= ge) {
                        len_np[v] = 0;
                    } else {
                        len_np[v] = 15;
                    }
                }
            }
            gs = ge + 1;
            remF -= aFreq;
        }
    }

    private int sendMTFValues1(int nGroups, int alphaSize) {
        Data dataShadow = this.data;
        int[][] rfreq = dataShadow.sendMTFValues_rfreq;
        int[] fave = dataShadow.sendMTFValues_fave;
        short[] cost = dataShadow.sendMTFValues_cost;
        char[] sfmap = dataShadow.sfmap;
        byte[] selector = dataShadow.selector;
        byte[][] len = dataShadow.sendMTFValues_len;
        byte[] len_0 = len[0];
        byte[] len_1 = len[1];
        byte[] len_2 = len[2];
        byte[] len_3 = len[3];
        byte[] len_4 = len[4];
        byte[] len_5 = len[5];
        int nMTFShadow = this.nMTF;
        int nSelectors = 0;
        for (int iter = 0; iter < 4; iter++) {
            int t = nGroups;
            while (true) {
                t--;
                if (t < 0) {
                    break;
                }
                fave[t] = 0;
                int[] rfreqt = rfreq[t];
                int i = alphaSize;
                while (true) {
                    i--;
                    if (i >= 0) {
                        rfreqt[i] = 0;
                    }
                }
            }
            nSelectors = 0;
            int i2 = 0;
            while (true) {
                int gs = i2;
                if (gs >= this.nMTF) {
                    break;
                }
                int ge = Math.min((gs + 50) - 1, nMTFShadow - 1);
                if (nGroups == 6) {
                    short cost0 = 0;
                    short cost1 = 0;
                    short cost2 = 0;
                    short cost3 = 0;
                    short cost4 = 0;
                    short cost5 = 0;
                    for (int i3 = gs; i3 <= ge; i3++) {
                        char c = sfmap[i3];
                        cost0 = (short) (cost0 + (len_0[c] & 255));
                        cost1 = (short) (cost1 + (len_1[c] & 255));
                        cost2 = (short) (cost2 + (len_2[c] & 255));
                        cost3 = (short) (cost3 + (len_3[c] & 255));
                        cost4 = (short) (cost4 + (len_4[c] & 255));
                        cost5 = (short) (cost5 + (len_5[c] & 255));
                    }
                    cost[0] = cost0;
                    cost[1] = cost1;
                    cost[2] = cost2;
                    cost[3] = cost3;
                    cost[4] = cost4;
                    cost[5] = cost5;
                } else {
                    int t2 = nGroups;
                    while (true) {
                        t2--;
                        if (t2 < 0) {
                            break;
                        }
                        cost[t2] = 0;
                    }
                    for (int i4 = gs; i4 <= ge; i4++) {
                        char c2 = sfmap[i4];
                        int t3 = nGroups;
                        while (true) {
                            t3--;
                            if (t3 >= 0) {
                                cost[t3] = (short) (cost[t3] + (len[t3][c2] & 255));
                            }
                        }
                    }
                }
                int bt = -1;
                int t4 = nGroups;
                int bc = 999999999;
                while (true) {
                    t4--;
                    if (t4 < 0) {
                        break;
                    }
                    short s = cost[t4];
                    if (s < bc) {
                        bc = s;
                        bt = t4;
                    }
                }
                int i5 = bt;
                fave[i5] = fave[i5] + 1;
                selector[nSelectors] = (byte) bt;
                nSelectors++;
                int[] rfreq_bt = rfreq[bt];
                for (int i6 = gs; i6 <= ge; i6++) {
                    char c3 = sfmap[i6];
                    rfreq_bt[c3] = rfreq_bt[c3] + 1;
                }
                i2 = ge + 1;
            }
            for (int t5 = 0; t5 < nGroups; t5++) {
                hbMakeCodeLengths(len[t5], rfreq[t5], this.data, alphaSize, 20);
            }
        }
        return nSelectors;
    }

    private void sendMTFValues2(int nGroups, int nSelectors) {
        Data dataShadow = this.data;
        byte[] pos = dataShadow.sendMTFValues2_pos;
        int i = nGroups;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            pos[i] = (byte) i;
        }
        for (int i2 = 0; i2 < nSelectors; i2++) {
            byte ll_i = dataShadow.selector[i2];
            byte tmp = pos[0];
            int j = 0;
            while (ll_i != tmp) {
                j++;
                byte tmp2 = tmp;
                tmp = pos[j];
                pos[j] = tmp2;
            }
            pos[0] = tmp;
            dataShadow.selectorMtf[i2] = (byte) j;
        }
    }

    private void sendMTFValues3(int nGroups, int alphaSize) {
        int[][] code = this.data.sendMTFValues_code;
        byte[][] len = this.data.sendMTFValues_len;
        for (int t = 0; t < nGroups; t++) {
            int minLen = 32;
            int maxLen = 0;
            byte[] len_t = len[t];
            int i = alphaSize;
            while (true) {
                i--;
                if (i >= 0) {
                    int l = len_t[i] & 255;
                    if (l > maxLen) {
                        maxLen = l;
                    }
                    if (l < minLen) {
                        minLen = l;
                    }
                }
            }
            hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
        }
    }

    private void sendMTFValues4() throws IOException {
        boolean[] inUse = this.data.inUse;
        boolean[] inUse16 = this.data.sentMTFValues4_inUse16;
        int i = 16;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            inUse16[i] = false;
            int i16 = i * 16;
            int j = 16;
            while (true) {
                j--;
                if (j < 0) {
                    break;
                } else if (inUse[i16 + j]) {
                    inUse16[i] = true;
                    break;
                }
            }
        }
        for (int i2 = 0; i2 < 16; i2++) {
            bsW(1, inUse16[i2] ? 1 : 0);
        }
        OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        for (int i3 = 0; i3 < 16; i3++) {
            if (inUse16[i3]) {
                int i162 = i3 * 16;
                for (int j2 = 0; j2 < 16; j2++) {
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    if (inUse[i162 + j2]) {
                        bsBuffShadow |= 1 << ((32 - bsLiveShadow) - 1);
                    }
                    bsLiveShadow++;
                }
            }
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    private void sendMTFValues5(int nGroups, int nSelectors) throws IOException {
        bsW(3, nGroups);
        bsW(15, nSelectors);
        OutputStream outShadow = this.out;
        byte[] selectorMtf = this.data.selectorMtf;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        for (int i = 0; i < nSelectors; i++) {
            int hj = selectorMtf[i] & 255;
            for (int j = 0; j < hj; j++) {
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                bsBuffShadow |= 1 << ((32 - bsLiveShadow) - 1);
                bsLiveShadow++;
            }
            while (bsLiveShadow >= 8) {
                outShadow.write(bsBuffShadow >> 24);
                bsBuffShadow <<= 8;
                bsLiveShadow -= 8;
            }
            bsLiveShadow++;
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    private void sendMTFValues6(int nGroups, int alphaSize) throws IOException {
        byte[][] len = this.data.sendMTFValues_len;
        OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        for (int t = 0; t < nGroups; t++) {
            byte[] len_t = len[t];
            int curr = len_t[0] & 255;
            while (bsLiveShadow >= 8) {
                outShadow.write(bsBuffShadow >> 24);
                bsBuffShadow <<= 8;
                bsLiveShadow -= 8;
            }
            bsBuffShadow |= curr << ((32 - bsLiveShadow) - 5);
            bsLiveShadow += 5;
            for (int i = 0; i < alphaSize; i++) {
                int lti = len_t[i] & 255;
                while (curr < lti) {
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    bsBuffShadow |= 2 << ((32 - bsLiveShadow) - 2);
                    bsLiveShadow += 2;
                    curr++;
                }
                while (curr > lti) {
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24);
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    bsBuffShadow |= 3 << ((32 - bsLiveShadow) - 2);
                    bsLiveShadow += 2;
                    curr--;
                }
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                bsLiveShadow++;
            }
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    private void sendMTFValues7() throws IOException {
        Data dataShadow = this.data;
        byte[][] len = dataShadow.sendMTFValues_len;
        int[][] code = dataShadow.sendMTFValues_code;
        OutputStream outShadow = this.out;
        byte[] selector = dataShadow.selector;
        char[] sfmap = dataShadow.sfmap;
        int nMTFShadow = this.nMTF;
        int selCtr = 0;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;
        int gs = 0;
        while (gs < nMTFShadow) {
            int ge = Math.min((gs + 50) - 1, nMTFShadow - 1);
            int selector_selCtr = selector[selCtr] & 255;
            int[] code_selCtr = code[selector_selCtr];
            byte[] len_selCtr = len[selector_selCtr];
            while (gs <= ge) {
                char c = sfmap[gs];
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                int n = len_selCtr[c] & 255;
                bsBuffShadow |= code_selCtr[c] << ((32 - bsLiveShadow) - n);
                bsLiveShadow += n;
                gs++;
            }
            gs = ge + 1;
            selCtr++;
        }
        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    private void moveToFrontCodeAndSend() throws IOException {
        bsW(24, this.data.origPtr);
        generateMTFValues();
        sendMTFValues();
    }

    private void blockSort() {
        this.blockSorter.blockSort(this.data, this.last);
    }

    private void generateMTFValues() {
        int lastShadow = this.last;
        Data dataShadow = this.data;
        boolean[] inUse = dataShadow.inUse;
        byte[] block = dataShadow.block;
        int[] fmap = dataShadow.fmap;
        char[] sfmap = dataShadow.sfmap;
        int[] mtfFreq = dataShadow.mtfFreq;
        byte[] unseqToSeq = dataShadow.unseqToSeq;
        byte[] yy = dataShadow.generateMTFValues_yy;
        int nInUseShadow = 0;
        for (int i = 0; i < 256; i++) {
            if (inUse[i]) {
                unseqToSeq[i] = (byte) nInUseShadow;
                nInUseShadow++;
            }
        }
        this.nInUse = nInUseShadow;
        int eob = nInUseShadow + 1;
        Arrays.fill(mtfFreq, 0, eob + 1, 0);
        int i2 = nInUseShadow;
        while (true) {
            i2--;
            if (i2 < 0) {
                break;
            }
            yy[i2] = (byte) i2;
        }
        int wr = 0;
        int zPend = 0;
        for (int i3 = 0; i3 <= lastShadow; i3++) {
            byte ll_i = unseqToSeq[block[fmap[i3]] & 255];
            byte tmp = yy[0];
            int j = 0;
            while (ll_i != tmp) {
                j++;
                byte tmp2 = tmp;
                tmp = yy[j];
                yy[j] = tmp2;
            }
            yy[0] = tmp;
            if (j == 0) {
                zPend++;
            } else {
                if (zPend > 0) {
                    int zPend2 = zPend - 1;
                    while (true) {
                        if ((zPend2 & 1) == 0) {
                            sfmap[wr] = 0;
                            wr++;
                            mtfFreq[0] = mtfFreq[0] + 1;
                        } else {
                            sfmap[wr] = 1;
                            wr++;
                            mtfFreq[1] = mtfFreq[1] + 1;
                        }
                        if (zPend2 < 2) {
                            break;
                        }
                        zPend2 = (zPend2 - 2) >> 1;
                    }
                    zPend = 0;
                }
                sfmap[wr] = (char) (j + 1);
                wr++;
                int i4 = j + 1;
                mtfFreq[i4] = mtfFreq[i4] + 1;
            }
        }
        if (zPend > 0) {
            int zPend3 = zPend - 1;
            while (true) {
                if ((zPend3 & 1) == 0) {
                    sfmap[wr] = 0;
                    wr++;
                    mtfFreq[0] = mtfFreq[0] + 1;
                } else {
                    sfmap[wr] = 1;
                    wr++;
                    mtfFreq[1] = mtfFreq[1] + 1;
                }
                if (zPend3 < 2) {
                    break;
                }
                zPend3 = (zPend3 - 2) >> 1;
            }
        }
        sfmap[wr] = (char) eob;
        mtfFreq[eob] = mtfFreq[eob] + 1;
        this.nMTF = wr + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/bzip2/BZip2CompressorOutputStream$Data.class */
    public static final class Data {
        final boolean[] inUse = new boolean[256];
        final byte[] unseqToSeq = new byte[256];
        final int[] mtfFreq = new int[BZip2Constants.MAX_ALPHA_SIZE];
        final byte[] selector = new byte[BZip2Constants.MAX_SELECTORS];
        final byte[] selectorMtf = new byte[BZip2Constants.MAX_SELECTORS];
        final byte[] generateMTFValues_yy = new byte[256];
        final byte[][] sendMTFValues_len = new byte[6][BZip2Constants.MAX_ALPHA_SIZE];
        final int[][] sendMTFValues_rfreq = new int[6][BZip2Constants.MAX_ALPHA_SIZE];
        final int[] sendMTFValues_fave = new int[6];
        final short[] sendMTFValues_cost = new short[6];
        final int[][] sendMTFValues_code = new int[6][BZip2Constants.MAX_ALPHA_SIZE];
        final byte[] sendMTFValues2_pos = new byte[6];
        final boolean[] sentMTFValues4_inUse16 = new boolean[16];
        final int[] heap = new int[260];
        final int[] weight = new int[516];
        final int[] parent = new int[516];
        final byte[] block;
        final int[] fmap;
        final char[] sfmap;
        int origPtr;

        Data(int blockSize100k) {
            int n = blockSize100k * BZip2Constants.BASEBLOCKSIZE;
            this.block = new byte[n + 1 + 20];
            this.fmap = new int[n];
            this.sfmap = new char[2 * n];
        }
    }
}
