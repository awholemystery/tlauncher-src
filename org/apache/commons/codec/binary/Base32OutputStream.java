package org.apache.commons.codec.binary;

import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/binary/Base32OutputStream.class */
public class Base32OutputStream extends BaseNCodecOutputStream {
    public Base32OutputStream(OutputStream out) {
        this(out, true);
    }

    public Base32OutputStream(OutputStream out, boolean doEncode) {
        super(out, new Base32(false), doEncode);
    }

    public Base32OutputStream(OutputStream out, boolean doEncode, int lineLength, byte[] lineSeparator) {
        super(out, new Base32(lineLength, lineSeparator), doEncode);
    }
}
