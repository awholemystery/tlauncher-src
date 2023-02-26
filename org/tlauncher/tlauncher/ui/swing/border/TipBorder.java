package org.tlauncher.tlauncher.ui.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.MatteBorder;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/border/TipBorder.class */
public class TipBorder extends MatteBorder {
    private final BORDER_POS pos;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/border/TipBorder$BORDER_POS.class */
    public enum BORDER_POS {
        UP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public static TipBorder createInstance(int width, BORDER_POS pos, Color matteColor) {
        switch (pos) {
            case UP:
                return new TipBorder(width, 0, 0, 0, matteColor, pos);
            case RIGHT:
                return new TipBorder(0, 0, 0, width, matteColor, pos);
            case BOTTOM:
                return new TipBorder(0, 0, width, 0, matteColor, pos);
            case LEFT:
                return new TipBorder(0, width, 0, 0, matteColor, pos);
            default:
                throw new IllegalArgumentException(pos.toString());
        }
    }

    private TipBorder(int top, int left, int bottom, int right, Color matteColor, BORDER_POS pos) {
        super(top, left, bottom, right, matteColor);
        this.pos = pos;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets insets = getBorderInsets(c);
        Color oldColor = g.getColor();
        g.translate(x, y);
        if (this.tileIcon != null) {
            this.color = this.tileIcon.getIconWidth() == -1 ? Color.gray : null;
        }
        if (this.color != null) {
            g.setColor(this.color);
            int[] xT = new int[0];
            int[] yT = new int[0];
            switch (this.pos) {
                case UP:
                    int top = insets.top;
                    xT = new int[]{width - (2 * top), (width - (2 * top)) + (top / 2), (width - (2 * top)) + top};
                    yT = new int[]{0 + top, 0, 0 + top};
                    break;
                case BOTTOM:
                    int bottom = insets.bottom;
                    xT = new int[]{width - (2 * bottom), (width - (2 * bottom)) + (bottom / 2), (width - (2 * bottom)) + bottom};
                    yT = new int[]{height - bottom, height, height - bottom};
                    break;
            }
            g.fillPolygon(xT, yT, 3);
            g.translate(-x, -y);
            g.setColor(oldColor);
        }
    }
}
