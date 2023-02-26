package org.tlauncher.tlauncher.downloader;

import ch.qos.logback.core.CoreConstants;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.http.HttpHeaders;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/Downloadable.class */
public class Downloadable {
    private static final boolean DEFAULT_FORCE = false;
    private static final boolean DEFAULT_FAST = false;
    private final List<File> additionalDestinations;
    private final List<DownloadableHandler> handlers;
    private Repo repo;
    private MetadataDTO metadataDTO;
    private boolean forceDownload;
    private boolean fastDownload;
    private boolean locked;
    private DownloadableContainer container;
    private long alreadyDownloaded;
    private Throwable error;

    public Downloadable(Repo repo, MetadataDTO metadataDTO, boolean forceDownload, boolean fastDownload) {
        this();
        String unb = TLauncher.getInnerSettings().getArray("file.server")[1];
        if (metadataDTO.getUrl().startsWith(unb)) {
            this.metadataDTO = new MetadataDTO();
            this.metadataDTO.setSha1(metadataDTO.getSha1());
            this.metadataDTO.setSize(metadataDTO.getSize());
            this.metadataDTO.setLocalDestination(metadataDTO.getLocalDestination());
            this.metadataDTO.setPath(metadataDTO.getPath());
            this.metadataDTO.setUrl(metadataDTO.getUrl().replace(unb, CoreConstants.EMPTY_STRING));
            setRepo(ClientInstanceRepo.createModpackRepo());
        } else {
            this.metadataDTO = metadataDTO;
            setRepo(repo);
        }
        this.forceDownload = forceDownload;
        this.fastDownload = fastDownload;
    }

    public Downloadable(Repo repo, MetadataDTO metadataDTO, boolean forceDownload) {
        this(repo, metadataDTO, forceDownload, false);
    }

    public Downloadable(Repo repo, MetadataDTO metadataDTO) {
        this(repo, metadataDTO, false, false);
    }

    private Downloadable() {
        this.additionalDestinations = Collections.synchronizedList(new ArrayList());
        this.handlers = Collections.synchronizedList(new ArrayList());
    }

    public void setRepo(Repo repo) {
        if (repo == null) {
            throw new NullPointerException("Repository is NULL!");
        }
        checkLocked();
        this.repo = repo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getAlreadyDownloaded() {
        return this.alreadyDownloaded;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAlreadyDownloaded(long alreadyDownloaded) {
        this.alreadyDownloaded = alreadyDownloaded;
    }

    public boolean isForce() {
        return this.forceDownload;
    }

    public void setForce(boolean force) {
        checkLocked();
        this.forceDownload = force;
    }

    public boolean isFast() {
        return this.fastDownload;
    }

    public void setFast(boolean fast) {
        checkLocked();
        this.fastDownload = fast;
    }

    public Repo getRepository() {
        return this.repo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasRepository() {
        return this.repo != null;
    }

    public String getFilename() {
        return FileUtil.getFilename(this.metadataDTO.getPath());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<File> getAdditionalDestinations() {
        return Collections.unmodifiableList(this.additionalDestinations);
    }

    public void addAdditionalDestination(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        checkLocked();
        this.additionalDestinations.add(file);
    }

    public void addHandler(DownloadableHandler handler) {
        if (handler == null) {
            throw new NullPointerException();
        }
        checkLocked();
        this.handlers.add(handler);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setContainer(DownloadableContainer container) {
        checkLocked();
        this.container = container;
    }

    public Throwable getError() {
        return this.error;
    }

    private void setLocked(boolean locked) {
        this.locked = locked;
    }

    private void checkLocked() {
        if (this.locked) {
            throw new IllegalStateException("Downloadable is locked!");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onStart() {
        setLocked(true);
        for (DownloadableHandler handler : this.handlers) {
            handler.onStart(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAbort(AbortedDownloadException ae) {
        setLocked(false);
        this.error = ae;
        for (DownloadableHandler handler : this.handlers) {
            handler.onAbort(this);
        }
        if (this.container != null) {
            this.container.onAbort(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onComplete() throws RetryDownloadException {
        setLocked(false);
        for (DownloadableHandler handler : this.handlers) {
            handler.onComplete(this);
        }
        if (this.container != null) {
            this.container.onComplete(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onError(Throwable e) {
        this.error = e;
        if (e == null) {
            return;
        }
        setLocked(false);
        for (DownloadableHandler handler : this.handlers) {
            handler.onError(this, e);
        }
        if (this.container != null) {
            this.container.onError(this, e);
        }
    }

    public String toString() {
        return getClass().getSimpleName() + "{path='" + this.metadataDTO.getPath() + "'; repo=" + this.repo + "; destinations=" + this.metadataDTO.getLocalDestination() + "," + this.additionalDestinations + "; force=" + this.forceDownload + "; fast=" + this.fastDownload + "; locked=" + this.locked + "; container=" + this.container + "; handlers=" + this.handlers + "; error=" + this.error + ";}";
    }

    public static HttpURLConnection setUp(URLConnection connection0, int timeout, boolean fake) {
        String userAgent;
        if (connection0 == null) {
            throw new NullPointerException();
        }
        if (!(connection0 instanceof HttpURLConnection)) {
            throw new IllegalArgumentException("Unknown connection protocol: " + connection0);
        }
        HttpURLConnection connection = (HttpURLConnection) connection0;
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setInstanceFollowRedirects(true);
        connection.setUseCaches(false);
        connection.setDefaultUseCaches(false);
        connection.setRequestProperty(HttpHeaders.CACHE_CONTROL, "no-store,max-age=0,no-cache");
        connection.setRequestProperty(HttpHeaders.EXPIRES, "0");
        connection.setRequestProperty(HttpHeaders.PRAGMA, "no-cache");
        if (!fake) {
            return connection;
        }
        switch (OS.CURRENT) {
            case OSX:
                userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8) AppleWebKit/535.18.5 (KHTML, like Gecko) Version/5.2 Safari/535.18.5";
                break;
            case WINDOWS:
                userAgent = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0; .NET4.0C)";
                break;
            default:
                userAgent = "Mozilla/5.0 (Linux; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0";
                break;
        }
        connection.setRequestProperty("User-Agent", userAgent);
        return connection;
    }

    public static HttpURLConnection setUp(URLConnection connection, boolean fake) {
        return setUp(connection, U.getConnectionTimeout(), fake);
    }

    public static String getEtag(String etag) {
        if (etag == null) {
            return "-";
        }
        if (etag.startsWith("\"") && etag.endsWith("\"")) {
            return etag.substring(1, etag.length() - 1);
        }
        return etag;
    }

    public MetadataDTO getMetadataDTO() {
        return this.metadataDTO;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && getClass() == o.getClass() && o.hashCode() == hashCode()) {
            Downloadable that = (Downloadable) o;
            return Objects.equals(this.additionalDestinations, that.additionalDestinations) && Objects.equals(this.repo, that.repo) && Objects.equals(this.metadataDTO, that.metadataDTO);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.additionalDestinations, this.repo, this.metadataDTO);
    }
}
