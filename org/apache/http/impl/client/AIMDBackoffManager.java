package org.apache.http.impl.client;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.BackoffManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.util.Args;

/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/AIMDBackoffManager.class */
public class AIMDBackoffManager implements BackoffManager {
    private final ConnPoolControl<HttpRoute> connPerRoute;
    private final Clock clock;
    private final Map<HttpRoute, Long> lastRouteProbes;
    private final Map<HttpRoute, Long> lastRouteBackoffs;
    private long coolDown;
    private double backoffFactor;
    private int cap;

    public AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute) {
        this(connPerRoute, new SystemClock());
    }

    AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute, Clock clock) {
        this.coolDown = 5000L;
        this.backoffFactor = 0.5d;
        this.cap = 2;
        this.clock = clock;
        this.connPerRoute = connPerRoute;
        this.lastRouteProbes = new HashMap();
        this.lastRouteBackoffs = new HashMap();
    }

    @Override // org.apache.http.client.BackoffManager
    public void backOff(HttpRoute route) {
        synchronized (this.connPerRoute) {
            int curr = this.connPerRoute.getMaxPerRoute(route);
            Long lastUpdate = getLastUpdate(this.lastRouteBackoffs, route);
            long now = this.clock.getCurrentTime();
            if (now - lastUpdate.longValue() < this.coolDown) {
                return;
            }
            this.connPerRoute.setMaxPerRoute(route, getBackedOffPoolSize(curr));
            this.lastRouteBackoffs.put(route, Long.valueOf(now));
        }
    }

    private int getBackedOffPoolSize(int curr) {
        if (curr <= 1) {
            return 1;
        }
        return (int) Math.floor(this.backoffFactor * curr);
    }

    @Override // org.apache.http.client.BackoffManager
    public void probe(HttpRoute route) {
        synchronized (this.connPerRoute) {
            int curr = this.connPerRoute.getMaxPerRoute(route);
            int max = curr >= this.cap ? this.cap : curr + 1;
            Long lastProbe = getLastUpdate(this.lastRouteProbes, route);
            Long lastBackoff = getLastUpdate(this.lastRouteBackoffs, route);
            long now = this.clock.getCurrentTime();
            if (now - lastProbe.longValue() < this.coolDown || now - lastBackoff.longValue() < this.coolDown) {
                return;
            }
            this.connPerRoute.setMaxPerRoute(route, max);
            this.lastRouteProbes.put(route, Long.valueOf(now));
        }
    }

    private Long getLastUpdate(Map<HttpRoute, Long> updates, HttpRoute route) {
        Long lastUpdate = updates.get(route);
        if (lastUpdate == null) {
            lastUpdate = 0L;
        }
        return lastUpdate;
    }

    public void setBackoffFactor(double d) {
        Args.check(d > 0.0d && d < 1.0d, "Backoff factor must be 0.0 < f < 1.0");
        this.backoffFactor = d;
    }

    public void setCooldownMillis(long l) {
        Args.positive(this.coolDown, "Cool down");
        this.coolDown = l;
    }

    public void setPerHostConnectionCap(int cap) {
        Args.positive(cap, "Per host connection cap");
        this.cap = cap;
    }
}
