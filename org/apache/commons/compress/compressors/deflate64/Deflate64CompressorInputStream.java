package org.apache.commons.compress.compressors.deflate64;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.InputStreamStatistics;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/deflate64/Deflate64CompressorInputStream.class */
public class Deflate64CompressorInputStream extends CompressorInputStream implements InputStreamStatistics {
    private InputStream originalStream;
    private HuffmanDecoder decoder;
    private long compressedBytesRead;
    private final byte[] oneByte;

    public Deflate64CompressorInputStream(InputStream in) {
        this(new HuffmanDecoder(in));
        this.originalStream = in;
    }

    Deflate64CompressorInputStream(HuffmanDecoder decoder) {
        this.oneByte = new byte[1];
        this.decoder = decoder;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        while (true) {
            int r = read(this.oneByte);
            switch (r) {
                case -1:
                    return -1;
                case 0:
                    break;
                case 1:
                    return this.oneByte[0] & 255;
                default:
                    throw new IllegalStateException("Invalid return value from read: " + r);
            }
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int read = -1;
        if (this.decoder != null) {
            try {
                read = this.decoder.decode(b, off, len);
                this.compressedBytesRead = this.decoder.getBytesRead();
                count(read);
                if (read == -1) {
                    closeDecoder();
                }
            } catch (RuntimeException ex) {
                throw new IOException("Invalid Deflate64 input", ex);
            }
        }
        return read;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.decoder != null) {
            return this.decoder.available();
        }
        return 0;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            closeDecoder();
        } finally {
            if (this.originalStream != null) {
                this.originalStream.close();
                this.originalStream = null;
            }
        }
    }

    @Override // org.apache.commons.compress.utils.InputStreamStatistics
    public long getCompressedCount() {
        return this.compressedBytesRead;
    }

    private void closeDecoder() {
        IOUtils.closeQuietly(this.decoder);
        this.decoder = null;
    }
}
