package org.tlauncher.tlauncher.ui.swing;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import org.tlauncher.tlauncher.ui.converter.StringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/OrderNumberDefaultConverterCellRenderer.class */
public class OrderNumberDefaultConverterCellRenderer<T> extends DefaultConverterCellRenderer<T> {
    public OrderNumberDefaultConverterCellRenderer(StringConverter<T> converter) {
        super(converter);
    }

    @Override // org.tlauncher.tlauncher.ui.swing.DefaultConverterCellRenderer
    public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel l = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        l.setText((index + 1) + ") " + l.getText());
        return l;
    }
}
