package org.apache.http;

import java.nio.charset.CharacterCodingException;

/* loaded from: TLauncher-2.876.jar:org/apache/http/MessageConstraintException.class */
public class MessageConstraintException extends CharacterCodingException {
    private static final long serialVersionUID = 6077207720446368695L;
    private final String message;

    public MessageConstraintException(String message) {
        this.message = message;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.message;
    }
}
