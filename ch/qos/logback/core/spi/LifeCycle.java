package ch.qos.logback.core.spi;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/spi/LifeCycle.class */
public interface LifeCycle {
    void start();

    void stop();

    boolean isStarted();
}
