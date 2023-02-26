package org.tlauncher.modpack.domain.client.share;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/SiteParams.class */
public class SiteParams {
    private GameType type;
    private String lang;
    private Integer page;
    private Integer pageSize;
    private Object gameVersion;
    private List<String> categories;
    private String search;
    private String jwt;
    private NameIdDTO minecraftVersionType;

    public GameType getType() {
        return this.type;
    }

    public String getLang() {
        return this.lang;
    }

    public Integer getPage() {
        return this.page;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Object getGameVersion() {
        return this.gameVersion;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public String getSearch() {
        return this.search;
    }

    public String getJwt() {
        return this.jwt;
    }

    public NameIdDTO getMinecraftVersionType() {
        return this.minecraftVersionType;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setGameVersion(Object gameVersion) {
        this.gameVersion = gameVersion;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setMinecraftVersionType(NameIdDTO minecraftVersionType) {
        this.minecraftVersionType = minecraftVersionType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof SiteParams) {
            SiteParams other = (SiteParams) o;
            if (other.canEqual(this)) {
                Object this$page = getPage();
                Object other$page = other.getPage();
                if (this$page == null) {
                    if (other$page != null) {
                        return false;
                    }
                } else if (!this$page.equals(other$page)) {
                    return false;
                }
                Object this$pageSize = getPageSize();
                Object other$pageSize = other.getPageSize();
                if (this$pageSize == null) {
                    if (other$pageSize != null) {
                        return false;
                    }
                } else if (!this$pageSize.equals(other$pageSize)) {
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
                Object this$lang = getLang();
                Object other$lang = other.getLang();
                if (this$lang == null) {
                    if (other$lang != null) {
                        return false;
                    }
                } else if (!this$lang.equals(other$lang)) {
                    return false;
                }
                Object this$gameVersion = getGameVersion();
                Object other$gameVersion = other.getGameVersion();
                if (this$gameVersion == null) {
                    if (other$gameVersion != null) {
                        return false;
                    }
                } else if (!this$gameVersion.equals(other$gameVersion)) {
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
                Object this$search = getSearch();
                Object other$search = other.getSearch();
                if (this$search == null) {
                    if (other$search != null) {
                        return false;
                    }
                } else if (!this$search.equals(other$search)) {
                    return false;
                }
                Object this$jwt = getJwt();
                Object other$jwt = other.getJwt();
                if (this$jwt == null) {
                    if (other$jwt != null) {
                        return false;
                    }
                } else if (!this$jwt.equals(other$jwt)) {
                    return false;
                }
                Object this$minecraftVersionType = getMinecraftVersionType();
                Object other$minecraftVersionType = other.getMinecraftVersionType();
                return this$minecraftVersionType == null ? other$minecraftVersionType == null : this$minecraftVersionType.equals(other$minecraftVersionType);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SiteParams;
    }

    public int hashCode() {
        Object $page = getPage();
        int result = (1 * 59) + ($page == null ? 43 : $page.hashCode());
        Object $pageSize = getPageSize();
        int result2 = (result * 59) + ($pageSize == null ? 43 : $pageSize.hashCode());
        Object $type = getType();
        int result3 = (result2 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $lang = getLang();
        int result4 = (result3 * 59) + ($lang == null ? 43 : $lang.hashCode());
        Object $gameVersion = getGameVersion();
        int result5 = (result4 * 59) + ($gameVersion == null ? 43 : $gameVersion.hashCode());
        Object $categories = getCategories();
        int result6 = (result5 * 59) + ($categories == null ? 43 : $categories.hashCode());
        Object $search = getSearch();
        int result7 = (result6 * 59) + ($search == null ? 43 : $search.hashCode());
        Object $jwt = getJwt();
        int result8 = (result7 * 59) + ($jwt == null ? 43 : $jwt.hashCode());
        Object $minecraftVersionType = getMinecraftVersionType();
        return (result8 * 59) + ($minecraftVersionType == null ? 43 : $minecraftVersionType.hashCode());
    }

    public String toString() {
        return "SiteParams(type=" + getType() + ", lang=" + getLang() + ", page=" + getPage() + ", pageSize=" + getPageSize() + ", gameVersion=" + getGameVersion() + ", categories=" + getCategories() + ", search=" + getSearch() + ", jwt=" + getJwt() + ", minecraftVersionType=" + getMinecraftVersionType() + ")";
    }

    public SiteParams(GameType type, String lang, Integer page, Integer pageSize, Object gameVersion, List<String> categories, String search, String jwt, NameIdDTO minecraftVersionType) {
        this.type = type;
        this.lang = lang;
        this.page = page;
        this.pageSize = pageSize;
        this.gameVersion = gameVersion;
        this.categories = categories;
        this.search = search;
        this.jwt = jwt;
        this.minecraftVersionType = minecraftVersionType;
    }
}
