package org.tlauncher.tlauncher.exceptions.auth;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/auth/ServiceUnavailableException.class */
public class ServiceUnavailableException extends AuthenticatorException {
    public ServiceUnavailableException(String message) {
        super(message, "unavailable");
    }
}
