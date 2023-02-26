package org.tlauncher.tlauncher.ui.converter.dummy;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/dummy/DummyStringConverter.class */
public class DummyStringConverter extends DummyConverter<String> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public String fromDummyString(String from) throws RuntimeException {
        return from;
    }

    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public String toDummyValue(String value) throws RuntimeException {
        return value;
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<String> getObjectClass() {
        return String.class;
    }
}
