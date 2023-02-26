package org.tlauncher.modpack.domain.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import org.tlauncher.modpack.domain.client.share.Category;
import org.tlauncher.modpack.domain.client.share.CategoryDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/GameEntityDTO.class */
public class GameEntityDTO {
    private Long id;
    private String name;
    private String author;
    private String uuid;
    private String linkProject;
    private String tlmodsLinkProject;
    private Map<String, String> descriptions;
    private String shortDescription;
    private String description;
    private String enDescription;
    private boolean favorite;
    private GameType type;
    private List<CategoryDTO> categories;
    private List<Category> categories1;
    private List<TagDTO> tags;
    private Long picture;
    private List<Long> pictures;
    private String officialSite;
    private String lanName;
    private VersionDTO version;
    private List<VersionDTO> versions;
    private Long installed;
    private Long downloadMonth;
    private Long watched;
    @JsonIgnore
    private boolean userInstall;
    @JsonIgnore
    private boolean populateStatus;
    private List<GameEntityDependencyDTO> dependencies;
    private List<GameVersionDTO> gameVersions;
    private GameVersionDTO lastGameVersion;
    private Long updated;
    private Long update;
    private Long downloadALL;
    private boolean available;
    private boolean parser;
    private long totalComments;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getLinkProject() {
        return this.linkProject;
    }

    public String getTlmodsLinkProject() {
        return this.tlmodsLinkProject;
    }

