package org.apache.log4j.pattern;

import org.apache.log4j.spi.LoggingEvent;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/LoggingEventPatternConverter.class */
public abstract class LoggingEventPatternConverter extends PatternConverter {
    public abstract void format(LoggingEvent loggingEvent, StringBuffer stringBuffer);

    /* JADX INFO: Access modifiers changed from: protected */
    public LoggingEventPatternConverter(String name, String style) {
        super(name, style);
    }

    @Override // org.apache.log4j.pattern.PatternConverter
    public void format(Object obj, StringBuffer output) {
        if (obj instanceof LoggingEvent) {
            format((LoggingEvent) obj, output);
        }
    }

    public boolean handlesThrowable() {
        return false;
    }
}
