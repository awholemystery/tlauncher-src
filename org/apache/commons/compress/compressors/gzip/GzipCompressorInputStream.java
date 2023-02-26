package org.apache.commons.compress.compressors.gzip;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.ByteUtils;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.InputStreamStatistics;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/gzip/GzipCompressorInputStream.class */
public class GzipCompressorInputStream extends CompressorInputStream implements InputStreamStatistics {
    private static final int FHCRC = 2;
    private static final int FEXTRA = 4;
    private static final int FNAME = 8;
    private static final int FCOMMENT = 16;
    private static final int FRESERVED = 224;
    private final CountingInputStream countingStream;
    private final InputStream in;
    private final boolean decompressConcatenated;
    private final byte[] buf;
    private int bufUsed;
    private Inflater inf;
    private final CRC32 crc;
    private boolean endReached;
    private final byte[] oneByte;
    private final GzipParameters parameters;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GzipCompressorInputStream.class.desiredAssertionStatus();
    }

    public GzipCompressorInputStream(InputStream inputStream) throws IOException {
        this(inputStream, false);
    }

    public GzipCompressorInputStream(InputStream inputStream, boolean decompressConcatenated) throws IOException {
        this.buf = new byte[8192];
        this.inf = new Inflater(true);
        this.crc = new CRC32();
        this.oneByte = new byte[1];
        this.parameters = new GzipParameters();
        this.countingStream = new CountingInputStream(inputStream);
        if (this.countingStream.markSupported()) {
            this.in = this.countingStream;
        } else {
            this.in = new BufferedInputStream(this.countingStream);
        }
        this.decompressConcatenated = decompressConcatenated;
        init(true);
    }

    public GzipParameters getMetaData() {
        return this.parameters;
    }

    private boolean init(boolean isFirstMember) throws IOException {
        if ($assertionsDisabled || isFirstMember || this.decompressConcatenated) {
            int magic0 = this.in.read();
            if (magic0 == -1 && !isFirstMember) {
                return false;
            }
            if (magic0 != 31 || this.in.read() != 139) {
                throw new IOException(isFirstMember ? "Input is not in the .gz format" : "Garbage after a valid .gz stream");
            }
            DataInput inData = new DataInputStream(this.in);
            int method = inData.readUnsignedByte();
            if (method != 8) {
                throw new IOException("Unsupported compression method " + method + " in the .gz header");
            }
            int flg = inData.readUnsignedByte();
            if ((flg & FRESERVED) != 0) {
                throw new IOException("Reserved flags are set in the .gz header");
            }
            this.parameters.setModificationTime(ByteUtils.fromLittleEndian(inData, 4) * 1000);
            switch (inData.readUnsignedByte()) {
                case 2:
                    this.parameters.setCompressionLevel(9);
                    break;
                case 4:
                    this.parameters.setCompressionLevel(1);
                    break;
            }
            this.parameters.setOperatingSystem(inData.readUnsignedByte());
            if ((flg & 4) != 0) {
                int xlen = inData.readUnsignedByte();
                int xlen2 = xlen | (inData.readUnsignedByte() << 8);
                while (true) {
                    int i = xlen2;
                    xlen2--;
                    if (i > 0) {
                        inData.readUnsignedByte();
                    }
                }
            }
            if ((flg & 8) != 0) {
                this.parameters.setFilename(new String(readToNull(inData), StandardCharsets.ISO_8859_1));
            }
            if ((flg & 16) != 0) {
                this.parameters.setComment(new String(readToNull(inData), StandardCharsets.ISO_8859_1));
            }
            if ((flg & 2) != 0) {
                inData.readShort();
            }
            this.inf.reset();
            this.crc.reset();
            return true;
        }
        throw new AssertionError();
    }

    private static byte[] readToNull(DataInput inData) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Throwable th = null;
        while (true) {
            try {
                int b = inData.readUnsignedByte();
                if (b == 0) {
                    break;
                }
                bos.write(b);
            } finally {
            }
        }
        byte[] byteArray = bos.toByteArray();
        if (bos != null) {
            if (0 != 0) {
                try {
                    bos.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            } else {
                bos.close();
            }
        }
        return byteArray;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (read(this.oneByte, 0, 1) == -1) {
            return -1;
        }
        return this.oneByte[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        if (this.endReached) {
            return -1;
        }
        int size = 0;
        while (len > 0) {
            if (this.inf.needsInput()) {
                this.in.mark(this.buf.length);
                this.bufUsed = this.in.read(this.buf);
                if (this.bufUsed == -1) {
                    throw new EOFException();
                }
                this.inf.setInput(this.buf, 0, this.bufUsed);
            }
            try {
                int ret = this.inf.inflate(b, off, len);
                this.crc.update(b, off, ret);
                off += ret;
                len -= ret;
                size += ret;
                count(ret);
                if (this.inf.finished()) {
                    this.in.reset();
                    int skipAmount = this.bufUsed - this.inf.getRemaining();
                    if (IOUtils.skip(this.in, skipAmount) != skipAmount) {
                        throw new IOException();
                    }
                    this.bufUsed = 0;
                    DataInput inData = new DataInputStream(this.in);
                    long crcStored = ByteUtils.fromLittleEndian(inData, 4);
                    if (crcStored != this.crc.getValue()) {
                        throw new IOException("Gzip-compressed data is corrupt (CRC32 error)");
                    }
                    long isize = ByteUtils.fromLittleEndian(inData, 4);
                    if (isize != (this.inf.getBytesWritten() & 4294967295L)) {
                        throw new IOException("Gzip-compressed data is corrupt(uncompressed size mismatch)");
                    }
                    if (!this.decompressConcatenated || !init(false)) {
                        this.inf.end();
                        this.inf = null;
                        this.endReached = true;
                        if (size == 0) {
                            return -1;
                        }
                        return size;
                    }
                }
            } catch (DataFormatException e) {
                throw new IOException("Gzip-compressed data is corrupt");
            }
        }
        return size;
    }

    public static boolean matches(byte[] signature, int length) {
        return length >= 2 && signature[0] == 31 && signature[1] == -117;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.inf != null) {
            this.inf.end();
            this.inf = null;
        }
        if (this.in != System.in) {
            this.in.close();
        }
    }

    @Override // org.apache.commons.compress.utils.InputStreamStatistics
    public long getCompressedCount() {
        return this.countingStream.getBytesRead();
    }
}
