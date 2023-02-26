package org.tlauncher.tlauncher.ui.versions;

import java.util.List;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.managers.VersionManager;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionHandlerListener.class */
public interface VersionHandlerListener {
    void onVersionRefreshing(VersionManager versionManager);

    void onVersionRefreshed(VersionManager versionManager);

    void onVersionSelected(List<VersionSyncInfo> list);

    void onVersionDeselected();

    void onVersionDownload(List<VersionSyncInfo> list);
}
