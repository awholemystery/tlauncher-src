package org.apache.http.conn;

import javax.net.ssl.SSLSession;
import org.apache.http.HttpInetConnection;
import org.apache.http.conn.routing.HttpRoute;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/HttpRoutedConnection.class */
public interface HttpRoutedConnection extends HttpInetConnection {
    boolean isSecure();

    HttpRoute getRoute();

    SSLSession getSSLSession();
}
