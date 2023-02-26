package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.pool.PoolEntry;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/conn/HttpPoolEntry.class */
class HttpPoolEntry extends PoolEntry<HttpRoute, OperatedClientConnection> {
    private final Log log;
    private final RouteTracker tracker;

    public HttpPoolEntry(Log log, String id, HttpRoute route, OperatedClientConnection conn, long timeToLive, TimeUnit tunit) {
        super(id, route, conn, timeToLive, tunit);
        this.log = log;
        this.tracker = new RouteTracker(route);
    }

    @Override // org.apache.http.pool.PoolEntry
    public boolean isExpired(long now) {
        boolean expired = super.isExpired(now);
        if (expired && this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
        }
        return expired;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RouteTracker getTracker() {
        return this.tracker;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HttpRoute getPlannedRoute() {
        return getRoute();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HttpRoute getEffectiveRoute() {
        return this.tracker.toRoute();
    }

    @Override // org.apache.http.pool.PoolEntry
    public boolean isClosed() {
        OperatedClientConnection conn = getConnection();
        return !conn.isOpen();
    }

    @Override // org.apache.http.pool.PoolEntry
    public void close() {
        OperatedClientConnection conn = getConnection();
        try {
            conn.close();
        } catch (IOException ex) {
            this.log.debug("I/O error closing connection", ex);
        }
    }
}
