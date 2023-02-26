package org.tlauncher.tlauncher.downloader;

import com.sothawo.mapjfx.cache.CachingURLStreamHandlerFactory;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.SSLContext;
import org.apache.http.HeaderElementIterator;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.Args;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.util.U;
import org.tlauncher.util.async.ExtendedThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/Downloader.class */
public class Downloader extends ExtendedThread {
    public static final int MAX_THREADS = 3;
    private static final String DOWNLOAD_BLOCK = "download";
    static final String ITERATION_BLOCK = "iteration";
    private final DownloaderThread[] threads;
    private final List<Downloadable> list;
    private final List<DownloaderListener> listeners;
    private final List<DownloadableContainer> downloadableContainers;
    private final AtomicInteger remainingObjects;
    private final Object workLock;
    private ConnectionQuality configuration;
    private int runningThreads;
    private int workingThreads;
    private final CloseableHttpClient client;
    private boolean aborted;
    private boolean haveWork;
    private double averageProgress;
    private double remainedData;
    private long firstVisitTime;
    private double speed;

    public Downloader(ConnectionQuality configuration) {
        super("MD");
        this.remainingObjects = new AtomicInteger();
        setConfiguration(configuration);
        this.threads = new DownloaderThread[3];
        this.list = Collections.synchronizedList(new ArrayList());
        this.listeners = Collections.synchronizedList(new ArrayList());
        this.downloadableContainers = new ArrayList();
        this.workLock = new Object();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(5);
        cm.setMaxTotal(20);
        HttpClientBuilder builder = HttpClients.custom().setKeepAliveStrategy(response, context -> {
            Args.notNull(response, "HTTP response");
            HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            if (it.hasNext()) {
                log("used keep alive 5000");
                return 5000L;
            }
            return -1L;
        }).setConnectionManager(cm).evictIdleConnections(10L, TimeUnit.SECONDS);
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial((KeyStore) null, x509CertChain, authType -> {
                return true;
            }).build();
            builder.setSSLContext(sslContext).setConnectionManager(new PoolingHttpClientConnectionManager(RegistryBuilder.create().register("http", PlainConnectionSocketFactory.INSTANCE).register(CachingURLStreamHandlerFactory.PROTO_HTTPS, new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)).build()));
        } catch (Exception e) {
            U.log(e);
        }
        this.client = builder.build();
        startAndWait();
    }

    private static void log(Object... o) {
        U.log("[Downloader2]", o);
    }

    public ConnectionQuality getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(ConnectionQuality configuration) {
        if (configuration == null) {
            throw new NullPointerException();
        }
        log("Loaded configuration:", configuration);
        this.configuration = configuration;
    }

    public int getRemaining() {
        return this.remainingObjects.get();
    }

    public double getProgress() {
        return this.averageProgress;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void add(Downloadable d) {
        if (d == null) {
            throw new NullPointerException();
        }
        this.list.add(d);
    }

    public void add(DownloadableContainer c) {
        if (c == null) {
            throw new NullPointerException();
        }
        this.downloadableContainers.add(c);
        this.list.addAll(c.list);
    }

    public List<DownloadableContainer> getDownloadableContainers() {
        return this.downloadableContainers;
    }

    public void addAll(Downloadable... ds) {
        if (ds == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < ds.length; i++) {
            if (ds[i] == null) {
                throw new NullPointerException("Downloadable at " + i + " is NULL!");
            }
            this.list.add(ds[i]);
        }
    }

    public void addAll(Collection<Downloadable> coll) {
        if (coll == null) {
            throw new NullPointerException();
        }
        int i = -1;
        for (Downloadable d : coll) {
            i++;
            if (d == null) {
                throw new NullPointerException("Downloadable at" + i + " is NULL!");
            }
            this.list.add(d);
        }
    }

    public void addListener(DownloaderListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        this.listeners.add(listener);
    }

    public boolean startDownload() {
        boolean haveWork = !this.list.isEmpty();
        if (haveWork) {
            unlockThread(ITERATION_BLOCK);
        }
        return haveWork;
    }

    public void startDownloadAndWait() {
        if (startDownload()) {
            waitWork();
        }
    }

    private void waitWork() {
        this.haveWork = true;
        while (this.haveWork) {
            synchronized (this.workLock) {
                try {
                    this.workLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyWork() {
        this.haveWork = false;
        synchronized (this.workLock) {
            this.workLock.notifyAll();
        }
    }

    public void stopDownload() {
        if (!isThreadLocked()) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!isThreadLocked()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < this.runningThreads; i++) {
            this.threads[i].stopDownload();
        }
        this.aborted = true;
        if (isThreadLocked()) {
            tryUnlock("download");
        }
    }

    public void stopDownloadAndWait() {
        stopDownload();
        waitForThreads();
    }

    @Override // org.tlauncher.util.async.ExtendedThread, java.lang.Thread, java.lang.Runnable
    public void run() {
        checkCurrent();
        while (true) {
            lockThread(ITERATION_BLOCK);
            log("Files in queue", Integer.valueOf(this.list.size()));
            synchronized (this.list) {
                sortOut();
            }
            for (int i = 0; i < this.runningThreads; i++) {
                this.threads[i].startDownload();
            }
            lockThread("download");
            if (this.aborted) {
                waitForThreads();
                onAbort();
                this.aborted = false;
            }
            notifyWork();
            this.averageProgress = 0.0d;
            this.workingThreads = 0;
            this.remainingObjects.set(0);
            this.list.clear();
            this.downloadableContainers.clear();
        }
    }

    private void sortOut() {
        int size = this.list.size();
        if (size == 0) {
            return;
        }
        int downloadablesAtThread = U.getMaxMultiply(size, 3);
        int x = 0;
        log("Starting download " + size + " files...");
        onStart(size);
        int max = this.configuration.getMaxThreads();
        boolean[] workers = new boolean[max];
        this.firstVisitTime = System.currentTimeMillis();
        while (size > 0) {
            for (int i = 0; i < max; i++) {
                workers[i] = true;
                size -= downloadablesAtThread;
                if (this.threads[i] == null) {
                    int i2 = this.runningThreads + 1;
                    this.runningThreads = i2;
                    this.threads[i] = new DownloaderThread(this, i2);
                }
                int y = x;
                while (y < x + downloadablesAtThread) {
                    this.threads[i].add(this.list.get(y));
                    y++;
                }
                x = y;
                if (size == 0) {
                    break;
                }
            }
            downloadablesAtThread = U.getMaxMultiply(size, 3);
        }
        for (boolean worker : workers) {
            if (worker) {
                this.workingThreads++;
            }
        }
    }

    private void onStart(int size) {
        for (DownloaderListener listener : this.listeners) {
            listener.onDownloaderStart(this, size);
        }
        this.remainingObjects.addAndGet(size);
    }

    private void onAbort() {
        for (DownloaderListener listener : this.listeners) {
            listener.onDownloaderAbort(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onProgress() {
        long downloaded = 0;
        long totalFilesSize = 0;
        this.speed = 0.0d;
        for (Downloadable downloadable : this.list) {
            totalFilesSize += downloadable.getMetadataDTO().getSize();
            downloaded += downloadable.getAlreadyDownloaded();
        }
        long currentTime = System.currentTimeMillis() - this.firstVisitTime;
        this.speed = (downloaded / (1048576.0d * currentTime)) * 1000.0d;
        this.remainedData = (totalFilesSize - downloaded) / 1048576.0d;
        if (totalFilesSize != 0) {
            this.averageProgress = (downloaded / totalFilesSize) * 100.0d;
        }
        for (DownloaderListener listener : this.listeners) {
            listener.onDownloaderProgress(this, this.averageProgress, this.speed, this.remainedData);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFileComplete(Downloadable file) {
        int remaining = this.remainingObjects.decrementAndGet();
        for (DownloaderListener listener : this.listeners) {
            listener.onDownloaderFileComplete(this, file);
        }
        if (remaining < 1) {
            onComplete();
        }
    }

    private void onComplete() {
        for (DownloaderListener listener : this.listeners) {
            listener.onDownloaderComplete(this);
        }
        unlockThread("download");
    }

    private void waitForThreads() {
        boolean blocked;
        log("Waiting for", Integer.valueOf(this.workingThreads), "threads...");
        do {
            blocked = true;
            for (int i = 0; i < this.workingThreads; i++) {
                if (!this.threads[i].isThreadLocked()) {
                    blocked = false;
                }
            }
        } while (!blocked);
        log("All threads are blocked by now");
    }

    public CloseableHttpClient getClient() {
        return this.client;
    }

    public double getRemainedData() {
        return this.remainedData;
    }
}
