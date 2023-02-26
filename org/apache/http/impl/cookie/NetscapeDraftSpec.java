package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.Obsolete;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@ThreadSafe
@Obsolete
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/cookie/NetscapeDraftSpec.class */
public class NetscapeDraftSpec extends CookieSpecBase {
    protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yy HH:mm:ss z";

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public NetscapeDraftSpec(java.lang.String[] r12) {
        /*
            r11 = this;
            r0 = r11
            r1 = 5
            org.apache.http.cookie.CommonCookieAttributeHandler[] r1 = new org.apache.http.cookie.CommonCookieAttributeHandler[r1]
            r2 = r1
            r3 = 0
            org.apache.http.impl.cookie.BasicPathHandler r4 = new org.apache.http.impl.cookie.BasicPathHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 1
            org.apache.http.impl.cookie.NetscapeDomainHandler r4 = new org.apache.http.impl.cookie.NetscapeDomainHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 2
            org.apache.http.impl.cookie.BasicSecureHandler r4 = new org.apache.http.impl.cookie.BasicSecureHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 3
            org.apache.http.impl.cookie.BasicCommentHandler r4 = new org.apache.http.impl.cookie.BasicCommentHandler
            r5 = r4
            r5.<init>()
            r2[r3] = r4
            r2 = r1
            r3 = 4
            org.apache.http.impl.cookie.BasicExpiresHandler r4 = new org.apache.http.impl.cookie.BasicExpiresHandler
            r5 = r4
            r6 = r12
            if (r6 == 0) goto L41
            r6 = r12
            java.lang.Object r6 = r6.clone()
            java.lang.String[] r6 = (java.lang.String[]) r6
            goto L4a
        L41:
            r6 = 1
            java.lang.String[] r6 = new java.lang.String[r6]
            r7 = r6
            r8 = 0
            java.lang.String r9 = "EEE, dd-MMM-yy HH:mm:ss z"
            r7[r8] = r9
        L4a:
            r5.<init>(r6)
            r2[r3] = r4
            r0.<init>(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.cookie.NetscapeDraftSpec.<init>(java.lang.String[]):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NetscapeDraftSpec(CommonCookieAttributeHandler... handlers) {
        super(handlers);
    }

    public NetscapeDraftSpec() {
        this((String[]) null);
    }

    @Override // org.apache.http.cookie.CookieSpec
    public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
        CharArrayBuffer buffer;
        ParserCursor cursor;
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        if (!header.getName().equalsIgnoreCase(SM.SET_COOKIE)) {
            throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
        }
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
        return parse(new HeaderElement[]{parser.parseHeader(buffer, cursor)}, origin);
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
            buffer.append(cookie.getName());
            String s = cookie.getValue();
            if (s != null) {
                buffer.append("=");
                buffer.append(s);
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
        return "netscape";
    }
}
