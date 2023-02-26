package org.tlauncher.tlauncher.managers;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/VersionManagerListener.class */
public interface VersionManagerListener {
    void onVersionsRefreshing(VersionManager versionManager);

    void onVersionsRefreshingFailed(VersionManager versionManager);

    void onVersionsRefreshed(VersionManager versionManager);
}
