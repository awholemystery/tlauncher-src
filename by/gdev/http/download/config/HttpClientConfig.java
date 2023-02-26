package by.gdev.http.download.config;

import java.util.concurrent.TimeUnit;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/config/HttpClientConfig.class */
public class HttpClientConfig {
    public static CloseableHttpClient getInstanceHttpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(5);
        cm.setMaxTotal(20);
        CloseableHttpClient builder = HttpClients.custom().setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE).setConnectionManager(cm).evictIdleConnections(10L, TimeUnit.SECONDS).build();
        return builder;
    }
}
