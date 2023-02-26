package org.tlauncher.tlauncher.ui.converter.dummy;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/dummy/DummyDoubleConverter.class */
public class DummyDoubleConverter extends DummyConverter<Double> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public Double fromDummyString(String from) throws RuntimeException {
        return Double.valueOf(Double.parseDouble(from));
    }

    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public String toDummyValue(Double value) throws RuntimeException {
        return value.toString();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<Double> getObjectClass() {
        return Double.class;
    }
}
