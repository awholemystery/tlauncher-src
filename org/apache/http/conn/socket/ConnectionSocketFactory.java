package org.apache.http.conn.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.protocol.HttpContext;

/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/socket/ConnectionSocketFactory.class */
public interface ConnectionSocketFactory {
    Socket createSocket(HttpContext httpContext) throws IOException;

    Socket connectSocket(int i, Socket socket, HttpHost httpHost, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, HttpContext httpContext) throws IOException;
}
