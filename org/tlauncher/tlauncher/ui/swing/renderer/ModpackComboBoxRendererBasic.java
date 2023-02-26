package org.tlauncher.tlauncher.ui.swing.renderer;

import ch.qos.logback.core.CoreConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/ModpackComboBoxRendererBasic.class */
public abstract class ModpackComboBoxRendererBasic extends DefaultListCellRenderer {
    private static final long serialVersionUID = 2710505952547859346L;
    static final int GUP_LEFT = 13;
    public static final Color LINE = new Color(149, 149, 149);
    public static final Color TEXT_COLOR = new Color(25, 25, 25);

    public abstract String getRenderText(Object obj);

    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (label == null) {
            return null;
        }
        SwingUtil.changeFontFamily(label, FontTL.ROBOTO_MEDIUM, 14, ColorUtil.COLOR_25);
        label.setPreferredSize(new Dimension(238, 44));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setForeground(TEXT_COLOR);
        if (isSelected && index != -1) {
            label.setBackground(new Color(235, 235, 235));
        } else {
            label.setBackground(Color.white);
        }
        label.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
        label.setText(Objects.isNull(value) ? CoreConstants.EMPTY_STRING : getRenderText(value));
        return label;
    }
}
