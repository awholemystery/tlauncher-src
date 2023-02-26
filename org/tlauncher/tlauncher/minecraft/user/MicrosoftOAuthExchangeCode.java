package org.tlauncher.tlauncher.minecraft.user;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/MicrosoftOAuthExchangeCode.class */
public class MicrosoftOAuthExchangeCode {
    private final RedirectUrl redirectUrl;
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof MicrosoftOAuthExchangeCode) {
            MicrosoftOAuthExchangeCode other = (MicrosoftOAuthExchangeCode) o;
            if (other.canEqual(this)) {
                Object this$redirectUrl = getRedirectUrl();
                Object other$redirectUrl = other.getRedirectUrl();
                if (this$redirectUrl == null) {
                    if (other$redirectUrl != null) {
                        return false;
                    }
                } else if (!this$redirectUrl.equals(other$redirectUrl)) {
                    return false;
                }
                Object this$code = getCode();
                Object other$code = other.getCode();
                return this$code == null ? other$code == null : this$code.equals(other$code);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MicrosoftOAuthExchangeCode;
    }

    public int hashCode() {
        Object $redirectUrl = getRedirectUrl();
        int result = (1 * 59) + ($redirectUrl == null ? 43 : $redirectUrl.hashCode());
        Object $code = getCode();
        return (result * 59) + ($code == null ? 43 : $code.hashCode());
    }

    public String toString() {
        return "MicrosoftOAuthExchangeCode(redirectUrl=" + getRedirectUrl() + ", code=" + getCode() + ")";
    }

    public RedirectUrl getRedirectUrl() {
        return this.redirectUrl;
    }

    public String getCode() {
        return this.code;
    }

    public MicrosoftOAuthExchangeCode(String code, RedirectUrl redirectUrl) {
        this.code = code;
        this.redirectUrl = redirectUrl;
    }
}
