package org.apache.commons.codec.binary;

import java.io.InputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/binary/Base32InputStream.class */
public class Base32InputStream extends BaseNCodecInputStream {
    public Base32InputStream(InputStream in) {
        this(in, false);
    }

    public Base32InputStream(InputStream in, boolean doEncode) {
        super(in, new Base32(false), doEncode);
    }

    public Base32InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator) {
        super(in, new Base32(lineLength, lineSeparator), doEncode);
    }
}
