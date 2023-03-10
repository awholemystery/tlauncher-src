package org.tlauncher.tlauncher.ui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/Del.class */
public class Del extends ExtendedPanel {
    private static final int TOP = -1;
    public static final int CENTER = 0;
    private static final int BOTTOM = 1;
    private static final long serialVersionUID = -2252007533187803762L;
    private int size;
    private int aligment;
    private Color color;

    public Del(int size, int aligment, Color color) {
        this.size = size;
        this.aligment = aligment;
        this.color = color;
    }

    public Del(int size, int aligment, int width, int height, Color color) {
        this.size = size;
        this.aligment = aligment;
        this.color = color;
        setPreferredSize(new Dimension(width, height));
    }

    public void paint(Graphics g) {
        g.setColor(this.color);
        switch (this.aligment) {
            case -1:
                g.fillRect(0, 0, getWidth(), this.size);
                return;
            case 0:
                g.fillRect(0, (getHeight() / 2) - this.size, getWidth(), this.size);
                return;
            case 1:
                g.fillRect(0, getHeight() - this.size, getWidth(), this.size);
                return;
            default:
                return;
        }
    }
}
