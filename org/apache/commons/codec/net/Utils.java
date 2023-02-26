package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/net/Utils.class */
class Utils {
    Utils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int digit16(byte b) throws DecoderException {
        int i = Character.digit((char) b, 16);
        if (i == -1) {
            throw new DecoderException("Invalid URL encoding: not a valid digit (radix 16): " + ((int) b));
        }
        return i;
    }
}
