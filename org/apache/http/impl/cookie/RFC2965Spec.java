package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Obsolete;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@ThreadSafe
@Obsolete
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/cookie/RFC2965Spec.class */
public class RFC2965Spec extends RFC2109Spec {
    public RFC2965Spec() {
        this((String[]) null, false);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public RFC2965Spec(java.lang.String[] r10, boolean r11) {
        /*
            r9 = this;
            r0 = r9
            r1 = r11
            r2 = 10
            org.apache.http.cookie.CommonCookieAttributeHandler[] r2 = new org.apache.http.cookie.CommonCookieAttributeHandler[r2]
            r3 = r2
            r4 = 0
            org.apache.http.impl.cookie.RFC2965VersionAttributeHandler r5 = new org.apache.http.impl.cookie.RFC2965VersionAttributeHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r3 = r2
            r4 = 1
            org.apache.http.impl.cookie.BasicPathHandler r5 = new org.apache.http.impl.cookie.BasicPathHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r3 = r2
            r4 = 2
            org.apache.http.impl.cookie.RFC2965DomainAttributeHandler r5 = new org.apache.http.impl.cookie.RFC2965DomainAttributeHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r3 = r2
            r4 = 3
            org.apache.http.impl.cookie.RFC2965PortAttributeHandler r5 = new org.apache.http.impl.cookie.RFC2965PortAttributeHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r3 = r2
            r4 = 4
            org.apache.http.impl.cookie.BasicMaxAgeHandler r5 = new org.apache.http.impl.cookie.BasicMaxAgeHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r3 = r2
            r4 = 5
            org.apache.http.impl.cookie.BasicSecureHandler r5 = new org.apache.http.impl.cookie.BasicSecureHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r3 = r2
            r4 = 6
            org.apache.http.impl.cookie.BasicCommentHandler r5 = new org.apache.http.impl.cookie.BasicCommentHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r3 = r2
            r4 = 7
            org.apache.http.impl.cookie.BasicExpiresHandler r5 = new org.apache.http.impl.cookie.BasicExpiresHandler
            r6 = r5
            r7 = r10
            if (r7 == 0) goto L63
            r7 = r10
            java.lang.Object r7 = r7.clone()
            java.lang.String[] r7 = (java.lang.String[]) r7
            goto L66
        L63:
            java.lang.String[] r7 = org.apache.http.impl.cookie.RFC2965Spec.DATE_PATTERNS
        L66:
            r6.<init>(r7)
            r3[r4] = r5
            r3 = r2
            r4 = 8
            org.apache.http.impl.cookie.RFC2965CommentUrlAttributeHandler r5 = new org.apache.http.impl.cookie.RFC2965CommentUrlAttributeHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r3 = r2
            r4 = 9
            org.apache.http.impl.cookie.RFC2965DiscardAttributeHandler r5 = new org.apache.http.impl.cookie.RFC2965DiscardAttributeHandler
            r6 = r5
            r6.<init>()
            r3[r4] = r5
            r0.<init>(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.cookie.RFC2965Spec.<init>(java.lang.String[], boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RFC2965Spec(boolean oneHeader, CommonCookieAttributeHandler... handlers) {
        super(oneHeader, handlers);
    }

    @Override // org.apache.http.impl.cookie.RFC2109Spec, org.apache.http.cookie.CookieSpec
    public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        if (!header.getName().equalsIgnoreCase(SM.SET_COOKIE2)) {
            throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
        }
        HeaderElement[] elems = header.getElements();
        return createCookies(elems, adjustEffectiveHost(origin));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.http.impl.cookie.CookieSpecBase
    public List<Cookie> parse(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
        return createCookies(elems, adjustEffectiveHost(origin));
    }

    private List<Cookie> createCookies(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
        List<Cookie> cookies = new ArrayList<>(elems.length);
        for (HeaderElement headerelement : elems) {
            String name = headerelement.getName();
            String value = headerelement.getValue();
            if (name == null || name.isEmpty()) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            BasicClientCookie2 cookie = new BasicClientCookie2(name, value);
            cookie.setPath(getDefaultPath(origin));
            cookie.setDomain(getDefaultDomain(origin));
            cookie.setPorts(new int[]{origin.getPort()});
            NameValuePair[] attribs = headerelement.getParameters();
            Map<String, NameValuePair> attribmap = new HashMap<>(attribs.length);
            for (int j = attribs.length - 1; j >= 0; j--) {
                NameValuePair param = attribs[j];
                attribmap.put(param.getName().toLowerCase(Locale.ROOT), param);
            }
            for (Map.Entry<String, NameValuePair> entry : attribmap.entrySet()) {
                NameValuePair attrib = entry.getValue();
                String s = attrib.getName().toLowerCase(Locale.ROOT);
                cookie.setAttribute(s, attrib.getValue());
                CookieAttributeHandler handler = findAttribHandler(s);
                if (handler != null) {
                    handler.parse(cookie, attrib.getValue());
                }
            }
            cookies.add(cookie);
        }
        return cookies;
    }

    @Override // org.apache.http.impl.cookie.RFC2109Spec, org.apache.http.impl.cookie.CookieSpecBase, org.apache.http.cookie.CookieSpec
    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(cookie, SM.COOKIE);
        Args.notNull(origin, "Cookie origin");
        super.validate(cookie, adjustEffectiveHost(origin));
    }

    @Override // org.apache.http.impl.cookie.CookieSpecBase, org.apache.http.cookie.CookieSpec
    public boolean match(Cookie cookie, CookieOrigin origin) {
        Args.notNull(cookie, SM.COOKIE);
        Args.notNull(origin, "Cookie origin");
        return super.match(cookie, adjustEffectiveHost(origin));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.http.impl.cookie.RFC2109Spec
    public void formatCookieAsVer(CharArrayBuffer buffer, Cookie cookie, int version) {
        String s;
        int[] ports;
        super.formatCookieAsVer(buffer, cookie, version);
        if ((cookie instanceof ClientCookie) && (s = ((ClientCookie) cookie).getAttribute(ClientCookie.PORT_ATTR)) != null) {
            buffer.append("; $Port");
            buffer.append("=\"");
            if (!s.trim().isEmpty() && (ports = cookie.getPorts()) != null) {
                int len = ports.length;
                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        buffer.append(",");
                    }
                    buffer.append(Integer.toString(ports[i]));
                }
            }
            buffer.append("\"");
        }
    }

    private static CookieOrigin adjustEffectiveHost(CookieOrigin origin) {
        String host = origin.getHost();
        boolean isLocalHost = true;
        for (int i = 0; i < host.length(); i++) {
            char ch2 = host.charAt(i);
            if (ch2 == '.' || ch2 == ':') {
                isLocalHost = false;
                break;
            }
        }
        if (isLocalHost) {
            return new CookieOrigin(host + ".local", origin.getPort(), origin.getPath(), origin.isSecure());
        }
        return origin;
    }

    @Override // org.apache.http.impl.cookie.RFC2109Spec, org.apache.http.cookie.CookieSpec
    public int getVersion() {
        return 1;
    }

    @Override // org.apache.http.impl.cookie.RFC2109Spec, org.apache.http.cookie.CookieSpec
    public Header getVersionHeader() {
        CharArrayBuffer buffer = new CharArrayBuffer(40);
        buffer.append(SM.COOKIE2);
        buffer.append(": ");
        buffer.append("$Version=");
        buffer.append(Integer.toString(getVersion()));
        return new BufferedHeader(buffer);
    }

    @Override // org.apache.http.impl.cookie.RFC2109Spec
    public String toString() {
        return CookiePolicy.RFC_2965;
    }
}
