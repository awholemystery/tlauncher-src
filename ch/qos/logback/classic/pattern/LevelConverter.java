package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/pattern/LevelConverter.class */
public class LevelConverter extends ClassicConverter {
    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent le) {
        return le.getLevel().toString();
    }
}
