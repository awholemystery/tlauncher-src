package org.apache.commons.compress.compressors.zstandard;

import com.github.luben.zstd.BufferPool;
import com.github.luben.zstd.ZstdInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.InputStreamStatistics;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/zstandard/ZstdCompressorInputStream.class */
public class ZstdCompressorInputStream extends CompressorInputStream implements InputStreamStatistics {
    private final CountingInputStream countingStream;
    private final ZstdInputStream decIS;

    public ZstdCompressorInputStream(InputStream in) throws IOException {
        CountingInputStream countingInputStream = new CountingInputStream(in);
        this.countingStream = countingInputStream;
        this.decIS = new ZstdInputStream(countingInputStream);
    }

    public ZstdCompressorInputStream(InputStream in, BufferPool bufferPool) throws IOException {
        CountingInputStream countingInputStream = new CountingInputStream(in);
        this.countingStream = countingInputStream;
        this.decIS = new ZstdInputStream(countingInputStream, bufferPool);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.decIS.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.decIS.close();
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        return IOUtils.skip(this.decIS, n);
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.decIS.mark(readlimit);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.decIS.markSupported();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int ret = this.decIS.read();
        count(ret == -1 ? 0 : 1);
        return ret;
    }

    @Override // java.io.InputStream
    public int read(byte[] buf, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int ret = this.decIS.read(buf, off, len);
        count(ret);
        return ret;
    }

    public String toString() {
        return this.decIS.toString();
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        this.decIS.reset();
    }

    @Override // org.apache.commons.compress.utils.InputStreamStatistics
    public long getCompressedCount() {
        return this.countingStream.getBytesRead();
    }
}
