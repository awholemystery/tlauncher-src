package org.tlauncher.modpack.domain.client.auth;

import java.io.Serializable;
import java.util.Collection;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/auth/JwtAuthenticationResponse.class */
public class JwtAuthenticationResponse implements Serializable {
    private static final long serialVersionUID = 1250166508152483573L;
    private String token;
    private Collection<String> authorities;
    private Integer codeError;
    private String causeError;

    public void setToken(String token) {
        this.token = token;
    }

    public void setAuthorities(Collection<String> authorities) {
        this.authorities = authorities;
    }

    public void setCodeError(Integer codeError) {
        this.codeError = codeError;
    }

    public void setCauseError(String causeError) {
        this.causeError = causeError;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JwtAuthenticationResponse) {
            JwtAuthenticationResponse other = (JwtAuthenticationResponse) o;
            if (other.canEqual(this)) {
                Object this$codeError = getCodeError();
                Object other$codeError = other.getCodeError();
                if (this$codeError == null) {
                    if (other$codeError != null) {
                        return false;
                    }
                } else if (!this$codeError.equals(other$codeError)) {
                    return false;
                }
                Object this$token = getToken();
                Object other$token = other.getToken();
                if (this$token == null) {
                    if (other$token != null) {
                        return false;
                    }
                } else if (!this$token.equals(other$token)) {
                    return false;
                }
                Object this$authorities = getAuthorities();
                Object other$authorities = other.getAuthorities();
                if (this$authorities == null) {
                    if (other$authorities != null) {
                        return false;
                    }
                } else if (!this$authorities.equals(other$authorities)) {
                    return false;
                }
                Object this$causeError = getCauseError();
                Object other$causeError = other.getCauseError();
                return this$causeError == null ? other$causeError == null : this$causeError.equals(other$causeError);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JwtAuthenticationResponse;
    }

    public int hashCode() {
        Object $codeError = getCodeError();
        int result = (1 * 59) + ($codeError == null ? 43 : $codeError.hashCode());
        Object $token = getToken();
        int result2 = (result * 59) + ($token == null ? 43 : $token.hashCode());
        Object $authorities = getAuthorities();
        int result3 = (result2 * 59) + ($authorities == null ? 43 : $authorities.hashCode());
        Object $causeError = getCauseError();
        return (result3 * 59) + ($causeError == null ? 43 : $causeError.hashCode());
    }

    public String toString() {
        return "JwtAuthenticationResponse(token=" + getToken() + ", authorities=" + getAuthorities() + ", codeError=" + getCodeError() + ", causeError=" + getCauseError() + ")";
    }

    public String getToken() {
        return this.token;
    }

    public Collection<String> getAuthorities() {
        return this.authorities;
    }

    public Integer getCodeError() {
        return this.codeError;
    }

    public String getCauseError() {
        return this.causeError;
    }

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public JwtAuthenticationResponse(String token, Collection<String> authorities) {
        this.token = token;
        this.authorities = authorities;
    }

    public JwtAuthenticationResponse(String token, Collection<String> authorities, Integer codeError, String causeError) {
        this.token = token;
        this.authorities = authorities;
        this.codeError = codeError;
        this.causeError = causeError;
    }
}
