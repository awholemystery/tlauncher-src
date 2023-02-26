package org.tlauncher.tlauncher.ui.converter;

import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;
import org.tlauncher.util.Direction;
import org.tlauncher.util.Reflect;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/DirectionConverter.class */
public class DirectionConverter extends LocalizableStringConverter<Direction> {
    public DirectionConverter() {
        super("settings.direction");
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Direction fromString(String from) {
        return (Direction) Reflect.parseEnum(Direction.class, from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(Direction from) {
        if (from == null) {
            return null;
        }
        return from.toString().toLowerCase();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<Direction> getObjectClass() {
        return Direction.class;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter
    public String toPath(Direction from) {
        return toValue(from);
    }
}
