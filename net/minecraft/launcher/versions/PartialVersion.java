package net.minecraft.launcher.versions;

import java.util.Date;
import net.minecraft.launcher.updater.VersionList;
import org.tlauncher.tlauncher.repository.Repo;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/PartialVersion.class */
public class PartialVersion extends AbstractVersion implements Cloneable {
    private String id;
    private String jar;
    private Date time;
    private Date releaseTime;
    private ReleaseType type;
    private Repo source;
    private VersionList list;

    @Override // net.minecraft.launcher.versions.Version
    public String getID() {
        return this.id;
    }

    @Override // net.minecraft.launcher.versions.Version
    public String getJar() {
        return this.jar;
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setID(String id) {
        this.id = id;
    }

    @Override // net.minecraft.launcher.versions.Version
    public ReleaseType getReleaseType() {
        return this.type;
    }

    @Override // net.minecraft.launcher.versions.Version
    public Repo getSource() {
        return this.source;
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setSource(Repo repo) {
        if (repo == null) {
            throw new NullPointerException();
        }
        this.source = repo;
    }

    @Override // net.minecraft.launcher.versions.Version
    public Date getUpdatedTime() {
        return this.time;
    }

    @Override // net.minecraft.launcher.versions.Version
    public Date getReleaseTime() {
        return this.releaseTime;
    }

    @Override // net.minecraft.launcher.versions.Version
    public VersionList getVersionList() {
        return this.list;
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setVersionList(VersionList list) {
        if (list == null) {
            throw new NullPointerException();
        }
        this.list = list;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (hashCode() == o.hashCode()) {
            return true;
        }
        if (!(o instanceof Version)) {
            return false;
        }
        Version compare = (Version) o;
        if (compare.getID() == null) {
            return false;
        }
        return compare.getID().equals(this.id);
    }

    public String toString() {
        return getClass().getSimpleName() + "{id='" + this.id + "', time=" + this.time + ", release=" + this.releaseTime + ", type=" + this.type + ", source=" + this.source + ", list=" + this.list + "}";
    }

    @Override // net.minecraft.launcher.versions.Version
    public boolean isActivateSkinCapeForUserVersion() {
        return false;
    }
}
