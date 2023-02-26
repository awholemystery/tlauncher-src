package org.tlauncher.modpack.domain.client;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/GameVersionDTO.class */
public class GameVersionDTO {
    private Long id;
    private String name;
    private List<String> forgeVersions;
    private ForgeVersionDTO forgeVersion;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setForgeVersions(List<String> forgeVersions) {
        this.forgeVersions = forgeVersions;
    }

    public void setForgeVersion(ForgeVersionDTO forgeVersion) {
        this.forgeVersion = forgeVersion;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof GameVersionDTO) {
            GameVersionDTO other = (GameVersionDTO) o;
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
                Object this$name = getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }
                Object this$forgeVersions = getForgeVersions();
                Object other$forgeVersions = other.getForgeVersions();
                if (this$forgeVersions == null) {
                    if (other$forgeVersions != null) {
                        return false;
                    }
                } else if (!this$forgeVersions.equals(other$forgeVersions)) {
                    return false;
                }
                Object this$forgeVersion = getForgeVersion();
                Object other$forgeVersion = other.getForgeVersion();
                return this$forgeVersion == null ? other$forgeVersion == null : this$forgeVersion.equals(other$forgeVersion);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameVersionDTO;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $forgeVersions = getForgeVersions();
        int result3 = (result2 * 59) + ($forgeVersions == null ? 43 : $forgeVersions.hashCode());
        Object $forgeVersion = getForgeVersion();
        return (result3 * 59) + ($forgeVersion == null ? 43 : $forgeVersion.hashCode());
    }

    public String toString() {
        return "GameVersionDTO(id=" + getId() + ", name=" + getName() + ", forgeVersions=" + getForgeVersions() + ", forgeVersion=" + getForgeVersion() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getForgeVersions() {
        return this.forgeVersions;
    }

    public ForgeVersionDTO getForgeVersion() {
        return this.forgeVersion;
    }

    public GameVersionDTO copyWithoutForges() {
        GameVersionDTO g = new GameVersionDTO();
        g.setId(this.id);
        g.setName(this.name);
        return g;
    }
}
