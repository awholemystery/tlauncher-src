package org.tlauncher.tlauncher.entity;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.commons.lang3.StringUtils;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/TLauncherVersionChanger.class */
public class TLauncherVersionChanger {
    private Set<String> tlauncherSkinCapeVersion;
    private double version;
    private List<TLauncherLib> libraries;
    private List<TLauncherLib> additionalMods;
    public static final List<String> defaultVersionTypes = Lists.newArrayList(new String[]{"Forge", "OptiFine", "Fabric"});

    public void setTlauncherSkinCapeVersion(Set<String> tlauncherSkinCapeVersion) {
        this.tlauncherSkinCapeVersion = tlauncherSkinCapeVersion;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public void setLibraries(List<TLauncherLib> libraries) {
        this.libraries = libraries;
    }

    public void setAdditionalMods(List<TLauncherLib> additionalMods) {
        this.additionalMods = additionalMods;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof TLauncherVersionChanger) {
            TLauncherVersionChanger other = (TLauncherVersionChanger) o;
            if (other.canEqual(this)) {
                Object this$tlauncherSkinCapeVersion = getTlauncherSkinCapeVersion();
                Object other$tlauncherSkinCapeVersion = other.getTlauncherSkinCapeVersion();
                if (this$tlauncherSkinCapeVersion == null) {
                    if (other$tlauncherSkinCapeVersion != null) {
                        return false;
                    }
                } else if (!this$tlauncherSkinCapeVersion.equals(other$tlauncherSkinCapeVersion)) {
                    return false;
                }
                if (Double.compare(getVersion(), other.getVersion()) != 0) {
                    return false;
                }
                Object this$libraries = getLibraries();
                Object other$libraries = other.getLibraries();
                if (this$libraries == null) {
                    if (other$libraries != null) {
                        return false;
                    }
                } else if (!this$libraries.equals(other$libraries)) {
                    return false;
                }
                Object this$additionalMods = getAdditionalMods();
                Object other$additionalMods = other.getAdditionalMods();
                return this$additionalMods == null ? other$additionalMods == null : this$additionalMods.equals(other$additionalMods);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TLauncherVersionChanger;
    }

    public int hashCode() {
        Object $tlauncherSkinCapeVersion = getTlauncherSkinCapeVersion();
        int result = (1 * 59) + ($tlauncherSkinCapeVersion == null ? 43 : $tlauncherSkinCapeVersion.hashCode());
        long $version = Double.doubleToLongBits(getVersion());
        int result2 = (result * 59) + ((int) (($version >>> 32) ^ $version));
        Object $libraries = getLibraries();
        int result3 = (result2 * 59) + ($libraries == null ? 43 : $libraries.hashCode());
        Object $additionalMods = getAdditionalMods();
        return (result3 * 59) + ($additionalMods == null ? 43 : $additionalMods.hashCode());
    }

    public String toString() {
        return "TLauncherVersionChanger(tlauncherSkinCapeVersion=" + getTlauncherSkinCapeVersion() + ", version=" + getVersion() + ", libraries=" + getLibraries() + ", additionalMods=" + getAdditionalMods() + ")";
    }

    public Set<String> getTlauncherSkinCapeVersion() {
        return this.tlauncherSkinCapeVersion;
    }

    public double getVersion() {
        return this.version;
    }

    public List<TLauncherLib> getLibraries() {
        return this.libraries;
    }

    public List<TLauncherLib> getAdditionalMods() {
        return this.additionalMods;
    }

    public List<TLauncherLib> getAddedMods(CompleteVersion v) {
        String id = getVersionIDForUserSkinCapeVersion(v);
        return (List) this.additionalMods.stream().filter(l -> {
            return l.getSupports().contains(id);
        }).collect(Collectors.toList());
    }

    public List<TLauncherLib> getAddedMods(CompleteVersion v, boolean tlSkinType) {
        return (List) getAddedMods(v).stream().filter(l -> {
            return l.isProperAccountTypeLib(tlSkinType);
        }).collect(Collectors.toList());
    }

    public String getVersionIDForUserSkinCapeVersion(CompleteVersion complete) {
        if (complete.isActivateSkinCapeForUserVersion() && Objects.nonNull(complete.getJar())) {
            Optional<String> res = defaultVersionTypes.stream().filter(s -> {
                return StringUtils.containsIgnoreCase(complete.getID(), s);
            }).findFirst();
            if (res.isPresent()) {
                return res.get() + " " + complete.getJar();
            }
        }
        return complete.getID();
    }
}
