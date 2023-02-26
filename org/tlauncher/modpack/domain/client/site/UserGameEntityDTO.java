package org.tlauncher.modpack.domain.client.site;

import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/UserGameEntityDTO.class */
public class UserGameEntityDTO {
    private Long id;
    private String uuid;
    private GameType gameType;
    private GameEntityDTO gameEntity;

    public Long getId() {
        return this.id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public GameEntityDTO getGameEntity() {
        return this.gameEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setGameEntity(GameEntityDTO gameEntity) {
        this.gameEntity = gameEntity;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof UserGameEntityDTO) {
            UserGameEntityDTO other = (UserGameEntityDTO) o;
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
                Object this$uuid = getUuid();
                Object other$uuid = other.getUuid();
                if (this$uuid == null) {
                    if (other$uuid != null) {
                        return false;
                    }
                } else if (!this$uuid.equals(other$uuid)) {
                    return false;
                }
                Object this$gameType = getGameType();
                Object other$gameType = other.getGameType();
                if (this$gameType == null) {
                    if (other$gameType != null) {
                        return false;
                    }
                } else if (!this$gameType.equals(other$gameType)) {
                    return false;
                }
                Object this$gameEntity = getGameEntity();
                Object other$gameEntity = other.getGameEntity();
                return this$gameEntity == null ? other$gameEntity == null : this$gameEntity.equals(other$gameEntity);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserGameEntityDTO;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $uuid = getUuid();
        int result2 = (result * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $gameType = getGameType();
        int result3 = (result2 * 59) + ($gameType == null ? 43 : $gameType.hashCode());
        Object $gameEntity = getGameEntity();
        return (result3 * 59) + ($gameEntity == null ? 43 : $gameEntity.hashCode());
    }

    public String toString() {
        return "UserGameEntityDTO(id=" + getId() + ", uuid=" + getUuid() + ", gameType=" + getGameType() + ", gameEntity=" + getGameEntity() + ")";
    }
}
