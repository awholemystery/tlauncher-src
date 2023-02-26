package org.apache.http.client;

import org.apache.http.HttpResponse;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/ConnectionBackoffStrategy.class */
public interface ConnectionBackoffStrategy {
    boolean shouldBackoff(Throwable th);

    boolean shouldBackoff(HttpResponse httpResponse);
}
