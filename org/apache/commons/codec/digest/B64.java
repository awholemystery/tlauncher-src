package org.apache.commons.codec.digest;

import java.util.Random;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/digest/B64.class */
class B64 {
    static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    B64() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b64from24bit(byte b2, byte b1, byte b0, int outLen, StringBuilder buffer) {
        int w = ((b2 << 16) & 16777215) | ((b1 << 8) & 65535) | (b0 & 255);
        int n = outLen;
        while (true) {
            int i = n;
            n--;
            if (i > 0) {
                buffer.append(B64T.charAt(w & 63));
                w >>= 6;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getRandomSalt(int num) {
        StringBuilder saltString = new StringBuilder();
        for (int i = 1; i <= num; i++) {
            saltString.append(B64T.charAt(new Random().nextInt(B64T.length())));
        }
        return saltString.toString();
    }
}
