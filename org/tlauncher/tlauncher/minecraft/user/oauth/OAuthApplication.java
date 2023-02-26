package org.tlauncher.tlauncher.minecraft.user.oauth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/oauth/OAuthApplication.class */
public class OAuthApplication {
    public static OAuthApplication TLAUNCHER_PARAMETERS = new OAuthApplication("2b8231e2-3b7e-4252-90d9-0732cf38e529", "Xboxlive.signin offline_access", "https://login.live.com/oauth20_authorize.srf", "https://login.live.com/oauth20_token.srf", "https://login.microsoftonline.com/common/oauth2/nativeclient", "https://login.microsoftonline.com/common/oauth2/nativeclient?error=access_denied");
    private final String clientId;
    private final String scope;
    private final boolean useWeirdXboxTokenPrefix = true;
    private String basicURL;
    private String tokenURL;
    private String redirectURL;
    private String backButtonURL;

    public void setBasicURL(String basicURL) {
        this.basicURL = basicURL;
    }

    public void setTokenURL(String tokenURL) {
        this.tokenURL = tokenURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public void setBackButtonURL(String backButtonURL) {
        this.backButtonURL = backButtonURL;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof OAuthApplication) {
            OAuthApplication other = (OAuthApplication) o;
            if (other.canEqual(this)) {
                Object this$clientId = getClientId();
                Object other$clientId = other.getClientId();
                if (this$clientId == null) {
                    if (other$clientId != null) {
                        return false;
                    }
                } else if (!this$clientId.equals(other$clientId)) {
                    return false;
                }
                Object this$scope = getScope();
                Object other$scope = other.getScope();
                if (this$scope == null) {
                    if (other$scope != null) {
                        return false;
                    }
                } else if (!this$scope.equals(other$scope)) {
                    return false;
                }
                if (isUseWeirdXboxTokenPrefix() != other.isUseWeirdXboxTokenPrefix()) {
                    return false;
                }
                Object this$basicURL = getBasicURL();
                Object other$basicURL = other.getBasicURL();
                if (this$basicURL == null) {
                    if (other$basicURL != null) {
                        return false;
                    }
                } else if (!this$basicURL.equals(other$basicURL)) {
                    return false;
                }
                Object this$tokenURL = getTokenURL();
                Object other$tokenURL = other.getTokenURL();
                if (this$tokenURL == null) {
                    if (other$tokenURL != null) {
                        return false;
                    }
                } else if (!this$tokenURL.equals(other$tokenURL)) {
                    return false;
                }
                Object this$redirectURL = getRedirectURL();
                Object other$redirectURL = other.getRedirectURL();
                if (this$redirectURL == null) {
                    if (other$redirectURL != null) {
                        return false;
                    }
                } else if (!this$redirectURL.equals(other$redirectURL)) {
                    return false;
                }
                Object this$backButtonURL = getBackButtonURL();
                Object other$backButtonURL = other.getBackButtonURL();
                return this$backButtonURL == null ? other$backButtonURL == null : this$backButtonURL.equals(other$backButtonURL);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof OAuthApplication;
    }

    public int hashCode() {
        Object $clientId = getClientId();
        int result = (1 * 59) + ($clientId == null ? 43 : $clientId.hashCode());
        Object $scope = getScope();
        int result2 = (((result * 59) + ($scope == null ? 43 : $scope.hashCode())) * 59) + (isUseWeirdXboxTokenPrefix() ? 79 : 97);
        Object $basicURL = getBasicURL();
        int result3 = (result2 * 59) + ($basicURL == null ? 43 : $basicURL.hashCode());
        Object $tokenURL = getTokenURL();
        int result4 = (result3 * 59) + ($tokenURL == null ? 43 : $tokenURL.hashCode());
        Object $redirectURL = getRedirectURL();
        int result5 = (result4 * 59) + ($redirectURL == null ? 43 : $redirectURL.hashCode());
        Object $backButtonURL = getBackButtonURL();
        return (result5 * 59) + ($backButtonURL == null ? 43 : $backButtonURL.hashCode());
    }

    public String toString() {
        return "OAuthApplication(clientId=" + getClientId() + ", scope=" + getScope() + ", useWeirdXboxTokenPrefix=" + isUseWeirdXboxTokenPrefix() + ", basicURL=" + getBasicURL() + ", tokenURL=" + getTokenURL() + ", redirectURL=" + getRedirectURL() + ", backButtonURL=" + getBackButtonURL() + ")";
    }

    public OAuthApplication(String clientId, String scope, String basicURL, String tokenURL, String redirectURL, String backButtonURL) {
        this.clientId = clientId;
        this.scope = scope;
        this.basicURL = basicURL;
        this.tokenURL = tokenURL;
        this.redirectURL = redirectURL;
        this.backButtonURL = backButtonURL;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getScope() {
        return this.scope;
    }

    public boolean isUseWeirdXboxTokenPrefix() {
        getClass();
        return true;
    }

    public String getBasicURL() {
        return this.basicURL;
    }

    public String getTokenURL() {
        return this.tokenURL;
    }

    public String getRedirectURL() {
        return this.redirectURL;
    }

    public String getBackButtonURL() {
        return this.backButtonURL;
    }
}
