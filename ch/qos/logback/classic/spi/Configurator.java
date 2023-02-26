package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.spi.ContextAware;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/spi/Configurator.class */
public interface Configurator extends ContextAware {
    void configure(LoggerContext loggerContext);
}
