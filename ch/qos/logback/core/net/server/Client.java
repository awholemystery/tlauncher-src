package ch.qos.logback.core.net.server;

import java.io.Closeable;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/net/server/Client.class */
public interface Client extends Runnable, Closeable {
    void close();
}
