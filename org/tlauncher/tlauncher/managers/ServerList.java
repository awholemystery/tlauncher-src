package org.tlauncher.tlauncher.managers;

import java.util.ArrayList;
import java.util.List;
import org.tlauncher.tlauncher.entity.server.RemoteServer;
import org.tlauncher.tlauncher.entity.server.ReplacedAddressServer;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ServerList.class */
public class ServerList {
    private List<RemoteServer> newServers = new ArrayList();
    private List<String> removedServers = new ArrayList();
    private List<ReplacedAddressServer> clientChangedAddress = new ArrayList();

    public void setNewServers(List<RemoteServer> newServers) {
        this.newServers = newServers;
    }

    public void setRemovedServers(List<String> removedServers) {
        this.removedServers = removedServers;
    }

    public void setClientChangedAddress(List<ReplacedAddressServer> clientChangedAddress) {
        this.clientChangedAddress = clientChangedAddress;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ServerList) {
            ServerList other = (ServerList) o;
            if (other.canEqual(this)) {
                Object this$newServers = getNewServers();
                Object other$newServers = other.getNewServers();
                if (this$newServers == null) {
                    if (other$newServers != null) {
                        return false;
                    }
                } else if (!this$newServers.equals(other$newServers)) {
                    return false;
                }
                Object this$removedServers = getRemovedServers();
                Object other$removedServers = other.getRemovedServers();
                if (this$removedServers == null) {
                    if (other$removedServers != null) {
                        return false;
                    }
                } else if (!this$removedServers.equals(other$removedServers)) {
                    return false;
                }
                Object this$clientChangedAddress = getClientChangedAddress();
                Object other$clientChangedAddress = other.getClientChangedAddress();
                return this$clientChangedAddress == null ? other$clientChangedAddress == null : this$clientChangedAddress.equals(other$clientChangedAddress);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerList;
    }

    public int hashCode() {
        Object $newServers = getNewServers();
        int result = (1 * 59) + ($newServers == null ? 43 : $newServers.hashCode());
        Object $removedServers = getRemovedServers();
        int result2 = (result * 59) + ($removedServers == null ? 43 : $removedServers.hashCode());
        Object $clientChangedAddress = getClientChangedAddress();
        return (result2 * 59) + ($clientChangedAddress == null ? 43 : $clientChangedAddress.hashCode());
    }

    public String toString() {
        return "ServerList(newServers=" + getNewServers() + ", removedServers=" + getRemovedServers() + ", clientChangedAddress=" + getClientChangedAddress() + ")";
    }

    public List<RemoteServer> getNewServers() {
        return this.newServers;
    }

    public List<String> getRemovedServers() {
        return this.removedServers;
    }

    public List<ReplacedAddressServer> getClientChangedAddress() {
        return this.clientChangedAddress;
    }
}
