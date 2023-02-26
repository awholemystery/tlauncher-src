package net.minecraft.launcher.versions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/ReleaseType.class */
public enum ReleaseType {
    RELEASE("release", false, true),
    SNAPSHOT("snapshot"),
    MODIFIED("modified", true, true),
    OLD_BETA("old-beta"),
    OLD_ALPHA("old-alpha"),
    PENDING("pending", false, false),
    UNKNOWN("unknown", false, false);
    
    private static final Map<String, ReleaseType> lookup;
    private static final List<ReleaseType> defaultTypes;
    private static final List<ReleaseType> definableTypes;
    private final String name;
    private final boolean isDefinable;
    private final boolean isDefault;

    static {
        ReleaseType[] values;
        HashMap<String, ReleaseType> types = new HashMap<>(values().length);
        ArrayList<ReleaseType> deflTypes = new ArrayList<>();
        ArrayList<ReleaseType> defnTypes = new ArrayList<>();
        for (ReleaseType type : values()) {
            types.put(type.getName(), type);
            if (type.isDefault()) {
                deflTypes.add(type);
            }
            if (type.isDefinable()) {
                defnTypes.add(type);
            }
        }
        lookup = Collections.unmodifiableMap(types);
        defaultTypes = Collections.unmodifiableList(deflTypes);
        definableTypes = Collections.unmodifiableList(defnTypes);
    }

    ReleaseType(String name, boolean isDefinable, boolean isDefault) {
        this.name = name;
        this.isDefinable = isDefinable;
        this.isDefault = isDefault;
    }

    String getName() {
        return this.name;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public boolean isDefinable() {
        return this.isDefinable;
    }

    @Override // java.lang.Enum
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static ReleaseType getByName(String name) {
        return lookup.get(name);
    }

    public static Collection<ReleaseType> valuesCollection() {
        return lookup.values();
    }

    public static List<ReleaseType> getDefault() {
        return defaultTypes;
    }

    public static List<ReleaseType> getDefinable() {
        return definableTypes;
    }

    ReleaseType(String name) {
        this(name, true, false);
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/ReleaseType$SubType.class */
    public enum SubType {
        OLD_RELEASE("old_release") { // from class: net.minecraft.launcher.versions.ReleaseType.SubType.1
            private final Date marker;

            @Override // net.minecraft.launcher.versions.ReleaseType.SubType
            public boolean isSubType(Version version) {
                try {
                    if (!version.getReleaseType().toString().startsWith("old") && version.getReleaseTime().getTime() >= 0) {
                        if (version.getReleaseTime().before(this.marker)) {
                            return true;
                        }
                    }
                    return false;
                } catch (NullPointerException e) {
                    U.log("nullpointer is sub type in version" + version.getID());
                    return false;
                }
            }
        },
        REMOTE("remote") { // from class: net.minecraft.launcher.versions.ReleaseType.SubType.2
            @Override // net.minecraft.launcher.versions.ReleaseType.SubType
            public boolean isSubType(Version version) {
                return version.getSource() != ClientInstanceRepo.LOCAL_VERSION_REPO;
            }
        };
        
        private static final Map<String, SubType> lookup;
        private static final List<SubType> defaultSubTypes;
        private final String name;
        private final boolean isDefault;

        public abstract boolean isSubType(Version version);

        static {
            SubType[] values;
            HashMap<String, SubType> subTypes = new HashMap<>(values().length);
            ArrayList<SubType> defSubTypes = new ArrayList<>();
            for (SubType subType : values()) {
                subTypes.put(subType.getName(), subType);
                if (subType.isDefault()) {
                    defSubTypes.add(subType);
                }
            }
            lookup = Collections.unmodifiableMap(subTypes);
            defaultSubTypes = Collections.unmodifiableList(defSubTypes);
        }

        SubType(String name, boolean isDefault) {
            this.name = name;
            this.isDefault = isDefault;
        }

        SubType(String name) {
            this(name, true);
        }

        public String getName() {
            return this.name;
        }

        public boolean isDefault() {
            return this.isDefault;
        }

        @Override // java.lang.Enum
        public String toString() {
            return super.toString().toLowerCase();
        }

        public static SubType getByName(String name) {
            return lookup.get(name);
        }

        public static Collection<SubType> valuesCollection() {
            return lookup.values();
        }

        public static List<SubType> getDefault() {
            return defaultSubTypes;
        }

        public static SubType get(Version version) {
            SubType[] values;
            for (SubType subType : values()) {
                if (subType.isSubType(version)) {
                    return subType;
                }
            }
            return null;
        }
    }
}
