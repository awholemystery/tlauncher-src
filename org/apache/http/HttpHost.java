package org.apache.http;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/HttpHost.class */
public final class HttpHost implements Cloneable, Serializable {
    private static final long serialVersionUID = -7529410654042457626L;
    public static final String DEFAULT_SCHEME_NAME = "http";
    protected final String hostname;
    protected final String lcHostname;
    protected final int port;
    protected final String schemeName;
    protected final InetAddress address;

    public HttpHost(String hostname, int port, String scheme) {
        this.hostname = (String) Args.containsNoBlanks(hostname, "Host name");
        this.lcHostname = hostname.toLowerCase(Locale.ROOT);
        if (scheme != null) {
            this.schemeName = scheme.toLowerCase(Locale.ROOT);
        } else {
            this.schemeName = "http";
        }
        this.port = port;
        this.address = null;
    }

    public HttpHost(String hostname, int port) {
        this(hostname, port, (String) null);
    }

    public static HttpHost create(String s) {
        Args.containsNoBlanks(s, "HTTP Host");
        String text = s;
        String scheme = null;
        int schemeIdx = text.indexOf("://");
        if (schemeIdx > 0) {
            scheme = text.substring(0, schemeIdx);
            text = text.substring(schemeIdx + 3);
        }
        int port = -1;
        int portIdx = text.lastIndexOf(":");
        if (portIdx > 0) {
            try {
                port = Integer.parseInt(text.substring(portIdx + 1));
                text = text.substring(0, portIdx);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid HTTP host: " + text);
            }
        }
        return new HttpHost(text, port, scheme);
    }

    public HttpHost(String hostname) {
        this(hostname, -1, (String) null);
    }

    public HttpHost(InetAddress address, int port, String scheme) {
        this((InetAddress) Args.notNull(address, "Inet address"), address.getHostName(), port, scheme);
    }

    public HttpHost(InetAddress address, String hostname, int port, String scheme) {
        this.address = (InetAddress) Args.notNull(address, "Inet address");
        this.hostname = (String) Args.notNull(hostname, "Hostname");
        this.lcHostname = this.hostname.toLowerCase(Locale.ROOT);
        if (scheme != null) {
            this.schemeName = scheme.toLowerCase(Locale.ROOT);
        } else {
            this.schemeName = "http";
        }
        this.port = port;
    }

    public HttpHost(InetAddress address, int port) {
        this(address, port, (String) null);
    }

    public HttpHost(InetAddress address) {
        this(address, -1, (String) null);
    }

    public HttpHost(HttpHost httphost) {
        Args.notNull(httphost, "HTTP host");
        this.hostname = httphost.hostname;
        this.lcHostname = httphost.lcHostname;
        this.schemeName = httphost.schemeName;
        this.port = httphost.port;
        this.address = httphost.address;
    }

    public String getHostName() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public String getSchemeName() {
        return this.schemeName;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public String toURI() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.schemeName);
        buffer.append("://");
        buffer.append(this.hostname);
        if (this.port != -1) {
            buffer.append(':');
            buffer.append(Integer.toString(this.port));
        }
        return buffer.toString();
    }

    public String toHostString() {
        if (this.port != -1) {
            StringBuilder buffer = new StringBuilder(this.hostname.length() + 6);
            buffer.append(this.hostname);
            buffer.append(":");
            buffer.append(Integer.toString(this.port));
            return buffer.toString();
        }
        return this.hostname;
    }

    public String toString() {
        return toURI();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof HttpHost) {
            HttpHost that = (HttpHost) obj;
            return this.lcHostname.equals(that.lcHostname) && this.port == that.port && this.schemeName.equals(that.schemeName) && (this.address != null ? this.address.equals(that.address) : that.address == null);
        }
        return false;
    }

    public int hashCode() {
        int hash = LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.lcHostname), this.port), this.schemeName);
        if (this.address != null) {
            hash = LangUtils.hashCode(hash, this.address);
        }
        return hash;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
