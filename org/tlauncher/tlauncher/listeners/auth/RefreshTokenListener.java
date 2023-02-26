package org.tlauncher.tlauncher.listeners.auth;

import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/listeners/auth/RefreshTokenListener.class */
public class RefreshTokenListener implements AuthenticatorListener {
    private final ValidateAccountToken validateAccountToken;
    private volatile Exception exception;

    public RefreshTokenListener(ValidateAccountToken validateAccountToken) {
        this.validateAccountToken = validateAccountToken;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof RefreshTokenListener) {
            RefreshTokenListener other = (RefreshTokenListener) o;
            if (other.canEqual(this)) {
                Object this$validateAccountToken = getValidateAccountToken();
                Object other$validateAccountToken = other.getValidateAccountToken();
                if (this$validateAccountToken == null) {
                    if (other$validateAccountToken != null) {
                        return false;
                    }
                } else if (!this$validateAccountToken.equals(other$validateAccountToken)) {
                    return false;
                }
                Object this$exception = getException();
                Object other$exception = other.getException();
                return this$exception == null ? other$exception == null : this$exception.equals(other$exception);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RefreshTokenListener;
    }

    public int hashCode() {
        Object $validateAccountToken = getValidateAccountToken();
        int result = (1 * 59) + ($validateAccountToken == null ? 43 : $validateAccountToken.hashCode());
        Object $exception = getException();
        return (result * 59) + ($exception == null ? 43 : $exception.hashCode());
    }

    public String toString() {
        return "RefreshTokenListener(validateAccountToken=" + getValidateAccountToken() + ", exception=" + getException() + ")";
    }

    public ValidateAccountToken getValidateAccountToken() {
        return this.validateAccountToken;
    }

    public Exception getException() {
        return this.exception;
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassing(Authenticator auth) {
        this.exception = null;
    }

    private void unblock() {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassingError(Authenticator auth, Exception e) {
        this.exception = e;
        unblock();
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassed(Authenticator auth) {
        unblock();
    }
}
