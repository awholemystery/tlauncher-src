package by.gdev.http.upload.download.downloader;

import by.gdev.http.download.handler.PostHandler;
import by.gdev.util.model.download.Metadata;
import by.gdev.util.model.download.Repo;
import java.time.LocalTime;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/upload/download/downloader/DownloadElement.class */
public class DownloadElement {
    private List<PostHandler> handlers;
    private Metadata metadata;
    private String pathToDownload;
    private LocalTime start;
    private LocalTime end;
    private Repo repo;
    private volatile long downloadBytes;
    private volatile Throwable error;

    public void setHandlers(List<PostHandler> handlers) {
        this.handlers = handlers;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public void setPathToDownload(String pathToDownload) {
        this.pathToDownload = pathToDownload;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    public void setDownloadBytes(long downloadBytes) {
        this.downloadBytes = downloadBytes;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DownloadElement) {
            DownloadElement other = (DownloadElement) o;
            if (other.canEqual(this)) {
                Object this$handlers = getHandlers();
                Object other$handlers = other.getHandlers();
                if (this$handlers == null) {
                    if (other$handlers != null) {
                        return false;
                    }
                } else if (!this$handlers.equals(other$handlers)) {
                    return false;
                }
                Object this$metadata = getMetadata();
                Object other$metadata = other.getMetadata();
                if (this$metadata == null) {
                    if (other$metadata != null) {
                        return false;
                    }
                } else if (!this$metadata.equals(other$metadata)) {
                    return false;
                }
                Object this$pathToDownload = getPathToDownload();
                Object other$pathToDownload = other.getPathToDownload();
                if (this$pathToDownload == null) {
                    if (other$pathToDownload != null) {
                        return false;
                    }
                } else if (!this$pathToDownload.equals(other$pathToDownload)) {
                    return false;
                }
                Object this$start = getStart();
                Object other$start = other.getStart();
                if (this$start == null) {
                    if (other$start != null) {
                        return false;
                    }
                } else if (!this$start.equals(other$start)) {
                    return false;
                }
                Object this$end = getEnd();
                Object other$end = other.getEnd();
                if (this$end == null) {
                    if (other$end != null) {
                        return false;
                    }
                } else if (!this$end.equals(other$end)) {
                    return false;
                }
                Object this$repo = getRepo();
                Object other$repo = other.getRepo();
                if (this$repo == null) {
                    if (other$repo != null) {
                        return false;
                    }
                } else if (!this$repo.equals(other$repo)) {
                    return false;
                }
                if (getDownloadBytes() != other.getDownloadBytes()) {
                    return false;
                }
                Object this$error = getError();
                Object other$error = other.getError();
                return this$error == null ? other$error == null : this$error.equals(other$error);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloadElement;
    }

    public int hashCode() {
        Object $handlers = getHandlers();
        int result = (1 * 59) + ($handlers == null ? 43 : $handlers.hashCode());
        Object $metadata = getMetadata();
        int result2 = (result * 59) + ($metadata == null ? 43 : $metadata.hashCode());
        Object $pathToDownload = getPathToDownload();
        int result3 = (result2 * 59) + ($pathToDownload == null ? 43 : $pathToDownload.hashCode());
        Object $start = getStart();
        int result4 = (result3 * 59) + ($start == null ? 43 : $start.hashCode());
        Object $end = getEnd();
        int result5 = (result4 * 59) + ($end == null ? 43 : $end.hashCode());
        Object $repo = getRepo();
        int result6 = (result5 * 59) + ($repo == null ? 43 : $repo.hashCode());
        long $downloadBytes = getDownloadBytes();
        int result7 = (result6 * 59) + ((int) (($downloadBytes >>> 32) ^ $downloadBytes));
        Object $error = getError();
        return (result7 * 59) + ($error == null ? 43 : $error.hashCode());
    }

    public String toString() {
        return "DownloadElement(handlers=" + getHandlers() + ", metadata=" + getMetadata() + ", pathToDownload=" + getPathToDownload() + ", start=" + getStart() + ", end=" + getEnd() + ", repo=" + getRepo() + ", downloadBytes=" + getDownloadBytes() + ", error=" + getError() + ")";
    }

    public List<PostHandler> getHandlers() {
        return this.handlers;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public String getPathToDownload() {
        return this.pathToDownload;
    }

    public LocalTime getStart() {
        return this.start;
    }

    public LocalTime getEnd() {
        return this.end;
    }

    public Repo getRepo() {
        return this.repo;
    }

    public long getDownloadBytes() {
        return this.downloadBytes;
    }

    public Throwable getError() {
        return this.error;
    }
}
