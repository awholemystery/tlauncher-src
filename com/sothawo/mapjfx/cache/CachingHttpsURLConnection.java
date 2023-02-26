package com.sothawo.mapjfx.cache;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Path;
import java.security.Permission;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.ExternallyRolledFileAppender;

/* loaded from: TLauncher-2.876.jar:com/sothawo/mapjfx/cache/CachingHttpsURLConnection.class */
class CachingHttpsURLConnection extends HttpsURLConnection {
    private static final Logger logger = Logger.getLogger(CachingHttpsURLConnection.class);
    private final HttpsURLConnection delegate;
    private final Path cacheFile;
    private final OfflineCache cache;
    private boolean readFromCache;
    private InputStream inputStream;
    private CachedDataInfo cachedDataInfo;

    private CachingHttpsURLConnection(URL url) {
        super(url);
        this.readFromCache = false;
        this.cache = null;
        this.delegate = null;
        this.cacheFile = null;
    }

    public CachingHttpsURLConnection(OfflineCache cache, HttpsURLConnection delegate) throws IOException {
        super(delegate.getURL());
        this.readFromCache = false;
        this.cache = cache;
        this.delegate = delegate;
        this.cacheFile = cache.filenameForURL(delegate.getURL());
        this.cachedDataInfo = cache.readCachedDataInfo(this.cacheFile);
        this.readFromCache = cache.isCached(delegate.getURL()) && null != this.cachedDataInfo;
        if (!this.readFromCache) {
            this.cachedDataInfo = new CachedDataInfo();
        }
        if (logger.isTraceEnabled()) {
            logger.trace("in cache: " + this.readFromCache + ", URL: " + delegate.getURL().toExternalForm() + ", cache file: " + this.cacheFile);
        }
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        if (!this.readFromCache) {
            if (logger.isTraceEnabled()) {
                logger.trace("connect to " + this.delegate.getURL().toExternalForm());
            }
            this.delegate.connect();
        }
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public String getCipherSuite() {
        return this.delegate.getCipherSuite();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public Certificate[] getLocalCertificates() {
        return this.delegate.getLocalCertificates();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
        return this.delegate.getServerCertificates();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        return this.delegate.getPeerPrincipal();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public Principal getLocalPrincipal() {
        return this.delegate.getLocalPrincipal();
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderFieldKey(int n) {
        return this.delegate.getHeaderFieldKey(n);
    }

    @Override // java.net.HttpURLConnection
    public void setFixedLengthStreamingMode(int contentLength) {
        this.delegate.setFixedLengthStreamingMode(contentLength);
    }

    @Override // java.net.URLConnection
    public void addRequestProperty(String key, String value) {
        this.delegate.addRequestProperty(key, value);
    }

    @Override // java.net.HttpURLConnection
    public void setFixedLengthStreamingMode(long contentLength) {
        this.delegate.setFixedLengthStreamingMode(contentLength);
    }

    @Override // java.net.HttpURLConnection
    public void setChunkedStreamingMode(int chunklen) {
        this.delegate.setChunkedStreamingMode(chunklen);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public String getHeaderField(int n) {
        return this.delegate.getHeaderField(n);
    }

    @Override // java.net.HttpURLConnection
    public void disconnect() {
        if (!this.readFromCache) {
            this.delegate.disconnect();
        }
    }

    @Override // java.net.URLConnection
    public boolean getAllowUserInteraction() {
        return this.delegate.getAllowUserInteraction();
    }

    @Override // java.net.URLConnection
    public int getConnectTimeout() {
        if (this.readFromCache) {
            return 10;
        }
        return this.delegate.getConnectTimeout();
    }

    @Override // java.net.URLConnection
    public Object getContent() throws IOException {
        return this.delegate.getContent();
    }

    @Override // java.net.URLConnection
    public Object getContent(Class[] classes) throws IOException {
        return this.delegate.getContent(classes);
    }

    @Override // java.net.URLConnection
    public String getContentEncoding() {
        if (!this.readFromCache) {
            this.cachedDataInfo.setContentEncoding(this.delegate.getContentEncoding());
        }
        return this.cachedDataInfo.getContentEncoding();
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        if (this.readFromCache) {
            return -1;
        }
        return this.delegate.getContentLength();
    }

    @Override // java.net.URLConnection
    public long getContentLengthLong() {
        if (this.readFromCache) {
            return -1L;
        }
        return this.delegate.getContentLengthLong();
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        if (!this.readFromCache) {
            this.cachedDataInfo.setContentType(this.delegate.getContentType());
        }
        return this.cachedDataInfo.getContentType();
    }

    @Override // java.net.URLConnection
    public long getDate() {
        if (this.readFromCache) {
            return 0L;
        }
        return this.delegate.getDate();
    }

    @Override // java.net.URLConnection
    public boolean getDefaultUseCaches() {
        return this.delegate.getDefaultUseCaches();
    }

    @Override // java.net.URLConnection
    public boolean getDoInput() {
        return this.delegate.getDoInput();
    }

    @Override // java.net.URLConnection
    public boolean getDoOutput() {
        return this.delegate.getDoOutput();
    }

    @Override // java.net.HttpURLConnection
    public InputStream getErrorStream() {
        return this.delegate.getErrorStream();
    }

    @Override // java.net.URLConnection
    public long getExpiration() {
        if (this.readFromCache) {
            return 0L;
        }
        return this.delegate.getExpiration();
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String name) {
        return this.delegate.getHeaderField(name);
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public long getHeaderFieldDate(String name, long Default) {
        return this.delegate.getHeaderFieldDate(name, Default);
    }

    @Override // java.net.URLConnection
    public int getHeaderFieldInt(String name, int Default) {
        return this.delegate.getHeaderFieldInt(name, Default);
    }

    @Override // java.net.URLConnection
    public long getHeaderFieldLong(String name, long Default) {
        return this.delegate.getHeaderFieldLong(name, Default);
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getHeaderFields() {
        if (!this.readFromCache) {
            setHeaderFieldsInCachedDataInfo();
        }
        return this.cachedDataInfo.getHeaderFields();
    }

    private void setHeaderFieldsInCachedDataInfo() {
        this.cachedDataInfo.setHeaderFields(this.delegate.getHeaderFields());
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public HostnameVerifier getHostnameVerifier() {
        return this.delegate.getHostnameVerifier();
    }

    @Override // java.net.URLConnection
    public long getIfModifiedSince() {
        return this.delegate.getIfModifiedSince();
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        if (null == this.inputStream) {
            if (this.readFromCache) {
                this.inputStream = new FileInputStream(this.cacheFile.toFile());
            } else {
                WriteCacheFileInputStream wis = new WriteCacheFileInputStream(this.delegate.getInputStream(), new FileOutputStream(this.cacheFile.toFile()));
                wis.onInputStreamClose(() -> {
                    try {
                        this.cachedDataInfo.setFromHttpUrlConnection(this.delegate);
                        int responseCode = this.delegate.getResponseCode();
                        if (responseCode == 200) {
                            this.cache.saveCachedDataInfo(this.cacheFile, this.cachedDataInfo);
                        } else if (logger.isTraceEnabled()) {
                            logger.warn("not caching because of response code " + responseCode + ": " + getURL());
                        }
                    } catch (IOException e) {
                        if (logger.isTraceEnabled()) {
                            logger.warn("cannot retrieve response code");
                        }
                    }
                });
                this.inputStream = wis;
            }
        }
        return this.inputStream;
    }

    @Override // java.net.HttpURLConnection
    public boolean getInstanceFollowRedirects() {
        return this.delegate.getInstanceFollowRedirects();
    }

    @Override // java.net.URLConnection
    public long getLastModified() {
        if (this.readFromCache) {
            return 0L;
        }
        return this.delegate.getLastModified();
    }

    @Override // java.net.URLConnection
    public OutputStream getOutputStream() throws IOException {
        return this.delegate.getOutputStream();
    }

    @Override // java.net.HttpURLConnection, java.net.URLConnection
    public Permission getPermission() throws IOException {
        return this.delegate.getPermission();
    }

    @Override // java.net.URLConnection
    public int getReadTimeout() {
        return this.delegate.getReadTimeout();
    }

    @Override // java.net.HttpURLConnection
    public String getRequestMethod() {
        return this.delegate.getRequestMethod();
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getRequestProperties() {
        return this.delegate.getRequestProperties();
    }

    @Override // java.net.URLConnection
    public String getRequestProperty(String key) {
        return this.delegate.getRequestProperty(key);
    }

    @Override // java.net.HttpURLConnection
    public int getResponseCode() throws IOException {
        return this.readFromCache ? HttpStatus.SC_OK : this.delegate.getResponseCode();
    }

    @Override // java.net.HttpURLConnection
    public String getResponseMessage() throws IOException {
        return this.readFromCache ? ExternallyRolledFileAppender.OK : this.delegate.getResponseMessage();
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public SSLSocketFactory getSSLSocketFactory() {
        return this.delegate.getSSLSocketFactory();
    }

    @Override // java.net.URLConnection
    public URL getURL() {
        return this.delegate.getURL();
    }

    @Override // java.net.URLConnection
    public boolean getUseCaches() {
        return this.delegate.getUseCaches();
    }

    @Override // java.net.URLConnection
    public void setAllowUserInteraction(boolean allowuserinteraction) {
        this.delegate.setAllowUserInteraction(allowuserinteraction);
    }

    @Override // java.net.URLConnection
    public void setConnectTimeout(int timeout) {
        this.delegate.setConnectTimeout(timeout);
    }

    @Override // java.net.URLConnection
    public void setDefaultUseCaches(boolean defaultusecaches) {
        this.delegate.setDefaultUseCaches(defaultusecaches);
    }

    @Override // java.net.URLConnection
    public void setDoInput(boolean doinput) {
        this.delegate.setDoInput(doinput);
    }

    @Override // java.net.URLConnection
    public void setDoOutput(boolean dooutput) {
        this.delegate.setDoOutput(dooutput);
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.delegate.setHostnameVerifier(hostnameVerifier);
    }

    @Override // java.net.URLConnection
    public void setIfModifiedSince(long ifmodifiedsince) {
        this.delegate.setIfModifiedSince(ifmodifiedsince);
    }

    @Override // java.net.HttpURLConnection
    public void setInstanceFollowRedirects(boolean followRedirects) {
        this.delegate.setInstanceFollowRedirects(followRedirects);
    }

    @Override // java.net.URLConnection
    public void setReadTimeout(int timeout) {
        this.delegate.setReadTimeout(timeout);
    }

    @Override // java.net.HttpURLConnection
    public void setRequestMethod(String method) throws ProtocolException {
        this.delegate.setRequestMethod(method);
    }

    @Override // java.net.URLConnection
    public void setRequestProperty(String key, String value) {
        this.delegate.setRequestProperty(key, value);
    }

    @Override // javax.net.ssl.HttpsURLConnection
    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.delegate.setSSLSocketFactory(sslSocketFactory);
    }

    @Override // java.net.URLConnection
    public void setUseCaches(boolean usecaches) {
        this.delegate.setUseCaches(usecaches);
    }

    @Override // java.net.HttpURLConnection
    public boolean usingProxy() {
        return this.delegate.usingProxy();
    }
}
