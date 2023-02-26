package org.apache.http.conn.params;

import org.apache.http.conn.routing.HttpRoute;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/params/ConnPerRoute.class */
public interface ConnPerRoute {
    int getMaxForRoute(HttpRoute httpRoute);
}
