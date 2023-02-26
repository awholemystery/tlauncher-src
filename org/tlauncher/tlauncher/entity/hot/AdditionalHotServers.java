package org.tlauncher.tlauncher.entity.hot;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/hot/AdditionalHotServers.class */
public class AdditionalHotServers {
    private List<AdditionalHotServer> list;
    private HotBanner upBanner;
    private HotBanner downBanner;
    private boolean shuffle;

    public void setList(List<AdditionalHotServer> list) {
        this.list = list;
    }

    public void setUpBanner(HotBanner upBanner) {
        this.upBanner = upBanner;
    }

    public void setDownBanner(HotBanner downBanner) {
        this.downBanner = downBanner;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AdditionalHotServers) {
            AdditionalHotServers other = (AdditionalHotServers) o;
            if (other.canEqual(this)) {
                Object this$list = getList();
                Object other$list = other.getList();
                if (this$list == null) {
                    if (other$list != null) {
                        return false;
                    }
                } else if (!this$list.equals(other$list)) {
                    return false;
                }
                Object this$upBanner = getUpBanner();
                Object other$upBanner = other.getUpBanner();
                if (this$upBanner == null) {
                    if (other$upBanner != null) {
                        return false;
                    }
                } else if (!this$upBanner.equals(other$upBanner)) {
                    return false;
                }
                Object this$downBanner = getDownBanner();
                Object other$downBanner = other.getDownBanner();
                if (this$downBanner == null) {
                    if (other$downBanner != null) {
                        return false;
                    }
                } else if (!this$downBanner.equals(other$downBanner)) {
                    return false;
                }
                return isShuffle() == other.isShuffle();
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdditionalHotServers;
    }

    public int hashCode() {
        Object $list = getList();
        int result = (1 * 59) + ($list == null ? 43 : $list.hashCode());
        Object $upBanner = getUpBanner();
        int result2 = (result * 59) + ($upBanner == null ? 43 : $upBanner.hashCode());
        Object $downBanner = getDownBanner();
        return (((result2 * 59) + ($downBanner == null ? 43 : $downBanner.hashCode())) * 59) + (isShuffle() ? 79 : 97);
    }

    public String toString() {
        return "AdditionalHotServers(list=" + getList() + ", upBanner=" + getUpBanner() + ", downBanner=" + getDownBanner() + ", shuffle=" + isShuffle() + ")";
    }

    public List<AdditionalHotServer> getList() {
        return this.list;
    }

    public HotBanner getUpBanner() {
        return this.upBanner;
    }

    public HotBanner getDownBanner() {
        return this.downBanner;
    }

    public boolean isShuffle() {
        return this.shuffle;
    }
}
