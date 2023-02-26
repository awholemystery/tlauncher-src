package org.apache.http.impl.conn;

import com.sothawo.mapjfx.cache.CachingURLStreamHandlerFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.util.Args;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/conn/DefaultSchemePortResolver.class */
public class DefaultSchemePortResolver implements SchemePortResolver {
    public static final DefaultSchemePortResolver INSTANCE = new DefaultSchemePortResolver();

    @Override // org.apache.http.conn.SchemePortResolver
    public int resolve(HttpHost host) throws UnsupportedSchemeException {
        Args.notNull(host, "HTTP host");
        int port = host.getPort();
        if (port > 0) {
            return port;
        }
        String name = host.getSchemeName();
        if (name.equalsIgnoreCase("http")) {
            return 80;
        }
        if (name.equalsIgnoreCase(CachingURLStreamHandlerFactory.PROTO_HTTPS)) {
            return 443;
        }
        throw new UnsupportedSchemeException(name + " protocol is not supported");
    }
}
