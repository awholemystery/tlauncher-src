package org.apache.http.impl.bootstrap;

import java.io.IOException;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpService;

/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/bootstrap/Worker.class */
class Worker implements Runnable {
    private final HttpService httpservice;
    private final HttpServerConnection conn;
    private final ExceptionLogger exceptionLogger;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Worker(HttpService httpservice, HttpServerConnection conn, ExceptionLogger exceptionLogger) {
        this.httpservice = httpservice;
        this.conn = conn;
        this.exceptionLogger = exceptionLogger;
    }

    public HttpServerConnection getConnection() {
        return this.conn;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                BasicHttpContext localContext = new BasicHttpContext();
                HttpCoreContext context = HttpCoreContext.adapt(localContext);
                while (!Thread.interrupted() && this.conn.isOpen()) {
                    this.httpservice.handleRequest(this.conn, context);
                    localContext.clear();
                }
                this.conn.close();
            } catch (Exception ex) {
                this.exceptionLogger.log(ex);
                try {
                    this.conn.shutdown();
                } catch (IOException ex2) {
                    this.exceptionLogger.log(ex2);
                }
            }
        } finally {
            try {
                this.conn.shutdown();
            } catch (IOException ex3) {
                this.exceptionLogger.log(ex3);
            }
        }
    }
}
