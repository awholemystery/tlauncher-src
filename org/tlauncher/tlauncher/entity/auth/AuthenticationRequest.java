package org.tlauncher.tlauncher.entity.auth;

import org.tlauncher.tlauncher.minecraft.auth.Agent;
import org.tlauncher.tlauncher.minecraft.auth.Authenticator;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/auth/AuthenticationRequest.class */
public class AuthenticationRequest extends Request {
    private Agent agent = Agent.MINECRAFT;
    private String username;
    private String password;

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Request
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AuthenticationRequest) {
            AuthenticationRequest other = (AuthenticationRequest) o;
            if (other.canEqual(this)) {
                Object this$agent = getAgent();
                Object other$agent = other.getAgent();
                if (this$agent == null) {
                    if (other$agent != null) {
                        return false;
                    }
                } else if (!this$agent.equals(other$agent)) {
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
                Object this$password = getPassword();
                Object other$password = other.getPassword();
                return this$password == null ? other$password == null : this$password.equals(other$password);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Request
    protected boolean canEqual(Object other) {
        return other instanceof AuthenticationRequest;
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Request
    public int hashCode() {
        Object $agent = getAgent();
        int result = (1 * 59) + ($agent == null ? 43 : $agent.hashCode());
        Object $username = getUsername();
        int result2 = (result * 59) + ($username == null ? 43 : $username.hashCode());
        Object $password = getPassword();
        return (result2 * 59) + ($password == null ? 43 : $password.hashCode());
    }

    @Override // org.tlauncher.tlauncher.entity.auth.Request
    public String toString() {
        return "AuthenticationRequest(agent=" + getAgent() + ", username=" + getUsername() + ", password=" + getPassword() + ")";
    }

    public Agent getAgent() {
        return this.agent;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public AuthenticationRequest(Authenticator auth) {
        this.username = auth.getAccount().getUsername();
        this.password = auth.getAccount().getPassword();
        setClientToken(Authenticator.getClientToken().toString());
    }
}
