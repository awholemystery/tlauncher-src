package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/spi/LoggerContextListener.class */
public interface LoggerContextListener {
    boolean isResetResistant();

    void onStart(LoggerContext loggerContext);

    void onReset(LoggerContext loggerContext);

    void onStop(LoggerContext loggerContext);

    void onLevelChange(Logger logger, Level level);
}
