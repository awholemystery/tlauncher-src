package org.tlauncher.tlauncher.ui.util;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/util/ViewlUtil.class */
public class ViewlUtil {
    public static final int minCountCharater = 12;

    public static Dimension calculateSizeReview(JFrame parent) {
        Dimension dimension = parent.getSize();
        dimension.setSize((dimension.getWidth() * 2.0d) / 3.0d, ((dimension.getHeight() * 4.0d) / 5.0d) + 10.0d);
        return dimension;
    }

    public static JPanel createBoxYPanel(JLabel label, JTextComponent component) {
        JPanel p = new JPanel();
        p.setAlignmentX(0.0f);
        BoxLayout layout = new BoxLayout(p, 1);
        p.setLayout(layout);
        label.setAlignmentX(0.0f);
        component.setAlignmentY(0.0f);
        p.add(label);
        p.add(component);
        return p;
    }

    public static String addSpaces(String line, String serverName) {
        int addedSpaces = 12 - serverName.length();
        if (addedSpaces <= 0) {
            return line;
        }
        StringBuilder builder = new StringBuilder(line);
        for (int i = 0; i < addedSpaces; i++) {
            builder.append(' ');
        }
        return builder.toString();
    }
}
