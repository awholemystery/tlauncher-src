package org.tlauncher.tlauncher.entity.auth;

import org.tlauncher.tlauncher.minecraft.auth.GameProfile;
import org.tlauncher.tlauncher.minecraft.auth.User;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/auth/RefreshResponse.class */
public class RefreshResponse extends Response {
    private String accessToken;
    private String clientToken;
    private GameProfile selectedProfile;
    private User user;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public void setSelectedProfile(GameProfile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Response
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof RefreshResponse) {
            RefreshResponse other = (RefreshResponse) o;
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
                Object this$clientToken = getClientToken();
                Object other$clientToken = other.getClientToken();
                if (this$clientToken == null) {
                    if (other$clientToken != null) {
                        return false;
                    }
                } else if (!this$clientToken.equals(other$clientToken)) {
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
                Object this$user = getUser();
                Object other$user = other.getUser();
                return this$user == null ? other$user == null : this$user.equals(other$user);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Response
    protected boolean canEqual(Object other) {
        return other instanceof RefreshResponse;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Response
    public int hashCode() {
        Object $accessToken = getAccessToken();
        int result = (1 * 59) + ($accessToken == null ? 43 : $accessToken.hashCode());
        Object $clientToken = getClientToken();
        int result2 = (result * 59) + ($clientToken == null ? 43 : $clientToken.hashCode());
        Object $selectedProfile = getSelectedProfile();
        int result3 = (result2 * 59) + ($selectedProfile == null ? 43 : $selectedProfile.hashCode());
        Object $user = getUser();
        return (result3 * 59) + ($user == null ? 43 : $user.hashCode());
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Response
    public String toString() {
        return "RefreshResponse(accessToken=" + getAccessToken() + ", clientToken=" + getClientToken() + ", selectedProfile=" + getSelectedProfile() + ", user=" + getUser() + ")";
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getClientToken() {
        return this.clientToken;
    }

    public GameProfile getSelectedProfile() {
        return this.selectedProfile;
    }

    public User getUser() {
        return this.user;
    }
}
