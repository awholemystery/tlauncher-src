package org.apache.commons.codec.binary;

import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/binary/Base64OutputStream.class */
public class Base64OutputStream extends BaseNCodecOutputStream {
    public Base64OutputStream(OutputStream out) {
        this(out, true);
    }

    public Base64OutputStream(OutputStream out, boolean doEncode) {
        super(out, new Base64(false), doEncode);
    }

    public Base64OutputStream(OutputStream out, boolean doEncode, int lineLength, byte[] lineSeparator) {
        super(out, new Base64(lineLength, lineSeparator), doEncode);
    }
}
