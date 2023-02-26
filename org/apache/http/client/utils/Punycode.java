package org.apache.http.client.utils;

import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/client/utils/Punycode.class */
public class Punycode {
    private static final Idn impl;

    static {
        Idn _impl;
        try {
            _impl = new JdkIdn();
        } catch (Exception e) {
            _impl = new Rfc3492Idn();
        }
        impl = _impl;
    }

    public static String toUnicode(String punycode) {
        return impl.toUnicode(punycode);
    }
}
