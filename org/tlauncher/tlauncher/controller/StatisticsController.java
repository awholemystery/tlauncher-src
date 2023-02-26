package org.tlauncher.tlauncher.controller;

import com.google.inject.Inject;
import java.io.IOException;
import java.util.Objects;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/controller/StatisticsController.class */
public class StatisticsController {
    @Inject
    private CloseableHttpClient closeableHttpClient;
    @Inject
    private RequestConfig config;

    public void removeUserData() throws ClientProtocolException, IOException {
        HttpDelete http = new HttpDelete(TLauncher.getInnerSettings().get("statistics.url") + String.format("user/client/%s", TLauncher.getInstance().getConfiguration().getClient().toString()));
        http.setConfig(this.config);
        HttpResponse hr = null;
        try {
            hr = this.closeableHttpClient.execute((HttpUriRequest) http);
            if (hr.getStatusLine().getStatusCode() >= 300) {
                throw new IOException("not proper code " + hr.getStatusLine().toString());
            } else if (Objects.nonNull(null)) {
                EntityUtils.consumeQuietly(hr.getEntity());
            }
        } catch (Throwable th) {
            if (Objects.nonNull(null)) {
                EntityUtils.consumeQuietly(hr.getEntity());
            }
            throw th;
        }
    }
}
