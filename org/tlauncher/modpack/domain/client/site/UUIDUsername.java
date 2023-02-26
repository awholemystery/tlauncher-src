package org.tlauncher.modpack.domain.client.site;

import org.tlauncher.modpack.domain.client.share.AuthorityName;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/UUIDUsername.class */
public class UUIDUsername {
    private String uuid;
    private String username;
    private String email;
    private AuthorityName authority;

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthority(AuthorityName authority) {
        this.authority = authority;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof UUIDUsername) {
            UUIDUsername other = (UUIDUsername) o;
            if (other.canEqual(this)) {
                Object this$uuid = getUuid();
                Object other$uuid = other.getUuid();
                if (this$uuid == null) {
                    if (other$uuid != null) {
                        return false;
                    }
                } else if (!this$uuid.equals(other$uuid)) {
                    return false;
                }
                Object this$username = getUsername();
                Object other$username = other.getUsername();
                if (this$username == null) {
                    if (other$username != null) {
                        return false;
                    }
                } else if (!this$username.equals(other$username)) {
                    return false;
                }
                Object this$email = getEmail();
                Object other$email = other.getEmail();
                if (this$email == null) {
                    if (other$email != null) {
                        return false;
                    }
                } else if (!this$email.equals(other$email)) {
                    return false;
                }
                Object this$authority = getAuthority();
                Object other$authority = other.getAuthority();
                return this$authority == null ? other$authority == null : this$authority.equals(other$authority);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UUIDUsername;
    }

    public int hashCode() {
        Object $uuid = getUuid();
        int result = (1 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $username = getUsername();
        int result2 = (result * 59) + ($username == null ? 43 : $username.hashCode());
        Object $email = getEmail();
        int result3 = (result2 * 59) + ($email == null ? 43 : $email.hashCode());
        Object $authority = getAuthority();
        return (result3 * 59) + ($authority == null ? 43 : $authority.hashCode());
    }

    public String toString() {
        return "UUIDUsername(uuid=" + getUuid() + ", username=" + getUsername() + ", email=" + getEmail() + ", authority=" + getAuthority() + ")";
    }

    public UUIDUsername(String uuid, String username, String email, AuthorityName authority) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.authority = authority;
    }

    public UUIDUsername() {
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public AuthorityName getAuthority() {
        return this.authority;
    }
}
