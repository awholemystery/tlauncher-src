package org.tlauncher.tlauncher.ui.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import org.tlauncher.tlauncher.ui.converter.StringConverter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/DefaultConverterCellRenderer.class */
public class DefaultConverterCellRenderer<T> extends ConverterCellRenderer<T> {
    private final DefaultListCellRenderer defaultRenderer;
    public static final Color DARK_COLOR_TEXT = new Color(77, 77, 77);
    public static final Color OVER_ITEM = new Color(235, 235, 235);

    public DefaultConverterCellRenderer(StringConverter<T> converter) {
        super(converter);
        this.defaultRenderer = new DefaultListCellRenderer();
    }

    public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) {
            renderer.setBackground(OVER_ITEM);
        } else {
            renderer.setBackground(Color.white);
        }
        renderer.setForeground(DARK_COLOR_TEXT);
        renderer.setOpaque(true);
        renderer.setText(this.converter.toString(value));
        renderer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
        return renderer;
    }
}
