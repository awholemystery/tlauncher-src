package org.tlauncher.tlauncher.ui.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/SettingsSliderUI.class */
public class SettingsSliderUI extends BasicSliderUI {
    public static Color TRACK_LEFT = new Color(0, 174, 239);
    public static Color TRACK_RIGHT = new Color(181, 181, 181);
    public static Color THUMB_COLOR = new Color(0, 174, 239);
    public static Color SERIFS = new Color(37, 37, 37);
    private final JSlider jSlider;

    public SettingsSliderUI(JSlider b) {
        super(b);
        this.jSlider = b;
        uninstallListeners(this.jSlider);
    }

    protected Dimension getThumbSize() {
        Dimension size = new Dimension();
        size.width = 19;
        size.height = 19;
        return size;
    }

    public void paintTrack(Graphics g) {
        Rectangle trackBounds = this.trackRect;
        int cy = (trackBounds.height / 2) - 2;
        int cw = trackBounds.width;
        g.translate(trackBounds.x, trackBounds.y + cy);
        g.setColor(TRACK_LEFT);
        g.fillRect(0, 0, this.thumbRect.x, 2);
        g.setColor(TRACK_RIGHT);
        g.fillRect(this.thumbRect.x, 0, this.trackRect.width - this.thumbRect.x, 2);
        int major = this.jSlider.getMajorTickSpacing();
        int width = this.jSlider.getMaximum() - this.jSlider.getMinimum();
        int count = width / major;
        g.setColor(SERIFS);
        for (int i = 0; i < count; i++) {
            g.fillRect((i * cw) / count, 0, 2, 2);
        }
        if (count == 0) {
            g.fillRect(0, 0, 2, 2);
        } else {
            g.fillRect((count * cw) / count, 0, 2, 2);
        }
        g.translate(-trackBounds.x, -(trackBounds.y + cy));
    }

    protected void paintHorizontalLabel(Graphics g, int value, Component label) {
        label.setForeground(new Color(96, 96, 96));
        super.paintHorizontalLabel(g, value, label);
    }

    public void paintThumb(Graphics g) {
        Rectangle knobBounds = this.thumbRect;
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(THUMB_COLOR);
        g.fillOval(knobBounds.x, knobBounds.y, knobBounds.width, knobBounds.height);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    public void paintFocus(Graphics g) {
    }
}
