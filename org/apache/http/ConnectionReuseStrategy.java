package org.apache.http;

import org.apache.http.protocol.HttpContext;

/* loaded from: TLauncher-2.876.jar:org/apache/http/ConnectionReuseStrategy.class */
public interface ConnectionReuseStrategy {
    boolean keepAlive(HttpResponse httpResponse, HttpContext httpContext);
}
