package org.tlauncher.tlauncher.ui.background;

import java.awt.Color;
import java.awt.Graphics;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/background/Background.class */
public abstract class Background extends ExtendedLayeredPane {
    private static final long serialVersionUID = -1353975966057230209L;
    protected Color coverColor;

    public abstract void paintBackground(Graphics graphics);

    public Background(BackgroundHolder holder, Color coverColor) {
        super(holder);
        this.coverColor = coverColor;
    }

    public Color getCoverColor() {
        return this.coverColor;
    }

    public final void paint(Graphics g) {
        paintBackground(g);
        super.paint(g);
    }
}
