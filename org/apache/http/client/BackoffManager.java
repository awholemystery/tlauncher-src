package org.apache.http.client;

import org.apache.http.conn.routing.HttpRoute;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/BackoffManager.class */
public interface BackoffManager {
    void backOff(HttpRoute httpRoute);

    void probe(HttpRoute httpRoute);
}
