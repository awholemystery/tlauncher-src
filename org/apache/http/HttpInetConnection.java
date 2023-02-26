package org.apache.http;

import java.net.InetAddress;

/* loaded from: TLauncher-2.876.jar:org/apache/http/HttpInetConnection.class */
public interface HttpInetConnection extends HttpConnection {
    InetAddress getLocalAddress();

    int getLocalPort();

    InetAddress getRemoteAddress();

    int getRemotePort();
}
