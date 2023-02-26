package org.apache.http.client.config;

import org.apache.http.annotation.Immutable;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/client/config/AuthSchemes.class */
public final class AuthSchemes {
    public static final String BASIC = "Basic";
    public static final String DIGEST = "Digest";
    public static final String NTLM = "NTLM";
    public static final String SPNEGO = "Negotiate";
    public static final String KERBEROS = "Kerberos";

    private AuthSchemes() {
    }
}
