package net.minecraft.launcher.updater;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/AssetIndex.class */
public class AssetIndex {
    public static final String DEFAULT_ASSET_NAME = "legacy";
    private Map<String, AssetObject> objects = new LinkedHashMap();
    private boolean virtual;

    public Map<String, AssetObject> getFileMap() {
        return this.objects;
    }

    public Set<AssetObject> getUniqueObjects() {
        return new HashSet(this.objects.values());
    }

    public boolean isVirtual() {
        return this.virtual;
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/AssetIndex$AssetObject.class */
    public class AssetObject {
        private String filename;
        private String hash;
        private long size;

        public AssetObject() {
        }

        public String getHash() {
            return this.hash;
        }

        public long getSize() {
            return this.size;
        }

        public String getFilename() {
            if (this.filename == null) {
                this.filename = getHash().substring(0, 2) + "/" + getHash();
            }
            return this.filename;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            AssetObject that = (AssetObject) o;
            if (this.size != that.size) {
                return false;
            }
            return this.hash.equals(that.hash);
        }

        public int hashCode() {
            int result = this.hash.hashCode();
            return (31 * result) + ((int) (this.size ^ (this.size >>> 32)));
        }
    }
}
