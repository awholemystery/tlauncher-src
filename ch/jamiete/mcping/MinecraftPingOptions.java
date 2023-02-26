package ch.jamiete.mcping;

/* loaded from: TLauncher-2.876.jar:ch/jamiete/mcping/MinecraftPingOptions.class */
public class MinecraftPingOptions {
    private String hostname;
    private int port = 25565;
    private int timeout = 2000;
    private String charset = "UTF-8";

    public String getHostname() {
        return this.hostname;
    }

    public MinecraftPingOptions setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public MinecraftPingOptions setPort(int port) {
        this.port = port;
        return this;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public MinecraftPingOptions setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getCharset() {
        return this.charset;
    }

    public MinecraftPingOptions setCharset(String charset) {
        this.charset = charset;
        return this;
    }
}
