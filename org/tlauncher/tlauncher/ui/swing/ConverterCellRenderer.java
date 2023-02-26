package org.tlauncher.tlauncher.ui.swing;

import javax.swing.ListCellRenderer;
import org.tlauncher.tlauncher.ui.converter.StringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/ConverterCellRenderer.class */
public abstract class ConverterCellRenderer<T> implements ListCellRenderer<T> {
    protected final StringConverter<T> converter;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConverterCellRenderer(StringConverter<T> converter) {
        if (converter == null) {
            throw new NullPointerException();
        }
        this.converter = converter;
    }

    public StringConverter<T> getConverter() {
        return this.converter;
    }
}
