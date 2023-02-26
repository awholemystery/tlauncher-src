package org.tlauncher.tlauncher.ui.converter;

import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/ActionOnLaunchConverter.class */
public class ActionOnLaunchConverter extends LocalizableStringConverter<ActionOnLaunch> {
    public ActionOnLaunchConverter() {
        super("settings.launch-action");
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public ActionOnLaunch fromString(String from) {
        return ActionOnLaunch.get(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(ActionOnLaunch from) {
        return from.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter
    public String toPath(ActionOnLaunch from) {
        return from.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<ActionOnLaunch> getObjectClass() {
        return ActionOnLaunch.class;
    }
}
