package org.tlauncher.tlauncher.ui.converter.dummy;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/dummy/DummyLongConverter.class */
public class DummyLongConverter extends DummyConverter<Long> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public Long fromDummyString(String from) throws RuntimeException {
        return Long.valueOf(Long.parseLong(from));
    }

    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public String toDummyValue(Long value) throws RuntimeException {
        return value.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<Long> getObjectClass() {
        return Long.class;
    }
}
