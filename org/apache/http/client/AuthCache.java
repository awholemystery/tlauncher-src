package org.apache.http.client;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/AuthCache.class */
public interface AuthCache {
    void put(HttpHost httpHost, AuthScheme authScheme);

    AuthScheme get(HttpHost httpHost);

    void remove(HttpHost httpHost);

    void clear();
}
