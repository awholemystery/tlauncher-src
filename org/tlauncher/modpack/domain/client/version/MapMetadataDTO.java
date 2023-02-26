package org.tlauncher.modpack.domain.client.version;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/version/MapMetadataDTO.class */
public class MapMetadataDTO extends MetadataDTO {
    private List<String> folders;

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    @Override // org.tlauncher.modpack.domain.client.version.MetadataDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof MapMetadataDTO) {
            MapMetadataDTO other = (MapMetadataDTO) o;
            if (other.canEqual(this)) {
                Object this$folders = getFolders();
                Object other$folders = other.getFolders();
                return this$folders == null ? other$folders == null : this$folders.equals(other$folders);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.version.MetadataDTO
    protected boolean canEqual(Object other) {
        return other instanceof MapMetadataDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.version.MetadataDTO
    public int hashCode() {
        Object $folders = getFolders();
        int result = (1 * 59) + ($folders == null ? 43 : $folders.hashCode());
        return result;
    }

    @Override // org.tlauncher.modpack.domain.client.version.MetadataDTO
    public String toString() {
        return "MapMetadataDTO(folders=" + getFolders() + ")";
    }

    public List<String> getFolders() {
        return this.folders;
    }
}
