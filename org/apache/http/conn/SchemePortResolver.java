package org.apache.http.conn;

import org.apache.http.HttpHost;

/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/SchemePortResolver.class */
public interface SchemePortResolver {
    int resolve(HttpHost httpHost) throws UnsupportedSchemeException;
}
