package org.apache.http.pool;

/* loaded from: TLauncher-2.876.jar:org/apache/http/pool/PoolEntryCallback.class */
public interface PoolEntryCallback<T, C> {
    void process(PoolEntry<T, C> poolEntry);
}
