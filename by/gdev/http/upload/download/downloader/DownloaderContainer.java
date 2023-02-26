package by.gdev.http.upload.download.downloader;

import by.gdev.http.download.handler.PostHandler;
import by.gdev.http.download.model.Headers;
import by.gdev.util.DesktopUtil;
import by.gdev.util.model.download.Metadata;
import by.gdev.util.model.download.Repo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/upload/download/downloader/DownloaderContainer.class */
public class DownloaderContainer {
    private static final Logger log = LoggerFactory.getLogger(DownloaderContainer.class);
    private String destinationRepositories;
    private long containerSize;
    private Repo repo;
    private List<PostHandler> handlers;

    public void setDestinationRepositories(String destinationRepositories) {
        this.destinationRepositories = destinationRepositories;
    }

    public void setContainerSize(long containerSize) {
        this.containerSize = containerSize;
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    public void setHandlers(List<PostHandler> handlers) {
        this.handlers = handlers;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DownloaderContainer) {
            DownloaderContainer other = (DownloaderContainer) o;
            if (other.canEqual(this)) {
                Object this$destinationRepositories = getDestinationRepositories();
                Object other$destinationRepositories = other.getDestinationRepositories();
                if (this$destinationRepositories == null) {
                    if (other$destinationRepositories != null) {
                        return false;
                    }
                } else if (!this$destinationRepositories.equals(other$destinationRepositories)) {
                    return false;
                }
                if (getContainerSize() != other.getContainerSize()) {
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
                Object this$handlers = getHandlers();
                Object other$handlers = other.getHandlers();
                return this$handlers == null ? other$handlers == null : this$handlers.equals(other$handlers);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloaderContainer;
    }

    public int hashCode() {
        Object $destinationRepositories = getDestinationRepositories();
        int result = (1 * 59) + ($destinationRepositories == null ? 43 : $destinationRepositories.hashCode());
        long $containerSize = getContainerSize();
        int result2 = (result * 59) + ((int) (($containerSize >>> 32) ^ $containerSize));
        Object $repo = getRepo();
        int result3 = (result2 * 59) + ($repo == null ? 43 : $repo.hashCode());
        Object $handlers = getHandlers();
        return (result3 * 59) + ($handlers == null ? 43 : $handlers.hashCode());
    }

    public String toString() {
        return "DownloaderContainer(destinationRepositories=" + getDestinationRepositories() + ", containerSize=" + getContainerSize() + ", repo=" + getRepo() + ", handlers=" + getHandlers() + ")";
    }

    public String getDestinationRepositories() {
        return this.destinationRepositories;
    }

    public long getContainerSize() {
        return this.containerSize;
    }

    public Repo getRepo() {
        return this.repo;
    }

    public List<PostHandler> getHandlers() {
        return this.handlers;
    }

    public void filterNotExistResoursesAndSetRepo(Repo repo, String workDirectory) throws NoSuchAlgorithmException, IOException {
        this.repo = new Repo();
        List<Metadata> listRes = new ArrayList<>();
        for (Metadata meta : repo.getResources()) {
            File localFile = Paths.get(workDirectory, meta.getPath()).toAbsolutePath().toFile();
            if (localFile.exists()) {
                String shaLocalFile = DesktopUtil.getChecksum(localFile, Headers.SHA1.getValue());
                BasicFileAttributes attr = Files.readAttributes(localFile.toPath(), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                if ((!attr.isSymbolicLink()) & (!shaLocalFile.equals(meta.getSha1()))) {
                    listRes.add(meta);
                    log.warn("The hash sum of the file is not equal. File " + localFile + " will be deleted. Size = " + ((localFile.length() / 1024) / 1024));
                    Files.delete(localFile.toPath());
                }
            } else {
                listRes.add(meta);
            }
        }
        this.repo.setResources(listRes);
        this.repo.setRepositories(repo.getRepositories());
    }

    public void conteinerAllSize(Repo repo) {
        List<Long> sizeList = new ArrayList<>();
        repo.getResources().forEach(size -> {
            sizeList.add(Long.valueOf(size.getSize()));
        });
        long sum = 0;
        for (Long l : sizeList) {
            long l2 = l.longValue();
            sum += l2;
        }
        this.containerSize = sum;
    }
}
