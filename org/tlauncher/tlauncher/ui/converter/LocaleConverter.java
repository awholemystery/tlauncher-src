package org.tlauncher.tlauncher.ui.converter;

import java.util.Locale;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/LocaleConverter.class */
public class LocaleConverter implements StringConverter<Locale> {
    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toString(Locale from) {
        if (from == null) {
            return null;
        }
        if (from.equals(Locale.ENGLISH)) {
            TLauncher.getInstance();
            return TLauncher.getInnerSettings().get("converter.value" + from.toString());
        }
        TLauncher.getInstance();
        return TLauncher.getInnerSettings().get("converter.value." + from.toString());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Locale fromString(String from) {
        return Configuration.getLocaleOf(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(Locale from) {
        if (from == null) {
            return null;
        }
        return from.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<Locale> getObjectClass() {
        return Locale.class;
    }
}
