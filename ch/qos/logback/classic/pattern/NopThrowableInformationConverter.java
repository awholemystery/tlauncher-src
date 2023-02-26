package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/pattern/NopThrowableInformationConverter.class */
public class NopThrowableInformationConverter extends ThrowableHandlingConverter {
    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent event) {
        return CoreConstants.EMPTY_STRING;
    }
}
