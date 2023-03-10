package org.apache.http.conn;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Arrays;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/HttpHostConnectException.class */
public class HttpHostConnectException extends ConnectException {
    private static final long serialVersionUID = -3194482710275220224L;
    private final HttpHost host;

    @Deprecated
    public HttpHostConnectException(HttpHost host, ConnectException cause) {
        this(cause, host, null);
    }

    public HttpHostConnectException(IOException cause, HttpHost host, InetAddress... remoteAddresses) {
        super("Connect to " + (host != null ? host.toHostString() : "remote host") + ((remoteAddresses == null || remoteAddresses.length <= 0) ? CoreConstants.EMPTY_STRING : " " + Arrays.asList(remoteAddresses)) + ((cause == null || cause.getMessage() == null) ? " refused" : " failed: " + cause.getMessage()));
        this.host = host;
        initCause(cause);
    }

    public HttpHost getHost() {
        return this.host;
    }
}
