package org.apache.commons.compress.compressors.z;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.compress.compressors.lzw.LZWInputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/z/ZCompressorInputStream.class */
public class ZCompressorInputStream extends LZWInputStream {
    private static final int MAGIC_1 = 31;
    private static final int MAGIC_2 = 157;
    private static final int BLOCK_MODE_MASK = 128;
    private static final int MAX_CODE_SIZE_MASK = 31;
    private final boolean blockMode;
    private final int maxCodeSize;
    private long totalCodesRead;

    public ZCompressorInputStream(InputStream inputStream, int memoryLimitInKb) throws IOException {
        super(inputStream, ByteOrder.LITTLE_ENDIAN);
        int firstByte = (int) this.in.readBits(8);
        int secondByte = (int) this.in.readBits(8);
        int thirdByte = (int) this.in.readBits(8);
        if (firstByte != 31 || secondByte != MAGIC_2 || thirdByte < 0) {
            throw new IOException("Input is not in .Z format");
        }
        this.blockMode = (thirdByte & 128) != 0;
        this.maxCodeSize = thirdByte & 31;
        if (this.blockMode) {
            setClearCode(9);
        }
        initializeTables(this.maxCodeSize, memoryLimitInKb);
        clearEntries();
    }

    public ZCompressorInputStream(InputStream inputStream) throws IOException {
        this(inputStream, -1);
    }

    private void clearEntries() {
        setTableSize(256 + (this.blockMode ? 1 : 0));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.compress.compressors.lzw.LZWInputStream
    public int readNextCode() throws IOException {
        int code = super.readNextCode();
        if (code >= 0) {
            this.totalCodesRead++;
        }
        return code;
    }

    private void reAlignReading() throws IOException {
        long codeReadsToThrowAway = 8 - (this.totalCodesRead % 8);
        if (codeReadsToThrowAway == 8) {
            codeReadsToThrowAway = 0;
        }
        long j = 0;
        while (true) {
            long i = j;
            if (i < codeReadsToThrowAway) {
                readNextCode();
                j = i + 1;
            } else {
                this.in.clearBitCache();
                return;
            }
        }
    }

    @Override // org.apache.commons.compress.compressors.lzw.LZWInputStream
    protected int addEntry(int previousCode, byte character) throws IOException {
        int maxTableSize = 1 << getCodeSize();
        int r = addEntry(previousCode, character, maxTableSize);
        if (getTableSize() == maxTableSize && getCodeSize() < this.maxCodeSize) {
            reAlignReading();
            incrementCodeSize();
        }
        return r;
    }

    @Override // org.apache.commons.compress.compressors.lzw.LZWInputStream
    protected int decompressNextSymbol() throws IOException {
        int code = readNextCode();
        if (code < 0) {
            return -1;
        }
        if (this.blockMode && code == getClearCode()) {
            clearEntries();
            reAlignReading();
            resetCodeSize();
            resetPreviousCode();
            return 0;
        }
        boolean addedUnfinishedEntry = false;
        if (code == getTableSize()) {
            addRepeatOfPreviousCode();
            addedUnfinishedEntry = true;
        } else if (code > getTableSize()) {
            throw new IOException(String.format("Invalid %d bit code 0x%x", Integer.valueOf(getCodeSize()), Integer.valueOf(code)));
        }
        return expandCodeToOutputStack(code, addedUnfinishedEntry);
    }

    public static boolean matches(byte[] signature, int length) {
        return length > 3 && signature[0] == 31 && signature[1] == -99;
    }
}
