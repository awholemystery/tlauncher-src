package org.apache.http.conn;

import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/DnsResolver.class */
public interface DnsResolver {
    InetAddress[] resolve(String str) throws UnknownHostException;
}
