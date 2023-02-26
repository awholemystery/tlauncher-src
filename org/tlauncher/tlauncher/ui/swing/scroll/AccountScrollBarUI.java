package org.tlauncher.tlauncher.ui.swing.scroll;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/scroll/AccountScrollBarUI.class */
public class AccountScrollBarUI extends BasicScrollBarUI {
    protected int heightThubm = 46;
    protected Color trackColor = new Color(225, 234, 238);
    protected Color thumbColor = new Color((int) HttpStatus.SC_RESET_CONTENT, (int) CompleteSubEntityScene.FullGameEntity.CompleteDescriptionGamePanel.SHADOW_PANEL, 233);

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
    }

    protected Dimension getMinimumThumbSize() {
        return new Dimension(7, 46);
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
}
