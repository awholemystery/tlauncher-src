package org.tlauncher.tlauncher.ui.menu;

import java.util.List;
import java.util.Objects;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.entity.ServerInfo;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/menu/PopupMenuModel.class */
public class PopupMenuModel {
    private List<VersionSyncInfo> servers;
    private ServerInfo info;
    private boolean mainPage;
    private VersionSyncInfo selected;

    public void setServers(List<VersionSyncInfo> servers) {
        this.servers = servers;
    }

    public void setInfo(ServerInfo info) {
        this.info = info;
    }

    public void setMainPage(boolean mainPage) {
        this.mainPage = mainPage;
    }

    public void setSelected(VersionSyncInfo selected) {
        this.selected = selected;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof PopupMenuModel) {
            PopupMenuModel other = (PopupMenuModel) o;
            if (other.canEqual(this)) {
                Object this$servers = getServers();
                Object other$servers = other.getServers();
                if (this$servers == null) {
                    if (other$servers != null) {
                        return false;
                    }
                } else if (!this$servers.equals(other$servers)) {
                    return false;
                }
                Object this$info = getInfo();
                Object other$info = other.getInfo();
                if (this$info == null) {
                    if (other$info != null) {
                        return false;
                    }
                } else if (!this$info.equals(other$info)) {
                    return false;
                }
                if (isMainPage() != other.isMainPage()) {
                    return false;
                }
                Object this$selected = getSelected();
                Object other$selected = other.getSelected();
                return this$selected == null ? other$selected == null : this$selected.equals(other$selected);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PopupMenuModel;
    }

    public int hashCode() {
        Object $servers = getServers();
        int result = (1 * 59) + ($servers == null ? 43 : $servers.hashCode());
        Object $info = getInfo();
        int result2 = (((result * 59) + ($info == null ? 43 : $info.hashCode())) * 59) + (isMainPage() ? 79 : 97);
        Object $selected = getSelected();
        return (result2 * 59) + ($selected == null ? 43 : $selected.hashCode());
    }

    public String toString() {
        return "PopupMenuModel(servers=" + getServers() + ", info=" + getInfo() + ", mainPage=" + isMainPage() + ", selected=" + getSelected() + ")";
    }

    public List<VersionSyncInfo> getServers() {
        return this.servers;
    }

    public boolean isMainPage() {
        return this.mainPage;
    }

    public VersionSyncInfo getSelected() {
        return this.selected;
    }

    public ServerInfo getInfo() {
        return this.info;
    }

    public PopupMenuModel(List<VersionSyncInfo> servers, ServerInfo info, boolean mainPage) {
        this.info = info;
        this.servers = servers;
        this.mainPage = mainPage;
    }

    public String getName() {
        return getAddress().split(":")[0];
    }

    public String getAddress() {
        return this.info.getAddress();
    }

    public String getResolvedAddress() {
        if (Objects.nonNull(this.info.getRedirectAddress())) {
            return this.info.getRedirectAddress();
        }
        return getAddress();
    }

    public String getServerId() {
        return this.info.getServerId();
    }
}
