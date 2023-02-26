package net.minecraft.launcher.versions;

import java.util.Date;
import net.minecraft.launcher.updater.VersionList;
import org.tlauncher.tlauncher.repository.Repo;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/Version.class */
public interface Version {
    boolean isActivateSkinCapeForUserVersion();

    String getID();

    void setID(String str);

    String getJar();

    ReleaseType getReleaseType();

    Repo getSource();

    void setSource(Repo repo);

    Date getUpdatedTime();

    Date getReleaseTime();

    VersionList getVersionList();

    void setVersionList(VersionList versionList);

    boolean isSkinVersion();

    void setSkinVersion(boolean z);

    String getUrl();

    void setUrl(String str);
}
