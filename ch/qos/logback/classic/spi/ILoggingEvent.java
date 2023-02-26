package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import java.util.Map;
import org.slf4j.Marker;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/spi/ILoggingEvent.class */
public interface ILoggingEvent extends DeferredProcessingAware {
    String getThreadName();

    Level getLevel();

    String getMessage();

    Object[] getArgumentArray();

    String getFormattedMessage();

    String getLoggerName();

    LoggerContextVO getLoggerContextVO();

    IThrowableProxy getThrowableProxy();

    StackTraceElement[] getCallerData();

    boolean hasCallerData();

    Marker getMarker();

    Map<String, String> getMDCPropertyMap();

    Map<String, String> getMdc();

    long getTimeStamp();

    @Override // ch.qos.logback.core.spi.DeferredProcessingAware
    void prepareForDeferredProcessing();
}
