package org.tlauncher.tlauncher.exceptions.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/auth/NotCorrectPasswordOrLogingException.class */
public class NotCorrectPasswordOrLogingException extends AuthenticatorException {
    public NotCorrectPasswordOrLogingException() {
        super("Invalid user or password", "relogin");
    }
}
