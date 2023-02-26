package org.tlauncher.modpack.domain.client.version;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/version/ModVersionDTO.class */
public class ModVersionDTO extends VersionDTO {
    private String downForge;
    private String upForge;
    private List<String> incompatibleMods;

    public void setDownForge(String downForge) {
        this.downForge = downForge;
    }

    public void setUpForge(String upForge) {
        this.upForge = upForge;
    }

    public void setIncompatibleMods(List<String> incompatibleMods) {
        this.incompatibleMods = incompatibleMods;
    }

    @Override // org.tlauncher.modpack.domain.client.version.VersionDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModVersionDTO) {
            ModVersionDTO other = (ModVersionDTO) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$downForge = getDownForge();
                Object other$downForge = other.getDownForge();
                if (this$downForge == null) {
                    if (other$downForge != null) {
                        return false;
                    }
                } else if (!this$downForge.equals(other$downForge)) {
                    return false;
                }
                Object this$upForge = getUpForge();
                Object other$upForge = other.getUpForge();
                if (this$upForge == null) {
                    if (other$upForge != null) {
                        return false;
                    }
                } else if (!this$upForge.equals(other$upForge)) {
                    return false;
                }
                Object this$incompatibleMods = getIncompatibleMods();
                Object other$incompatibleMods = other.getIncompatibleMods();
                return this$incompatibleMods == null ? other$incompatibleMods == null : this$incompatibleMods.equals(other$incompatibleMods);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.version.VersionDTO
    protected boolean canEqual(Object other) {
        return other instanceof ModVersionDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.version.VersionDTO
    public int hashCode() {
        int result = super.hashCode();
        Object $downForge = getDownForge();
        int result2 = (result * 59) + ($downForge == null ? 43 : $downForge.hashCode());
        Object $upForge = getUpForge();
        int result3 = (result2 * 59) + ($upForge == null ? 43 : $upForge.hashCode());
        Object $incompatibleMods = getIncompatibleMods();
        return (result3 * 59) + ($incompatibleMods == null ? 43 : $incompatibleMods.hashCode());
    }

    @Override // org.tlauncher.modpack.domain.client.version.VersionDTO
    public String toString() {
        return "ModVersionDTO(super=" + super.toString() + ", downForge=" + getDownForge() + ", upForge=" + getUpForge() + ", incompatibleMods=" + getIncompatibleMods() + ")";
    }

    public String getDownForge() {
        return this.downForge;
    }

    public String getUpForge() {
        return this.upForge;
    }

    public List<String> getIncompatibleMods() {
        return this.incompatibleMods;
    }
}
