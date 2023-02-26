package org.apache.log4j.lf5;

import ch.qos.logback.core.CoreConstants;
import java.awt.Toolkit;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.lf5.viewer.LogBrokerMonitor;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/lf5/LF5Appender.class */
public class LF5Appender extends AppenderSkeleton {
    protected LogBrokerMonitor _logMonitor;
    protected static LogBrokerMonitor _defaultLogMonitor;
    protected static AppenderFinalizer _finalizer;

    public LF5Appender() {
        this(getDefaultInstance());
    }

    public LF5Appender(LogBrokerMonitor monitor) {
        if (monitor != null) {
            this._logMonitor = monitor;
        }
    }

    @Override // org.apache.log4j.AppenderSkeleton
    public void append(LoggingEvent event) {
        String category = event.getLoggerName();
        String logMessage = event.getRenderedMessage();
        String nestedDiagnosticContext = event.getNDC();
        String threadDescription = event.getThreadName();
        String level = event.getLevel().toString();
        long time = event.timeStamp;
        LocationInfo locationInfo = event.getLocationInformation();
        Log4JLogRecord record = new Log4JLogRecord();
        record.setCategory(category);
        record.setMessage(logMessage);
        record.setLocation(locationInfo.fullInfo);
        record.setMillis(time);
        record.setThreadDescription(threadDescription);
        if (nestedDiagnosticContext != null) {
            record.setNDC(nestedDiagnosticContext);
        } else {
            record.setNDC(CoreConstants.EMPTY_STRING);
        }
        if (event.getThrowableInformation() != null) {
            record.setThrownStackTrace(event.getThrowableInformation());
        }
        try {
            record.setLevel(LogLevel.valueOf(level));
        } catch (LogLevelFormatException e) {
            record.setLevel(LogLevel.WARN);
        }
        if (this._logMonitor != null) {
            this._logMonitor.addMessage(record);
        }
    }

    @Override // org.apache.log4j.Appender
    public void close() {
    }

    @Override // org.apache.log4j.Appender
    public boolean requiresLayout() {
        return false;
    }

    public void setCallSystemExitOnClose(boolean callSystemExitOnClose) {
        this._logMonitor.setCallSystemExitOnClose(callSystemExitOnClose);
    }

    public boolean equals(LF5Appender compareTo) {
        return this._logMonitor == compareTo.getLogBrokerMonitor();
    }

    public LogBrokerMonitor getLogBrokerMonitor() {
        return this._logMonitor;
    }

    public static void main(String[] args) {
        new LF5Appender();
    }

    public void setMaxNumberOfRecords(int maxNumberOfRecords) {
        _defaultLogMonitor.setMaxNumberOfLogRecords(maxNumberOfRecords);
    }

    protected static synchronized LogBrokerMonitor getDefaultInstance() {
        if (_defaultLogMonitor == null) {
            try {
                _defaultLogMonitor = new LogBrokerMonitor(LogLevel.getLog4JLevels());
                _finalizer = new AppenderFinalizer(_defaultLogMonitor);
                _defaultLogMonitor.setFrameSize(getDefaultMonitorWidth(), getDefaultMonitorHeight());
                _defaultLogMonitor.setFontSize(12);
                _defaultLogMonitor.show();
            } catch (SecurityException e) {
                _defaultLogMonitor = null;
            }
        }
        return _defaultLogMonitor;
    }

    protected static int getScreenWidth() {
        try {
            return Toolkit.getDefaultToolkit().getScreenSize().width;
        } catch (Throwable th) {
            return 800;
        }
    }

    protected static int getScreenHeight() {
        try {
            return Toolkit.getDefaultToolkit().getScreenSize().height;
        } catch (Throwable th) {
            return 600;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getDefaultMonitorWidth() {
        return (3 * getScreenWidth()) / 4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getDefaultMonitorHeight() {
        return (3 * getScreenHeight()) / 4;
    }
}
