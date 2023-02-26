package org.apache.http.impl.client;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;

@ThreadSafe
@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/ContentEncodingHttpClient.class */
public class ContentEncodingHttpClient extends DefaultHttpClient {
    public ContentEncodingHttpClient(ClientConnectionManager conman, HttpParams params) {
        super(conman, params);
    }

    public ContentEncodingHttpClient(HttpParams params) {
        this(null, params);
    }

    public ContentEncodingHttpClient() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.http.impl.client.DefaultHttpClient, org.apache.http.impl.client.AbstractHttpClient
    public BasicHttpProcessor createHttpProcessor() {
        BasicHttpProcessor result = super.createHttpProcessor();
        result.addRequestInterceptor(new RequestAcceptEncoding());
        result.addResponseInterceptor(new ResponseContentEncoding());
        return result;
    }
}
