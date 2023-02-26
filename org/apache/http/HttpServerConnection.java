package org.apache.http;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/http/HttpServerConnection.class */
public interface HttpServerConnection extends HttpConnection {
    HttpRequest receiveRequestHeader() throws HttpException, IOException;

    void receiveRequestEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException;

    void sendResponseHeader(HttpResponse httpResponse) throws HttpException, IOException;

    void sendResponseEntity(HttpResponse httpResponse) throws HttpException, IOException;

    void flush() throws IOException;
}
