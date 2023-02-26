package org.tlauncher.modpack.domain.client.site;

import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/GameEntityPairDTO.class */
public class GameEntityPairDTO {
    private Long id;
    private GameEntityDTO gameEntity;
    private VersionDTO version;

    public void setId(Long id) {
        this.id = id;
    }

    public void setGameEntity(GameEntityDTO gameEntity) {
        this.gameEntity = gameEntity;
    }

    public void setVersion(VersionDTO version) {
        this.version = version;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof GameEntityPairDTO) {
            GameEntityPairDTO other = (GameEntityPairDTO) o;
            if (other.canEqual(this)) {
                Object this$id = getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }
                Object this$gameEntity = getGameEntity();
                Object other$gameEntity = other.getGameEntity();
                if (this$gameEntity == null) {
                    if (other$gameEntity != null) {
                        return false;
                    }
                } else if (!this$gameEntity.equals(other$gameEntity)) {
                    return false;
                }
                Object this$version = getVersion();
                Object other$version = other.getVersion();
                return this$version == null ? other$version == null : this$version.equals(other$version);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameEntityPairDTO;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $gameEntity = getGameEntity();
        int result2 = (result * 59) + ($gameEntity == null ? 43 : $gameEntity.hashCode());
        Object $version = getVersion();
        return (result2 * 59) + ($version == null ? 43 : $version.hashCode());
    }

    public String toString() {
        return "GameEntityPairDTO(id=" + getId() + ", gameEntity=" + getGameEntity() + ", version=" + getVersion() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public GameEntityDTO getGameEntity() {
        return this.gameEntity;
    }

    public VersionDTO getVersion() {
        return this.version;
    }
}
