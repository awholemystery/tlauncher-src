package org.apache.http.auth;

import org.apache.http.params.HttpParams;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/auth/AuthSchemeFactory.class */
public interface AuthSchemeFactory {
    AuthScheme newInstance(HttpParams httpParams);
}
