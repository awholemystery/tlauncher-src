package org.apache.http.pool;

/* loaded from: TLauncher-2.876.jar:org/apache/http/pool/ConnPoolControl.class */
public interface ConnPoolControl<T> {
    void setMaxTotal(int i);

    int getMaxTotal();

    void setDefaultMaxPerRoute(int i);

    int getDefaultMaxPerRoute();

    void setMaxPerRoute(T t, int i);

    int getMaxPerRoute(T t);

    PoolStats getTotalStats();

    PoolStats getStats(T t);
}
