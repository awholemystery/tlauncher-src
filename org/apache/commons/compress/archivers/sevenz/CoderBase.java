package org.apache.commons.compress.archivers.sevenz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.utils.ByteUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/CoderBase.class */
abstract class CoderBase {
    private final Class<?>[] acceptableOptions;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract InputStream decode(String str, InputStream inputStream, long j, Coder coder, byte[] bArr, int i) throws IOException;

    /* JADX INFO: Access modifiers changed from: protected */
    public CoderBase(Class<?>... acceptableOptions) {
        this.acceptableOptions = acceptableOptions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canAcceptOptions(Object opts) {
        Class<?>[] clsArr;
        for (Class<?> c : this.acceptableOptions) {
            if (c.isInstance(opts)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getOptionsAsProperties(Object options) throws IOException {
        return ByteUtils.EMPTY_BYTE_ARRAY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getOptionsFromCoder(Coder coder, InputStream in) throws IOException {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OutputStream encode(OutputStream out, Object options) throws IOException {
        throw new UnsupportedOperationException("Method doesn't support writing");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int numberOptionOrDefault(Object options, int defaultValue) {
        return options instanceof Number ? ((Number) options).intValue() : defaultValue;
    }
}
