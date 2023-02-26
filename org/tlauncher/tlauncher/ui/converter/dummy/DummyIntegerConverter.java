package org.tlauncher.tlauncher.ui.converter.dummy;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/dummy/DummyIntegerConverter.class */
public class DummyIntegerConverter extends DummyConverter<Integer> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public Integer fromDummyString(String from) throws RuntimeException {
        return Integer.valueOf(Integer.parseInt(from));
    }

    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public String toDummyValue(Integer value) throws RuntimeException {
        return value.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<Integer> getObjectClass() {
        return Integer.class;
    }
}
