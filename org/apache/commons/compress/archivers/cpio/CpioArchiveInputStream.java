package org.apache.commons.compress.archivers.cpio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/cpio/CpioArchiveInputStream.class */
public class CpioArchiveInputStream extends ArchiveInputStream implements CpioConstants {
    private boolean closed;
    private CpioArchiveEntry entry;
    private long entryBytesRead;
    private boolean entryEOF;
    private final byte[] tmpbuf;
    private long crc;
    private final InputStream in;
    private final byte[] twoBytesBuf;
    private final byte[] fourBytesBuf;
    private final byte[] sixBytesBuf;
    private final int blockSize;
    private final ZipEncoding zipEncoding;
    final String encoding;

    public CpioArchiveInputStream(InputStream in) {
        this(in, 512, "US-ASCII");
    }

    public CpioArchiveInputStream(InputStream in, String encoding) {
        this(in, 512, encoding);
    }

    public CpioArchiveInputStream(InputStream in, int blockSize) {
        this(in, blockSize, "US-ASCII");
    }

    public CpioArchiveInputStream(InputStream in, int blockSize, String encoding) {
        this.tmpbuf = new byte[CpioConstants.C_ISFIFO];
        this.twoBytesBuf = new byte[2];
        this.fourBytesBuf = new byte[4];
        this.sixBytesBuf = new byte[6];
        this.in = in;
        if (blockSize <= 0) {
            throw new IllegalArgumentException("blockSize must be bigger than 0");
        }
        this.blockSize = blockSize;
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        ensureOpen();
        if (this.entryEOF) {
            return 0;
        }
        return 1;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.in.close();
            this.closed = true;
        }
    }

    private void closeEntry() throws IOException {
        do {
        } while (skip(2147483647L) == 2147483647L);
    }

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public CpioArchiveEntry getNextCPIOEntry() throws IOException {
        ensureOpen();
        if (this.entry != null) {
            closeEntry();
        }
        readFully(this.twoBytesBuf, 0, this.twoBytesBuf.length);
        if (CpioUtil.byteArray2long(this.twoBytesBuf, false) == 29127) {
            this.entry = readOldBinaryEntry(false);
        } else if (CpioUtil.byteArray2long(this.twoBytesBuf, true) == 29127) {
            this.entry = readOldBinaryEntry(true);
        } else {
            System.arraycopy(this.twoBytesBuf, 0, this.sixBytesBuf, 0, this.twoBytesBuf.length);
            readFully(this.sixBytesBuf, this.twoBytesBuf.length, this.fourBytesBuf.length);
            String magicString = ArchiveUtils.toAsciiString(this.sixBytesBuf);
            boolean z = true;
            switch (magicString.hashCode()) {
                case 1426477263:
                    if (magicString.equals(CpioConstants.MAGIC_NEW)) {
                        z = false;
                        break;
                    }
                    break;
                case 1426477264:
                    if (magicString.equals(CpioConstants.MAGIC_NEW_CRC)) {
                        z = true;
                        break;
                    }
                    break;
                case 1426477269:
                    if (magicString.equals(CpioConstants.MAGIC_OLD_ASCII)) {
                        z = true;
                        break;
                    }
                    break;
            }
            switch (z) {
                case false:
                    this.entry = readNewEntry(false);
                    break;
                case true:
                    this.entry = readNewEntry(true);
                    break;
                case true:
                    this.entry = readOldAsciiEntry();
                    break;
                default:
                    throw new IOException("Unknown magic [" + magicString + "]. Occurred at byte: " + getBytesRead());
            }
        }
        this.entryBytesRead = 0L;
        this.entryEOF = false;
        this.crc = 0L;
        if (this.entry.getName().equals(CpioConstants.CPIO_TRAILER)) {
            this.entryEOF = true;
            skipRemainderOfLastBlock();
            return null;
        }
        return this.entry;
    }

    private void skip(int bytes) throws IOException {
        if (bytes > 0) {
            readFully(this.fourBytesBuf, 0, bytes);
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        if (this.entry == null || this.entryEOF) {
            return -1;
        }
        if (this.entryBytesRead == this.entry.getSize()) {
            skip(this.entry.getDataPadCount());
            this.entryEOF = true;
            if (this.entry.getFormat() == 2 && this.crc != this.entry.getChksum()) {
                throw new IOException("CRC Error. Occurred at byte: " + getBytesRead());
            }
            return -1;
        }
        int tmplength = (int) Math.min(len, this.entry.getSize() - this.entryBytesRead);
        if (tmplength < 0) {
            return -1;
        }
        int tmpread = readFully(b, off, tmplength);
        if (this.entry.getFormat() == 2) {
            for (int pos = 0; pos < tmpread; pos++) {
                this.crc += b[pos] & 255;
                this.crc &= 4294967295L;
            }
        }
        if (tmpread > 0) {
            this.entryBytesRead += tmpread;
        }
        return tmpread;
    }

    private final int readFully(byte[] b, int off, int len) throws IOException {
        int count = IOUtils.readFully(this.in, b, off, len);
        count(count);
        if (count < len) {
            throw new EOFException();
        }
        return count;
    }

    private final byte[] readRange(int len) throws IOException {
        byte[] b = IOUtils.readRange(this.in, len);
        count(b.length);
        if (b.length < len) {
            throw new EOFException();
        }
        return b;
    }

    private long readBinaryLong(int length, boolean swapHalfWord) throws IOException {
        byte[] tmp = readRange(length);
        return CpioUtil.byteArray2long(tmp, swapHalfWord);
    }

    private long readAsciiLong(int length, int radix) throws IOException {
        byte[] tmpBuffer = readRange(length);
        return Long.parseLong(ArchiveUtils.toAsciiString(tmpBuffer), radix);
    }

    private CpioArchiveEntry readNewEntry(boolean hasCrc) throws IOException {
        CpioArchiveEntry ret;
        if (hasCrc) {
            ret = new CpioArchiveEntry((short) 2);
        } else {
            ret = new CpioArchiveEntry((short) 1);
        }
        ret.setInode(readAsciiLong(8, 16));
        long mode = readAsciiLong(8, 16);
        if (CpioUtil.fileType(mode) != 0) {
            ret.setMode(mode);
        }
        ret.setUID(readAsciiLong(8, 16));
        ret.setGID(readAsciiLong(8, 16));
        ret.setNumberOfLinks(readAsciiLong(8, 16));
        ret.setTime(readAsciiLong(8, 16));
        ret.setSize(readAsciiLong(8, 16));
        if (ret.getSize() < 0) {
            throw new IOException("Found illegal entry with negative length");
        }
        ret.setDeviceMaj(readAsciiLong(8, 16));
        ret.setDeviceMin(readAsciiLong(8, 16));
        ret.setRemoteDeviceMaj(readAsciiLong(8, 16));
        ret.setRemoteDeviceMin(readAsciiLong(8, 16));
        long namesize = readAsciiLong(8, 16);
        if (namesize < 0) {
            throw new IOException("Found illegal entry with negative name length");
        }
        ret.setChksum(readAsciiLong(8, 16));
        String name = readCString((int) namesize);
        ret.setName(name);
        if (CpioUtil.fileType(mode) == 0 && !name.equals(CpioConstants.CPIO_TRAILER)) {
            throw new IOException("Mode 0 only allowed in the trailer. Found entry name: " + ArchiveUtils.sanitize(name) + " Occurred at byte: " + getBytesRead());
        }
        skip(ret.getHeaderPadCount(namesize - 1));
        return ret;
    }

    private CpioArchiveEntry readOldAsciiEntry() throws IOException {
        CpioArchiveEntry ret = new CpioArchiveEntry((short) 4);
        ret.setDevice(readAsciiLong(6, 8));
        ret.setInode(readAsciiLong(6, 8));
        long mode = readAsciiLong(6, 8);
        if (CpioUtil.fileType(mode) != 0) {
            ret.setMode(mode);
        }
        ret.setUID(readAsciiLong(6, 8));
        ret.setGID(readAsciiLong(6, 8));
        ret.setNumberOfLinks(readAsciiLong(6, 8));
        ret.setRemoteDevice(readAsciiLong(6, 8));
        ret.setTime(readAsciiLong(11, 8));
        long namesize = readAsciiLong(6, 8);
        if (namesize < 0) {
            throw new IOException("Found illegal entry with negative name length");
        }
        ret.setSize(readAsciiLong(11, 8));
        if (ret.getSize() < 0) {
            throw new IOException("Found illegal entry with negative length");
        }
        String name = readCString((int) namesize);
        ret.setName(name);
        if (CpioUtil.fileType(mode) == 0 && !name.equals(CpioConstants.CPIO_TRAILER)) {
            throw new IOException("Mode 0 only allowed in the trailer. Found entry: " + ArchiveUtils.sanitize(name) + " Occurred at byte: " + getBytesRead());
        }
        return ret;
    }

    private CpioArchiveEntry readOldBinaryEntry(boolean swapHalfWord) throws IOException {
        CpioArchiveEntry ret = new CpioArchiveEntry((short) 8);
        ret.setDevice(readBinaryLong(2, swapHalfWord));
        ret.setInode(readBinaryLong(2, swapHalfWord));
        long mode = readBinaryLong(2, swapHalfWord);
        if (CpioUtil.fileType(mode) != 0) {
            ret.setMode(mode);
        }
        ret.setUID(readBinaryLong(2, swapHalfWord));
        ret.setGID(readBinaryLong(2, swapHalfWord));
        ret.setNumberOfLinks(readBinaryLong(2, swapHalfWord));
        ret.setRemoteDevice(readBinaryLong(2, swapHalfWord));
        ret.setTime(readBinaryLong(4, swapHalfWord));
        long namesize = readBinaryLong(2, swapHalfWord);
        if (namesize < 0) {
            throw new IOException("Found illegal entry with negative name length");
        }
        ret.setSize(readBinaryLong(4, swapHalfWord));
        if (ret.getSize() < 0) {
            throw new IOException("Found illegal entry with negative length");
        }
        String name = readCString((int) namesize);
        ret.setName(name);
        if (CpioUtil.fileType(mode) == 0 && !name.equals(CpioConstants.CPIO_TRAILER)) {
            throw new IOException("Mode 0 only allowed in the trailer. Found entry: " + ArchiveUtils.sanitize(name) + "Occurred at byte: " + getBytesRead());
        }
        skip(ret.getHeaderPadCount(namesize - 1));
        return ret;
    }

    private String readCString(int length) throws IOException {
        byte[] tmpBuffer = readRange(length - 1);
        if (this.in.read() == -1) {
            throw new EOFException();
        }
        return this.zipEncoding.decode(tmpBuffer);
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        int total;
        if (n < 0) {
            throw new IllegalArgumentException("Negative skip length");
        }
        ensureOpen();
        int max = (int) Math.min(n, 2147483647L);
        int i = 0;
        while (true) {
            total = i;
            if (total >= max) {
                break;
            }
            int len = max - total;
            if (len > this.tmpbuf.length) {
                len = this.tmpbuf.length;
            }
            int len2 = read(this.tmpbuf, 0, len);
            if (len2 == -1) {
                this.entryEOF = true;
                break;
            }
            i = total + len2;
        }
        return total;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveInputStream
    public ArchiveEntry getNextEntry() throws IOException {
        return getNextCPIOEntry();
    }

    private void skipRemainderOfLastBlock() throws IOException {
        long readFromLastBlock = getBytesRead() % this.blockSize;
        long j = readFromLastBlock == 0 ? 0L : this.blockSize - readFromLastBlock;
        while (true) {
            long remainingBytes = j;
            if (remainingBytes > 0) {
                long skipped = skip(this.blockSize - readFromLastBlock);
                if (skipped > 0) {
                    j = remainingBytes - skipped;
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public static boolean matches(byte[] signature, int length) {
        if (length < 6) {
            return false;
        }
        if (signature[0] == 113 && (signature[1] & 255) == 199) {
            return true;
        }
        if (signature[1] == 113 && (signature[0] & 255) == 199) {
            return true;
        }
        if (signature[0] != 48 || signature[1] != 55 || signature[2] != 48 || signature[3] != 55 || signature[4] != 48) {
            return false;
        }
        if (signature[5] == 49 || signature[5] == 50 || signature[5] == 55) {
            return true;
        }
        return false;
    }
}
