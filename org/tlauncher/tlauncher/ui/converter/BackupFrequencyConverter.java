package org.tlauncher.tlauncher.ui.converter;

import ch.qos.logback.core.CoreConstants;
import org.tlauncher.tlauncher.configuration.enums.BackupFrequency;
import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/BackupFrequencyConverter.class */
public class BackupFrequencyConverter extends LocalizableStringConverter<BackupFrequency> {
    public BackupFrequencyConverter() {
        super(CoreConstants.EMPTY_STRING);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public BackupFrequency fromString(String from) {
        return BackupFrequency.get(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(BackupFrequency from) {
        if (from == null) {
            return null;
        }
        return BackupFrequency.convert(from);
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter
    public String toPath(BackupFrequency from) {
        if (from == null) {
            return null;
        }
        return from.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<BackupFrequency> getObjectClass() {
        return BackupFrequency.class;
    }
}
