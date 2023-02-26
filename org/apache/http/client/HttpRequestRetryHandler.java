package org.apache.http.client;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/HttpRequestRetryHandler.class */
public interface HttpRequestRetryHandler {
    boolean retryRequest(IOException iOException, int i, HttpContext httpContext);
}
