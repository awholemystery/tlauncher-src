package org.tlauncher.tlauncher.ui.swing.scroll;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/scroll/VersionScrollBarUI.class */
public class VersionScrollBarUI extends BasicScrollBarUI {
    protected int heightThubm = 16;
    protected int gapThubm = 6;
    protected Color lineColor = new Color(255, 255, 255);
    protected Color trackColor = new Color(30, 134, 187);
    protected Color thumbColor = new Color(191, 219, 235);

    public int getHeightThubm() {
        return this.heightThubm;
    }

    public void setHeightThubm(int heightThubm) {
        this.heightThubm = heightThubm;
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle rec = g2.getClipBounds();
        g2.setColor(this.trackColor);
        g2.fillRect(rec.x, rec.y, rec.width, rec.height);
    }

    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.thumbColor);
        g2.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        int width = thumbBounds.width - (thumbBounds.width / 3);
        int startX = thumbBounds.x + (thumbBounds.width / 6) + 1;
        int startY = (thumbBounds.y + (thumbBounds.height / 2)) - (this.heightThubm / 2);
        int i = 0;
        while (i < 4) {
            drawLines(g2, startX, startY, width);
            i++;
            startY += this.gapThubm;
        }
    }

    private void drawLines(Graphics2D g2, int startX, int startY, int width) {
        g2.setColor(Color.WHITE);
        g2.drawLine(startX, startY, (startX + width) - 1, startY);
        g2.setColor(new Color(190, 190, 190));
        g2.drawLine(startX, startY + 1, (startX + width) - 1, startY + 1);
    }

    protected Dimension getMinimumThumbSize() {
        return new Dimension(10, 80);
    }

    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton jbutton = new JButton();
        jbutton.setPreferredSize(new Dimension(0, 0));
        jbutton.setMinimumSize(new Dimension(0, 0));
        jbutton.setMaximumSize(new Dimension(0, 0));
        return jbutton;
    }

    public int getGapThubm() {
        return this.gapThubm;
    }

    public void setGapThubm(int gapThubm) {
        this.gapThubm = gapThubm;
    }
}
