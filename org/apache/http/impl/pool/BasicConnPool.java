package org.apache.http.impl.pool;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.ConnFactory;

@ThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/pool/BasicConnPool.class */
public class BasicConnPool extends AbstractConnPool<HttpHost, HttpClientConnection, BasicPoolEntry> {
    private static final AtomicLong COUNTER = new AtomicLong();

    public BasicConnPool(ConnFactory<HttpHost, HttpClientConnection> connFactory) {
        super(connFactory, 2, 20);
    }

    @Deprecated
    public BasicConnPool(HttpParams params) {
        super(new BasicConnFactory(params), 2, 20);
    }

    public BasicConnPool(SocketConfig sconfig, ConnectionConfig cconfig) {
        super(new BasicConnFactory(sconfig, cconfig), 2, 20);
    }

    public BasicConnPool() {
        super(new BasicConnFactory(SocketConfig.DEFAULT, ConnectionConfig.DEFAULT), 2, 20);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.http.pool.AbstractConnPool
    public BasicPoolEntry createEntry(HttpHost host, HttpClientConnection conn) {
        return new BasicPoolEntry(Long.toString(COUNTER.getAndIncrement()), host, conn);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.http.pool.AbstractConnPool
    public boolean validate(BasicPoolEntry entry) {
        return !entry.getConnection().isStale();
    }
}
