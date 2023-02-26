package net.minecraft.launcher.updater;

import net.minecraft.launcher.versions.ReleaseType;
import net.minecraft.launcher.versions.Version;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/LatestVersionSyncInfo.class */
public class LatestVersionSyncInfo extends VersionSyncInfo {
    private final ReleaseType type;

    public LatestVersionSyncInfo(ReleaseType type, Version localVersion, Version remoteVersion) {
        super(localVersion, remoteVersion);
        if (type == null) {
            throw new NullPointerException("ReleaseType cannot be NULL!");
        }
        this.type = type;
        setID("latest-" + type.toString());
    }

    public LatestVersionSyncInfo(ReleaseType type, VersionSyncInfo syncInfo) {
        this(type, syncInfo.getLocal(), syncInfo.getRemote());
    }

    public String getVersionID() {
        if (this.localVersion != null) {
            return this.localVersion.getID();
        }
        if (this.remoteVersion != null) {
            return this.remoteVersion.getID();
        }
        return null;
    }

    public ReleaseType getReleaseType() {
        return this.type;
    }

    @Override // net.minecraft.launcher.updater.VersionSyncInfo
    public String toString() {
        return getClass().getSimpleName() + "{id='" + getID() + "', releaseType=" + this.type + ",\nlocal=" + this.localVersion + ",\nremote=" + this.remoteVersion + ", isInstalled=" + isInstalled() + ", hasRemote=" + hasRemote() + ", isUpToDate=" + isUpToDate() + "}";
    }
}
