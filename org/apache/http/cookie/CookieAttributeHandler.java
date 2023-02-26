package org.apache.http.cookie;

/* loaded from: TLauncher-2.876.jar:org/apache/http/cookie/CookieAttributeHandler.class */
public interface CookieAttributeHandler {
    void parse(SetCookie setCookie, String str) throws MalformedCookieException;

    void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException;

    boolean match(Cookie cookie, CookieOrigin cookieOrigin);
}
