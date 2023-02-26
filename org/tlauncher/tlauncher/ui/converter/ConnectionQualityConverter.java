package org.tlauncher.tlauncher.ui.converter;

import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/ConnectionQualityConverter.class */
public class ConnectionQualityConverter extends LocalizableStringConverter<ConnectionQuality> {
    public ConnectionQualityConverter() {
        super("settings.connection");
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public ConnectionQuality fromString(String from) {
        return ConnectionQuality.get(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(ConnectionQuality from) {
        return from.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter
    public String toPath(ConnectionQuality from) {
        return from.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<ConnectionQuality> getObjectClass() {
        return ConnectionQuality.class;
    }
}
