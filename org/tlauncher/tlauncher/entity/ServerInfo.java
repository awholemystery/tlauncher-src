package org.tlauncher.tlauncher.entity;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/ServerInfo.class */
public class ServerInfo {
    protected String serverId;
    protected String address;
    protected String minVersion;
    protected List<String> ignoreVersions = new ArrayList();
    protected List<String> includeVersions = new ArrayList();
    private String redirectAddress;
    private boolean mojangAccount;

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public void setIgnoreVersions(List<String> ignoreVersions) {
        this.ignoreVersions = ignoreVersions;
    }

    public void setIncludeVersions(List<String> includeVersions) {
        this.includeVersions = includeVersions;
    }

    public void setRedirectAddress(String redirectAddress) {
        this.redirectAddress = redirectAddress;
    }

    public void setMojangAccount(boolean mojangAccount) {
        this.mojangAccount = mojangAccount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ServerInfo) {
            ServerInfo other = (ServerInfo) o;
            if (other.canEqual(this)) {
                Object this$serverId = getServerId();
                Object other$serverId = other.getServerId();
                if (this$serverId == null) {
                    if (other$serverId != null) {
                        return false;
                    }
                } else if (!this$serverId.equals(other$serverId)) {
                    return false;
                }
                Object this$address = getAddress();
                Object other$address = other.getAddress();
                if (this$address == null) {
                    if (other$address != null) {
                        return false;
                    }
                } else if (!this$address.equals(other$address)) {
                    return false;
                }
                Object this$minVersion = getMinVersion();
                Object other$minVersion = other.getMinVersion();
                if (this$minVersion == null) {
                    if (other$minVersion != null) {
                        return false;
                    }
                } else if (!this$minVersion.equals(other$minVersion)) {
                    return false;
                }
                Object this$ignoreVersions = getIgnoreVersions();
                Object other$ignoreVersions = other.getIgnoreVersions();
                if (this$ignoreVersions == null) {
                    if (other$ignoreVersions != null) {
                        return false;
                    }
                } else if (!this$ignoreVersions.equals(other$ignoreVersions)) {
                    return false;
                }
                Object this$includeVersions = getIncludeVersions();
                Object other$includeVersions = other.getIncludeVersions();
                if (this$includeVersions == null) {
                    if (other$includeVersions != null) {
                        return false;
                    }
                } else if (!this$includeVersions.equals(other$includeVersions)) {
                    return false;
                }
                Object this$redirectAddress = getRedirectAddress();
                Object other$redirectAddress = other.getRedirectAddress();
                if (this$redirectAddress == null) {
                    if (other$redirectAddress != null) {
                        return false;
                    }
                } else if (!this$redirectAddress.equals(other$redirectAddress)) {
                    return false;
                }
                return isMojangAccount() == other.isMojangAccount();
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerInfo;
    }

    public int hashCode() {
        Object $serverId = getServerId();
        int result = (1 * 59) + ($serverId == null ? 43 : $serverId.hashCode());
        Object $address = getAddress();
        int result2 = (result * 59) + ($address == null ? 43 : $address.hashCode());
        Object $minVersion = getMinVersion();
        int result3 = (result2 * 59) + ($minVersion == null ? 43 : $minVersion.hashCode());
        Object $ignoreVersions = getIgnoreVersions();
        int result4 = (result3 * 59) + ($ignoreVersions == null ? 43 : $ignoreVersions.hashCode());
        Object $includeVersions = getIncludeVersions();
        int result5 = (result4 * 59) + ($includeVersions == null ? 43 : $includeVersions.hashCode());
        Object $redirectAddress = getRedirectAddress();
        return (((result5 * 59) + ($redirectAddress == null ? 43 : $redirectAddress.hashCode())) * 59) + (isMojangAccount() ? 79 : 97);
    }

    public String toString() {
        return "ServerInfo(serverId=" + getServerId() + ", address=" + getAddress() + ", minVersion=" + getMinVersion() + ", ignoreVersions=" + getIgnoreVersions() + ", includeVersions=" + getIncludeVersions() + ", redirectAddress=" + getRedirectAddress() + ", mojangAccount=" + isMojangAccount() + ")";
    }

    public String getServerId() {
        return this.serverId;
    }

    public String getAddress() {
        return this.address;
    }

    public String getMinVersion() {
        return this.minVersion;
    }

    public List<String> getIgnoreVersions() {
        return this.ignoreVersions;
    }

    public List<String> getIncludeVersions() {
        return this.includeVersions;
    }

    public String getRedirectAddress() {
        return this.redirectAddress;
    }

    public boolean isMojangAccount() {
        return this.mojangAccount;
    }
}
