package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/pattern/ThreadConverter.class */
public class ThreadConverter extends ClassicConverter {
    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent event) {
        return event.getThreadName();
    }
}
