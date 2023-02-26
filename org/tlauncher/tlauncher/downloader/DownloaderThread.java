package org.tlauncher.tlauncher.downloader;

import ch.qos.logback.core.CoreConstants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.compress.harmony.unpack200.IcTuple;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.async.ExtendedThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/DownloaderThread.class */
public class DownloaderThread extends ExtendedThread {
    private static final String ITERATION_BLOCK = "iteration";
    private final int ID;
    private final String LOGGER_PREFIX;
    private final Downloader downloader;
    private final List<Downloadable> list;
    private Downloadable current;
    private boolean launched;
    private boolean initDownloadedLink;
    private String incorrectUrl;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DownloaderThread(Downloader d, int id) {
        super("DT#" + id);
        this.incorrectUrl = CoreConstants.EMPTY_STRING;
        this.ID = id;
        this.LOGGER_PREFIX = "[D#" + id + "]";
        this.downloader = d;
        this.list = new ArrayList();
        startAndWait();
    }

    int getID() {
        return this.ID;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(Downloadable d) {
        this.list.add(d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startDownload() {
        this.launched = true;
        unlockThread(ITERATION_BLOCK);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopDownload() {
        this.launched = false;
    }

    @Override // org.tlauncher.util.async.ExtendedThread, java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            this.launched = true;
            Iterator<Downloadable> it = this.list.iterator();
            while (true) {
                if (it.hasNext()) {
                    Downloadable d = it.next();
                    this.current = d;
                    onStart();
                    int attempt = 0;
                    Throwable error = null;
                    this.initDownloadedLink = false;
                    while (true) {
                        int i = attempt;
                        int max = this.downloader.getConfiguration().getTries(d.isFast());
                        if (i >= max) {
                            break;
                        }
                        attempt++;
                        int timeout = attempt * this.downloader.getConfiguration().getTimeout();
                        try {
                            download(timeout);
                            break;
                        } catch (AbortedDownloadException e) {
                            dlog("This download process has been aborted.");
                            error = e;
                        } catch (GaveUpDownloadException e2) {
                            dlog("File is not reachable at all.");
                            error = e2;
                            if (attempt >= max) {
                                FileUtil.deleteFile(d.getMetadataDTO().getLocalDestination());
                                for (File file : d.getAdditionalDestinations()) {
                                    FileUtil.deleteFile(file);
                                }
                                dlog("Gave up trying to download this file.", error);
                                onError(error);
                            }
                        }
                    }
                    if (error instanceof AbortedDownloadException) {
                        tlog("Thread is aborting...");
                        for (Downloadable downloadable : this.list) {
                            downloadable.onAbort((AbortedDownloadException) error);
                        }
                    }
                }
            }
            this.list.clear();
            lockThread(ITERATION_BLOCK);
            this.launched = false;
        }
    }

    private void download(int timeout) throws GaveUpDownloadException, AbortedDownloadException {
        boolean hasRepo = this.current.hasRepository();
        int max = hasRepo ? this.current.getRepository().getCount() : 1;
        IOException cause = null;
        for (int attempt = 0; attempt < max; attempt++) {
            String url = CoreConstants.EMPTY_STRING;
            try {
                if (hasRepo) {
                    Repo r = this.current.getRepository();
                    if (r.equals(ClientInstanceRepo.EMPTY_REPO) && attempt == 1) {
                        url = this.current.getRepository().getRepo(attempt) + URLEncoder.encode(this.current.getMetadataDTO().getUrl(), StandardCharsets.UTF_8.name());
                    } else {
                        url = this.current.getRepository().getRepo(attempt) + this.current.getMetadataDTO().getUrl();
                    }
                } else {
                    url = this.current.getMetadataDTO().getUrl();
                }
                dlog(url);
                downloadURL(url, timeout);
                return;
            } catch (IOException e) {
                dlog("Failed to download from: ", url, e);
                if (!this.initDownloadedLink) {
                    this.incorrectUrl = url;
                    this.initDownloadedLink = true;
                }
                cause = e;
            }
        }
        throw new GaveUpDownloadException(this.incorrectUrl + " -> " + this.current.getMetadataDTO().getLocalDestination(), cause);
    }

    private void downloadURL(String url, int timeout) throws IOException, AbortedDownloadException {
        if (!this.launched) {
            throw new AbortedDownloadException();
        }
        InputStream in = null;
        OutputStream out = null;
        File file = this.current.getMetadataDTO().getLocalDestination();
        File temp = FileUtil.makeTemp(new File(file.getAbsolutePath() + ".tlauncherdownload"));
        HttpGet g = new HttpGet(url);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
            g.setConfig(requestConfig);
            CloseableHttpResponse r = this.downloader.getClient().execute((HttpUriRequest) g);
            HttpEntity entity = r.getEntity();
            if (r.getStatusLine().getStatusCode() != 200) {
                EntityUtils.consume(entity);
                throw new IOException(String.valueOf(r.getStatusLine().getStatusCode()));
            }
            in = new BufferedInputStream(entity.getContent());
            if (this.current.getMetadataDTO().getSize() == 0) {
                this.current.getMetadataDTO().setSize(entity.getContentLength());
            }
            this.current.setAlreadyDownloaded(0L);
            out = new BufferedOutputStream(new FileOutputStream(temp));
            long downloaded_s = System.currentTimeMillis();
            long speed_s = downloaded_s;
            byte[] buffer = new byte[IcTuple.NESTED_CLASS_FLAG];
            int curread = in.read(buffer);
            while (curread != -1) {
                if (!this.launched) {
                    throw new AbortedDownloadException();
                }
                out.write(buffer, 0, curread);
                curread = in.read(buffer);
                if (curread == -1) {
                    break;
                }
                this.current.setAlreadyDownloaded(this.current.getAlreadyDownloaded() + curread);
                long speed_e = System.currentTimeMillis() - speed_s;
                if (speed_e >= 300) {
                    speed_s = System.currentTimeMillis();
                    this.downloader.onProgress();
                }
            }
            Files.deleteIfExists(file.toPath());
            Files.move(temp.toPath(), file.toPath(), new CopyOption[0]);
            List<File> copies = this.current.getAdditionalDestinations();
            if (copies.size() > 0) {
                dlog("Found additional destinations. Copying...");
                for (File copy : copies) {
                    dlog("Copying " + copy + "...");
                    FileUtil.copyFile(file, copy, this.current.isForce());
                    dlog("Success!");
                }
                dlog("Copying completed.");
            }
            onComplete();
        } finally {
            g.abort();
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    private void onStart() {
        this.current.onStart();
    }

    private void onError(Throwable e) {
        this.current.onError(e);
        this.downloader.onFileComplete(this.current);
    }

    private void onComplete() throws RetryDownloadException {
        this.current.onComplete();
        this.downloader.onProgress();
        this.downloader.onFileComplete(this.current);
    }

    private void tlog(Object... o) {
        U.plog(this.LOGGER_PREFIX, o);
    }

    private void dlog(String message, String url, Throwable ex) {
        if (ex == null) {
            U.plog(this.LOGGER_PREFIX, "> ", message, url);
        } else {
            U.plog(this.LOGGER_PREFIX, "> ", message, url, ex);
        }
    }

    private void dlog(String message) {
        dlog(message, CoreConstants.EMPTY_STRING, null);
    }

    private void dlog(String message, Throwable ex) {
        dlog(message, CoreConstants.EMPTY_STRING, ex);
    }
}
