package net.minecraft.launcher.versions;

import java.util.Date;
import java.util.List;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.repository.Repo;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/ModifiedVersion.class */
public class ModifiedVersion {
    private boolean activateSkinCapeForUserVersion;
    private boolean skipHashsumValidation;
    private List<Library> modsLibraries;
    private List<MetadataDTO> additionalFiles;
    private Integer tlauncherVersion = 0;
    private ModpackDTO modpack;
    private Repo source;
    private String jar;
    private boolean skinVersion;
    private String url;
    private transient String remoteVersion;
    private Date updatedTime;

    public void setActivateSkinCapeForUserVersion(boolean activateSkinCapeForUserVersion) {
        this.activateSkinCapeForUserVersion = activateSkinCapeForUserVersion;
    }

    public void setSkipHashsumValidation(boolean skipHashsumValidation) {
        this.skipHashsumValidation = skipHashsumValidation;
    }

    public void setModsLibraries(List<Library> modsLibraries) {
        this.modsLibraries = modsLibraries;
    }

    public void setAdditionalFiles(List<MetadataDTO> additionalFiles) {
        this.additionalFiles = additionalFiles;
    }

    public void setTlauncherVersion(Integer tlauncherVersion) {
        this.tlauncherVersion = tlauncherVersion;
    }

    public void setModpack(ModpackDTO modpack) {
        this.modpack = modpack;
    }

    public void setSource(Repo source) {
        this.source = source;
    }

    public void setJar(String jar) {
        this.jar = jar;
    }

    public void setSkinVersion(boolean skinVersion) {
        this.skinVersion = skinVersion;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRemoteVersion(String remoteVersion) {
        this.remoteVersion = remoteVersion;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModifiedVersion) {
            ModifiedVersion other = (ModifiedVersion) o;
            if (other.canEqual(this) && isActivateSkinCapeForUserVersion() == other.isActivateSkinCapeForUserVersion() && isSkipHashsumValidation() == other.isSkipHashsumValidation()) {
                Object this$modsLibraries = getModsLibraries();
                Object other$modsLibraries = other.getModsLibraries();
                if (this$modsLibraries == null) {
                    if (other$modsLibraries != null) {
                        return false;
                    }
                } else if (!this$modsLibraries.equals(other$modsLibraries)) {
                    return false;
                }
                Object this$additionalFiles = getAdditionalFiles();
                Object other$additionalFiles = other.getAdditionalFiles();
                if (this$additionalFiles == null) {
                    if (other$additionalFiles != null) {
                        return false;
                    }
                } else if (!this$additionalFiles.equals(other$additionalFiles)) {
                    return false;
                }
                Object this$tlauncherVersion = getTlauncherVersion();
                Object other$tlauncherVersion = other.getTlauncherVersion();
                if (this$tlauncherVersion == null) {
                    if (other$tlauncherVersion != null) {
                        return false;
                    }
                } else if (!this$tlauncherVersion.equals(other$tlauncherVersion)) {
                    return false;
                }
                Object this$modpack = getModpack();
                Object other$modpack = other.getModpack();
                if (this$modpack == null) {
                    if (other$modpack != null) {
                        return false;
                    }
                } else if (!this$modpack.equals(other$modpack)) {
                    return false;
                }
                Object this$source = getSource();
                Object other$source = other.getSource();
                if (this$source == null) {
                    if (other$source != null) {
                        return false;
                    }
                } else if (!this$source.equals(other$source)) {
                    return false;
                }
                Object this$jar = getJar();
                Object other$jar = other.getJar();
                if (this$jar == null) {
                    if (other$jar != null) {
                        return false;
                    }
                } else if (!this$jar.equals(other$jar)) {
                    return false;
                }
                if (isSkinVersion() != other.isSkinVersion()) {
                    return false;
                }
                Object this$url = getUrl();
                Object other$url = other.getUrl();
                if (this$url == null) {
                    if (other$url != null) {
                        return false;
                    }
                } else if (!this$url.equals(other$url)) {
                    return false;
                }
                Object this$updatedTime = getUpdatedTime();
                Object other$updatedTime = other.getUpdatedTime();
                return this$updatedTime == null ? other$updatedTime == null : this$updatedTime.equals(other$updatedTime);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ModifiedVersion;
    }

    public int hashCode() {
        int result = (1 * 59) + (isActivateSkinCapeForUserVersion() ? 79 : 97);
        int result2 = (result * 59) + (isSkipHashsumValidation() ? 79 : 97);
        Object $modsLibraries = getModsLibraries();
        int result3 = (result2 * 59) + ($modsLibraries == null ? 43 : $modsLibraries.hashCode());
        Object $additionalFiles = getAdditionalFiles();
        int result4 = (result3 * 59) + ($additionalFiles == null ? 43 : $additionalFiles.hashCode());
        Object $tlauncherVersion = getTlauncherVersion();
        int result5 = (result4 * 59) + ($tlauncherVersion == null ? 43 : $tlauncherVersion.hashCode());
        Object $modpack = getModpack();
        int result6 = (result5 * 59) + ($modpack == null ? 43 : $modpack.hashCode());
        Object $source = getSource();
        int result7 = (result6 * 59) + ($source == null ? 43 : $source.hashCode());
        Object $jar = getJar();
        int result8 = (((result7 * 59) + ($jar == null ? 43 : $jar.hashCode())) * 59) + (isSkinVersion() ? 79 : 97);
        Object $url = getUrl();
        int result9 = (result8 * 59) + ($url == null ? 43 : $url.hashCode());
        Object $updatedTime = getUpdatedTime();
        return (result9 * 59) + ($updatedTime == null ? 43 : $updatedTime.hashCode());
    }

    public String toString() {
        return "ModifiedVersion(activateSkinCapeForUserVersion=" + isActivateSkinCapeForUserVersion() + ", skipHashsumValidation=" + isSkipHashsumValidation() + ", modsLibraries=" + getModsLibraries() + ", additionalFiles=" + getAdditionalFiles() + ", tlauncherVersion=" + getTlauncherVersion() + ", modpack=" + getModpack() + ", source=" + getSource() + ", jar=" + getJar() + ", skinVersion=" + isSkinVersion() + ", url=" + getUrl() + ", remoteVersion=" + getRemoteVersion() + ", updatedTime=" + getUpdatedTime() + ")";
    }

    public boolean isActivateSkinCapeForUserVersion() {
        return this.activateSkinCapeForUserVersion;
    }

    public boolean isSkipHashsumValidation() {
        return this.skipHashsumValidation;
    }

    public List<Library> getModsLibraries() {
        return this.modsLibraries;
    }

    public List<MetadataDTO> getAdditionalFiles() {
        return this.additionalFiles;
    }

    public Integer getTlauncherVersion() {
        return this.tlauncherVersion;
    }

    public ModpackDTO getModpack() {
        return this.modpack;
    }

    public Repo getSource() {
        return this.source;
    }

    public String getJar() {
        return this.jar;
    }

    public boolean isSkinVersion() {
        return this.skinVersion;
    }

    public String getUrl() {
        return this.url;
    }

    public String getRemoteVersion() {
        return this.remoteVersion;
    }

    public Date getUpdatedTime() {
        return this.updatedTime;
    }
}
