package org.apache.http.auth;

import org.apache.http.protocol.HttpContext;

/* loaded from: TLauncher-2.876.jar:org/apache/http/auth/AuthSchemeProvider.class */
public interface AuthSchemeProvider {
    AuthScheme create(HttpContext httpContext);
}
