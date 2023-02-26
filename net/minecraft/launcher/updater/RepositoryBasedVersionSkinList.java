package net.minecraft.launcher.updater;

import java.io.IOException;
import net.minecraft.launcher.updater.VersionList;
import net.minecraft.launcher.versions.PartialVersion;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.util.Time;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/RepositoryBasedVersionSkinList.class */
public class RepositoryBasedVersionSkinList extends RepositoryBasedVersionList {
    public RepositoryBasedVersionSkinList(Repo repo) {
        super(repo);
    }

    @Override // net.minecraft.launcher.updater.RepositoryBasedVersionList, net.minecraft.launcher.updater.VersionList
    public VersionList.RawVersionList getRawList() throws IOException {
        Object lock = new Object();
        Time.start(lock);
        VersionList.RawVersionList list = (VersionList.RawVersionList) this.gson.fromJson(getUrl("versions/versions-1.0.json"), (Class<Object>) VersionList.RawVersionList.class);
        for (PartialVersion version : list.versions) {
            version.setVersionList(this);
        }
        log("Got in", Long.valueOf(Time.stop(lock)), "ms");
        for (Version version2 : list.getVersions()) {
            version2.setSource(this.repo);
        }
        return list;
    }
}
