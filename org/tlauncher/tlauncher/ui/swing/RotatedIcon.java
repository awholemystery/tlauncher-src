package org.tlauncher.tlauncher.ui.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/RotatedIcon.class */
public class RotatedIcon implements Icon {
    private Icon icon;
    private Rotate rotate;
    private double angle;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/RotatedIcon$Rotate.class */
    public enum Rotate {
        DOWN,
        UP,
        UPSIDE_DOWN,
        ABOUT_CENTER
    }

    public RotatedIcon(Icon icon) {
        this(icon, Rotate.UP);
    }

    private RotatedIcon(Icon icon, Rotate rotate) {
        this.icon = icon;
        this.rotate = rotate;
    }

    public RotatedIcon(Icon icon, double angle) {
        this(icon, Rotate.ABOUT_CENTER);
        this.angle = angle;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public Rotate getRotate() {
        return this.rotate;
    }

    public double getAngle() {
        return this.angle;
    }

    public int getIconWidth() {
        if (this.rotate == Rotate.ABOUT_CENTER) {
            double radians = Math.toRadians(this.angle);
            double sin = Math.abs(Math.sin(radians));
            double cos = Math.abs(Math.cos(radians));
            int width = (int) Math.floor((this.icon.getIconWidth() * cos) + (this.icon.getIconHeight() * sin));
            return width;
        } else if (this.rotate == Rotate.UPSIDE_DOWN) {
            return this.icon.getIconWidth();
        } else {
            return this.icon.getIconHeight();
        }
    }

    public int getIconHeight() {
        if (this.rotate == Rotate.ABOUT_CENTER) {
            double radians = Math.toRadians(this.angle);
            double sin = Math.abs(Math.sin(radians));
            double cos = Math.abs(Math.cos(radians));
            int height = (int) Math.floor((this.icon.getIconHeight() * cos) + (this.icon.getIconWidth() * sin));
            return height;
        } else if (this.rotate == Rotate.UPSIDE_DOWN) {
            return this.icon.getIconHeight();
        } else {
            return this.icon.getIconWidth();
        }
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = g.create();
        int cWidth = this.icon.getIconWidth() / 2;
        int cHeight = this.icon.getIconHeight() / 2;
        int xAdjustment = this.icon.getIconWidth() % 2 == 0 ? 0 : -1;
        int yAdjustment = this.icon.getIconHeight() % 2 == 0 ? 0 : -1;
        if (this.rotate == Rotate.DOWN) {
            g2.translate(x + cHeight, y + cWidth);
            g2.rotate(Math.toRadians(90.0d));
            this.icon.paintIcon(c, g2, -cWidth, yAdjustment - cHeight);
        } else if (this.rotate == Rotate.UP) {
            g2.translate(x + cHeight, y + cWidth);
            g2.rotate(Math.toRadians(-90.0d));
            this.icon.paintIcon(c, g2, xAdjustment - cWidth, -cHeight);
        } else if (this.rotate == Rotate.UPSIDE_DOWN) {
            g2.translate(x + cWidth, y + cHeight);
            g2.rotate(Math.toRadians(180.0d));
            this.icon.paintIcon(c, g2, xAdjustment - cWidth, yAdjustment - cHeight);
        } else if (this.rotate == Rotate.ABOUT_CENTER) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AffineTransform original = g2.getTransform();
            AffineTransform at = new AffineTransform();
            at.concatenate(original);
            at.translate((getIconWidth() - this.icon.getIconWidth()) / 2, (getIconHeight() - this.icon.getIconHeight()) / 2);
            at.rotate(Math.toRadians(this.angle), x + cWidth, y + cHeight);
            g2.setTransform(at);
            this.icon.paintIcon(c, g2, x, y);
            g2.setTransform(original);
        }
    }
}
