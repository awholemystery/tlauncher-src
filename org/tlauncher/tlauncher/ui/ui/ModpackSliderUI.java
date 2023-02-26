package org.tlauncher.tlauncher.ui.ui;

import ch.qos.logback.core.CoreConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JSlider;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/ModpackSliderUI.class */
public class ModpackSliderUI extends SettingsSliderUI {
    public ModpackSliderUI(JSlider b) {
        super(b);
    }

    @Override // org.tlauncher.tlauncher.ui.ui.SettingsSliderUI
    public void paintThumb(Graphics g) {
        super.paintThumb(g);
        g.setColor(Color.BLACK);
        g.setFont(this.slider.getFont());
        Rectangle knobBounds = this.thumbRect;
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (9999 < this.slider.getValue()) {
            graphics2d.drawString(CoreConstants.EMPTY_STRING + this.slider.getValue(), knobBounds.x - 20, knobBounds.y - 8);
        } else if (9999 > this.slider.getValue() && 1000 <= this.slider.getValue()) {
            graphics2d.drawString(CoreConstants.EMPTY_STRING + this.slider.getValue(), knobBounds.x - 10, knobBounds.y - 8);
        } else {
            graphics2d.drawString(CoreConstants.EMPTY_STRING + this.slider.getValue(), knobBounds.x, knobBounds.y - 8);
        }
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
}
