package org.tlauncher.tlauncher.ui.review;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/review/ProgressBar.class */
public class ProgressBar extends JFrame {
    private JProgressBar progress = new JProgressBar();
    private JFrame parent;

    public ProgressBar(JFrame parent, JButton cancel) {
        this.parent = parent;
        this.progress.setPreferredSize(new Dimension(180, 20));
        this.progress.setIndeterminate(true);
        setUndecorated(true);
        setSize(180, 75);
        add(this.progress, "Center");
        add(cancel, "South");
        parent.setGlassPane(new JComponent() { // from class: org.tlauncher.tlauncher.ui.review.ProgressBar.1
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 50));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        });
        pack();
    }

    public void setVisible(boolean b) {
        if (b) {
            setLocation((this.parent.getX() + (this.parent.getWidth() / 2)) - (getWidth() / 2), (this.parent.getY() + (this.parent.getHeight() / 2)) - (getHeight() / 2));
            this.parent.setEnabled(false);
        } else {
            this.parent.setEnabled(true);
        }
        this.parent.getGlassPane().setVisible(b);
        super.setVisible(b);
    }
}
