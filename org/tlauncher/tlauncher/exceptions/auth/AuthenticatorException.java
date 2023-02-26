package org.tlauncher.tlauncher.exceptions.auth;

import org.tlauncher.tlauncher.entity.auth.Response;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/auth/AuthenticatorException.class */
public class AuthenticatorException extends Exception {
    private static final long serialVersionUID = -6773418626800336871L;
    private Response response;
    private String langpath;

    public AuthenticatorException(Throwable cause) {
        super(cause);
    }

    public AuthenticatorException(String message, String langpath) {
        super(message);
        this.langpath = langpath;
    }

    public AuthenticatorException(Response response, String langpath) {
        super(response.getErrorMessage());
        this.response = response;
        this.langpath = langpath;
    }

    public AuthenticatorException(String message, String langpath, Throwable cause) {
        super(message, cause);
        this.langpath = langpath;
    }

    public String getLangpath() {
        return this.langpath;
    }

    public Response getResponse() {
        return this.response;
    }
}
