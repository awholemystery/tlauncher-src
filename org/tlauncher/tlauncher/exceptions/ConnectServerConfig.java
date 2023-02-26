package org.tlauncher.tlauncher.exceptions;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/exceptions/ConnectServerConfig.class */
public class ConnectServerConfig extends Exception {
    private static final long serialVersionUID = 2977918735746519247L;

    public ConnectServerConfig(String name) {
        super(name);
    }

    public ConnectServerConfig(String name, Throwable er) {
        super(name, er);
    }
}
