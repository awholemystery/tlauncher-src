package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.apache.http.annotation.Immutable;
import org.ietf.jgss.GSSCredential;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/auth/KerberosCredentials.class */
public class KerberosCredentials implements Credentials, Serializable {
    private static final long serialVersionUID = 487421613855550713L;
    private final GSSCredential gssCredential;

    public KerberosCredentials(GSSCredential gssCredential) {
        this.gssCredential = gssCredential;
    }

    public GSSCredential getGSSCredential() {
        return this.gssCredential;
    }

    @Override // org.apache.http.auth.Credentials
    public Principal getUserPrincipal() {
        return null;
    }

    @Override // org.apache.http.auth.Credentials
    public String getPassword() {
        return null;
    }
}
