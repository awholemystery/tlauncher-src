package org.tlauncher.tlauncher.ui.converter;

import net.minecraft.launcher.versions.ReleaseType;
import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/ReleaseTypeConverter.class */
public class ReleaseTypeConverter extends LocalizableStringConverter<ReleaseType> {
    public ReleaseTypeConverter() {
        super("version.description");
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public ReleaseType fromString(String from) {
        ReleaseType[] values;
        if (from == null) {
            return ReleaseType.UNKNOWN;
        }
        for (ReleaseType type : ReleaseType.values()) {
            if (type.toString().equals(from)) {
                return type;
            }
        }
        return null;
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(ReleaseType from) {
        if (from == null) {
            return ReleaseType.UNKNOWN.toString();
        }
        return from.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter
    public String toPath(ReleaseType from) {
        if (from == null) {
            return ReleaseType.UNKNOWN.toString();
        }
        return toValue(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<ReleaseType> getObjectClass() {
        return ReleaseType.class;
    }
}
