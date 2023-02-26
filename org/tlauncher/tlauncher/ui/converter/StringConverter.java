package org.tlauncher.tlauncher.ui.converter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/StringConverter.class */
public interface StringConverter<T> {
    T fromString(String str);

    String toString(T t);

    String toValue(T t);

    Class<T> getObjectClass();
}
