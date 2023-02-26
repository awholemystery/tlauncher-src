package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.protocol.HttpContext;

@NotThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/conn/DefaultManagedHttpClientConnection.class */
public class DefaultManagedHttpClientConnection extends DefaultBHttpClientConnection implements ManagedHttpClientConnection, HttpContext {
    private final String id;
    private final Map<String, Object> attributes;
    private volatile boolean shutdown;

    public DefaultManagedHttpClientConnection(String id, int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
        super(buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy, requestWriterFactory, responseParserFactory);
        this.id = id;
        this.attributes = new ConcurrentHashMap();
    }

    public DefaultManagedHttpClientConnection(String id, int buffersize) {
        this(id, buffersize, buffersize, null, null, null, null, null, null, null);
    }

    @Override // org.apache.http.conn.ManagedHttpClientConnection
    public String getId() {
        return this.id;
    }

    @Override // org.apache.http.impl.BHttpConnectionBase, org.apache.http.HttpConnection
    public void shutdown() throws IOException {
        this.shutdown = true;
        super.shutdown();
    }

    @Override // org.apache.http.protocol.HttpContext
    public Object getAttribute(String id) {
        return this.attributes.get(id);
    }

    @Override // org.apache.http.protocol.HttpContext
    public Object removeAttribute(String id) {
        return this.attributes.remove(id);
    }

    @Override // org.apache.http.protocol.HttpContext
    public void setAttribute(String id, Object obj) {
        this.attributes.put(id, obj);
    }

    @Override // org.apache.http.impl.DefaultBHttpClientConnection, org.apache.http.impl.BHttpConnectionBase
    public void bind(Socket socket) throws IOException {
        if (this.shutdown) {
            socket.close();
            throw new InterruptedIOException("Connection already shutdown");
        } else {
            super.bind(socket);
        }
    }

    @Override // org.apache.http.impl.BHttpConnectionBase, org.apache.http.conn.ManagedHttpClientConnection
    public Socket getSocket() {
        return super.getSocket();
    }

    @Override // org.apache.http.conn.ManagedHttpClientConnection
    public SSLSession getSSLSession() {
        Socket socket = super.getSocket();
        if (socket instanceof SSLSocket) {
            return ((SSLSocket) socket).getSession();
        }
        return null;
    }
}
