package org.apache.commons.compress.archivers.sevenz;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
import org.apache.commons.compress.utils.FlushShieldFilterOutputStream;
import org.tukaani.xz.ARMOptions;
import org.tukaani.xz.ARMThumbOptions;
import org.tukaani.xz.FilterOptions;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.IA64Options;
import org.tukaani.xz.PowerPCOptions;
import org.tukaani.xz.SPARCOptions;
import org.tukaani.xz.X86Options;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Coders.class */
class Coders {
    private static final Map<SevenZMethod, CoderBase> CODER_MAP = new HashMap<SevenZMethod, CoderBase>() { // from class: org.apache.commons.compress.archivers.sevenz.Coders.1
        private static final long serialVersionUID = 1664829131806520867L;

        {
            put(SevenZMethod.COPY, new CopyDecoder());
            put(SevenZMethod.LZMA, new LZMADecoder());
            put(SevenZMethod.LZMA2, new LZMA2Decoder());
            put(SevenZMethod.DEFLATE, new DeflateDecoder());
            put(SevenZMethod.DEFLATE64, new Deflate64Decoder());
            put(SevenZMethod.BZIP2, new BZIP2Decoder());
            put(SevenZMethod.AES256SHA256, new AES256SHA256Decoder());
            put(SevenZMethod.BCJ_X86_FILTER, new BCJDecoder(new X86Options()));
            put(SevenZMethod.BCJ_PPC_FILTER, new BCJDecoder(new PowerPCOptions()));
            put(SevenZMethod.BCJ_IA64_FILTER, new BCJDecoder(new IA64Options()));
            put(SevenZMethod.BCJ_ARM_FILTER, new BCJDecoder(new ARMOptions()));
            put(SevenZMethod.BCJ_ARM_THUMB_FILTER, new BCJDecoder(new ARMThumbOptions()));
            put(SevenZMethod.BCJ_SPARC_FILTER, new BCJDecoder(new SPARCOptions()));
            put(SevenZMethod.DELTA_FILTER, new DeltaDecoder());
        }
    };

    Coders() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CoderBase findByMethod(SevenZMethod method) {
        return CODER_MAP.get(method);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InputStream addDecoder(String archiveName, InputStream is, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
        CoderBase cb = findByMethod(SevenZMethod.byId(coder.decompressionMethodId));
        if (cb == null) {
            throw new IOException("Unsupported compression method " + Arrays.toString(coder.decompressionMethodId) + " used in " + archiveName);
        }
        return cb.decode(archiveName, is, uncompressedLength, coder, password, maxMemoryLimitInKb);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OutputStream addEncoder(OutputStream out, SevenZMethod method, Object options) throws IOException {
        CoderBase cb = findByMethod(method);
        if (cb == null) {
            throw new IOException("Unsupported compression method " + method);
        }
        return cb.encode(out, options);
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Coders$CopyDecoder.class */
    static class CopyDecoder extends CoderBase {
        CopyDecoder() {
            super(new Class[0]);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
            return in;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public OutputStream encode(OutputStream out, Object options) {
            return out;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Coders$BCJDecoder.class */
    static class BCJDecoder extends CoderBase {
        private final FilterOptions opts;

        BCJDecoder(FilterOptions opts) {
            super(new Class[0]);
            this.opts = opts;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
            try {
                return this.opts.getInputStream(in);
            } catch (AssertionError e) {
                throw new IOException("BCJ filter used in " + archiveName + " needs XZ for Java > 1.4 - see https://commons.apache.org/proper/commons-compress/limitations.html#7Z", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public OutputStream encode(OutputStream out, Object options) {
            return new FlushShieldFilterOutputStream(this.opts.getOutputStream(new FinishableWrapperOutputStream(out)));
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Coders$DeflateDecoder.class */
    static class DeflateDecoder extends CoderBase {
        private static final byte[] ONE_ZERO_BYTE = new byte[1];

        DeflateDecoder() {
            super(Number.class);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
            Inflater inflater = new Inflater(true);
            InflaterInputStream inflaterInputStream = new InflaterInputStream(new SequenceInputStream(in, new ByteArrayInputStream(ONE_ZERO_BYTE)), inflater);
            return new DeflateDecoderInputStream(inflaterInputStream, inflater);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public OutputStream encode(OutputStream out, Object options) {
            int level = numberOptionOrDefault(options, 9);
            Deflater deflater = new Deflater(level, true);
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out, deflater);
            return new DeflateDecoderOutputStream(deflaterOutputStream, deflater);
        }

        /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Coders$DeflateDecoder$DeflateDecoderInputStream.class */
        static class DeflateDecoderInputStream extends InputStream {
            final InflaterInputStream inflaterInputStream;
            Inflater inflater;

            public DeflateDecoderInputStream(InflaterInputStream inflaterInputStream, Inflater inflater) {
                this.inflaterInputStream = inflaterInputStream;
                this.inflater = inflater;
            }

            @Override // java.io.InputStream
            public int read() throws IOException {
                return this.inflaterInputStream.read();
            }

            @Override // java.io.InputStream
            public int read(byte[] b, int off, int len) throws IOException {
                return this.inflaterInputStream.read(b, off, len);
            }

            @Override // java.io.InputStream
            public int read(byte[] b) throws IOException {
                return this.inflaterInputStream.read(b);
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                try {
                    this.inflaterInputStream.close();
                } finally {
                    this.inflater.end();
                }
            }
        }

        /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Coders$DeflateDecoder$DeflateDecoderOutputStream.class */
        static class DeflateDecoderOutputStream extends OutputStream {
            final DeflaterOutputStream deflaterOutputStream;
            Deflater deflater;

            public DeflateDecoderOutputStream(DeflaterOutputStream deflaterOutputStream, Deflater deflater) {
                this.deflaterOutputStream = deflaterOutputStream;
                this.deflater = deflater;
            }

            @Override // java.io.OutputStream
            public void write(int b) throws IOException {
                this.deflaterOutputStream.write(b);
            }

            @Override // java.io.OutputStream
            public void write(byte[] b) throws IOException {
                this.deflaterOutputStream.write(b);
            }

            @Override // java.io.OutputStream
            public void write(byte[] b, int off, int len) throws IOException {
                this.deflaterOutputStream.write(b, off, len);
            }

            @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                try {
                    this.deflaterOutputStream.close();
                } finally {
                    this.deflater.end();
                }
            }
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Coders$Deflate64Decoder.class */
    static class Deflate64Decoder extends CoderBase {
        Deflate64Decoder() {
            super(Number.class);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
            return new Deflate64CompressorInputStream(in);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/Coders$BZIP2Decoder.class */
    static class BZIP2Decoder extends CoderBase {
        BZIP2Decoder() {
            super(Number.class);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
            return new BZip2CompressorInputStream(in);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // org.apache.commons.compress.archivers.sevenz.CoderBase
        public OutputStream encode(OutputStream out, Object options) throws IOException {
            int blockSize = numberOptionOrDefault(options, 9);
            return new BZip2CompressorOutputStream(out, blockSize);
        }
    }
}
