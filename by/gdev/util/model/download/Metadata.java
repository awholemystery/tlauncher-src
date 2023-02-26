package by.gdev.util.model.download;

import by.gdev.util.DesktopUtil;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/model/download/Metadata.class */
public class Metadata {
    private String sha1;
    private long size;
    private String path;
    private List<String> urls;
    private String relativeUrl;
    private boolean executable;
    private String link;

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Metadata) {
            Metadata other = (Metadata) o;
            if (other.canEqual(this)) {
                Object this$sha1 = getSha1();
                Object other$sha1 = other.getSha1();
                if (this$sha1 == null) {
                    if (other$sha1 != null) {
                        return false;
                    }
                } else if (!this$sha1.equals(other$sha1)) {
                    return false;
                }
                if (getSize() != other.getSize()) {
                    return false;
                }
                Object this$path = getPath();
                Object other$path = other.getPath();
                if (this$path == null) {
                    if (other$path != null) {
                        return false;
                    }
                } else if (!this$path.equals(other$path)) {
                    return false;
                }
                Object this$urls = getUrls();
                Object other$urls = other.getUrls();
                if (this$urls == null) {
                    if (other$urls != null) {
                        return false;
                    }
                } else if (!this$urls.equals(other$urls)) {
                    return false;
                }
                Object this$relativeUrl = getRelativeUrl();
                Object other$relativeUrl = other.getRelativeUrl();
                if (this$relativeUrl == null) {
                    if (other$relativeUrl != null) {
                        return false;
                    }
                } else if (!this$relativeUrl.equals(other$relativeUrl)) {
                    return false;
                }
                if (isExecutable() != other.isExecutable()) {
                    return false;
                }
                Object this$link = getLink();
                Object other$link = other.getLink();
                return this$link == null ? other$link == null : this$link.equals(other$link);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Metadata;
    }

    public int hashCode() {
        Object $sha1 = getSha1();
        int result = (1 * 59) + ($sha1 == null ? 43 : $sha1.hashCode());
        long $size = getSize();
        int result2 = (result * 59) + ((int) (($size >>> 32) ^ $size));
        Object $path = getPath();
        int result3 = (result2 * 59) + ($path == null ? 43 : $path.hashCode());
        Object $urls = getUrls();
        int result4 = (result3 * 59) + ($urls == null ? 43 : $urls.hashCode());
        Object $relativeUrl = getRelativeUrl();
        int result5 = (((result4 * 59) + ($relativeUrl == null ? 43 : $relativeUrl.hashCode())) * 59) + (isExecutable() ? 79 : 97);
        Object $link = getLink();
        return (result5 * 59) + ($link == null ? 43 : $link.hashCode());
    }

    public String toString() {
        return "Metadata(sha1=" + getSha1() + ", size=" + getSize() + ", path=" + getPath() + ", urls=" + getUrls() + ", relativeUrl=" + getRelativeUrl() + ", executable=" + isExecutable() + ", link=" + getLink() + ")";
    }

    public String getSha1() {
        return this.sha1;
    }

    public long getSize() {
        return this.size;
    }

    public String getPath() {
        return this.path;
    }

    public List<String> getUrls() {
        return this.urls;
    }

    public String getRelativeUrl() {
        return this.relativeUrl;
    }

    public boolean isExecutable() {
        return this.executable;
    }

    public String getLink() {
        return this.link;
    }

    public static Metadata createMetadata(Path config) throws NoSuchAlgorithmException, IOException {
        Metadata metadata = new Metadata();
        metadata.setPath(config.toString().replace("\\", "/"));
        metadata.setRelativeUrl(config.toString().replace("\\", "/"));
        metadata.setSha1(DesktopUtil.getChecksum(config.toFile(), "sha-1"));
        return metadata;
    }
}
