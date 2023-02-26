package org.apache.http;

import java.io.Closeable;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/http/HttpConnection.class */
public interface HttpConnection extends Closeable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    boolean isOpen();

    boolean isStale();

    void setSocketTimeout(int i);

    int getSocketTimeout();

    void shutdown() throws IOException;

    HttpConnectionMetrics getMetrics();
}
