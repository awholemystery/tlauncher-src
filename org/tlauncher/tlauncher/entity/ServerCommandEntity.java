package org.tlauncher.tlauncher.entity;

import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/ServerCommandEntity.class */
public class ServerCommandEntity {
    private String requestType;
    private String urn;
    private String body;
    private Map<String, String> queries;

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ServerCommandEntity) {
            ServerCommandEntity other = (ServerCommandEntity) o;
            if (other.canEqual(this)) {
                Object this$requestType = getRequestType();
                Object other$requestType = other.getRequestType();
                if (this$requestType == null) {
                    if (other$requestType != null) {
                        return false;
                    }
                } else if (!this$requestType.equals(other$requestType)) {
                    return false;
                }
                Object this$urn = getUrn();
                Object other$urn = other.getUrn();
                if (this$urn == null) {
                    if (other$urn != null) {
                        return false;
                    }
                } else if (!this$urn.equals(other$urn)) {
                    return false;
                }
                Object this$body = getBody();
                Object other$body = other.getBody();
                if (this$body == null) {
                    if (other$body != null) {
                        return false;
                    }
                } else if (!this$body.equals(other$body)) {
                    return false;
                }
                Object this$queries = getQueries();
                Object other$queries = other.getQueries();
                return this$queries == null ? other$queries == null : this$queries.equals(other$queries);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerCommandEntity;
    }

    public int hashCode() {
        Object $requestType = getRequestType();
        int result = (1 * 59) + ($requestType == null ? 43 : $requestType.hashCode());
        Object $urn = getUrn();
        int result2 = (result * 59) + ($urn == null ? 43 : $urn.hashCode());
        Object $body = getBody();
        int result3 = (result2 * 59) + ($body == null ? 43 : $body.hashCode());
        Object $queries = getQueries();
        return (result3 * 59) + ($queries == null ? 43 : $queries.hashCode());
    }

    public String toString() {
        return "ServerCommandEntity(requestType=" + getRequestType() + ", urn=" + getUrn() + ", body=" + getBody() + ", queries=" + getQueries() + ")";
    }

    public String getRequestType() {
        return this.requestType;
    }

    public String getUrn() {
        return this.urn;
    }

    public String getBody() {
        return this.body;
    }

    public Map<String, String> getQueries() {
        return this.queries;
    }
}