    public Map<String, String> getDescriptions() {
        return this.descriptions;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public String getEnDescription() {
        return this.enDescription;
    }

    public boolean isFavorite() {
        return this.favorite;
    }

    public GameType getType() {
        return this.type;
    }

    public List<CategoryDTO> getCategories() {
        return this.categories;
    }

    public List<Category> getCategories1() {
        return this.categories1;
    }

    public List<TagDTO> getTags() {
        return this.tags;
    }

    public Long getPicture() {
        return this.picture;
    }

    public List<Long> getPictures() {
        return this.pictures;
    }

    public String getOfficialSite() {
        return this.officialSite;
    }

    public String getLanName() {
        return this.lanName;
    }

    public VersionDTO getVersion() {
        return this.version;
    }

    public List<VersionDTO> getVersions() {
        return this.versions;
    }

    public Long getInstalled() {
        return this.installed;
    }

    public Long getDownloadMonth() {
        return this.downloadMonth;
    }

    public Long getWatched() {
        return this.watched;
    }

    public boolean isUserInstall() {
        return this.userInstall;
    }

    public boolean isPopulateStatus() {
        return this.populateStatus;
    }

    public List<GameEntityDependencyDTO> getDependencies() {
        return this.dependencies;
    }

    public List<GameVersionDTO> getGameVersions() {
        return this.gameVersions;
    }

    public GameVersionDTO getLastGameVersion() {
        return this.lastGameVersion;
    }

    public Long getUpdated() {
        return this.updated;
    }

    public Long getUpdate() {
        return this.update;
    }

    public Long getDownloadALL() {
        return this.downloadALL;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public boolean isParser() {
        return this.parser;
    }

    public long getTotalComments() {
        return this.totalComments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setLinkProject(String linkProject) {
        this.linkProject = linkProject;
    }

    public void setTlmodsLinkProject(String tlmodsLinkProject) {
        this.tlmodsLinkProject = tlmodsLinkProject;
    }

    public void setDescriptions(Map<String, String> descriptions) {
        this.descriptions = descriptions;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnDescription(String enDescription) {
        this.enDescription = enDescription;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public void setCategories1(List<Category> categories1) {
        this.categories1 = categories1;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
    }

    public void setPictures(List<Long> pictures) {
        this.pictures = pictures;
    }

    public void setOfficialSite(String officialSite) {
        this.officialSite = officialSite;
    }

    public void setLanName(String lanName) {
        this.lanName = lanName;
    }

    public void setVersion(VersionDTO version) {
        this.version = version;
    }

    public void setVersions(List<VersionDTO> versions) {
        this.versions = versions;
    }

    public void setInstalled(Long installed) {
        this.installed = installed;
    }

    public void setDownloadMonth(Long downloadMonth) {
        this.downloadMonth = downloadMonth;
    }

    public void setWatched(Long watched) {
        this.watched = watched;
    }

    @JsonIgnore
    public void setUserInstall(boolean userInstall) {
        this.userInstall = userInstall;
    }

    @JsonIgnore
    public void setPopulateStatus(boolean populateStatus) {
        this.populateStatus = populateStatus;
    }

    public void setDependencies(List<GameEntityDependencyDTO> dependencies) {
        this.dependencies = dependencies;
    }

    public void setGameVersions(List<GameVersionDTO> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public void setLastGameVersion(GameVersionDTO lastGameVersion) {
        this.lastGameVersion = lastGameVersion;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public void setUpdate(Long update) {
        this.update = update;
    }

    public void setDownloadALL(Long downloadALL) {
        this.downloadALL = downloadALL;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setParser(boolean parser) {
        this.parser = parser;
    }

    public void setTotalComments(long totalComments) {
        this.totalComments = totalComments;
    }

    public String toString() {
        return "GameEntityDTO(id=" + getId() + ", name=" + getName() + ", author=" + getAuthor() + ", uuid=" + getUuid() + ", linkProject=" + getLinkProject() + ", tlmodsLinkProject=" + getTlmodsLinkProject() + ", descriptions=" + getDescriptions() + ", shortDescription=" + getShortDescription() + ", description=" + getDescription() + ", enDescription=" + getEnDescription() + ", favorite=" + isFavorite() + ", type=" + getType() + ", categories=" + getCategories() + ", categories1=" + getCategories1() + ", tags=" + getTags() + ", picture=" + getPicture() + ", pictures=" + getPictures() + ", officialSite=" + getOfficialSite() + ", lanName=" + getLanName() + ", version=" + getVersion() + ", versions=" + getVersions() + ", installed=" + getInstalled() + ", downloadMonth=" + getDownloadMonth() + ", watched=" + getWatched() + ", userInstall=" + isUserInstall() + ", populateStatus=" + isPopulateStatus() + ", dependencies=" + getDependencies() + ", gameVersions=" + getGameVersions() + ", lastGameVersion=" + getLastGameVersion() + ", updated=" + getUpdated() + ", update=" + getUpdate() + ", downloadALL=" + getDownloadALL() + ", available=" + isAvailable() + ", parser=" + isParser() + ", totalComments=" + getTotalComments() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof GameEntityDTO) {
            GameEntityDTO other = (GameEntityDTO) o;
            if (other.canEqual(this) && isFavorite() == other.isFavorite() && isUserInstall() == other.isUserInstall() && isPopulateStatus() == other.isPopulateStatus() && isAvailable() == other.isAvailable() && isParser() == other.isParser() && getTotalComments() == other.getTotalComments()) {
                Object this$id = getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }
                Object this$picture = getPicture();
                Object other$picture = other.getPicture();
                if (this$picture == null) {
                    if (other$picture != null) {
                        return false;
                    }
                } else if (!this$picture.equals(other$picture)) {
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
                Object this$downloadMonth = getDownloadMonth();
                Object other$downloadMonth = other.getDownloadMonth();
                if (this$downloadMonth == null) {
                    if (other$downloadMonth != null) {
                        return false;
                    }
                } else if (!this$downloadMonth.equals(other$downloadMonth)) {
                    return false;
                }
                Object this$watched = getWatched();
                Object other$watched = other.getWatched();
                if (this$watched == null) {
                    if (other$watched != null) {
                        return false;
                    }
                } else if (!this$watched.equals(other$watched)) {
                    return false;
                }
                Object this$updated = getUpdated();
                Object other$updated = other.getUpdated();
                if (this$updated == null) {
                    if (other$updated != null) {
                        return false;
                    }
                } else if (!this$updated.equals(other$updated)) {
                    return false;
                }
                Object this$update = getUpdate();
                Object other$update = other.getUpdate();
                if (this$update == null) {
                    if (other$update != null) {
                        return false;
                    }
                } else if (!this$update.equals(other$update)) {
                    return false;
                }
                Object this$downloadALL = getDownloadALL();
                Object other$downloadALL = other.getDownloadALL();
                if (this$downloadALL == null) {
                    if (other$downloadALL != null) {
                        return false;
                    }
                } else if (!this$downloadALL.equals(other$downloadALL)) {
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
                Object this$author = getAuthor();
                Object other$author = other.getAuthor();
                if (this$author == null) {
                    if (other$author != null) {
                        return false;
                    }
                } else if (!this$author.equals(other$author)) {
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
                Object this$linkProject = getLinkProject();
                Object other$linkProject = other.getLinkProject();
                if (this$linkProject == null) {
                    if (other$linkProject != null) {
                        return false;
                    }
                } else if (!this$linkProject.equals(other$linkProject)) {
                    return false;
                }
                Object this$tlmodsLinkProject = getTlmodsLinkProject();
                Object other$tlmodsLinkProject = other.getTlmodsLinkProject();
                if (this$tlmodsLinkProject == null) {
                    if (other$tlmodsLinkProject != null) {
                        return false;
                    }
                } else if (!this$tlmodsLinkProject.equals(other$tlmodsLinkProject)) {
                    return false;
                }
                Object this$descriptions = getDescriptions();
                Object other$descriptions = other.getDescriptions();
                if (this$descriptions == null) {
                    if (other$descriptions != null) {
                        return false;
                    }
                } else if (!this$descriptions.equals(other$descriptions)) {
                    return false;
                }
                Object this$shortDescription = getShortDescription();
                Object other$shortDescription = other.getShortDescription();
                if (this$shortDescription == null) {
                    if (other$shortDescription != null) {
                        return false;
                    }
                } else if (!this$shortDescription.equals(other$shortDescription)) {
                    return false;
                }
                Object this$description = getDescription();
                Object other$description = other.getDescription();
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }
                Object this$enDescription = getEnDescription();
                Object other$enDescription = other.getEnDescription();
                if (this$enDescription == null) {
                    if (other$enDescription != null) {
                        return false;
                    }
                } else if (!this$enDescription.equals(other$enDescription)) {
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
                Object this$categories = getCategories();
                Object other$categories = other.getCategories();
                if (this$categories == null) {
                    if (other$categories != null) {
                        return false;
                    }
                } else if (!this$categories.equals(other$categories)) {
                    return false;
                }
                Object this$categories1 = getCategories1();
                Object other$categories1 = other.getCategories1();
                if (this$categories1 == null) {
                    if (other$categories1 != null) {
                        return false;
                    }
                } else if (!this$categories1.equals(other$categories1)) {
                    return false;
                }
                Object this$tags = getTags();
                Object other$tags = other.getTags();
                if (this$tags == null) {
                    if (other$tags != null) {
                        return false;
                    }
                } else if (!this$tags.equals(other$tags)) {
                    return false;
                }
                Object this$pictures = getPictures();
                Object other$pictures = other.getPictures();
                if (this$pictures == null) {
                    if (other$pictures != null) {
                        return false;
                    }
                } else if (!this$pictures.equals(other$pictures)) {
                    return false;
                }
                Object this$officialSite = getOfficialSite();
                Object other$officialSite = other.getOfficialSite();
                if (this$officialSite == null) {
                    if (other$officialSite != null) {
                        return false;
                    }
                } else if (!this$officialSite.equals(other$officialSite)) {
                    return false;
                }
                Object this$lanName = getLanName();
                Object other$lanName = other.getLanName();
                if (this$lanName == null) {
                    if (other$lanName != null) {
                        return false;
                    }
                } else if (!this$lanName.equals(other$lanName)) {
                    return false;
                }
                Object this$version = getVersion();
                Object other$version = other.getVersion();
                if (this$version == null) {
                    if (other$version != null) {
                        return false;
                    }
                } else if (!this$version.equals(other$version)) {
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
                Object this$dependencies = getDependencies();
                Object other$dependencies = other.getDependencies();
                if (this$dependencies == null) {
                    if (other$dependencies != null) {
                        return false;
                    }
                } else if (!this$dependencies.equals(other$dependencies)) {
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
                Object this$lastGameVersion = getLastGameVersion();
                Object other$lastGameVersion = other.getLastGameVersion();
                return this$lastGameVersion == null ? other$lastGameVersion == null : this$lastGameVersion.equals(other$lastGameVersion);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameEntityDTO;
    }

    public int hashCode() {
        int result = (1 * 59) + (isFavorite() ? 79 : 97);
        int result2 = (((((((result * 59) + (isUserInstall() ? 79 : 97)) * 59) + (isPopulateStatus() ? 79 : 97)) * 59) + (isAvailable() ? 79 : 97)) * 59) + (isParser() ? 79 : 97);
        long $totalComments = getTotalComments();
        int result3 = (result2 * 59) + ((int) ($totalComments ^ ($totalComments >>> 32)));
        Object $id = getId();
        int result4 = (result3 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $picture = getPicture();
        int result5 = (result4 * 59) + ($picture == null ? 43 : $picture.hashCode());
        Object $installed = getInstalled();
        int result6 = (result5 * 59) + ($installed == null ? 43 : $installed.hashCode());
        Object $downloadMonth = getDownloadMonth();
        int result7 = (result6 * 59) + ($downloadMonth == null ? 43 : $downloadMonth.hashCode());
        Object $watched = getWatched();
        int result8 = (result7 * 59) + ($watched == null ? 43 : $watched.hashCode());
        Object $updated = getUpdated();
        int result9 = (result8 * 59) + ($updated == null ? 43 : $updated.hashCode());
        Object $update = getUpdate();
        int result10 = (result9 * 59) + ($update == null ? 43 : $update.hashCode());
        Object $downloadALL = getDownloadALL();
        int result11 = (result10 * 59) + ($downloadALL == null ? 43 : $downloadALL.hashCode());
        Object $name = getName();
        int result12 = (result11 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $author = getAuthor();
        int result13 = (result12 * 59) + ($author == null ? 43 : $author.hashCode());
        Object $uuid = getUuid();
        int result14 = (result13 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $linkProject = getLinkProject();
        int result15 = (result14 * 59) + ($linkProject == null ? 43 : $linkProject.hashCode());
        Object $tlmodsLinkProject = getTlmodsLinkProject();
        int result16 = (result15 * 59) + ($tlmodsLinkProject == null ? 43 : $tlmodsLinkProject.hashCode());
        Object $descriptions = getDescriptions();
        int result17 = (result16 * 59) + ($descriptions == null ? 43 : $descriptions.hashCode());
        Object $shortDescription = getShortDescription();
        int result18 = (result17 * 59) + ($shortDescription == null ? 43 : $shortDescription.hashCode());
        Object $description = getDescription();
        int result19 = (result18 * 59) + ($description == null ? 43 : $description.hashCode());
        Object $enDescription = getEnDescription();
        int result20 = (result19 * 59) + ($enDescription == null ? 43 : $enDescription.hashCode());
        Object $type = getType();
        int result21 = (result20 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $categories = getCategories();
        int result22 = (result21 * 59) + ($categories == null ? 43 : $categories.hashCode());
        Object $categories1 = getCategories1();
        int result23 = (result22 * 59) + ($categories1 == null ? 43 : $categories1.hashCode());
        Object $tags = getTags();
        int result24 = (result23 * 59) + ($tags == null ? 43 : $tags.hashCode());
        Object $pictures = getPictures();
        int result25 = (result24 * 59) + ($pictures == null ? 43 : $pictures.hashCode());
        Object $officialSite = getOfficialSite();
        int result26 = (result25 * 59) + ($officialSite == null ? 43 : $officialSite.hashCode());
        Object $lanName = getLanName();
        int result27 = (result26 * 59) + ($lanName == null ? 43 : $lanName.hashCode());
        Object $version = getVersion();
        int result28 = (result27 * 59) + ($version == null ? 43 : $version.hashCode());
        Object $versions = getVersions();
        int result29 = (result28 * 59) + ($versions == null ? 43 : $versions.hashCode());
        Object $dependencies = getDependencies();
        int result30 = (result29 * 59) + ($dependencies == null ? 43 : $dependencies.hashCode());
        Object $gameVersions = getGameVersions();
        int result31 = (result30 * 59) + ($gameVersions == null ? 43 : $gameVersions.hashCode());
        Object $lastGameVersion = getLastGameVersion();
        return (result31 * 59) + ($lastGameVersion == null ? 43 : $lastGameVersion.hashCode());
    }
}
