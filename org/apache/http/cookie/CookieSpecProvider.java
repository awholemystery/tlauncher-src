package org.apache.http.cookie;

import org.apache.http.protocol.HttpContext;

/* loaded from: TLauncher-2.876.jar:org/apache/http/cookie/CookieSpecProvider.class */
public interface CookieSpecProvider {
    CookieSpec create(HttpContext httpContext);
}
