package org.apache.http.impl.execchain;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.params.HttpParams;

@NotThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/execchain/HttpResponseProxy.class */
class HttpResponseProxy implements CloseableHttpResponse {
    private final HttpResponse original;
    private final ConnectionHolder connHolder;

    public HttpResponseProxy(HttpResponse original, ConnectionHolder connHolder) {
        this.original = original;
        this.connHolder = connHolder;
        ResponseEntityProxy.enchance(original, connHolder);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.connHolder != null) {
            this.connHolder.close();
        }
    }

    @Override // org.apache.http.HttpResponse
    public StatusLine getStatusLine() {
        return this.original.getStatusLine();
    }

    @Override // org.apache.http.HttpResponse
    public void setStatusLine(StatusLine statusline) {
        this.original.setStatusLine(statusline);
    }

    @Override // org.apache.http.HttpResponse
    public void setStatusLine(ProtocolVersion ver, int code) {
        this.original.setStatusLine(ver, code);
    }

    @Override // org.apache.http.HttpResponse
    public void setStatusLine(ProtocolVersion ver, int code, String reason) {
        this.original.setStatusLine(ver, code, reason);
    }

    @Override // org.apache.http.HttpResponse
    public void setStatusCode(int code) throws IllegalStateException {
        this.original.setStatusCode(code);
    }

    @Override // org.apache.http.HttpResponse
    public void setReasonPhrase(String reason) throws IllegalStateException {
        this.original.setReasonPhrase(reason);
    }

    @Override // org.apache.http.HttpResponse
    public HttpEntity getEntity() {
        return this.original.getEntity();
    }

    @Override // org.apache.http.HttpResponse
    public void setEntity(HttpEntity entity) {
        this.original.setEntity(entity);
    }

    @Override // org.apache.http.HttpResponse
    public Locale getLocale() {
        return this.original.getLocale();
    }

    @Override // org.apache.http.HttpResponse
    public void setLocale(Locale loc) {
        this.original.setLocale(loc);
    }

    @Override // org.apache.http.HttpMessage
    public ProtocolVersion getProtocolVersion() {
        return this.original.getProtocolVersion();
    }

    @Override // org.apache.http.HttpMessage
    public boolean containsHeader(String name) {
        return this.original.containsHeader(name);
    }

    @Override // org.apache.http.HttpMessage
    public Header[] getHeaders(String name) {
        return this.original.getHeaders(name);
    }

    @Override // org.apache.http.HttpMessage
    public Header getFirstHeader(String name) {
        return this.original.getFirstHeader(name);
    }

    @Override // org.apache.http.HttpMessage
    public Header getLastHeader(String name) {
        return this.original.getLastHeader(name);
    }

    @Override // org.apache.http.HttpMessage
    public Header[] getAllHeaders() {
        return this.original.getAllHeaders();
    }

    @Override // org.apache.http.HttpMessage
    public void addHeader(Header header) {
        this.original.addHeader(header);
    }

    @Override // org.apache.http.HttpMessage
    public void addHeader(String name, String value) {
        this.original.addHeader(name, value);
    }

    @Override // org.apache.http.HttpMessage
    public void setHeader(Header header) {
        this.original.setHeader(header);
    }

    @Override // org.apache.http.HttpMessage
    public void setHeader(String name, String value) {
        this.original.setHeader(name, value);
    }

    @Override // org.apache.http.HttpMessage
    public void setHeaders(Header[] headers) {
        this.original.setHeaders(headers);
    }

    @Override // org.apache.http.HttpMessage
    public void removeHeader(Header header) {
        this.original.removeHeader(header);
    }

    @Override // org.apache.http.HttpMessage
    public void removeHeaders(String name) {
        this.original.removeHeaders(name);
    }

    @Override // org.apache.http.HttpMessage
    public HeaderIterator headerIterator() {
        return this.original.headerIterator();
    }

    @Override // org.apache.http.HttpMessage
    public HeaderIterator headerIterator(String name) {
        return this.original.headerIterator(name);
    }

    @Override // org.apache.http.HttpMessage
    @Deprecated
    public HttpParams getParams() {
        return this.original.getParams();
    }

    @Override // org.apache.http.HttpMessage
    @Deprecated
    public void setParams(HttpParams params) {
        this.original.setParams(params);
    }

    public String toString() {
        return "HttpResponseProxy{" + this.original + '}';
    }
}
