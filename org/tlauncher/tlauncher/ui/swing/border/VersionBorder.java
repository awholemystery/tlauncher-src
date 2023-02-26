package org.tlauncher.tlauncher.ui.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.MatteBorder;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/border/VersionBorder.class */
public class VersionBorder extends MatteBorder {
    public static final Color SEPARATOR_COLOR = new Color(220, 220, 220);

    public VersionBorder(int top, int left, int bottom, int right, Color matteColor) {
        super(top, left, bottom, right, matteColor);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets insets = getBorderInsets(c);
        Color oldColor = g.getColor();
        g.translate(x, y);
        if (this.tileIcon != null) {
            this.color = this.tileIcon.getIconWidth() == -1 ? Color.gray : null;
        }
        if (this.color != null) {
            g.setColor(SEPARATOR_COLOR);
            g.drawLine(0, height - 1, width, height - 1);
        } else if (this.tileIcon != null) {
            int tileW = this.tileIcon.getIconWidth();
            int tileH = this.tileIcon.getIconHeight();
            paintEdge(c, g, 0, 0, width - insets.right, insets.top, tileW, tileH);
            paintEdge(c, g, 0, insets.top, insets.left, height - insets.top, tileW, tileH);
            paintEdge(c, g, insets.left, height - insets.bottom, width - insets.left, insets.bottom, tileW, tileH);
            paintEdge(c, g, width - insets.right, 0, insets.right, height - insets.bottom, tileW, tileH);
        }
        g.translate(-x, -y);
        g.setColor(oldColor);
    }

    private void paintEdge(Component c, Graphics g, int x, int y, int width, int height, int tileW, int tileH) {
        Graphics g2 = g.create(x, y, width, height);
        int sY = -(y % tileH);
        int i = -(x % tileW);
        while (true) {
            int x2 = i;
            if (x2 < width) {
                int i2 = sY;
                while (true) {
                    int y2 = i2;
                    if (y2 < height) {
                        this.tileIcon.paintIcon(c, g2, x2, y2);
                        i2 = y2 + tileH;
                    }
                }
                i = x2 + tileW;
            } else {
                g2.dispose();
                return;
            }
        }
    }
}
