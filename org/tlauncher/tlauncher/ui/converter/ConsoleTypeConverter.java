package org.tlauncher.tlauncher.ui.converter;

import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/ConsoleTypeConverter.class */
public class ConsoleTypeConverter extends LocalizableStringConverter<ConsoleType> {
    public ConsoleTypeConverter() {
        super("settings.console");
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public ConsoleType fromString(String from) {
        return ConsoleType.get(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(ConsoleType from) {
        if (from == null) {
            return null;
        }
        return from.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter
    public String toPath(ConsoleType from) {
        if (from == null) {
            return null;
        }
        return from.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<ConsoleType> getObjectClass() {
        return ConsoleType.class;
    }
}
