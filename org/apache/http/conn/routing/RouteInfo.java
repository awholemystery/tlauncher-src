package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;

/* loaded from: TLauncher-2.876.jar:org/apache/http/conn/routing/RouteInfo.class */
public interface RouteInfo {

    /* loaded from: TLauncher-2.876.jar:org/apache/http/conn/routing/RouteInfo$LayerType.class */
    public enum LayerType {
        PLAIN,
        LAYERED
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/http/conn/routing/RouteInfo$TunnelType.class */
    public enum TunnelType {
        PLAIN,
        TUNNELLED
    }

    HttpHost getTargetHost();

    InetAddress getLocalAddress();

    int getHopCount();

    HttpHost getHopTarget(int i);

    HttpHost getProxyHost();

    TunnelType getTunnelType();

    boolean isTunnelled();

    LayerType getLayerType();

    boolean isLayered();

    boolean isSecure();
}
