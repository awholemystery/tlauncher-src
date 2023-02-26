package org.apache.http.conn;

import java.util.concurrent.TimeUnit;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/ClientConnectionRequest.class */
public interface ClientConnectionRequest {
    ManagedClientConnection getConnection(long j, TimeUnit timeUnit) throws InterruptedException, ConnectionPoolTimeoutException;

    void abortRequest();
}
