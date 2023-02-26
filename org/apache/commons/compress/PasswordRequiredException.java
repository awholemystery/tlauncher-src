package org.apache.commons.compress;

import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/PasswordRequiredException.class */
public class PasswordRequiredException extends IOException {
    private static final long serialVersionUID = 1391070005491684483L;

    public PasswordRequiredException(String name) {
        super("Cannot read encrypted content from " + name + " without a password.");
    }
}
