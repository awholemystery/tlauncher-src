package org.tlauncher.tlauncher.ui.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.tlauncher.tlauncher.ui.images.ImageIcon;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/server/BackPanel.class */
public class BackPanel extends JPanel {
    private static final long serialVersionUID = 1;
    public static final Color BACKGROUND_COLOR = new Color(60, 170, 232);
    private final JLabel backLabel;

    public BackPanel(String titleName, MouseListener listener, ImageIcon icon) {
        this.backLabel = new JLabel(icon);
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND_COLOR);
        this.backLabel.setPreferredSize(new Dimension(65, 25));
        this.backLabel.setBackground(new Color(46, (int) TarConstants.PREFIXLEN_XSTAR, 177));
        this.backLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        this.backLabel.addMouseListener(listener);
        add(this.backLabel, "West");
        LocalizableLabel label = new LocalizableLabel(titleName);
        label.setFont(label.getFont().deriveFont(1, 16.0f));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(0);
        add(label, "Center");
        this.backLabel.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.server.BackPanel.1
            public void mouseEntered(MouseEvent e) {
                BackPanel.this.backLabel.setOpaque(true);
                BackPanel.this.backLabel.repaint();
            }

            public void mouseExited(MouseEvent e) {
                BackPanel.this.backLabel.setOpaque(false);
                BackPanel.this.backLabel.repaint();
            }
        });
    }

    public void addBackListener(MouseListener listener) {
        this.backLabel.addMouseListener(listener);
    }
}
