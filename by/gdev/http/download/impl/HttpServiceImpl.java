package by.gdev.http.download.impl;

import by.gdev.http.download.model.Headers;
import by.gdev.http.download.model.RequestMetadata;
import by.gdev.http.download.service.HttpService;
import ch.qos.logback.core.CoreConstants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.compress.harmony.unpack200.IcTuple;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/impl/HttpServiceImpl.class */
public class HttpServiceImpl implements HttpService {
    private String proxy;
    private CloseableHttpClient httpclient;
    private RequestConfig requestConfig;
    private int maxAttepmts;

    public HttpServiceImpl(String proxy, CloseableHttpClient httpclient, RequestConfig requestConfig, int maxAttepmts) {
        this.proxy = proxy;
        this.httpclient = httpclient;
        this.requestConfig = requestConfig;
        this.maxAttepmts = maxAttepmts;
    }

    @Override // by.gdev.http.download.service.HttpService
    public RequestMetadata getRequestByUrlAndSave(String url, Path path) throws IOException {
        RequestMetadata request = null;
        int attepmts = 0;
        while (attepmts < this.maxAttepmts) {
            try {
                request = getResourseByUrl(url, path);
                break;
            } catch (SocketTimeoutException e) {
                attepmts++;
                if (attepmts != this.maxAttepmts) {
                    attepmts++;
                } else {
                    throw new SocketTimeoutException();
                }
            } catch (IOException e2) {
                if (Objects.nonNull(this.proxy)) {
                    request = getResourseByUrl(this.proxy + url, path);
                    attepmts++;
                } else {
                    throw e2;
                }
            }
        }
        return request;
    }

    @Override // by.gdev.http.download.service.HttpService
    public RequestMetadata getMetaByUrl(String url) throws IOException {
        RequestMetadata request = null;
        int attepmts = 0;
        while (attepmts < this.maxAttepmts) {
            try {
                request = getMetadata(url);
                break;
            } catch (SocketTimeoutException e) {
                int attepmts2 = attepmts + 1;
                if (attepmts2 != this.maxAttepmts) {
                    attepmts = attepmts2 + 1;
                } else {
                    throw new SocketTimeoutException();
                }
            }
        }
        return request;
    }

    @Override // by.gdev.http.download.service.HttpService
    public String getRequestByUrl(String url) throws IOException {
        return getRequestByUrl(url, null);
    }

    private String getStringByUrl(String url, Map<String, String> headers) throws IOException {
        HttpGet httpGet = null;
        try {
            HttpGet httpGet2 = new HttpGet(url);
            if (Objects.nonNull(headers)) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    httpGet2.addHeader(e.getKey(), e.getValue());
                }
            }
            CloseableHttpResponse response = getResponse(httpGet2);
            StatusLine st = response.getStatusLine();
            if (200 == response.getStatusLine().getStatusCode()) {
                InputStream in = response.getEntity().getContent();
                String iOUtils = IOUtils.toString(in, StandardCharsets.UTF_8);
                httpGet2.abort();
                IOUtils.closeQuietly(in);
                return iOUtils;
            }
            throw new IOException(String.format("code %s phrase %s %s", Integer.valueOf(st.getStatusCode()), st.getReasonPhrase(), url));
        } catch (Throwable th) {
            httpGet.abort();
            IOUtils.closeQuietly((InputStream) null);
            throw th;
        }
    }

    private RequestMetadata getMetadata(String url) throws IOException {
        RequestMetadata request = new RequestMetadata();
        HttpHead httpUrl = new HttpHead(url);
        CloseableHttpResponse response = getResponse(httpUrl);
        if (response.containsHeader(Headers.ETAG.getValue())) {
            request.setETag(response.getFirstHeader(Headers.ETAG.getValue()).getValue().replaceAll("\"", CoreConstants.EMPTY_STRING));
        } else {
            request.setETag(null);
        }
        if (response.containsHeader(Headers.LASTMODIFIED.getValue())) {
            request.setLastModified(response.getFirstHeader(Headers.LASTMODIFIED.getValue()).getValue().replaceAll("\"", CoreConstants.EMPTY_STRING));
        } else {
            request.setLastModified(null);
        }
        if (response.containsHeader(Headers.CONTENTLENGTH.getValue())) {
            request.setContentLength(response.getFirstHeader(Headers.CONTENTLENGTH.getValue()).getValue());
        } else {
            request.setContentLength(null);
        }
        return request;
    }

    private RequestMetadata getResourseByUrl(String url, Path path) throws IOException, SocketTimeoutException {
        HttpGet httpGet = new HttpGet(url);
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        if (!path.toFile().getParentFile().exists()) {
            path.toFile().getParentFile().mkdirs();
        }
        Path temp = Paths.get(path.toAbsolutePath().toString() + ".temp", new String[0]);
        try {
            CloseableHttpResponse response = getResponse(httpGet);
            StatusLine st = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (200 != response.getStatusLine().getStatusCode()) {
                throw new IOException(String.format("code %s phrase %s", Integer.valueOf(st.getStatusCode()), st.getReasonPhrase()));
            }
            in = new BufferedInputStream(entity.getContent());
            out = new BufferedOutputStream(new FileOutputStream(temp.toFile()));
            byte[] buffer = new byte[IcTuple.NESTED_CLASS_FLAG];
            for (int curread = in.read(buffer); curread != -1; curread = in.read(buffer)) {
                out.write(buffer, 0, curread);
            }
            Files.move(Paths.get(temp.toString(), new String[0]), path.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            return getMetadata(url);
        } finally {
            httpGet.abort();
            IOUtils.closeQuietly((InputStream) in);
            IOUtils.closeQuietly((OutputStream) out);
        }
    }

    private CloseableHttpResponse getResponse(HttpRequestBase http) throws IOException {
        http.setConfig(this.requestConfig);
        return this.httpclient.execute((HttpUriRequest) http);
    }

    @Override // by.gdev.http.download.service.HttpService
    public String getRequestByUrl(String url, Map<String, String> map) throws IOException {
        SocketTimeoutException ste = null;
        for (int attepmts = 0; attepmts < this.maxAttepmts; attepmts++) {
            try {
                return getStringByUrl(url, map);
            } catch (SocketTimeoutException e) {
                ste = e;
            }
        }
        throw ste;
    }
}
