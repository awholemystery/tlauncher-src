package org.tlauncher.tlauncher.entity.minecraft;

import org.tlauncher.modpack.domain.client.version.MetadataDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/minecraft/DownloadJVMFile.class */
public class DownloadJVMFile {
    private MetadataDTO lzma;
    private MetadataDTO raw;

    public void setLzma(MetadataDTO lzma) {
        this.lzma = lzma;
    }

    public void setRaw(MetadataDTO raw) {
        this.raw = raw;
    }

    public String toString() {
        return "DownloadJVMFile(lzma=" + getLzma() + ", raw=" + getRaw() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DownloadJVMFile) {
            DownloadJVMFile other = (DownloadJVMFile) o;
            if (other.canEqual(this)) {
                Object this$lzma = getLzma();
                Object other$lzma = other.getLzma();
                if (this$lzma == null) {
                    if (other$lzma != null) {
                        return false;
                    }
                } else if (!this$lzma.equals(other$lzma)) {
                    return false;
                }
                Object this$raw = getRaw();
                Object other$raw = other.getRaw();
                return this$raw == null ? other$raw == null : this$raw.equals(other$raw);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloadJVMFile;
    }

    public int hashCode() {
        Object $lzma = getLzma();
        int result = (1 * 59) + ($lzma == null ? 43 : $lzma.hashCode());
        Object $raw = getRaw();
        return (result * 59) + ($raw == null ? 43 : $raw.hashCode());
    }

    public MetadataDTO getLzma() {
        return this.lzma;
    }

    public MetadataDTO getRaw() {
        return this.raw;
    }
}
