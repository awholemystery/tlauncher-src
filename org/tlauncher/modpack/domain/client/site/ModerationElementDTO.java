package org.tlauncher.modpack.domain.client.site;

import java.util.List;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/ModerationElementDTO.class */
public class ModerationElementDTO {
    private GameEntityDTO gameEntity;
    private CommonPage<VersionDTO> versions;
    private List<String> changedFields;

    public void setGameEntity(GameEntityDTO gameEntity) {
        this.gameEntity = gameEntity;
    }

    public void setVersions(CommonPage<VersionDTO> versions) {
        this.versions = versions;
    }

    public void setChangedFields(List<String> changedFields) {
        this.changedFields = changedFields;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModerationElementDTO) {
            ModerationElementDTO other = (ModerationElementDTO) o;
            if (other.canEqual(this)) {
                Object this$gameEntity = getGameEntity();
                Object other$gameEntity = other.getGameEntity();
                if (this$gameEntity == null) {
                    if (other$gameEntity != null) {
                        return false;
                    }
                } else if (!this$gameEntity.equals(other$gameEntity)) {
                    return false;
                }
                Object this$versions = getVersions();
                Object other$versions = other.getVersions();
                if (this$versions == null) {
                    if (other$versions != null) {
                        return false;
                    }
                } else if (!this$versions.equals(other$versions)) {
                    return false;
                }
                Object this$changedFields = getChangedFields();
                Object other$changedFields = other.getChangedFields();
                return this$changedFields == null ? other$changedFields == null : this$changedFields.equals(other$changedFields);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ModerationElementDTO;
    }

    public int hashCode() {
        Object $gameEntity = getGameEntity();
        int result = (1 * 59) + ($gameEntity == null ? 43 : $gameEntity.hashCode());
        Object $versions = getVersions();
        int result2 = (result * 59) + ($versions == null ? 43 : $versions.hashCode());
        Object $changedFields = getChangedFields();
        return (result2 * 59) + ($changedFields == null ? 43 : $changedFields.hashCode());
    }

    public String toString() {
        return "ModerationElementDTO(gameEntity=" + getGameEntity() + ", versions=" + getVersions() + ", changedFields=" + getChangedFields() + ")";
    }

    public GameEntityDTO getGameEntity() {
        return this.gameEntity;
    }

    public CommonPage<VersionDTO> getVersions() {
        return this.versions;
    }

    public List<String> getChangedFields() {
        return this.changedFields;
    }
}
