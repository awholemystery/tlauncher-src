package net.minecraft.launcher.updater;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.launcher.updater.VersionList;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/RepositoryBasedVersionList.class */
public class RepositoryBasedVersionList extends RemoteVersionList {
    protected final Repo repo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RepositoryBasedVersionList(Repo repo) {
        if (repo == null) {
            throw new NullPointerException();
        }
        this.repo = repo;
    }

    @Override // net.minecraft.launcher.updater.VersionList
    public VersionList.RawVersionList getRawList() throws IOException {
        VersionList.RawVersionList rawList = super.getRawList();
        for (Version version : rawList.getVersions()) {
            version.setSource(this.repo);
        }
        return rawList;
    }

    @Override // net.minecraft.launcher.updater.VersionList
    public CompleteVersion getCompleteVersion(Version version) throws JsonSyntaxException, IOException {
        CompleteVersion complete = super.getCompleteVersion(version);
        complete.setSource(this.repo);
        return complete;
    }

    @Override // net.minecraft.launcher.updater.VersionList
    public boolean hasAllFiles(CompleteVersion paramCompleteVersion, OS paramOperatingSystem) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.minecraft.launcher.updater.VersionList
    public String getUrl(String uri) throws IOException {
        return this.repo.getUrl(uri);
    }
}
