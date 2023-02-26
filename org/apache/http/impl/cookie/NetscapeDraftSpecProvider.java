package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.annotation.Obsolete;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.protocol.HttpContext;

@Obsolete
@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/cookie/NetscapeDraftSpecProvider.class */
public class NetscapeDraftSpecProvider implements CookieSpecProvider {
    private final String[] datepatterns;
    private volatile CookieSpec cookieSpec;

    public NetscapeDraftSpecProvider(String[] datepatterns) {
        this.datepatterns = datepatterns;
    }

    public NetscapeDraftSpecProvider() {
        this(null);
    }

    @Override // org.apache.http.cookie.CookieSpecProvider
    public CookieSpec create(HttpContext context) {
        if (this.cookieSpec == null) {
            synchronized (this) {
                if (this.cookieSpec == null) {
                    this.cookieSpec = new NetscapeDraftSpec(this.datepatterns);
                }
            }
        }
        return this.cookieSpec;
    }
}
