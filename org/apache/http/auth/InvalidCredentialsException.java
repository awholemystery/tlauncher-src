package org.apache.http.auth;

import org.apache.http.annotation.Immutable;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/auth/InvalidCredentialsException.class */
public class InvalidCredentialsException extends AuthenticationException {
    private static final long serialVersionUID = -4834003835215460648L;

    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
