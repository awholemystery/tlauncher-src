package org.tlauncher.tlauncher.entity.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/auth/Response.class */
public class Response {
    private String error;
    private String errorMessage;
    private String cause;

    public void setError(String error) {
        this.error = error;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Response) {
            Response other = (Response) o;
            if (other.canEqual(this)) {
                Object this$error = getError();
                Object other$error = other.getError();
                if (this$error == null) {
                    if (other$error != null) {
                        return false;
                    }
                } else if (!this$error.equals(other$error)) {
                    return false;
                }
                Object this$errorMessage = getErrorMessage();
                Object other$errorMessage = other.getErrorMessage();
                if (this$errorMessage == null) {
                    if (other$errorMessage != null) {
                        return false;
                    }
                } else if (!this$errorMessage.equals(other$errorMessage)) {
                    return false;
                }
                Object this$cause = getCause();
                Object other$cause = other.getCause();
                return this$cause == null ? other$cause == null : this$cause.equals(other$cause);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Response;
    }

    public int hashCode() {
        Object $error = getError();
        int result = (1 * 59) + ($error == null ? 43 : $error.hashCode());
        Object $errorMessage = getErrorMessage();
        int result2 = (result * 59) + ($errorMessage == null ? 43 : $errorMessage.hashCode());
        Object $cause = getCause();
        return (result2 * 59) + ($cause == null ? 43 : $cause.hashCode());
    }

    public String toString() {
        return "Response(error=" + getError() + ", errorMessage=" + getErrorMessage() + ", cause=" + getCause() + ")";
    }

    public String getError() {
        return this.error;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getCause() {
        return this.cause;
    }
}
