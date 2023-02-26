package org.tlauncher.modpack.domain.client.version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/version/MetadataDTO.class */
public class MetadataDTO {
    protected String sha1;
    protected long size;
    protected String path;
    protected String url;
    @JsonIgnore
    protected transient File localDestination;

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonIgnore
    public void setLocalDestination(File localDestination) {
        this.localDestination = localDestination;
    }

    public String toString() {
        return "MetadataDTO(sha1=" + getSha1() + ", size=" + getSize() + ", path=" + getPath() + ", url=" + getUrl() + ", localDestination=" + getLocalDestination() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof MetadataDTO) {
            MetadataDTO other = (MetadataDTO) o;
            if (other.canEqual(this) && getSize() == other.getSize()) {
                Object this$sha1 = getSha1();
                Object other$sha1 = other.getSha1();
                if (this$sha1 == null) {
                    if (other$sha1 != null) {
                        return false;
                    }
                } else if (!this$sha1.equals(other$sha1)) {
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
                Object this$url = getUrl();
                Object other$url = other.getUrl();
                return this$url == null ? other$url == null : this$url.equals(other$url);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MetadataDTO;
    }

    public int hashCode() {
        long $size = getSize();
        int result = (1 * 59) + ((int) (($size >>> 32) ^ $size));
        Object $sha1 = getSha1();
        int result2 = (result * 59) + ($sha1 == null ? 43 : $sha1.hashCode());
        Object $path = getPath();
        int result3 = (result2 * 59) + ($path == null ? 43 : $path.hashCode());
        Object $url = getUrl();
        return (result3 * 59) + ($url == null ? 43 : $url.hashCode());
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

    public String getUrl() {
        return this.url;
    }

    public File getLocalDestination() {
        return this.localDestination;
    }
}
