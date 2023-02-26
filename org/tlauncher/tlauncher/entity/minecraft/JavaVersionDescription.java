package org.tlauncher.tlauncher.entity.minecraft;

import org.tlauncher.modpack.domain.client.version.MetadataDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/minecraft/JavaVersionDescription.class */
public class JavaVersionDescription {
    private Availability availability;
    private MetadataDTO manifest;
    private JavaVersion version;

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public void setManifest(MetadataDTO manifest) {
        this.manifest = manifest;
    }

    public void setVersion(JavaVersion version) {
        this.version = version;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JavaVersionDescription) {
            JavaVersionDescription other = (JavaVersionDescription) o;
            if (other.canEqual(this)) {
                Object this$availability = getAvailability();
                Object other$availability = other.getAvailability();
                if (this$availability == null) {
                    if (other$availability != null) {
                        return false;
                    }
                } else if (!this$availability.equals(other$availability)) {
                    return false;
                }
                Object this$manifest = getManifest();
                Object other$manifest = other.getManifest();
                if (this$manifest == null) {
                    if (other$manifest != null) {
                        return false;
                    }
                } else if (!this$manifest.equals(other$manifest)) {
                    return false;
                }
                Object this$version = getVersion();
                Object other$version = other.getVersion();
                return this$version == null ? other$version == null : this$version.equals(other$version);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JavaVersionDescription;
    }

    public int hashCode() {
        Object $availability = getAvailability();
        int result = (1 * 59) + ($availability == null ? 43 : $availability.hashCode());
        Object $manifest = getManifest();
        int result2 = (result * 59) + ($manifest == null ? 43 : $manifest.hashCode());
        Object $version = getVersion();
        return (result2 * 59) + ($version == null ? 43 : $version.hashCode());
    }

    public String toString() {
        return "JavaVersionDescription(availability=" + getAvailability() + ", manifest=" + getManifest() + ", version=" + getVersion() + ")";
    }

    public Availability getAvailability() {
        return this.availability;
    }

    public MetadataDTO getManifest() {
        return this.manifest;
    }

    public JavaVersion getVersion() {
        return this.version;
    }
}
