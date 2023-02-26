package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/spi/ErrorHandler.class */
public interface ErrorHandler extends OptionHandler {
    void setLogger(Logger logger);

    void error(String str, Exception exc, int i);

    void error(String str);

    void error(String str, Exception exc, int i, LoggingEvent loggingEvent);

    void setAppender(Appender appender);

    void setBackupAppender(Appender appender);
}
