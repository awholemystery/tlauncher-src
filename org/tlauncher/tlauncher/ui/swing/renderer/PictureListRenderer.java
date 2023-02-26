package org.tlauncher.tlauncher.ui.swing.renderer;

import ch.qos.logback.core.CoreConstants;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/PictureListRenderer.class */
public class PictureListRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setIcon((ImageIcon) value);
        label.setPreferredSize(new Dimension(294 + 20, 190));
        label.setText(CoreConstants.EMPTY_STRING);
        label.setOpaque(false);
        if (index == 2) {
            label.setPreferredSize(new Dimension(294, 190));
        } else {
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        }
        return label;
    }
}
