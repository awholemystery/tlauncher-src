package org.tlauncher.tlauncher.ui.converter.dummy;

import java.util.Date;
import net.minecraft.launcher.versions.json.DateTypeAdapter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/dummy/DummyDateConverter.class */
public class DummyDateConverter extends DummyConverter<Date> {
    private final DateTypeAdapter dateAdapter = new DateTypeAdapter();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public Date fromDummyString(String from) throws RuntimeException {
        return this.dateAdapter.toDate(from);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.dummy.DummyConverter
    public String toDummyValue(Date value) throws RuntimeException {
        return this.dateAdapter.toString(value);
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<Date> getObjectClass() {
        return Date.class;
    }
}
