package org.tlauncher.tlauncher.managers;

import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/VersionSyncInfoContainer.class */
public class VersionSyncInfoContainer extends DownloadableContainer {
    private final VersionSyncInfo version;

    public VersionSyncInfoContainer(VersionSyncInfo version) {
        if (version == null) {
            throw new NullPointerException();
        }
        this.version = version;
    }

    public VersionSyncInfo getVersion() {
        return this.version;
    }
}
