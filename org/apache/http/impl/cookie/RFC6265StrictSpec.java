package org.apache.http.impl.cookie;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.cookie.CommonCookieAttributeHandler;

@ThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/cookie/RFC6265StrictSpec.class */
public class RFC6265StrictSpec extends RFC6265CookieSpecBase {
    static final String[] DATE_PATTERNS = {"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy"};

    public RFC6265StrictSpec() {
        super(new BasicPathHandler(), new BasicDomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(DATE_PATTERNS));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RFC6265StrictSpec(CommonCookieAttributeHandler... handlers) {
        super(handlers);
    }

    public String toString() {
        return "rfc6265-strict";
    }
}
