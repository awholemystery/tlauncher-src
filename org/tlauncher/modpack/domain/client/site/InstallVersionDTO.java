package org.tlauncher.modpack.domain.client.site;

import org.tlauncher.modpack.domain.client.share.GameType;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/InstallVersionDTO.class */
public class InstallVersionDTO {
    private GameType type;
    private Long gameEntityId;
    private Long versionId;

    public GameType getType() {
        return this.type;
    }

    public Long getGameEntityId() {
        return this.gameEntityId;
    }

    public Long getVersionId() {
        return this.versionId;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setGameEntityId(Long gameEntityId) {
        this.gameEntityId = gameEntityId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof InstallVersionDTO) {
            InstallVersionDTO other = (InstallVersionDTO) o;
            if (other.canEqual(this)) {
                Object this$gameEntityId = getGameEntityId();
                Object other$gameEntityId = other.getGameEntityId();
                if (this$gameEntityId == null) {
                    if (other$gameEntityId != null) {
                        return false;
                    }
                } else if (!this$gameEntityId.equals(other$gameEntityId)) {
                    return false;
                }
                Object this$versionId = getVersionId();
                Object other$versionId = other.getVersionId();
                if (this$versionId == null) {
                    if (other$versionId != null) {
                        return false;
                    }
                } else if (!this$versionId.equals(other$versionId)) {
                    return false;
                }
                Object this$type = getType();
                Object other$type = other.getType();
                return this$type == null ? other$type == null : this$type.equals(other$type);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstallVersionDTO;
    }

    public int hashCode() {
        Object $gameEntityId = getGameEntityId();
        int result = (1 * 59) + ($gameEntityId == null ? 43 : $gameEntityId.hashCode());
        Object $versionId = getVersionId();
        int result2 = (result * 59) + ($versionId == null ? 43 : $versionId.hashCode());
        Object $type = getType();
        return (result2 * 59) + ($type == null ? 43 : $type.hashCode());
    }

    public String toString() {
        return "InstallVersionDTO(type=" + getType() + ", gameEntityId=" + getGameEntityId() + ", versionId=" + getVersionId() + ")";
    }
}
