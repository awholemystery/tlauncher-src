package org.tlauncher.tlauncher.ui.modpack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/ShadowPanel.class */
public class ShadowPanel extends ExtendedPanel {
    private int colorStarted;

    public ShadowPanel(int colorStarted) {
        this.colorStarted = colorStarted;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Rectangle rec = getVisibleRect();
        int i = this.colorStarted;
        Graphics2D g2 = (Graphics2D) g0;
        for (int y = rec.y; y < rec.height + rec.y; y++) {
            g2.setColor(new Color(i, i, i));
            if (i != 255) {
                i++;
            }
            g2.drawLine(rec.x, y, rec.x + rec.width, y);
        }
    }
}
