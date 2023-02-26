package org.tlauncher.modpack.domain.client.auth;

import java.io.Serializable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/auth/JwtAuthenticationRequest.class */
public class JwtAuthenticationRequest implements Serializable {
    private static final long serialVersionUID = -8445943548965154778L;
    private String username;
    private String password;
    private String email;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JwtAuthenticationRequest) {
            JwtAuthenticationRequest other = (JwtAuthenticationRequest) o;
            if (other.canEqual(this)) {
                Object this$username = getUsername();
                Object other$username = other.getUsername();
                if (this$username == null) {
                    if (other$username != null) {
                        return false;
                    }
                } else if (!this$username.equals(other$username)) {
                    return false;
                }
                Object this$password = getPassword();
                Object other$password = other.getPassword();
                if (this$password == null) {
                    if (other$password != null) {
                        return false;
                    }
                } else if (!this$password.equals(other$password)) {
                    return false;
                }
                Object this$email = getEmail();
                Object other$email = other.getEmail();
                return this$email == null ? other$email == null : this$email.equals(other$email);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JwtAuthenticationRequest;
    }

    public int hashCode() {
        Object $username = getUsername();
        int result = (1 * 59) + ($username == null ? 43 : $username.hashCode());
        Object $password = getPassword();
        int result2 = (result * 59) + ($password == null ? 43 : $password.hashCode());
        Object $email = getEmail();
        return (result2 * 59) + ($email == null ? 43 : $email.hashCode());
    }

    public String toString() {
        return "JwtAuthenticationRequest(username=" + getUsername() + ", password=" + getPassword() + ", email=" + getEmail() + ")";
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }
}
