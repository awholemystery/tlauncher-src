package org.tlauncher.tlauncher.updater.bootstrapper.model;

import java.io.File;
import java.util.List;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/model/DownloadedElement.class */
public class DownloadedElement {
    private List<String> url;
    private String shaCode;
    private long size;
    private String storagePath;

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public void setShaCode(String shaCode) {
        this.shaCode = shaCode;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DownloadedElement) {
            DownloadedElement other = (DownloadedElement) o;
            if (other.canEqual(this)) {
                Object this$url = getUrl();
                Object other$url = other.getUrl();
                if (this$url == null) {
                    if (other$url != null) {
                        return false;
                    }
                } else if (!this$url.equals(other$url)) {
                    return false;
                }
                Object this$shaCode = getShaCode();
                Object other$shaCode = other.getShaCode();
                if (this$shaCode == null) {
                    if (other$shaCode != null) {
                        return false;
                    }
                } else if (!this$shaCode.equals(other$shaCode)) {
                    return false;
                }
                if (getSize() != other.getSize()) {
                    return false;
                }
                Object this$storagePath = getStoragePath();
                Object other$storagePath = other.getStoragePath();
                return this$storagePath == null ? other$storagePath == null : this$storagePath.equals(other$storagePath);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloadedElement;
    }

    public int hashCode() {
        Object $url = getUrl();
        int result = (1 * 59) + ($url == null ? 43 : $url.hashCode());
        Object $shaCode = getShaCode();
        int result2 = (result * 59) + ($shaCode == null ? 43 : $shaCode.hashCode());
        long $size = getSize();
        int result3 = (result2 * 59) + ((int) (($size >>> 32) ^ $size));
        Object $storagePath = getStoragePath();
        return (result3 * 59) + ($storagePath == null ? 43 : $storagePath.hashCode());
    }

    public String toString() {
        return "DownloadedElement(url=" + getUrl() + ", shaCode=" + getShaCode() + ", size=" + getSize() + ", storagePath=" + getStoragePath() + ")";
    }

    public List<String> getUrl() {
        return this.url;
    }

    public String getShaCode() {
        return this.shaCode;
    }

    public long getSize() {
        return this.size;
    }

    public String getStoragePath() {
        return this.storagePath;
    }

    public boolean notExistOrValid(File folder) {
        File library = new File(folder, this.storagePath);
        if (library.exists() && library.isFile()) {
            String sha = FileUtil.getChecksum(library);
            return !getShaCode().equals(sha);
        }
        return true;
    }
}
