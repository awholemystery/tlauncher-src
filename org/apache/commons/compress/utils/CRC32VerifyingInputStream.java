package org.apache.commons.compress.utils;

import java.io.InputStream;
import java.util.zip.CRC32;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/CRC32VerifyingInputStream.class */
public class CRC32VerifyingInputStream extends ChecksumVerifyingInputStream {
    public CRC32VerifyingInputStream(InputStream in, long size, int expectedCrc32) {
        this(in, size, expectedCrc32 & 4294967295L);
    }

    public CRC32VerifyingInputStream(InputStream in, long size, long expectedCrc32) {
        super(new CRC32(), in, size, expectedCrc32);
    }
}
