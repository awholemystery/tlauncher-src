package org.tlauncher.modpack.domain.client.auth;

import org.tlauncher.modpack.domain.client.site.UUIDUsername;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/auth/UserDTO.class */
public class UserDTO extends UUIDUsername {
    private String faceImage;
    private boolean alert;
    private boolean hasAlerts;

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public void setHasAlerts(boolean hasAlerts) {
        this.hasAlerts = hasAlerts;
    }

    @Override // org.tlauncher.modpack.domain.client.site.UUIDUsername
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof UserDTO) {
            UserDTO other = (UserDTO) o;
            return other.canEqual(this) && super.equals(o) && isAlert() == other.isAlert() && isHasAlerts() == other.isHasAlerts();
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.site.UUIDUsername
    protected boolean canEqual(Object other) {
        return other instanceof UserDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.site.UUIDUsername
    public int hashCode() {
        int result = super.hashCode();
        return (((result * 59) + (isAlert() ? 79 : 97)) * 59) + (isHasAlerts() ? 79 : 97);
    }

    @Override // org.tlauncher.modpack.domain.client.site.UUIDUsername
    public String toString() {
        return "UserDTO(super=" + super.toString() + ", alert=" + isAlert() + ", hasAlerts=" + isHasAlerts() + ")";
    }

    public String getFaceImage() {
        return this.faceImage;
    }

    public boolean isAlert() {
        return this.alert;
    }

    public boolean isHasAlerts() {
        return this.hasAlerts;
    }
}
