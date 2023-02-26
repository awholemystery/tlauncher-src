package org.tlauncher.tlauncher.ui.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/ModpackScrollBarUI.class */
public class ModpackScrollBarUI extends BasicScrollBarUI {
    protected int heightThubm = 16;
    protected int gapThubm = 6;
    int arc = 10;
    protected Color trackColor = new Color((int) ModpackScene.WIDTH_SEARCH_PANEL, (int) ModpackScene.WIDTH_SEARCH_PANEL, (int) ModpackScene.WIDTH_SEARCH_PANEL);
    protected Color thumbColor = new Color(182, 182, 182);
    private static final int gup = 2;

    public int getHeightThubm() {
        return this.heightThubm;
    }

    public void setHeightThubm(int heightThubm) {
        this.heightThubm = heightThubm;
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = g.create();
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);
        Container parent = c.getParent();
        Rectangle rec = this.trackRect;
        if (parent != null) {
            Color bg = parent.getBackground();
            g2.setColor(bg);
            g2.fillRect(rec.x, rec.y, rec.width, rec.height);
        }
        g2.setColor(this.trackColor);
        g2.fillRoundRect(rec.x, rec.y + 2, rec.width, rec.height - 4, this.arc, this.arc);
        g2.dispose();
    }

    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = g.create();
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);
        g2.setColor(this.thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y + 2, thumbBounds.width, thumbBounds.height - 4, this.arc, this.arc);
        g2.dispose();
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
        return jbutton;
    }

    public int getGapThubm() {
        return this.gapThubm;
    }

    public void setGapThubm(int gapThubm) {
        this.gapThubm = gapThubm;
    }
}
