package org.apache.http.protocol;

import org.apache.http.HttpRequest;

/* loaded from: TLauncher-2.876.jar:org/apache/http/protocol/HttpRequestHandlerMapper.class */
public interface HttpRequestHandlerMapper {
    HttpRequestHandler lookup(HttpRequest httpRequest);
}
