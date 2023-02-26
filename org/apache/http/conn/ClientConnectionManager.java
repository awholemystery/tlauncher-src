package org.apache.http.conn;

import java.util.concurrent.TimeUnit;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/ClientConnectionManager.class */
public interface ClientConnectionManager {
    SchemeRegistry getSchemeRegistry();

    ClientConnectionRequest requestConnection(HttpRoute httpRoute, Object obj);

    void releaseConnection(ManagedClientConnection managedClientConnection, long j, TimeUnit timeUnit);

    void closeIdleConnections(long j, TimeUnit timeUnit);

    void closeExpiredConnections();

    void shutdown();
}
