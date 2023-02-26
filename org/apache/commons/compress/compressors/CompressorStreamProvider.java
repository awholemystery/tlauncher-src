package org.apache.commons.compress.compressors;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/CompressorStreamProvider.class */
public interface CompressorStreamProvider {
    CompressorInputStream createCompressorInputStream(String str, InputStream inputStream, boolean z) throws CompressorException;

    CompressorOutputStream createCompressorOutputStream(String str, OutputStream outputStream) throws CompressorException;

    Set<String> getInputStreamCompressorNames();

    Set<String> getOutputStreamCompressorNames();
}
