package org.tlauncher.tlauncher.managers;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ServerListManagerListener.class */
public interface ServerListManagerListener {
    void onServersRefreshing(ServerListManager serverListManager);

    void onServersRefreshingFailed(ServerListManager serverListManager);

    void onServersRefreshed(ServerListManager serverListManager);
}
