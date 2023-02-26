package org.apache.http.impl.client;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/StandardHttpRequestRetryHandler.class */
public class StandardHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {
    private final Map<String, Boolean> idempotentMethods;

    public StandardHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
        super(retryCount, requestSentRetryEnabled);
        this.idempotentMethods = new ConcurrentHashMap();
        this.idempotentMethods.put(HttpGet.METHOD_NAME, Boolean.TRUE);
        this.idempotentMethods.put(HttpHead.METHOD_NAME, Boolean.TRUE);
        this.idempotentMethods.put(HttpPut.METHOD_NAME, Boolean.TRUE);
        this.idempotentMethods.put(HttpDelete.METHOD_NAME, Boolean.TRUE);
        this.idempotentMethods.put(HttpOptions.METHOD_NAME, Boolean.TRUE);
        this.idempotentMethods.put(HttpTrace.METHOD_NAME, Boolean.TRUE);
    }

    public StandardHttpRequestRetryHandler() {
        this(3, false);
    }

    @Override // org.apache.http.impl.client.DefaultHttpRequestRetryHandler
    protected boolean handleAsIdempotent(HttpRequest request) {
        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
        Boolean b = this.idempotentMethods.get(method);
        return b != null && b.booleanValue();
    }
}
