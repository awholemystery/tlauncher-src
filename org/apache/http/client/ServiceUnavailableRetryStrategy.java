package org.apache.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/ServiceUnavailableRetryStrategy.class */
public interface ServiceUnavailableRetryStrategy {
    boolean retryRequest(HttpResponse httpResponse, int i, HttpContext httpContext);

    long getRetryInterval();
}
