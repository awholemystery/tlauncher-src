package org.apache.http.auth;

import java.security.Principal;

/* loaded from: TLauncher-2.876.jar:org/apache/http/auth/Credentials.class */
public interface Credentials {
    Principal getUserPrincipal();

    String getPassword();
}
