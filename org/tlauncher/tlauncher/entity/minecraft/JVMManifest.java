package org.tlauncher.tlauncher.entity.minecraft;

import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/minecraft/JVMManifest.class */
public class JVMManifest {
    Map<String, JVMFile> files;

    public void setFiles(Map<String, JVMFile> files) {
        this.files = files;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JVMManifest) {
            JVMManifest other = (JVMManifest) o;
            if (other.canEqual(this)) {
                Object this$files = getFiles();
                Object other$files = other.getFiles();
                return this$files == null ? other$files == null : this$files.equals(other$files);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JVMManifest;
    }

    public int hashCode() {
        Object $files = getFiles();
        int result = (1 * 59) + ($files == null ? 43 : $files.hashCode());
        return result;
    }

    public String toString() {
        return "JVMManifest(files=" + getFiles() + ")";
    }

    public Map<String, JVMFile> getFiles() {
        return this.files;
    }
}
