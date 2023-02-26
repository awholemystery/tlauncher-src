package org.apache.http.conn;

import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpInetConnection;

/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/ManagedHttpClientConnection.class */
public interface ManagedHttpClientConnection extends HttpClientConnection, HttpInetConnection {
    String getId();

    void bind(Socket socket) throws IOException;

    Socket getSocket();

    SSLSession getSSLSession();
}
