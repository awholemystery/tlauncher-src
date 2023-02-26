package org.apache.commons.compress.utils;

import ch.qos.logback.core.FileAppender;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/SkipShieldingInputStream.class */
public class SkipShieldingInputStream extends FilterInputStream {
    private static final int SKIP_BUFFER_SIZE = 8192;
    private static final byte[] SKIP_BUFFER = new byte[8192];

    public SkipShieldingInputStream(InputStream in) {
        super(in);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long n) throws IOException {
        if (n < 0) {
            return 0L;
        }
        return read(SKIP_BUFFER, 0, (int) Math.min(n, (long) FileAppender.DEFAULT_BUFFER_SIZE));
    }
}
