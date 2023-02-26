package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.VersionInfo;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/execchain/MinimalClientExec.class */
public class MinimalClientExec implements ClientExecChain {
    private final Log log = LogFactory.getLog(getClass());
    private final HttpRequestExecutor requestExecutor;
    private final HttpClientConnectionManager connManager;
    private final ConnectionReuseStrategy reuseStrategy;
    private final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final HttpProcessor httpProcessor;

    public MinimalClientExec(HttpRequestExecutor requestExecutor, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy) {
        Args.notNull(requestExecutor, "HTTP request executor");
        Args.notNull(connManager, "Client connection manager");
        Args.notNull(reuseStrategy, "Connection reuse strategy");
        Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
        this.httpProcessor = new ImmutableHttpProcessor(new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", getClass())));
        this.requestExecutor = requestExecutor;
        this.connManager = connManager;
        this.reuseStrategy = reuseStrategy;
        this.keepAliveStrategy = keepAliveStrategy;
    }

    static void rewriteRequestURI(HttpRequestWrapper request, HttpRoute route) throws ProtocolException {
        URI uri;
        try {
            URI uri2 = request.getURI();
            if (uri2 != null) {
                if (uri2.isAbsolute()) {
                    uri = URIUtils.rewriteURI(uri2, null, true);
                } else {
                    uri = URIUtils.rewriteURI(uri2);
                }
                request.setURI(uri);
            }
        } catch (URISyntaxException ex) {
            throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
        }
    }

    @Override // org.apache.http.impl.execchain.ClientExecChain
    public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
        Args.notNull(route, "HTTP route");
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");
        rewriteRequestURI(request, route);
        ConnectionRequest connRequest = this.connManager.requestConnection(route, null);
        if (execAware != null) {
            if (execAware.isAborted()) {
                connRequest.cancel();
                throw new RequestAbortedException("Request aborted");
            }
            execAware.setCancellable(connRequest);
        }
        RequestConfig config = context.getRequestConfig();
        try {
            int timeout = config.getConnectionRequestTimeout();
            HttpClientConnection managedConn = connRequest.get(timeout > 0 ? timeout : 0L, TimeUnit.MILLISECONDS);
            ConnectionHolder releaseTrigger = new ConnectionHolder(this.log, this.connManager, managedConn);
            if (execAware != null) {
                try {
                    if (execAware.isAborted()) {
                        releaseTrigger.close();
                        throw new RequestAbortedException("Request aborted");
                    }
                    execAware.setCancellable(releaseTrigger);
                } catch (IOException ex) {
                    releaseTrigger.abortConnection();
                    throw ex;
                } catch (RuntimeException ex2) {
                    releaseTrigger.abortConnection();
                    throw ex2;
                } catch (HttpException ex3) {
                    releaseTrigger.abortConnection();
                    throw ex3;
                } catch (ConnectionShutdownException ex4) {
                    InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
                    ioex.initCause(ex4);
                    throw ioex;
                }
            }
            if (!managedConn.isOpen()) {
                int timeout2 = config.getConnectTimeout();
                this.connManager.connect(managedConn, route, timeout2 > 0 ? timeout2 : 0, context);
                this.connManager.routeComplete(managedConn, route, context);
            }
            int timeout3 = config.getSocketTimeout();
            if (timeout3 >= 0) {
                managedConn.setSocketTimeout(timeout3);
            }
            HttpHost target = null;
            HttpRequest original = request.getOriginal();
            if (original instanceof HttpUriRequest) {
                URI uri = ((HttpUriRequest) original).getURI();
                if (uri.isAbsolute()) {
                    target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
                }
            }
            if (target == null) {
                target = route.getTargetHost();
            }
            context.setAttribute("http.target_host", target);
            context.setAttribute("http.request", request);
            context.setAttribute("http.connection", managedConn);
            context.setAttribute("http.route", route);
            this.httpProcessor.process(request, context);
            HttpResponse response = this.requestExecutor.execute(request, managedConn, context);
            this.httpProcessor.process(response, context);
            if (this.reuseStrategy.keepAlive(response, context)) {
                long duration = this.keepAliveStrategy.getKeepAliveDuration(response, context);
                releaseTrigger.setValidFor(duration, TimeUnit.MILLISECONDS);
                releaseTrigger.markReusable();
            } else {
                releaseTrigger.markNonReusable();
            }
            HttpEntity entity = response.getEntity();
            if (entity == null || !entity.isStreaming()) {
                releaseTrigger.releaseConnection();
                return new HttpResponseProxy(response, null);
            }
            return new HttpResponseProxy(response, releaseTrigger);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new RequestAbortedException("Request aborted", interrupted);
        } catch (ExecutionException ex5) {
            Throwable cause = ex5.getCause();
            if (cause == null) {
                cause = ex5;
            }
            throw new RequestAbortedException("Request execution failed", cause);
        }
    }
}
