package org.tlauncher.util.salf.connection;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/salf/connection/ServerEntity.class */
public class ServerEntity {
    private String url;
    private int port;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String toString() {
        return "SALFServerEntity [url=" + this.url + ", port=" + this.port + "]";
    }
}
