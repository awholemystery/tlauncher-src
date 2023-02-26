package org.tlauncher.tlauncher.entity.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/auth/Request.class */
public class Request {
    private String clientToken;

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Request) {
            Request other = (Request) o;
            if (other.canEqual(this)) {
                Object this$clientToken = getClientToken();
                Object other$clientToken = other.getClientToken();
                return this$clientToken == null ? other$clientToken == null : this$clientToken.equals(other$clientToken);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Request;
    }

    public int hashCode() {
        Object $clientToken = getClientToken();
        int result = (1 * 59) + ($clientToken == null ? 43 : $clientToken.hashCode());
        return result;
    }

    public String toString() {
        return "Request(clientToken=" + getClientToken() + ")";
    }

    public String getClientToken() {
        return this.clientToken;
    }
}
