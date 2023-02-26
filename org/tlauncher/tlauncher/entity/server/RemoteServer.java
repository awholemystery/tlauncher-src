package org.tlauncher.tlauncher.entity.server;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/server/RemoteServer.class */
public class RemoteServer extends Server {
    private Date addedDate;
    private ServerState state;
    @SerializedName(value = "mojangAccount", alternate = {"officialAccount"})
    private boolean officialAccount;
    private boolean remote;
    private Integer recoveryServerTime = 0;
    private Integer maxRemovingCountServer = 0;
    private List<Long> removedTime = new ArrayList();
    private Set<String> locales = new HashSet();

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/server/RemoteServer$ServerState.class */
    public enum ServerState {
        DEACTIVATED,
        ACTIVE
    }

    public void setRemovedTime(List<Long> removedTime) {
        this.removedTime = removedTime;
    }

    public void setRecoveryServerTime(Integer recoveryServerTime) {
        this.recoveryServerTime = recoveryServerTime;
    }

    public void setMaxRemovingCountServer(Integer maxRemovingCountServer) {
        this.maxRemovingCountServer = maxRemovingCountServer;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public void setState(ServerState state) {
        this.state = state;
    }

    public void setLocales(Set<String> locales) {
        this.locales = locales;
    }

    public void setOfficialAccount(boolean officialAccount) {
        this.officialAccount = officialAccount;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    @Override // org.tlauncher.tlauncher.entity.server.Server
    public String toString() {
        return "RemoteServer(removedTime=" + getRemovedTime() + ", recoveryServerTime=" + getRecoveryServerTime() + ", maxRemovingCountServer=" + getMaxRemovingCountServer() + ", addedDate=" + getAddedDate() + ", state=" + getState() + ", locales=" + getLocales() + ", officialAccount=" + isOfficialAccount() + ", remote=" + isRemote() + ")";
    }

    public List<Long> getRemovedTime() {
        return this.removedTime;
    }

    public Integer getRecoveryServerTime() {
        return this.recoveryServerTime;
    }

    public Integer getMaxRemovingCountServer() {
        return this.maxRemovingCountServer;
    }

    public Date getAddedDate() {
        return this.addedDate;
    }

    public ServerState getState() {
        return this.state;
    }

    public Set<String> getLocales() {
        return this.locales;
    }

    public boolean isOfficialAccount() {
        return this.officialAccount;
    }

    public boolean isRemote() {
        return this.remote;
    }

    public void initRemote() {
        setRemote(true);
        this.addedDate = new Date();
        setState(ServerState.ACTIVE);
    }

    @Override // org.tlauncher.tlauncher.entity.server.Server
    public int hashCode() {
        return super.hashCode();
    }

    @Override // org.tlauncher.tlauncher.entity.server.Server
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
