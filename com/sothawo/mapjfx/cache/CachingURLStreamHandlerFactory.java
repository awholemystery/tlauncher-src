package com.sothawo.mapjfx.cache;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.HttpsURLConnection;
import org.apache.log4j.Logger;

/* loaded from: TLauncher-2.876.jar:com/sothawo/mapjfx/cache/CachingURLStreamHandlerFactory.class */
class CachingURLStreamHandlerFactory implements URLStreamHandlerFactory {
    public static final String PROTO_HTTP = "http";
    public static final String PROTO_HTTPS = "https";
    private static final Logger logger = Logger.getLogger(CachingURLStreamHandlerFactory.class);
    private final OfflineCache cache;
    private final Map<String, URLStreamHandler> handlers = new ConcurrentHashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public CachingURLStreamHandlerFactory(OfflineCache cache) {
        this.cache = cache;
        URLStreamHandler urlStreamHandler = getURLStreamHandler("http");
        if (urlStreamHandler != null) {
            this.handlers.put("http", urlStreamHandler);
        }
        if (getURLStreamHandler(PROTO_HTTPS) != null) {
            this.handlers.put(PROTO_HTTPS, getURLStreamHandler(PROTO_HTTPS));
        }
    }

    private URLStreamHandler getURLStreamHandler(String protocol) {
        try {
            Method method = URL.class.getDeclaredMethod("getURLStreamHandler", String.class);
            method.setAccessible(true);
            return (URLStreamHandler) method.invoke(null, protocol);
        } catch (Exception e) {
            if (logger.isTraceEnabled()) {
                logger.warn("could not access URL.getUrlStreamHandler for protocol " + protocol);
                return null;
            }
            return null;
        }
    }

    @Override // java.net.URLStreamHandlerFactory
    public URLStreamHandler createURLStreamHandler(final String protocol) {
        if (null == protocol) {
            throw new IllegalArgumentException("null protocol not allowed");
        }
        if (logger.isTraceEnabled()) {
            logger.trace("need to create URLStreamHandler for protocol " + protocol);
        }
        final String proto = protocol.toLowerCase();
        if ("http".equals(proto) || PROTO_HTTPS.equals(proto)) {
            if (this.handlers.get(protocol) == null) {
                logger.warn("default protocol handler for protocol {} not available " + protocol);
                return null;
            }
            return new URLStreamHandler() { // from class: com.sothawo.mapjfx.cache.CachingURLStreamHandlerFactory.1
                @Override // java.net.URLStreamHandler
                protected URLConnection openConnection(URL url) throws IOException {
                    if (CachingURLStreamHandlerFactory.logger.isTraceEnabled()) {
                        CachingURLStreamHandlerFactory.logger.trace("should open connection to " + url.toExternalForm());
                    }
                    URLConnection defaultUrlConnection = new URL(protocol, url.getHost(), url.getPort(), url.getFile(), (URLStreamHandler) CachingURLStreamHandlerFactory.this.handlers.get(protocol)).openConnection();
                    if (!CachingURLStreamHandlerFactory.this.cache.urlShouldBeCached(url)) {
                        if (CachingURLStreamHandlerFactory.logger.isTraceEnabled()) {
                            CachingURLStreamHandlerFactory.logger.trace("not using cache for " + url);
                        }
                        return defaultUrlConnection;
                    }
                    CachingURLStreamHandlerFactory.this.cache.filenameForURL(url);
                    if (CachingURLStreamHandlerFactory.this.cache.isCached(url)) {
                        return new CachingHttpURLConnection(CachingURLStreamHandlerFactory.this.cache, (HttpURLConnection) defaultUrlConnection);
                    }
                    String str = proto;
                    boolean z = true;
                    switch (str.hashCode()) {
                        case 3213448:
                            if (str.equals("http")) {
                                z = false;
                                break;
                            }
                            break;
                        case 99617003:
                            if (str.equals(CachingURLStreamHandlerFactory.PROTO_HTTPS)) {
                                z = true;
                                break;
                            }
                            break;
                    }
                    switch (z) {
                        case false:
                            return new CachingHttpURLConnection(CachingURLStreamHandlerFactory.this.cache, (HttpURLConnection) defaultUrlConnection);
                        case true:
                            return new CachingHttpsURLConnection(CachingURLStreamHandlerFactory.this.cache, (HttpsURLConnection) defaultUrlConnection);
                        default:
                            throw new IOException("no matching handler");
                    }
                }

                @Override // java.net.URLStreamHandler
                protected URLConnection openConnection(URL u, Proxy p) throws IOException {
                    if (CachingURLStreamHandlerFactory.logger.isTraceEnabled()) {
                        CachingURLStreamHandlerFactory.logger.trace("should open connection to" + u.toExternalForm());
                    }
                    URLConnection defaultUrlConnection = new URL(protocol, u.getHost(), u.getPort(), u.getFile(), (URLStreamHandler) CachingURLStreamHandlerFactory.this.handlers.get(protocol)).openConnection(p);
                    return defaultUrlConnection;
                }
            };
        }
        return null;
    }
}
