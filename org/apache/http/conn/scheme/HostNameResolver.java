package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/scheme/HostNameResolver.class */
public interface HostNameResolver {
    InetAddress resolve(String str) throws IOException;
}
