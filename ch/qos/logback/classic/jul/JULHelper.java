package ch.qos.logback.classic.jul;

import ch.qos.logback.core.CoreConstants;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/jul/JULHelper.class */
public class JULHelper {
    public static final boolean isRegularNonRootLogger(Logger julLogger) {
        return (julLogger == null || julLogger.getName().equals(CoreConstants.EMPTY_STRING)) ? false : true;
    }

    public static final boolean isRoot(Logger julLogger) {
        if (julLogger == null) {
            return false;
        }
        return julLogger.getName().equals(CoreConstants.EMPTY_STRING);
    }

    public static Level asJULLevel(ch.qos.logback.classic.Level lbLevel) {
        if (lbLevel == null) {
            throw new IllegalArgumentException("Unexpected level [null]");
        }
        switch (lbLevel.levelInt) {
            case Integer.MIN_VALUE:
                return Level.ALL;
            case 5000:
                return Level.FINEST;
            case 10000:
                return Level.FINE;
            case 20000:
                return Level.INFO;
            case 30000:
                return Level.WARNING;
            case 40000:
                return Level.SEVERE;
            case Integer.MAX_VALUE:
                return Level.OFF;
            default:
                throw new IllegalArgumentException("Unexpected level [" + lbLevel + "]");
        }
    }

    public static String asJULLoggerName(String loggerName) {
        if (org.slf4j.Logger.ROOT_LOGGER_NAME.equals(loggerName)) {
            return CoreConstants.EMPTY_STRING;
        }
        return loggerName;
    }

    public static Logger asJULLogger(String loggerName) {
        String julLoggerName = asJULLoggerName(loggerName);
        return Logger.getLogger(julLoggerName);
    }

    public static Logger asJULLogger(ch.qos.logback.classic.Logger logger) {
        return asJULLogger(logger.getName());
    }
}
