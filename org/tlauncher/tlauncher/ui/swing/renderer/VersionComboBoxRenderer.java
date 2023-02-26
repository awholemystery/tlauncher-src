package org.tlauncher.tlauncher.ui.swing.renderer;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import net.minecraft.launcher.versions.CompleteVersion;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/VersionComboBoxRenderer.class */
public class VersionComboBoxRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            label.setText(((CompleteVersion) value).getID());
        }
        return label;
    }
}
