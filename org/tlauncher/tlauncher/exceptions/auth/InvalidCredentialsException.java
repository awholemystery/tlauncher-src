package org.tlauncher.tlauncher.exceptions.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/auth/InvalidCredentialsException.class */
public class InvalidCredentialsException extends AuthenticatorException {
    private static final long serialVersionUID = 7221509839484990453L;

    public InvalidCredentialsException() {
        super("Invalid user / password / token", "relogin");
    }
}
