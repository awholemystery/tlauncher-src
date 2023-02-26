package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@ThreadSafe
@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/cookie/BrowserCompatSpec.class */
public class BrowserCompatSpec extends CookieSpecBase {
    private static final String[] DEFAULT_DATE_PATTERNS = {"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z"};

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public BrowserCompatSpec(java.lang.String[] r9, org.apache.http.impl.cookie.BrowserCompatSpecFactory.SecurityLevel r10) {
        /*
            r8 = this;
            r0 = r8
            r1 = 7
            org.apache.http.cookie.CommonCookieAttributeHandler[] r1 = new org.apache.http.cookie.CommonCookieAttributeHandler[r1]
            r2 = r1
            r3 = 0
            org.apache.http.impl.cookie.BrowserCompatVersionAttributeHandler r4 = new org.apache.http.impl.cookie.BrowserCompatVersionAttributeHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 1
            org.apache.http.impl.cookie.BasicDomainHandler r4 = new org.apache.http.impl.cookie.BasicDomainHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 2
            r4 = r10
            org.apache.http.impl.cookie.BrowserCompatSpecFactory$SecurityLevel r5 = org.apache.http.impl.cookie.BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_IE_MEDIUM
            if (r4 != r5) goto L2d
            org.apache.http.impl.cookie.BrowserCompatSpec$1 r4 = new org.apache.http.impl.cookie.BrowserCompatSpec$1
            r5 = r4
            r5.<init>()
            goto L34
        L2d:
            org.apache.http.impl.cookie.BasicPathHandler r4 = new org.apache.http.impl.cookie.BasicPathHandler
            r5 = r4
            r5.<init>()
        L34:
            r2[r3] = r4
            r2 = r1
            r3 = 3
            org.apache.http.impl.cookie.BasicMaxAgeHandler r4 = new org.apache.http.impl.cookie.BasicMaxAgeHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 4
            org.apache.http.impl.cookie.BasicSecureHandler r4 = new org.apache.http.impl.cookie.BasicSecureHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 5
            org.apache.http.impl.cookie.BasicCommentHandler r4 = new org.apache.http.impl.cookie.BasicCommentHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 6
            org.apache.http.impl.cookie.BasicExpiresHandler r4 = new org.apache.http.impl.cookie.BasicExpiresHandler
            r5 = r4
            r6 = r9
            if (r6 == 0) goto L68
            r6 = r9
            java.lang.Object r6 = r6.clone()
            java.lang.String[] r6 = (java.lang.String[]) r6
            goto L6b
        L68:
            java.lang.String[] r6 = org.apache.http.impl.cookie.BrowserCompatSpec.DEFAULT_DATE_PATTERNS
        L6b:
            r5.<init>(r6)
            r2[r3] = r4
            r0.<init>(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.cookie.BrowserCompatSpec.<init>(java.lang.String[], org.apache.http.impl.cookie.BrowserCompatSpecFactory$SecurityLevel):void");
    }

    public BrowserCompatSpec(String[] datepatterns) {
        this(datepatterns, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    public BrowserCompatSpec() {
        this(null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
    }

    @Override // org.apache.http.cookie.CookieSpec
    public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
        CharArrayBuffer buffer;
        ParserCursor cursor;
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        String headername = header.getName();
        if (!headername.equalsIgnoreCase(SM.SET_COOKIE)) {
            throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
        }
        HeaderElement[] helems = header.getElements();
        boolean versioned = false;
        boolean netscape = false;
        for (HeaderElement helem : helems) {
            if (helem.getParameterByName(ClientCookie.VERSION_ATTR) != null) {
                versioned = true;
            }
            if (helem.getParameterByName(ClientCookie.EXPIRES_ATTR) != null) {
                netscape = true;
            }
        }
        if (netscape || !versioned) {
            NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
            if (header instanceof FormattedHeader) {
                buffer = ((FormattedHeader) header).getBuffer();
                cursor = new ParserCursor(((FormattedHeader) header).getValuePos(), buffer.length());
            } else {
                String s = header.getValue();
                if (s == null) {
                    throw new MalformedCookieException("Header value is null");
                }
                buffer = new CharArrayBuffer(s.length());
                buffer.append(s);
                cursor = new ParserCursor(0, buffer.length());
            }
            HeaderElement elem = parser.parseHeader(buffer, cursor);
            String name = elem.getName();
            String value = elem.getValue();
            if (name == null || name.isEmpty()) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            BasicClientCookie cookie = new BasicClientCookie(name, value);
            cookie.setPath(getDefaultPath(origin));
            cookie.setDomain(getDefaultDomain(origin));
            NameValuePair[] attribs = elem.getParameters();
            for (int j = attribs.length - 1; j >= 0; j--) {
                NameValuePair attrib = attribs[j];
                String s2 = attrib.getName().toLowerCase(Locale.ROOT);
                cookie.setAttribute(s2, attrib.getValue());
                CookieAttributeHandler handler = findAttribHandler(s2);
                if (handler != null) {
                    handler.parse(cookie, attrib.getValue());
                }
            }
            if (netscape) {
                cookie.setVersion(0);
            }
            return Collections.singletonList(cookie);
        }
        return parse(helems, origin);
    }

    private static boolean isQuoteEnclosed(String s) {
        return s != null && s.startsWith("\"") && s.endsWith("\"");
    }

    @Override // org.apache.http.cookie.CookieSpec
    public List<Header> formatCookies(List<Cookie> cookies) {
        Args.notEmpty(cookies, "List of cookies");
        CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
        buffer.append(SM.COOKIE);
        buffer.append(": ");
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            if (i > 0) {
                buffer.append("; ");
            }
            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (cookie.getVersion() > 0 && !isQuoteEnclosed(cookieValue)) {
                BasicHeaderValueFormatter.INSTANCE.formatHeaderElement(buffer, (HeaderElement) new BasicHeaderElement(cookieName, cookieValue), false);
            } else {
                buffer.append(cookieName);
                buffer.append("=");
                if (cookieValue != null) {
                    buffer.append(cookieValue);
                }
            }
        }
        List<Header> headers = new ArrayList<>(1);
        headers.add(new BufferedHeader(buffer));
        return headers;
    }

    @Override // org.apache.http.cookie.CookieSpec
    public int getVersion() {
        return 0;
    }

    @Override // org.apache.http.cookie.CookieSpec
    public Header getVersionHeader() {
        return null;
    }

    public String toString() {
        return "compatibility";
    }
}
