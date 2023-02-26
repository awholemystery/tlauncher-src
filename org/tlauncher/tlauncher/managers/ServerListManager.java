package org.tlauncher.tlauncher.managers;

import com.google.gson.Gson;
import org.tlauncher.tlauncher.component.RefreshableComponent;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ServerListManager.class */
public class ServerListManager extends RefreshableComponent {
    private final Gson gson;
    private final Repo repo;
    private ServerList serverList;

    public ServerListManager(ComponentManager manager) throws Exception {
        super(manager);
        this.serverList = new ServerList();
        this.repo = ClientInstanceRepo.SERVER_LIST_REPO;
        this.gson = TLauncher.getGson();
    }

    @Override // org.tlauncher.tlauncher.component.RefreshableComponent
    protected boolean refresh() {
        try {
            this.serverList = (ServerList) this.gson.fromJson(this.repo.getUrl(), (Class<Object>) ServerList.class);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public ServerList getList() {
        return this.serverList;
    }
}
