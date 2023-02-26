package net.minecraft.launcher.updater;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Collections;
import net.minecraft.launcher.updater.VersionList;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.PartialVersion;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.util.Time;
import org.tlauncher.util.TlauncherUtil;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/UpgratedRepository.class */
public abstract class UpgratedRepository extends RepositoryBasedVersionList {
    /* JADX INFO: Access modifiers changed from: package-private */
    public UpgratedRepository(Repo repo) {
        super(repo);
    }

    @Override // net.minecraft.launcher.updater.RepositoryBasedVersionList, net.minecraft.launcher.updater.VersionList
    public VersionList.RawVersionList getRawList() throws IOException {
        Object lock = new Object();
        Time.start(lock);
        VersionList.RawVersionList list = (VersionList.RawVersionList) this.gson.fromJson(this.repo.getUrl("version_manifest.json"), (Class<Object>) VersionList.RawVersionList.class);
        for (PartialVersion version : list.versions) {
            version.setVersionList(this);
        }
        log("Got in", Long.valueOf(Time.stop(lock)), "ms");
        for (Version version2 : list.getVersions()) {
            version2.setSource(this.repo);
        }
        return list;
    }

    @Override // net.minecraft.launcher.updater.RepositoryBasedVersionList, net.minecraft.launcher.updater.VersionList
    public CompleteVersion getCompleteVersion(Version version) throws JsonSyntaxException, IOException {
        if (version instanceof CompleteVersion) {
            return (CompleteVersion) version;
        }
        if (version == null) {
            throw new NullPointerException("Version cannot be NULL!");
        }
        String value = ClientInstanceRepo.EMPTY_REPO.getUrl(version.getUrl());
        CompleteVersion complete = (CompleteVersion) this.gson.fromJson(value, (Class<Object>) CompleteVersion.class);
        TlauncherUtil.processRemoteVersionToSave(complete, value, this.gson);
        complete.getModifiedVersion().setUpdatedTime(version.getUpdatedTime());
        complete.setID(version.getID());
        complete.setVersionList(this);
        complete.setUpdatedTime(version.getUpdatedTime());
        Collections.replaceAll(this.versions, version, complete);
        complete.setSource(this.repo);
        return complete;
    }
}
