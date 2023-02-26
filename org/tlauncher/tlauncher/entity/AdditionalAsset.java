package org.tlauncher.tlauncher.entity;

import java.util.List;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/AdditionalAsset.class */
public class AdditionalAsset {
    private List<String> versions;
    private List<MetadataDTO> files;

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public void setFiles(List<MetadataDTO> files) {
        this.files = files;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AdditionalAsset) {
            AdditionalAsset other = (AdditionalAsset) o;
            if (other.canEqual(this)) {
                Object this$versions = getVersions();
                Object other$versions = other.getVersions();
                if (this$versions == null) {
                    if (other$versions != null) {
                        return false;
                    }
                } else if (!this$versions.equals(other$versions)) {
                    return false;
                }
                Object this$files = getFiles();
                Object other$files = other.getFiles();
                return this$files == null ? other$files == null : this$files.equals(other$files);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdditionalAsset;
    }

    public int hashCode() {
        Object $versions = getVersions();
        int result = (1 * 59) + ($versions == null ? 43 : $versions.hashCode());
        Object $files = getFiles();
        return (result * 59) + ($files == null ? 43 : $files.hashCode());
    }

    public String toString() {
        return "AdditionalAsset(versions=" + getVersions() + ", files=" + getFiles() + ")";
    }

    public List<String> getVersions() {
        return this.versions;
    }

    public List<MetadataDTO> getFiles() {
        return this.files;
    }
}
