package org.apache.http;

import java.util.Locale;

/* loaded from: TLauncher-2.876.jar:org/apache/http/HttpResponse.class */
public interface HttpResponse extends HttpMessage {
    StatusLine getStatusLine();

    void setStatusLine(StatusLine statusLine);

    void setStatusLine(ProtocolVersion protocolVersion, int i);

    void setStatusLine(ProtocolVersion protocolVersion, int i, String str);

    void setStatusCode(int i) throws IllegalStateException;

    void setReasonPhrase(String str) throws IllegalStateException;

    HttpEntity getEntity();

    void setEntity(HttpEntity httpEntity);

    Locale getLocale();

    void setLocale(Locale locale);
}
