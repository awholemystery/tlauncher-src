package by.gdev.http.download.impl;

import by.gdev.http.download.exeption.UploadFileException;
import by.gdev.http.upload.download.downloader.DownloadElement;
import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
import com.google.common.eventbus.EventBus;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/impl/DownloadRunnableImpl.class */
public class DownloadRunnableImpl implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(DownloadRunnableImpl.class);
    private volatile DownloaderStatusEnum status;
    private Queue<DownloadElement> downloadElements;
    private List<DownloadElement> processedElements;
    private CloseableHttpClient httpclient;
    private RequestConfig requestConfig;
    private EventBus eventBus;
    private int DEFAULT_MAX_ATTEMPTS = 3;

    public void setStatus(DownloaderStatusEnum status) {
        this.status = status;
    }

    public void setDownloadElements(Queue<DownloadElement> downloadElements) {
        this.downloadElements = downloadElements;
    }

    public void setProcessedElements(List<DownloadElement> processedElements) {
        this.processedElements = processedElements;
    }

    public void setHttpclient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setDEFAULT_MAX_ATTEMPTS(int DEFAULT_MAX_ATTEMPTS) {
        this.DEFAULT_MAX_ATTEMPTS = DEFAULT_MAX_ATTEMPTS;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DownloadRunnableImpl) {
            DownloadRunnableImpl other = (DownloadRunnableImpl) o;
            if (other.canEqual(this)) {
                Object this$status = getStatus();
                Object other$status = other.getStatus();
                if (this$status == null) {
                    if (other$status != null) {
                        return false;
                    }
                } else if (!this$status.equals(other$status)) {
                    return false;
                }
                Object this$downloadElements = getDownloadElements();
                Object other$downloadElements = other.getDownloadElements();
                if (this$downloadElements == null) {
                    if (other$downloadElements != null) {
                        return false;
                    }
                } else if (!this$downloadElements.equals(other$downloadElements)) {
                    return false;
                }
                Object this$processedElements = getProcessedElements();
                Object other$processedElements = other.getProcessedElements();
                if (this$processedElements == null) {
                    if (other$processedElements != null) {
                        return false;
                    }
                } else if (!this$processedElements.equals(other$processedElements)) {
                    return false;
                }
                Object this$httpclient = getHttpclient();
                Object other$httpclient = other.getHttpclient();
                if (this$httpclient == null) {
                    if (other$httpclient != null) {
                        return false;
                    }
                } else if (!this$httpclient.equals(other$httpclient)) {
                    return false;
                }
                Object this$requestConfig = getRequestConfig();
                Object other$requestConfig = other.getRequestConfig();
                if (this$requestConfig == null) {
                    if (other$requestConfig != null) {
                        return false;
                    }
                } else if (!this$requestConfig.equals(other$requestConfig)) {
                    return false;
                }
                Object this$eventBus = getEventBus();
                Object other$eventBus = other.getEventBus();
                if (this$eventBus == null) {
                    if (other$eventBus != null) {
                        return false;
                    }
                } else if (!this$eventBus.equals(other$eventBus)) {
                    return false;
                }
                return getDEFAULT_MAX_ATTEMPTS() == other.getDEFAULT_MAX_ATTEMPTS();
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloadRunnableImpl;
    }

    public int hashCode() {
        Object $status = getStatus();
        int result = (1 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $downloadElements = getDownloadElements();
        int result2 = (result * 59) + ($downloadElements == null ? 43 : $downloadElements.hashCode());
        Object $processedElements = getProcessedElements();
        int result3 = (result2 * 59) + ($processedElements == null ? 43 : $processedElements.hashCode());
        Object $httpclient = getHttpclient();
        int result4 = (result3 * 59) + ($httpclient == null ? 43 : $httpclient.hashCode());
        Object $requestConfig = getRequestConfig();
        int result5 = (result4 * 59) + ($requestConfig == null ? 43 : $requestConfig.hashCode());
        Object $eventBus = getEventBus();
        return (((result5 * 59) + ($eventBus == null ? 43 : $eventBus.hashCode())) * 59) + getDEFAULT_MAX_ATTEMPTS();
    }

    public String toString() {
        return "DownloadRunnableImpl(status=" + getStatus() + ", downloadElements=" + getDownloadElements() + ", processedElements=" + getProcessedElements() + ", httpclient=" + getHttpclient() + ", requestConfig=" + getRequestConfig() + ", eventBus=" + getEventBus() + ", DEFAULT_MAX_ATTEMPTS=" + getDEFAULT_MAX_ATTEMPTS() + ")";
    }

    public DownloaderStatusEnum getStatus() {
        return this.status;
    }

    public Queue<DownloadElement> getDownloadElements() {
        return this.downloadElements;
    }

    public List<DownloadElement> getProcessedElements() {
        return this.processedElements;
    }

    public CloseableHttpClient getHttpclient() {
        return this.httpclient;
    }

    public RequestConfig getRequestConfig() {
        return this.requestConfig;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public int getDEFAULT_MAX_ATTEMPTS() {
        return this.DEFAULT_MAX_ATTEMPTS;
    }

    public DownloadRunnableImpl(Queue<DownloadElement> downloadElements, List<DownloadElement> processedElements, CloseableHttpClient httpclient, RequestConfig requestConfig, EventBus eventBus) {
        this.downloadElements = downloadElements;
        this.processedElements = processedElements;
        this.httpclient = httpclient;
        this.requestConfig = requestConfig;
        this.eventBus = eventBus;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (this.status.equals(DownloaderStatusEnum.WORK) && !this.status.equals(DownloaderStatusEnum.CANCEL)) {
            DownloadElement element = this.downloadElements.poll();
            if (Objects.nonNull(element)) {
                try {
                    download(element);
                    element.getHandlers().forEach(post -> {
                        post.postProcessDownloadElement(element);
                    });
                } catch (Throwable e1) {
                    element.setError(new UploadFileException(element.getRepo().getRepositories().get(0) + element.getMetadata().getRelativeUrl(), element.getMetadata().getPath(), e1.getLocalizedMessage()));
                }
            } else {
                return;
            }
        }
    }

    private void download(DownloadElement element) throws IOException, InterruptedException {
        this.processedElements.add(element);
        File file = new File(element.getPathToDownload() + element.getMetadata().getPath());
        for (int attempt = 0; attempt < this.DEFAULT_MAX_ATTEMPTS; attempt++) {
            try {
                boolean resume = false;
                HttpGet httpGet = new HttpGet(element.getRepo().getRepositories().get(0) + element.getMetadata().getRelativeUrl());
                log.trace(String.valueOf(httpGet));
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists() && Objects.nonNull(element.getMetadata().getSha1()) && file.length() != element.getMetadata().getSize()) {
                    httpGet.addHeader(HttpHeaders.RANGE, "bytes= " + file.length() + "-" + element.getMetadata().getSize());
                    resume = true;
                }
                httpGet.setConfig(this.requestConfig);
                CloseableHttpResponse response = this.httpclient.execute((HttpUriRequest) httpGet);
                HttpEntity entity = response.getEntity();
                BufferedInputStream in = new BufferedInputStream(entity.getContent());
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file, resume));
                byte[] buffer = new byte[1024];
                int curread = in.read(buffer);
                while (curread != -1 && !this.status.equals(DownloaderStatusEnum.CANCEL)) {
                    out.write(buffer, 0, curread);
                    curread = in.read(buffer);
                    element.setDownloadBytes(element.getDownloadBytes() + curread);
                }
                this.eventBus.post(element);
                LocalTime endTime = LocalTime.now();
                element.setEnd(endTime);
                this.DEFAULT_MAX_ATTEMPTS = 1;
                httpGet.abort();
                IOUtils.close(out);
                IOUtils.close(in);
            } catch (SocketTimeoutException e) {
                if (attempt == this.DEFAULT_MAX_ATTEMPTS) {
                    throw new SocketTimeoutException();
                }
            }
        }
    }
}
