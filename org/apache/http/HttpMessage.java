package org.apache.http;

import org.apache.http.params.HttpParams;

/* loaded from: TLauncher-2.876.jar:org/apache/http/HttpMessage.class */
public interface HttpMessage {
    ProtocolVersion getProtocolVersion();

    boolean containsHeader(String str);

    Header[] getHeaders(String str);

    Header getFirstHeader(String str);

    Header getLastHeader(String str);

    Header[] getAllHeaders();

    void addHeader(Header header);

    void addHeader(String str, String str2);

    void setHeader(Header header);

    void setHeader(String str, String str2);

    void setHeaders(Header[] headerArr);

    void removeHeader(Header header);

    void removeHeaders(String str);

    HeaderIterator headerIterator();

    HeaderIterator headerIterator(String str);

    @Deprecated
    HttpParams getParams();

    @Deprecated
    void setParams(HttpParams httpParams);
}
