package org.apache.commons.codec.binary;

import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/binary/Base64InputStream.class */
public class Base64InputStream extends BaseNCodecInputStream {
    public Base64InputStream(InputStream in) {
        this(in, false);
    }

    public Base64InputStream(InputStream in, boolean doEncode) {
        super(in, new Base64(false), doEncode);
    }

    public Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator) {
        super(in, new Base64(lineLength, lineSeparator), doEncode);
    }
}
