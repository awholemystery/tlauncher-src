package org.tlauncher.modpack.domain.client.version;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.share.JavaEnum;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.modpack.domain.client.share.Type;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/version/VersionDTO.class */
public class VersionDTO {
    private Long id;
    private String name;
    private Date date;
    private Long updatedDate;
    private Long updateDate;
    private Type type;
    private List<String> gameVersions;
    private List<GameVersionDTO> gameVersionsDTO;
    private List<JavaEnum> javaVersions;
    private MetadataDTO metadata;
    private Long installed;
    private boolean available;
    private List<NameIdDTO> minecraftVersionTypes;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setGameVersions(List<String> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public void setGameVersionsDTO(List<GameVersionDTO> gameVersionsDTO) {
        this.gameVersionsDTO = gameVersionsDTO;
    }

    public void setJavaVersions(List<JavaEnum> javaVersions) {
        this.javaVersions = javaVersions;
    }

    public void setMetadata(MetadataDTO metadata) {
        this.metadata = metadata;
    }

    public void setInstalled(Long installed) {
        this.installed = installed;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setMinecraftVersionTypes(List<NameIdDTO> minecraftVersionTypes) {
        this.minecraftVersionTypes = minecraftVersionTypes;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof VersionDTO) {
            VersionDTO other = (VersionDTO) o;
            if (other.canEqual(this) && isAvailable() == other.isAvailable()) {
                Object this$id = getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }
                Object this$updatedDate = getUpdatedDate();
                Object other$updatedDate = other.getUpdatedDate();
                if (this$updatedDate == null) {
                    if (other$updatedDate != null) {
                        return false;
                    }
                } else if (!this$updatedDate.equals(other$updatedDate)) {
                    return false;
                }
                Object this$updateDate = getUpdateDate();
                Object other$updateDate = other.getUpdateDate();
                if (this$updateDate == null) {
                    if (other$updateDate != null) {
                        return false;
                    }
                } else if (!this$updateDate.equals(other$updateDate)) {
                    return false;
                }
                Object this$installed = getInstalled();
                Object other$installed = other.getInstalled();
                if (this$installed == null) {
                    if (other$installed != null) {
                        return false;
                    }
                } else if (!this$installed.equals(other$installed)) {
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
                Object this$date = getDate();
                Object other$date = other.getDate();
                if (this$date == null) {
                    if (other$date != null) {
                        return false;
                    }
                } else if (!this$date.equals(other$date)) {
                    return false;
                }
                Object this$type = getType();
                Object other$type = other.getType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
                    return false;
                }
                Object this$gameVersions = getGameVersions();
                Object other$gameVersions = other.getGameVersions();
                if (this$gameVersions == null) {
                    if (other$gameVersions != null) {
                        return false;
                    }
                } else if (!this$gameVersions.equals(other$gameVersions)) {
                    return false;
                }
                Object this$gameVersionsDTO = getGameVersionsDTO();
                Object other$gameVersionsDTO = other.getGameVersionsDTO();
                if (this$gameVersionsDTO == null) {
                    if (other$gameVersionsDTO != null) {
                        return false;
                    }
                } else if (!this$gameVersionsDTO.equals(other$gameVersionsDTO)) {
                    return false;
                }
                Object this$javaVersions = getJavaVersions();
                Object other$javaVersions = other.getJavaVersions();
                if (this$javaVersions == null) {
                    if (other$javaVersions != null) {
                        return false;
                    }
                } else if (!this$javaVersions.equals(other$javaVersions)) {
                    return false;
                }
                Object this$metadata = getMetadata();
                Object other$metadata = other.getMetadata();
                if (this$metadata == null) {
                    if (other$metadata != null) {
                        return false;
                    }
                } else if (!this$metadata.equals(other$metadata)) {
                    return false;
                }
                Object this$minecraftVersionTypes = getMinecraftVersionTypes();
                Object other$minecraftVersionTypes = other.getMinecraftVersionTypes();
                return this$minecraftVersionTypes == null ? other$minecraftVersionTypes == null : this$minecraftVersionTypes.equals(other$minecraftVersionTypes);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof VersionDTO;
    }

    public int hashCode() {
        int result = (1 * 59) + (isAvailable() ? 79 : 97);
        Object $id = getId();
        int result2 = (result * 59) + ($id == null ? 43 : $id.hashCode());
        Object $updatedDate = getUpdatedDate();
        int result3 = (result2 * 59) + ($updatedDate == null ? 43 : $updatedDate.hashCode());
        Object $updateDate = getUpdateDate();
        int result4 = (result3 * 59) + ($updateDate == null ? 43 : $updateDate.hashCode());
        Object $installed = getInstalled();
        int result5 = (result4 * 59) + ($installed == null ? 43 : $installed.hashCode());
        Object $name = getName();
        int result6 = (result5 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $date = getDate();
        int result7 = (result6 * 59) + ($date == null ? 43 : $date.hashCode());
        Object $type = getType();
        int result8 = (result7 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $gameVersions = getGameVersions();
        int result9 = (result8 * 59) + ($gameVersions == null ? 43 : $gameVersions.hashCode());
        Object $gameVersionsDTO = getGameVersionsDTO();
        int result10 = (result9 * 59) + ($gameVersionsDTO == null ? 43 : $gameVersionsDTO.hashCode());
        Object $javaVersions = getJavaVersions();
        int result11 = (result10 * 59) + ($javaVersions == null ? 43 : $javaVersions.hashCode());
        Object $metadata = getMetadata();
        int result12 = (result11 * 59) + ($metadata == null ? 43 : $metadata.hashCode());
        Object $minecraftVersionTypes = getMinecraftVersionTypes();
        return (result12 * 59) + ($minecraftVersionTypes == null ? 43 : $minecraftVersionTypes.hashCode());
    }

    public String toString() {
        return "VersionDTO(id=" + getId() + ", name=" + getName() + ", date=" + getDate() + ", updatedDate=" + getUpdatedDate() + ", updateDate=" + getUpdateDate() + ", type=" + getType() + ", gameVersions=" + getGameVersions() + ", gameVersionsDTO=" + getGameVersionsDTO() + ", javaVersions=" + getJavaVersions() + ", metadata=" + getMetadata() + ", installed=" + getInstalled() + ", available=" + isAvailable() + ", minecraftVersionTypes=" + getMinecraftVersionTypes() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }

    public Long getUpdatedDate() {
        return this.updatedDate;
    }

    public Long getUpdateDate() {
        return this.updateDate;
    }

    public Type getType() {
        return this.type;
    }

    public List<String> getGameVersions() {
        return this.gameVersions;
    }

    public List<GameVersionDTO> getGameVersionsDTO() {
        return this.gameVersionsDTO;
    }

    public List<JavaEnum> getJavaVersions() {
        return this.javaVersions;
    }

    public MetadataDTO getMetadata() {
        return this.metadata;
    }

    public Long getInstalled() {
        return this.installed;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public List<NameIdDTO> getMinecraftVersionTypes() {
        return this.minecraftVersionTypes;
    }

    public NameIdDTO findFirstMinecraftVersionType() {
        if (Objects.isNull(this.minecraftVersionTypes)) {
            return null;
        }
        Optional<NameIdDTO> op = this.minecraftVersionTypes.stream().findFirst();
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }
}
