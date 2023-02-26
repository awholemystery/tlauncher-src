package org.apache.http.protocol;

/* loaded from: TLauncher-2.876.jar:org/apache/http/protocol/HttpContext.class */
public interface HttpContext {
    public static final String RESERVED_PREFIX = "http.";

    Object getAttribute(String str);

    void setAttribute(String str, Object obj);

    Object removeAttribute(String str);
}
