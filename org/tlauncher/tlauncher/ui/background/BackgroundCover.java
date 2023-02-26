package org.tlauncher.tlauncher.ui.background;

import java.awt.Color;
import java.awt.Graphics;
import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/background/BackgroundCover.class */
public class BackgroundCover extends ExtendedPanel implements ResizeableComponent {
    private static final long serialVersionUID = -1801217638400760969L;
    private static final double opacityStep = 0.01d;
    private static final int timeFrame = 5;
    private final BackgroundHolder parent;
    private final Object animationLock;
    private double opacity;
    private Color opacityColor;
    private Color color;

    BackgroundCover(BackgroundHolder parent, Color opacityColor, double opacity) {
        if (parent == null) {
            throw new NullPointerException();
        }
        this.parent = parent;
        setColor(opacityColor, false);
        setBgOpacity(opacity, false);
        this.animationLock = new Object();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackgroundCover(BackgroundHolder parent) {
        this(parent, Color.white, 0.0d);
    }

    public void makeCover(boolean animate) {
        synchronized (this.animationLock) {
            if (animate) {
                while (this.opacity < 1.0d) {
                    setBgOpacity(this.opacity + opacityStep, true);
                    U.sleepFor(5L);
                }
            }
            setBgOpacity(1.0d, true);
        }
    }

    public void makeCover() {
        makeCover(true);
    }

    public void removeCover(boolean animate) {
        synchronized (this.animationLock) {
            if (animate) {
                while (this.opacity > 0.0d) {
                    setBgOpacity(this.opacity - opacityStep, true);
                    U.sleepFor(5L);
                }
            }
            setBgOpacity(0.0d, true);
        }
    }

    public void removeCover() {
        removeCover(true);
    }

    public boolean isCovered() {
        return this.opacity == 1.0d;
    }

    public void toggleCover(boolean animate) {
        if (isCovered()) {
            removeCover(animate);
        } else {
            makeCover(animate);
        }
    }

    public void paint(Graphics g) {
        g.setColor(this.opacityColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public double getBgOpacity() {
        return this.opacity;
    }

    public void setBgOpacity(double opacity, boolean repaint) {
        if (opacity < 0.0d) {
            opacity = 0.0d;
        } else if (opacity > 1.0d) {
            opacity = 1.0d;
        }
        this.opacity = opacity;
        this.opacityColor = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), (int) (255.0d * opacity));
        if (repaint) {
            repaint();
        }
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color, boolean repaint) {
        if (color == null) {
            throw new NullPointerException();
        }
        this.color = color;
        if (repaint) {
            repaint();
        }
    }

    @Override // org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        setSize(this.parent.getWidth(), this.parent.getHeight());
    }
}
