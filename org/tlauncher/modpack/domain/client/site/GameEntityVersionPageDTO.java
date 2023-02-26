package org.tlauncher.modpack.domain.client.site;

import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/GameEntityVersionPageDTO.class */
public class GameEntityVersionPageDTO {
    private GameEntityDTO gameEntity;
    private CommonPage<VersionDTO> versionPages;

    public void setGameEntity(GameEntityDTO gameEntity) {
        this.gameEntity = gameEntity;
    }

    public void setVersionPages(CommonPage<VersionDTO> versionPages) {
        this.versionPages = versionPages;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof GameEntityVersionPageDTO) {
            GameEntityVersionPageDTO other = (GameEntityVersionPageDTO) o;
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
                Object this$versionPages = getVersionPages();
                Object other$versionPages = other.getVersionPages();
                return this$versionPages == null ? other$versionPages == null : this$versionPages.equals(other$versionPages);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameEntityVersionPageDTO;
    }

    public int hashCode() {
        Object $gameEntity = getGameEntity();
        int result = (1 * 59) + ($gameEntity == null ? 43 : $gameEntity.hashCode());
        Object $versionPages = getVersionPages();
        return (result * 59) + ($versionPages == null ? 43 : $versionPages.hashCode());
    }

    public String toString() {
        return "GameEntityVersionPageDTO(gameEntity=" + getGameEntity() + ", versionPages=" + getVersionPages() + ")";
    }

    public GameEntityVersionPageDTO(GameEntityDTO gameEntity, CommonPage<VersionDTO> versionPages) {
        this.gameEntity = gameEntity;
        this.versionPages = versionPages;
    }

    public GameEntityVersionPageDTO() {
    }

    public GameEntityDTO getGameEntity() {
        return this.gameEntity;
    }

    public CommonPage<VersionDTO> getVersionPages() {
        return this.versionPages;
    }
}
