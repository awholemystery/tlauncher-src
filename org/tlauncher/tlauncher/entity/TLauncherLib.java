package org.tlauncher.tlauncher.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.Library;
import net.minecraft.launcher.versions.json.Argument;
import net.minecraft.launcher.versions.json.ArgumentType;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/TLauncherLib.class */
public class TLauncherLib extends Library {
    public static final String USER_CONFIG_SKIN_VERSION = "userConfigSkinVersion";
    private Pattern pattern;
    private Map<ArgumentType, List<Argument>> arguments;
    private String mainClass;
    private List<Library> requires = new ArrayList();
    private List<String> supports;
    private Set<Account.AccountType> accountTypes;
    private Map<String, MetadataDTO> downloads;

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public void setArguments(Map<ArgumentType, List<Argument>> arguments) {
        this.arguments = arguments;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public void setRequires(List<Library> requires) {
        this.requires = requires;
    }

    public void setSupports(List<String> supports) {
        this.supports = supports;
    }

    public void setAccountTypes(Set<Account.AccountType> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public void setDownloads(Map<String, MetadataDTO> downloads) {
        this.downloads = downloads;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof TLauncherLib) {
            TLauncherLib other = (TLauncherLib) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$pattern = getPattern();
                Object other$pattern = other.getPattern();
                if (this$pattern == null) {
                    if (other$pattern != null) {
                        return false;
                    }
                } else if (!this$pattern.equals(other$pattern)) {
                    return false;
                }
                Object this$arguments = getArguments();
                Object other$arguments = other.getArguments();
                if (this$arguments == null) {
                    if (other$arguments != null) {
                        return false;
                    }
                } else if (!this$arguments.equals(other$arguments)) {
                    return false;
                }
                Object this$mainClass = getMainClass();
                Object other$mainClass = other.getMainClass();
                if (this$mainClass == null) {
                    if (other$mainClass != null) {
                        return false;
                    }
                } else if (!this$mainClass.equals(other$mainClass)) {
                    return false;
                }
                Object this$requires = getRequires();
                Object other$requires = other.getRequires();
                if (this$requires == null) {
                    if (other$requires != null) {
                        return false;
                    }
                } else if (!this$requires.equals(other$requires)) {
                    return false;
                }
                Object this$supports = getSupports();
                Object other$supports = other.getSupports();
                if (this$supports == null) {
                    if (other$supports != null) {
                        return false;
                    }
                } else if (!this$supports.equals(other$supports)) {
                    return false;
                }
                Object this$accountTypes = getAccountTypes();
                Object other$accountTypes = other.getAccountTypes();
                if (this$accountTypes == null) {
                    if (other$accountTypes != null) {
                        return false;
                    }
                } else if (!this$accountTypes.equals(other$accountTypes)) {
                    return false;
                }
                Object this$downloads = getDownloads();
                Object other$downloads = other.getDownloads();
                return this$downloads == null ? other$downloads == null : this$downloads.equals(other$downloads);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TLauncherLib;
    }

    public int hashCode() {
        int result = super.hashCode();
        Object $pattern = getPattern();
        int result2 = (result * 59) + ($pattern == null ? 43 : $pattern.hashCode());
        Object $arguments = getArguments();
        int result3 = (result2 * 59) + ($arguments == null ? 43 : $arguments.hashCode());
        Object $mainClass = getMainClass();
        int result4 = (result3 * 59) + ($mainClass == null ? 43 : $mainClass.hashCode());
        Object $requires = getRequires();
        int result5 = (result4 * 59) + ($requires == null ? 43 : $requires.hashCode());
        Object $supports = getSupports();
        int result6 = (result5 * 59) + ($supports == null ? 43 : $supports.hashCode());
        Object $accountTypes = getAccountTypes();
        int result7 = (result6 * 59) + ($accountTypes == null ? 43 : $accountTypes.hashCode());
        Object $downloads = getDownloads();
        return (result7 * 59) + ($downloads == null ? 43 : $downloads.hashCode());
    }

    @Override // net.minecraft.launcher.versions.Library
    public String toString() {
        return "TLauncherLib(super=" + super.toString() + ", pattern=" + getPattern() + ", arguments=" + getArguments() + ", mainClass=" + getMainClass() + ", requires=" + getRequires() + ", supports=" + getSupports() + ", accountTypes=" + getAccountTypes() + ", downloads=" + getDownloads() + ")";
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public Map<ArgumentType, List<Argument>> getArguments() {
        return this.arguments;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public List<Library> getRequires() {
        return this.requires;
    }

    public List<String> getSupports() {
        return this.supports;
    }

    public Set<Account.AccountType> getAccountTypes() {
        return this.accountTypes;
    }

    public Map<String, MetadataDTO> getDownloads() {
        return this.downloads;
    }

    public TLauncherLib() {
        setUrl("/libraries/");
    }

    public boolean matches(Library lib) {
        return this.pattern != null && this.pattern.matcher(lib.getName()).matches();
    }

    public boolean isSupport(String version) {
        if (Objects.isNull(this.supports)) {
            return false;
        }
        return this.supports.contains(version);
    }

    public boolean isProperAccountTypeLib(boolean tlAccountImpl) {
        if (Objects.isNull(this.accountTypes)) {
            return true;
        }
        if (tlAccountImpl) {
            return this.accountTypes.contains(Account.AccountType.TLAUNCHER);
        }
        return this.accountTypes.contains(Account.AccountType.MOJANG) || this.accountTypes.contains(Account.AccountType.MICROSOFT);
    }

    public boolean isApply(Library lib, CompleteVersion v) {
        return matches(lib) && (Objects.isNull(this.supports) || isSupport(v.getID()) || v.isActivateSkinCapeForUserVersion());
    }

    @Override // net.minecraft.launcher.versions.Library
    public Downloadable getDownloadable(Repo versionSource, File file, OS os) {
        U.log("getting downloadable", getName(), versionSource, file, os);
        return super.getDownloadable(ClientInstanceRepo.EXTRA_VERSION_REPO, file, os);
    }
}
