package org.apache.http.cookie;

import java.util.List;
import org.apache.http.Header;
import org.apache.http.annotation.Obsolete;

/* loaded from: TLauncher-2.876.jar:org/apache/http/cookie/CookieSpec.class */
public interface CookieSpec {
    @Obsolete
    int getVersion();

    List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException;

    void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException;

    boolean match(Cookie cookie, CookieOrigin cookieOrigin);

    List<Header> formatCookies(List<Cookie> list);

    @Obsolete
    Header getVersionHeader();
}
