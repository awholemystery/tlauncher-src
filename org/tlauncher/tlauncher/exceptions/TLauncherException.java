package org.tlauncher.tlauncher.exceptions;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/TLauncherException.class */
public class TLauncherException extends RuntimeException {
    private static final long serialVersionUID = 5812333186574527445L;

    public TLauncherException(String message, Throwable e) {
        super(message, e);
        e.printStackTrace();
    }

    public TLauncherException(String message) {
        super(message);
    }
}
