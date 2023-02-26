package org.apache.commons.compress.compressors.bzip2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.CloseShieldFilterInputStream;
import org.apache.commons.compress.utils.InputStreamStatistics;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/bzip2/BZip2CompressorInputStream.class */
public class BZip2CompressorInputStream extends CompressorInputStream implements BZip2Constants, InputStreamStatistics {
    private int last;
    private int origPtr;
    private int blockSize100k;
    private boolean blockRandomised;
    private final CRC crc;
    private int nInUse;
    private BitInputStream bin;
    private final boolean decompressConcatenated;
    private static final int EOF = 0;
    private static final int START_BLOCK_STATE = 1;
    private static final int RAND_PART_A_STATE = 2;
    private static final int RAND_PART_B_STATE = 3;
    private static final int RAND_PART_C_STATE = 4;
    private static final int NO_RAND_PART_A_STATE = 5;
    private static final int NO_RAND_PART_B_STATE = 6;
    private static final int NO_RAND_PART_C_STATE = 7;
    private int currentState;
    private int storedBlockCRC;
    private int storedCombinedCRC;
    private int computedBlockCRC;
    private int computedCombinedCRC;
    private int su_count;
    private int su_ch2;
    private int su_chPrev;
    private int su_i2;
    private int su_j2;
    private int su_rNToGo;
    private int su_rTPos;
    private int su_tPos;
    private char su_z;
    private Data data;

    public BZip2CompressorInputStream(InputStream in) throws IOException {
        this(in, false);
    }

