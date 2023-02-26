package org.tlauncher.tlauncher.entity.auth;

import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
import org.tlauncher.tlauncher.minecraft.auth.GameProfile;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/auth/RefreshRequest.class */
public class RefreshRequest extends Request {
    private String accessToken;
    private GameProfile selectedProfile;
    private boolean requestUser = true;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setSelectedProfile(GameProfile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public void setRequestUser(boolean requestUser) {
        this.requestUser = requestUser;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Request
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof RefreshRequest) {
            RefreshRequest other = (RefreshRequest) o;
            if (other.canEqual(this)) {
                Object this$accessToken = getAccessToken();
                Object other$accessToken = other.getAccessToken();
                if (this$accessToken == null) {
                    if (other$accessToken != null) {
                        return false;
                    }
                } else if (!this$accessToken.equals(other$accessToken)) {
                    return false;
                }
                Object this$selectedProfile = getSelectedProfile();
                Object other$selectedProfile = other.getSelectedProfile();
                if (this$selectedProfile == null) {
                    if (other$selectedProfile != null) {
                        return false;
                    }
                } else if (!this$selectedProfile.equals(other$selectedProfile)) {
                    return false;
                }
                return isRequestUser() == other.isRequestUser();
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Request
    protected boolean canEqual(Object other) {
        return other instanceof RefreshRequest;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Request
    public int hashCode() {
        Object $accessToken = getAccessToken();
        int result = (1 * 59) + ($accessToken == null ? 43 : $accessToken.hashCode());
        Object $selectedProfile = getSelectedProfile();
        return (((result * 59) + ($selectedProfile == null ? 43 : $selectedProfile.hashCode())) * 59) + (isRequestUser() ? 79 : 97);
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Request
    public String toString() {
        return "RefreshRequest(accessToken=" + getAccessToken() + ", selectedProfile=" + getSelectedProfile() + ", requestUser=" + isRequestUser() + ")";
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public GameProfile getSelectedProfile() {
        return this.selectedProfile;
    }

    public boolean isRequestUser() {
        return this.requestUser;
    }

    public RefreshRequest(Authenticator auth) {
        setClientToken(Authenticator.getClientToken().toString());
        setAccessToken(auth.getAccount().getAccessToken());
    }
}
