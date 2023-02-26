package net.minecraft.launcher.updater;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.launcher.versions.ReleaseType;
import net.minecraft.launcher.versions.Version;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/VersionFilter.class */
public class VersionFilter {
    private final Set<ReleaseType> types = new HashSet(ReleaseType.valuesCollection());
    private final Set<ReleaseType.SubType> subTypes = new HashSet(ReleaseType.SubType.valuesCollection());

    public Set<ReleaseType> getTypes() {
        return this.types;
    }

    public Set<ReleaseType.SubType> getSubTypes() {
        return this.subTypes;
    }

    public VersionFilter onlyForType(ReleaseType... types) {
        this.types.clear();
        include(types);
        return this;
    }

    public VersionFilter onlyForType(ReleaseType.SubType... subTypes) {
        this.subTypes.clear();
        include(subTypes);
        return this;
    }

    public VersionFilter include(ReleaseType... types) {
        if (types != null) {
            Collections.addAll(this.types, types);
        }
        return this;
    }

    public VersionFilter include(ReleaseType.SubType... types) {
        if (types != null) {
            Collections.addAll(this.subTypes, types);
        }
        return this;
    }

    public VersionFilter exclude(ReleaseType... types) {
        if (types != null) {
            for (ReleaseType type : types) {
                this.types.remove(type);
            }
        }
        return this;
    }

    public VersionFilter exclude(ReleaseType.SubType... types) {
        if (types != null) {
            for (ReleaseType.SubType type : types) {
                this.subTypes.remove(type);
            }
        }
        return this;
    }

    public boolean satisfies(Version v) {
        ReleaseType releaseType = v.getReleaseType();
        if (releaseType == null) {
            return true;
        }
        if (!this.types.contains(releaseType)) {
            return false;
        }
        ReleaseType.SubType subType = ReleaseType.SubType.get(v);
        if (subType == null) {
            return true;
        }
        return this.subTypes.contains(subType);
    }

    public String toString() {
        return "VersionFilter" + this.types;
    }
}