    public BZip2CompressorInputStream(InputStream in, boolean decompressConcatenated) throws IOException {
        this.crc = new CRC();
        this.currentState = 1;
        this.bin = new BitInputStream(in == System.in ? new CloseShieldFilterInputStream(in) : in, ByteOrder.BIG_ENDIAN);
        this.decompressConcatenated = decompressConcatenated;
        init(true);
        initBlock();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.bin != null) {
            int r = read0();
            count(r < 0 ? -1 : 1);
            return r;
        }
        throw new IOException("Stream closed");
    }

    @Override // java.io.InputStream
    public int read(byte[] dest, int offs, int len) throws IOException {
        int b;
        if (offs < 0) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
        }
        if (offs + len > dest.length) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > dest.length(" + dest.length + ").");
        }
        if (this.bin == null) {
            throw new IOException("Stream closed");
        }
        if (len == 0) {
            return 0;
        }
        int hi = offs + len;
        int destOffs = offs;
        while (destOffs < hi && (b = read0()) >= 0) {
            int i = destOffs;
            destOffs++;
            dest[i] = (byte) b;
            count(1);
        }
        if (destOffs == offs) {
            return -1;
        }
        return destOffs - offs;
    }

    @Override // org.apache.commons.compress.utils.InputStreamStatistics
    public long getCompressedCount() {
        return this.bin.getBytesRead();
    }

    private void makeMaps() {
        boolean[] inUse = this.data.inUse;
        byte[] seqToUnseq = this.data.seqToUnseq;
        int nInUseShadow = 0;
        for (int i = 0; i < 256; i++) {
            if (inUse[i]) {
                int i2 = nInUseShadow;
                nInUseShadow++;
                seqToUnseq[i2] = (byte) i;
            }
        }
        this.nInUse = nInUseShadow;
    }

    private int read0() throws IOException {
        switch (this.currentState) {
            case 0:
                return -1;
            case 1:
                return setupBlock();
            case 2:
                throw new IllegalStateException();
            case 3:
                return setupRandPartB();
            case 4:
                return setupRandPartC();
            case 5:
                throw new IllegalStateException();
            case 6:
                return setupNoRandPartB();
            case 7:
                return setupNoRandPartC();
            default:
                throw new IllegalStateException();
        }
    }

    private int readNextByte(BitInputStream in) throws IOException {
        long b = in.readBits(8);
        return (int) b;
    }

    private boolean init(boolean isFirstStream) throws IOException {
        if (null == this.bin) {
            throw new IOException("No InputStream");
        }
        if (!isFirstStream) {
            this.bin.clearBitCache();
        }
        int magic0 = readNextByte(this.bin);
        if (magic0 == -1 && !isFirstStream) {
            return false;
        }
        int magic1 = readNextByte(this.bin);
        int magic2 = readNextByte(this.bin);
        if (magic0 != 66 || magic1 != 90 || magic2 != 104) {
            throw new IOException(isFirstStream ? "Stream is not in the BZip2 format" : "Garbage after a valid BZip2 stream");
        }
        int blockSize = readNextByte(this.bin);
        if (blockSize < 49 || blockSize > 57) {
            throw new IOException("BZip2 block size is invalid");
        }
        this.blockSize100k = blockSize - 48;
        this.computedCombinedCRC = 0;
        return true;
    }

    private void initBlock() throws IOException {
        BitInputStream bin = this.bin;
        do {
            char magic0 = bsGetUByte(bin);
            char magic1 = bsGetUByte(bin);
            char magic2 = bsGetUByte(bin);
            char magic3 = bsGetUByte(bin);
            char magic4 = bsGetUByte(bin);
            char magic5 = bsGetUByte(bin);
            if (magic0 != 23 || magic1 != 'r' || magic2 != 'E' || magic3 != '8' || magic4 != 'P' || magic5 != 144) {
                if (magic0 != '1' || magic1 != 'A' || magic2 != 'Y' || magic3 != '&' || magic4 != 'S' || magic5 != 'Y') {
                    this.currentState = 0;
                    throw new IOException("Bad block header");
                }
                this.storedBlockCRC = bsGetInt(bin);
                this.blockRandomised = bsR(bin, 1) == 1;
                if (this.data == null) {
                    this.data = new Data(this.blockSize100k);
                }
                getAndMoveToFrontDecode();
                this.crc.initializeCRC();
                this.currentState = 1;
                return;
            }
        } while (!complete());
    }

    private void endBlock() throws IOException {
        this.computedBlockCRC = this.crc.getFinalCRC();
        if (this.storedBlockCRC != this.computedBlockCRC) {
            this.computedCombinedCRC = (this.storedCombinedCRC << 1) | (this.storedCombinedCRC >>> 31);
            this.computedCombinedCRC ^= this.storedBlockCRC;
            throw new IOException("BZip2 CRC error");
        }
        this.computedCombinedCRC = (this.computedCombinedCRC << 1) | (this.computedCombinedCRC >>> 31);
        this.computedCombinedCRC ^= this.computedBlockCRC;
    }

    private boolean complete() throws IOException {
        this.storedCombinedCRC = bsGetInt(this.bin);
        this.currentState = 0;
        this.data = null;
        if (this.storedCombinedCRC != this.computedCombinedCRC) {
            throw new IOException("BZip2 CRC error");
        }
        return (this.decompressConcatenated && init(false)) ? false : true;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        BitInputStream inShadow = this.bin;
        if (inShadow != null) {
            try {
                inShadow.close();
            } finally {
                this.data = null;
                this.bin = null;
            }
        }
    }

    private static int bsR(BitInputStream bin, int n) throws IOException {
        long thech = bin.readBits(n);
        if (thech < 0) {
            throw new IOException("Unexpected end of stream");
        }
        return (int) thech;
    }

    private static boolean bsGetBit(BitInputStream bin) throws IOException {
        return bsR(bin, 1) != 0;
    }

    private static char bsGetUByte(BitInputStream bin) throws IOException {
        return (char) bsR(bin, 8);
    }

    private static int bsGetInt(BitInputStream bin) throws IOException {
        return bsR(bin, 32);
    }

    private static void checkBounds(int checkVal, int limitExclusive, String name) throws IOException {
        if (checkVal < 0) {
            throw new IOException("Corrupted input, " + name + " value negative");
        }
        if (checkVal >= limitExclusive) {
            throw new IOException("Corrupted input, " + name + " value too big");
        }
    }

    private static void hbCreateDecodeTables(int[] limit, int[] base, int[] perm, char[] length, int minLen, int maxLen, int alphaSize) throws IOException {
        int pp = 0;
        for (int i = minLen; i <= maxLen; i++) {
            for (int j = 0; j < alphaSize; j++) {
                if (length[j] == i) {
                    int i2 = pp;
                    pp++;
                    perm[i2] = j;
                }
            }
        }
        int i3 = 23;
        while (true) {
            i3--;
            if (i3 <= 0) {
                break;
            }
            base[i3] = 0;
            limit[i3] = 0;
        }
        for (int i4 = 0; i4 < alphaSize; i4++) {
            char c = length[i4];
            checkBounds(c, BZip2Constants.MAX_ALPHA_SIZE, "length");
            int i5 = c + 1;
            base[i5] = base[i5] + 1;
        }
        int b = base[0];
        for (int i6 = 1; i6 < 23; i6++) {
            b += base[i6];
            base[i6] = b;
        }
        int i7 = minLen;
        int vec = 0;
        int b2 = base[i7];
        while (i7 <= maxLen) {
            int nb = base[i7 + 1];
            int vec2 = vec + (nb - b2);
            b2 = nb;
            limit[i7] = vec2 - 1;
            vec = vec2 << 1;
            i7++;
        }
        for (int i8 = minLen + 1; i8 <= maxLen; i8++) {
            base[i8] = ((limit[i8 - 1] + 1) << 1) - base[i8];
        }
    }

    private void recvDecodingTables() throws IOException {
        BitInputStream bin = this.bin;
        Data dataShadow = this.data;
        boolean[] inUse = dataShadow.inUse;
        byte[] pos = dataShadow.recvDecodingTables_pos;
        byte[] selector = dataShadow.selector;
        byte[] selectorMtf = dataShadow.selectorMtf;
        int inUse16 = 0;
        for (int i = 0; i < 16; i++) {
            if (bsGetBit(bin)) {
                inUse16 |= 1 << i;
            }
        }
        Arrays.fill(inUse, false);
        for (int i2 = 0; i2 < 16; i2++) {
            if ((inUse16 & (1 << i2)) != 0) {
                int i16 = i2 << 4;
                for (int j = 0; j < 16; j++) {
                    if (bsGetBit(bin)) {
                        inUse[i16 + j] = true;
                    }
                }
            }
        }
        makeMaps();
        int alphaSize = this.nInUse + 2;
        int nGroups = bsR(bin, 3);
        int selectors = bsR(bin, 15);
        if (selectors < 0) {
            throw new IOException("Corrupted input, nSelectors value negative");
        }
        checkBounds(alphaSize, 259, "alphaSize");
        checkBounds(nGroups, 7, "nGroups");
        for (int i3 = 0; i3 < selectors; i3++) {
            int j2 = 0;
            while (bsGetBit(bin)) {
                j2++;
            }
            if (i3 < 18002) {
                selectorMtf[i3] = (byte) j2;
            }
        }
        int nSelectors = Math.min(selectors, (int) BZip2Constants.MAX_SELECTORS);
        int v = nGroups;
        while (true) {
            v--;
            if (v < 0) {
                break;
            }
            pos[v] = (byte) v;
        }
        for (int i4 = 0; i4 < nSelectors; i4++) {
            int v2 = selectorMtf[i4] & 255;
            checkBounds(v2, 6, "selectorMtf");
            byte tmp = pos[v2];
            while (v2 > 0) {
                pos[v2] = pos[v2 - 1];
                v2--;
            }
            pos[0] = tmp;
            selector[i4] = tmp;
        }
        char[][] len = dataShadow.temp_charArray2d;
        for (int t = 0; t < nGroups; t++) {
            int curr = bsR(bin, 5);
            char[] len_t = len[t];
            for (int i5 = 0; i5 < alphaSize; i5++) {
                while (bsGetBit(bin)) {
                    curr += bsGetBit(bin) ? -1 : 1;
                }
                len_t[i5] = (char) curr;
            }
        }
        createHuffmanDecodingTables(alphaSize, nGroups);
    }

    private void createHuffmanDecodingTables(int alphaSize, int nGroups) throws IOException {
        Data dataShadow = this.data;
        char[][] len = dataShadow.temp_charArray2d;
        int[] minLens = dataShadow.minLens;
        int[][] limit = dataShadow.limit;
        int[][] base = dataShadow.base;
        int[][] perm = dataShadow.perm;
        for (int t = 0; t < nGroups; t++) {
            int minLen = ' ';
            int maxLen = 0;
            char[] len_t = len[t];
            int i = alphaSize;
            while (true) {
                i--;
                if (i >= 0) {
                    char lent = len_t[i];
                    if (lent > maxLen) {
                        maxLen = lent;
                    }
                    if (lent < minLen) {
                        minLen = lent;
                    }
                }
            }
            hbCreateDecodeTables(limit[t], base[t], perm[t], len[t], minLen, maxLen, alphaSize);
            minLens[t] = minLen;
        }
    }

    private void getAndMoveToFrontDecode() throws IOException {
        int zvec;
        int zvec2;
        BitInputStream bin = this.bin;
        this.origPtr = bsR(bin, 24);
        recvDecodingTables();
        Data dataShadow = this.data;
        byte[] ll8 = dataShadow.ll8;
        int[] unzftab = dataShadow.unzftab;
        byte[] selector = dataShadow.selector;
        byte[] seqToUnseq = dataShadow.seqToUnseq;
        char[] yy = dataShadow.getAndMoveToFrontDecode_yy;
        int[] minLens = dataShadow.minLens;
        int[][] limit = dataShadow.limit;
        int[][] base = dataShadow.base;
        int[][] perm = dataShadow.perm;
        int limitLast = this.blockSize100k * BZip2Constants.BASEBLOCKSIZE;
        int i = 256;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            yy[i] = (char) i;
            unzftab[i] = 0;
        }
        int groupNo = 0;
        int groupPos = 49;
        int eob = this.nInUse + 1;
        int nextSym = getAndMoveToFrontDecode0();
        int lastShadow = -1;
        int zt = selector[0] & 255;
        checkBounds(zt, 6, "zt");
        int[] base_zt = base[zt];
        int[] limit_zt = limit[zt];
        int[] perm_zt = perm[zt];
        int minLens_zt = minLens[zt];
        while (nextSym != eob) {
            if (nextSym == 0 || nextSym == 1) {
                int s = -1;
                int i2 = 1;
                while (true) {
                    int n = i2;
                    if (nextSym == 0) {
                        s += n;
                    } else if (nextSym != 1) {
                        break;
                    } else {
                        s += n << 1;
                    }
                    if (groupPos == 0) {
                        groupPos = 49;
                        groupNo++;
                        checkBounds(groupNo, BZip2Constants.MAX_SELECTORS, "groupNo");
                        int zt2 = selector[groupNo] & 255;
                        checkBounds(zt2, 6, "zt");
                        base_zt = base[zt2];
                        limit_zt = limit[zt2];
                        perm_zt = perm[zt2];
                        minLens_zt = minLens[zt2];
                    } else {
                        groupPos--;
                    }
                    int zn = minLens_zt;
                    checkBounds(zn, BZip2Constants.MAX_ALPHA_SIZE, "zn");
                    int bsR = bsR(bin, zn);
                    while (true) {
                        zvec = bsR;
                        if (zvec > limit_zt[zn]) {
                            zn++;
                            checkBounds(zn, BZip2Constants.MAX_ALPHA_SIZE, "zn");
                            bsR = (zvec << 1) | bsR(bin, 1);
                        }
                    }
                    int tmp = zvec - base_zt[zn];
                    checkBounds(tmp, BZip2Constants.MAX_ALPHA_SIZE, "zvec");
                    nextSym = perm_zt[tmp];
                    i2 = n << 1;
                }
                checkBounds(s, this.data.ll8.length, "s");
                char c = yy[0];
                checkBounds(c, 256, "yy");
                byte ch2 = seqToUnseq[c];
                int i3 = ch2 & 255;
                unzftab[i3] = unzftab[i3] + s + 1;
                int lastShadow2 = lastShadow + 1;
                lastShadow = lastShadow2 + s;
                checkBounds(lastShadow, this.data.ll8.length, "lastShadow");
                Arrays.fill(ll8, lastShadow2, lastShadow + 1, ch2);
                if (lastShadow >= limitLast) {
                    throw new IOException("Block overrun while expanding RLE in MTF, " + lastShadow + " exceeds " + limitLast);
                }
            } else {
                lastShadow++;
                if (lastShadow >= limitLast) {
                    throw new IOException("Block overrun in MTF, " + lastShadow + " exceeds " + limitLast);
                }
                checkBounds(nextSym, TarConstants.MAGIC_OFFSET, "nextSym");
                char tmp2 = yy[nextSym - 1];
                checkBounds(tmp2, 256, "yy");
                int i4 = seqToUnseq[tmp2] & 255;
                unzftab[i4] = unzftab[i4] + 1;
                ll8[lastShadow] = seqToUnseq[tmp2];
                if (nextSym <= 16) {
                    int j = nextSym - 1;
                    while (j > 0) {
                        int i5 = j;
                        j--;
                        yy[i5] = yy[j];
                    }
                } else {
                    System.arraycopy(yy, 0, yy, 1, nextSym - 1);
                }
                yy[0] = tmp2;
                if (groupPos == 0) {
                    groupPos = 49;
                    groupNo++;
                    checkBounds(groupNo, BZip2Constants.MAX_SELECTORS, "groupNo");
                    int zt3 = selector[groupNo] & 255;
                    checkBounds(zt3, 6, "zt");
                    base_zt = base[zt3];
                    limit_zt = limit[zt3];
                    perm_zt = perm[zt3];
                    minLens_zt = minLens[zt3];
                } else {
                    groupPos--;
                }
                int zn2 = minLens_zt;
                checkBounds(zn2, BZip2Constants.MAX_ALPHA_SIZE, "zn");
                int bsR2 = bsR(bin, zn2);
                while (true) {
                    zvec2 = bsR2;
                    if (zvec2 <= limit_zt[zn2]) {
                        break;
                    }
                    zn2++;
                    checkBounds(zn2, BZip2Constants.MAX_ALPHA_SIZE, "zn");
                    bsR2 = (zvec2 << 1) | bsR(bin, 1);
                }
                int idx = zvec2 - base_zt[zn2];
                checkBounds(idx, BZip2Constants.MAX_ALPHA_SIZE, "zvec");
                nextSym = perm_zt[idx];
            }
        }
        this.last = lastShadow;
    }

    private int getAndMoveToFrontDecode0() throws IOException {
        Data dataShadow = this.data;
        int zt = dataShadow.selector[0] & 255;
        checkBounds(zt, 6, "zt");
        int[] limit_zt = dataShadow.limit[zt];
        int zn = dataShadow.minLens[zt];
        checkBounds(zn, BZip2Constants.MAX_ALPHA_SIZE, "zn");
        int bsR = bsR(this.bin, zn);
        while (true) {
            int zvec = bsR;
            if (zvec > limit_zt[zn]) {
                zn++;
                checkBounds(zn, BZip2Constants.MAX_ALPHA_SIZE, "zn");
                bsR = (zvec << 1) | bsR(this.bin, 1);
            } else {
                int tmp = zvec - dataShadow.base[zt][zn];
                checkBounds(tmp, BZip2Constants.MAX_ALPHA_SIZE, "zvec");
                return dataShadow.perm[zt][tmp];
            }
        }
    }

    private int setupBlock() throws IOException {
        if (this.currentState == 0 || this.data == null) {
            return -1;
        }
        int[] cftab = this.data.cftab;
        int ttLen = this.last + 1;
        int[] tt = this.data.initTT(ttLen);
        byte[] ll8 = this.data.ll8;
        cftab[0] = 0;
        System.arraycopy(this.data.unzftab, 0, cftab, 1, 256);
        int c = cftab[0];
        for (int i = 1; i <= 256; i++) {
            c += cftab[i];
            cftab[i] = c;
        }
        int lastShadow = this.last;
        for (int i2 = 0; i2 <= lastShadow; i2++) {
            int i3 = ll8[i2] & 255;
            int tmp = cftab[i3];
            cftab[i3] = tmp + 1;
            checkBounds(tmp, ttLen, "tt index");
            tt[tmp] = i2;
        }
        if (this.origPtr < 0 || this.origPtr >= tt.length) {
            throw new IOException("Stream corrupted");
        }
        this.su_tPos = tt[this.origPtr];
        this.su_count = 0;
        this.su_i2 = 0;
        this.su_ch2 = 256;
        if (this.blockRandomised) {
            this.su_rNToGo = 0;
            this.su_rTPos = 0;
            return setupRandPartA();
        }
        return setupNoRandPartA();
    }

    private int setupRandPartA() throws IOException {
        if (this.su_i2 <= this.last) {
            this.su_chPrev = this.su_ch2;
            int su_ch2Shadow = this.data.ll8[this.su_tPos] & 255;
            checkBounds(this.su_tPos, this.data.tt.length, "su_tPos");
            this.su_tPos = this.data.tt[this.su_tPos];
            if (this.su_rNToGo == 0) {
                this.su_rNToGo = Rand.rNums(this.su_rTPos) - 1;
                int i = this.su_rTPos + 1;
                this.su_rTPos = i;
                if (i == 512) {
                    this.su_rTPos = 0;
                }
            } else {
                this.su_rNToGo--;
            }
            int su_ch2Shadow2 = su_ch2Shadow ^ (this.su_rNToGo == 1 ? 1 : 0);
            this.su_ch2 = su_ch2Shadow2;
            this.su_i2++;
            this.currentState = 3;
            this.crc.updateCRC(su_ch2Shadow2);
            return su_ch2Shadow2;
        }
        endBlock();
        initBlock();
        return setupBlock();
    }

    private int setupNoRandPartA() throws IOException {
        if (this.su_i2 <= this.last) {
            this.su_chPrev = this.su_ch2;
            int su_ch2Shadow = this.data.ll8[this.su_tPos] & 255;
            this.su_ch2 = su_ch2Shadow;
            checkBounds(this.su_tPos, this.data.tt.length, "su_tPos");
            this.su_tPos = this.data.tt[this.su_tPos];
            this.su_i2++;
            this.currentState = 6;
            this.crc.updateCRC(su_ch2Shadow);
            return su_ch2Shadow;
        }
        this.currentState = 5;
        endBlock();
        initBlock();
        return setupBlock();
    }

    private int setupRandPartB() throws IOException {
        if (this.su_ch2 != this.su_chPrev) {
            this.currentState = 2;
            this.su_count = 1;
            return setupRandPartA();
        }
        int i = this.su_count + 1;
        this.su_count = i;
        if (i < 4) {
            this.currentState = 2;
            return setupRandPartA();
        }
        this.su_z = (char) (this.data.ll8[this.su_tPos] & 255);
        checkBounds(this.su_tPos, this.data.tt.length, "su_tPos");
        this.su_tPos = this.data.tt[this.su_tPos];
        if (this.su_rNToGo == 0) {
            this.su_rNToGo = Rand.rNums(this.su_rTPos) - 1;
            int i2 = this.su_rTPos + 1;
            this.su_rTPos = i2;
            if (i2 == 512) {
                this.su_rTPos = 0;
            }
        } else {
            this.su_rNToGo--;
        }
        this.su_j2 = 0;
        this.currentState = 4;
        if (this.su_rNToGo == 1) {
            this.su_z = (char) (this.su_z ^ 1);
        }
        return setupRandPartC();
    }

    private int setupRandPartC() throws IOException {
        if (this.su_j2 < this.su_z) {
            this.crc.updateCRC(this.su_ch2);
            this.su_j2++;
            return this.su_ch2;
        }
        this.currentState = 2;
        this.su_i2++;
        this.su_count = 0;
        return setupRandPartA();
    }

    private int setupNoRandPartB() throws IOException {
        if (this.su_ch2 != this.su_chPrev) {
            this.su_count = 1;
            return setupNoRandPartA();
        }
        int i = this.su_count + 1;
        this.su_count = i;
        if (i >= 4) {
            checkBounds(this.su_tPos, this.data.ll8.length, "su_tPos");
            this.su_z = (char) (this.data.ll8[this.su_tPos] & 255);
            this.su_tPos = this.data.tt[this.su_tPos];
            this.su_j2 = 0;
            return setupNoRandPartC();
        }
        return setupNoRandPartA();
    }

    private int setupNoRandPartC() throws IOException {
        if (this.su_j2 < this.su_z) {
            int su_ch2Shadow = this.su_ch2;
            this.crc.updateCRC(su_ch2Shadow);
            this.su_j2++;
            this.currentState = 7;
            return su_ch2Shadow;
        }
        this.su_i2++;
        this.su_count = 0;
        return setupNoRandPartA();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/bzip2/BZip2CompressorInputStream$Data.class */
    public static final class Data {
        final boolean[] inUse = new boolean[256];
        final byte[] seqToUnseq = new byte[256];
        final byte[] selector = new byte[BZip2Constants.MAX_SELECTORS];
        final byte[] selectorMtf = new byte[BZip2Constants.MAX_SELECTORS];
        final int[] unzftab = new int[256];
        final int[][] limit = new int[6][BZip2Constants.MAX_ALPHA_SIZE];
        final int[][] base = new int[6][BZip2Constants.MAX_ALPHA_SIZE];
        final int[][] perm = new int[6][BZip2Constants.MAX_ALPHA_SIZE];
        final int[] minLens = new int[6];
        final int[] cftab = new int[TarConstants.MAGIC_OFFSET];
        final char[] getAndMoveToFrontDecode_yy = new char[256];
        final char[][] temp_charArray2d = new char[6][BZip2Constants.MAX_ALPHA_SIZE];
        final byte[] recvDecodingTables_pos = new byte[6];
        int[] tt;
        final byte[] ll8;

        Data(int blockSize100k) {
            this.ll8 = new byte[blockSize100k * BZip2Constants.BASEBLOCKSIZE];
        }

        int[] initTT(int length) {
            int[] ttShadow = this.tt;
            if (ttShadow == null || ttShadow.length < length) {
                int[] iArr = new int[length];
                ttShadow = iArr;
                this.tt = iArr;
            }
            return ttShadow;
        }
    }

    public static boolean matches(byte[] signature, int length) {
        return length >= 3 && signature[0] == 66 && signature[1] == 90 && signature[2] == 104;
    }
}
