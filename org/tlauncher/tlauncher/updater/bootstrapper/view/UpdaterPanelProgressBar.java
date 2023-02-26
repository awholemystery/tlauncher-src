package org.tlauncher.tlauncher.updater.bootstrapper.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/view/UpdaterPanelProgressBar.class */
public class UpdaterPanelProgressBar extends JPanel {
    private static final long serialVersionUID = -8469500310564854471L;
    protected Insets insets = new Insets(5, 10, 10, 10);
    protected Color background = new Color(255, 255, 255, 220);
    private final Color border = new Color(255, 255, 255, 255);

    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(this.background);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        g.setColor(this.border);
        for (int x = 1; x < 2; x++) {
            g.drawRoundRect(x - 1, x - 1, (getWidth() - (2 * x)) + 1, (getHeight() - (2 * x)) + 1, 16, 16);
        }
        Color shadow = U.shiftAlpha(Color.gray, -200);
        int x2 = 2;
        while (true) {
            shadow = U.shiftAlpha(shadow, -8);
            if (shadow.getAlpha() != 0) {
                g.setColor(shadow);
                g.drawRoundRect(x2 - 1, x2 - 1, (getWidth() - (2 * x2)) + 1, (getHeight() - (2 * x2)) + 1, (16 - (2 * x2)) + 1, (16 - (2 * x2)) + 1);
                x2++;
            } else {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                super.paintComponent(g0);
                return;
            }
        }
    }

    public Insets getInsets() {
        return this.insets;
    }
}
