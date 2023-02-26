package org.tlauncher.tlauncher.ui.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.plaf.basic.BasicButtonUI;
import org.tlauncher.util.SwingUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/RadioSettingsUI.class */
public class RadioSettingsUI extends BasicButtonUI {
    Image backgroundImage;

    public RadioSettingsUI(Image image) {
        this.backgroundImage = image;
    }

    public void paint(Graphics g, JComponent c) {
        JRadioButton button = (JRadioButton) c;
        Rectangle rec = c.getVisibleRect();
        paintBackground(g, rec, button.isSelected());
        paintText(g, rec, button);
    }

    private void paintText(Graphics g, Rectangle rec, JRadioButton comp) {
        Graphics2D g2d = (Graphics2D) g;
        SwingUtil.paintText(g2d, comp, comp.getText());
    }

    private void paintBackground(Graphics g, Rectangle rec, boolean state) {
        if (state) {
            g.setColor(Color.WHITE);
            g.fillRect(rec.x, rec.y, (int) rec.getWidth(), (int) rec.getHeight());
            return;
        }
        g.drawImage(this.backgroundImage, rec.x, rec.y, (ImageObserver) null);
    }
}
