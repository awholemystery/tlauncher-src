package org.tlauncher.tlauncher.ui.converter.dummy;

import org.tlauncher.tlauncher.ui.converter.StringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/dummy/DummyConverter.class */
public abstract class DummyConverter<T> implements StringConverter<T> {
    private static DummyConverter<Object>[] converters;

    public abstract T fromDummyString(String str) throws RuntimeException;

    public abstract String toDummyValue(T t) throws RuntimeException;

    public static DummyConverter<Object>[] getConverters() {
        if (converters == null) {
            converters = new DummyConverter[]{new DummyStringConverter(), new DummyIntegerConverter(), new DummyDoubleConverter(), new DummyLongConverter(), new DummyDateConverter()};
        }
        return converters;
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public T fromString(String from) {
        return fromDummyString(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toString(T from) {
        return toValue(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(T from) {
        return toDummyValue(from);
    }
}
