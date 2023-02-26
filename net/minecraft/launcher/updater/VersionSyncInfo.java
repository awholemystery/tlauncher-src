package net.minecraft.launcher.updater;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.Library;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/VersionSyncInfo.class */
public class VersionSyncInfo {
    Version localVersion;
    Version remoteVersion;
    private CompleteVersion completeLocal;
    private CompleteVersion completeRemote;
    private String id;

    public VersionSyncInfo(Version localVersion, Version remoteVersion) {
        if (localVersion == null && remoteVersion == null) {
            throw new NullPointerException("Cannot createScrollWrapper sync info from NULLs!");
        }
        this.localVersion = localVersion;
        this.remoteVersion = remoteVersion;
        if (localVersion != null && remoteVersion != null) {
            localVersion.setVersionList(remoteVersion.getVersionList());
        }
        if (getID() == null) {
            throw new NullPointerException("Cannot createScrollWrapper sync info from versions that have NULL IDs");
        }
    }

    public VersionSyncInfo(VersionSyncInfo info) {
        this(info.getLocal(), info.getRemote());
    }

    private VersionSyncInfo() {
        this.localVersion = null;
        this.remoteVersion = null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (getID() == null || !(o instanceof VersionSyncInfo)) {
            return false;
        }
        VersionSyncInfo v = (VersionSyncInfo) o;
        return getID().equals(v.getID());
    }

    public Version getLocal() {
        return this.localVersion;
    }

    public void setLocal(Version version) {
        this.localVersion = version;
        if (version instanceof CompleteVersion) {
            this.completeLocal = (CompleteVersion) version;
        }
    }

    public Version getRemote() {
        return this.remoteVersion;
    }

    public void setRemote(Version version) {
        this.remoteVersion = version;
        if (version instanceof CompleteVersion) {
            this.completeRemote = (CompleteVersion) version;
        }
    }

    public String getID() {
        if (this.id != null) {
            return this.id;
        }
        if (this.localVersion != null) {
            return this.localVersion.getID();
        }
        if (this.remoteVersion != null) {
            return this.remoteVersion.getID();
        }
        return null;
    }

    public void setID(String id) {
        if (id != null && id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty!");
        }
        this.id = id;
    }

    public Version getLatestVersion() {
        if (this.remoteVersion != null) {
            return this.remoteVersion;
        }
        return this.localVersion;
    }

    public Version getAvailableVersion() {
        if (this.localVersion != null) {
            return this.localVersion;
        }
        return this.remoteVersion;
    }

    public boolean isInstalled() {
        return this.localVersion != null;
    }

    public boolean hasRemote() {
        return this.remoteVersion != null;
    }

    public boolean isUpToDate() {
        if (this.localVersion == null) {
            return false;
        }
        return this.remoteVersion == null || this.localVersion.getUpdatedTime().compareTo(this.remoteVersion.getUpdatedTime()) >= 0;
    }

    public String toString() {
        return getClass().getSimpleName() + "{id='" + getID() + "',\nlocal=" + this.localVersion + ",\nremote=" + this.remoteVersion + ", isInstalled=" + isInstalled() + ", hasRemote=" + hasRemote() + ", isUpToDate=" + isUpToDate() + "}";
    }

    public CompleteVersion resolveCompleteVersion(VersionManager manager, boolean latest) throws IOException {
        Version version;
        if (latest) {
            version = getLatestVersion();
        } else if (isInstalled()) {
            version = getLocal();
        } else {
            version = getRemote();
        }
        if (version.equals(this.localVersion) && this.completeLocal != null && this.completeLocal.getInheritsFrom() == null) {
            return this.completeLocal;
        }
        if (version.equals(this.remoteVersion) && this.completeRemote != null && this.completeRemote.getInheritsFrom() == null) {
            return this.completeRemote;
        }
        CompleteVersion complete = version.getVersionList().getCompleteVersion(version).resolve(manager, latest);
        if (version == this.localVersion) {
            this.completeLocal = complete;
        } else if (version == this.remoteVersion) {
            this.completeRemote = complete;
        }
        return complete;
    }

    public CompleteVersion getCompleteVersion(boolean latest) throws IOException {
        Version version;
        if (latest) {
            version = getLatestVersion();
        } else if (isInstalled()) {
            version = getLocal();
        } else {
            version = getRemote();
        }
        if (version.equals(this.localVersion) && this.completeLocal != null) {
            return this.completeLocal;
        }
        if (version.equals(this.remoteVersion) && this.completeRemote != null) {
            return this.completeRemote;
        }
        CompleteVersion complete = version.getVersionList().getCompleteVersion(version);
        if (version == this.localVersion) {
            this.completeLocal = complete;
        } else if (version == this.remoteVersion) {
            this.completeRemote = complete;
        }
        return complete;
    }

    public CompleteVersion getLatestCompleteVersion() throws IOException {
        return getCompleteVersion(true);
    }

    public CompleteVersion getLocalCompleteVersion() {
        return this.completeLocal;
    }

    private Set<Downloadable> getRequiredDownloadables(OS os, File targetDirectory, boolean force, boolean tlauncher) throws IOException {
        Set<Downloadable> neededFiles = new HashSet<>();
        CompleteVersion version = TLauncher.getInstance().getTLauncherManager().addFilesForDownloading(getCompleteVersion(force), tlauncher);
        Repo source = hasRemote() ? this.remoteVersion.getSource() : ClientInstanceRepo.OFFICIAL_VERSION_REPO;
        if (!source.isSelectable()) {
            return neededFiles;
        }
        for (Library library : version.getRelevantLibraries()) {
            File local = analizeFolderLibrary(os, targetDirectory, library);
            if (Objects.nonNull(local)) {
                neededFiles.add(library.getDownloadable(source, local, os));
            }
        }
        if (Objects.nonNull(version.getModsLibraries())) {
            for (Library library2 : version.getModsLibraries()) {
                File local2 = analizeFolderLibrary(os, targetDirectory, library2);
                if (Objects.nonNull(local2)) {
                    neededFiles.add(library2.getDownloadable(source, local2, os));
                }
            }
        }
        return neededFiles;
    }

    private File analizeFolderLibrary(OS os, File targetDirectory, Library library) {
        String file = null;
        if (library.getNatives() != null) {
            String natives = library.getNatives().get(os);
            if (natives != null) {
                file = library.getArtifactPath(natives);
            }
        } else {
            file = library.getArtifactPath();
        }
        if (file == null) {
            return null;
        }
        File local = new File(targetDirectory, "libraries/" + file);
        if (local.isFile() && (Objects.isNull(library.getChecksum()) || Objects.nonNull(library.getDeleteEntriesList()) || library.getChecksum().equals(FileUtil.getChecksum(local)))) {
            return null;
        }
        return local;
    }

    public Set<Downloadable> getRequiredDownloadables(File targetDirectory, boolean force, boolean tlauncher) throws IOException {
        return getRequiredDownloadables(OS.CURRENT, targetDirectory, force, tlauncher);
    }

    public static VersionSyncInfo createEmpty() {
        return new VersionSyncInfo();
    }
}
