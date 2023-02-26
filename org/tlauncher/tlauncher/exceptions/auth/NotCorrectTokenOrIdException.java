package org.tlauncher.tlauncher.exceptions.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/auth/NotCorrectTokenOrIdException.class */
public class NotCorrectTokenOrIdException extends AuthenticatorException {
    public NotCorrectTokenOrIdException() {
        super("Invalid client id or token", "authorization");
    }
}
