package org.tlauncher.modpack.domain.client;

import org.tlauncher.modpack.domain.client.share.DependencyType;
import org.tlauncher.modpack.domain.client.share.GameType;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/GameEntityDependencyDTO.class */
public class GameEntityDependencyDTO {
    private GameType gameType;
    private Long id;
    private String name;
    private Long gameEntityId;
    private DependencyType dependencyType;

    public GameType getGameType() {
        return this.gameType;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Long getGameEntityId() {
        return this.gameEntityId;
    }

    public DependencyType getDependencyType() {
        return this.dependencyType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGameEntityId(Long gameEntityId) {
        this.gameEntityId = gameEntityId;
    }

    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof GameEntityDependencyDTO) {
            GameEntityDependencyDTO other = (GameEntityDependencyDTO) o;
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
                Object this$gameEntityId = getGameEntityId();
                Object other$gameEntityId = other.getGameEntityId();
                if (this$gameEntityId == null) {
                    if (other$gameEntityId != null) {
                        return false;
                    }
                } else if (!this$gameEntityId.equals(other$gameEntityId)) {
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
                Object this$name = getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }
                Object this$dependencyType = getDependencyType();
                Object other$dependencyType = other.getDependencyType();
                return this$dependencyType == null ? other$dependencyType == null : this$dependencyType.equals(other$dependencyType);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameEntityDependencyDTO;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $gameEntityId = getGameEntityId();
        int result2 = (result * 59) + ($gameEntityId == null ? 43 : $gameEntityId.hashCode());
        Object $gameType = getGameType();
        int result3 = (result2 * 59) + ($gameType == null ? 43 : $gameType.hashCode());
        Object $name = getName();
        int result4 = (result3 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $dependencyType = getDependencyType();
        return (result4 * 59) + ($dependencyType == null ? 43 : $dependencyType.hashCode());
    }

    public String toString() {
        return "GameEntityDependencyDTO(gameType=" + getGameType() + ", id=" + getId() + ", name=" + getName() + ", gameEntityId=" + getGameEntityId() + ", dependencyType=" + getDependencyType() + ")";
    }

    public GameEntityDependencyDTO() {
    }

    public GameEntityDependencyDTO(GameType gameType, Long id, String name, Long gameEntityId, DependencyType dependencyType) {
        this.gameType = gameType;
        this.id = id;
        this.name = name;
        this.gameEntityId = gameEntityId;
        this.dependencyType = dependencyType;
    }
}
