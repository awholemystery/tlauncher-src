package org.tlauncher.tlauncher.ui.loc;

import org.tlauncher.tlauncher.ui.converter.StringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableStringConverter.class */
public abstract class LocalizableStringConverter<T> implements StringConverter<T> {
    private final String prefix;

    protected abstract String toPath(T t);

    public LocalizableStringConverter(String prefix) {
        this.prefix = prefix;
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toString(T from) {
        return Localizable.get(getPath(from));
    }

    String getPath(T from) {
        String prefix = getPrefix();
        if (prefix == null || prefix.isEmpty()) {
            return toPath(from);
        }
        String path = toPath(from);
        return prefix + "." + path;
    }

    String getPrefix() {
        return this.prefix;
    }
}
