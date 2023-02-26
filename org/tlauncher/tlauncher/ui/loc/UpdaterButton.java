package org.tlauncher.tlauncher.ui.loc;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.ButtonModel;
import javax.swing.JComponent;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/UpdaterButton.class */
public class UpdaterButton extends LocalizableButton {
    protected Color unEnableColor;
    private Color backgroundColor;

    public UpdaterButton(Color color, String value) {
        this.backgroundColor = color;
        setText(value);
        setOpaque(true);
        setBackground(color);
    }

    public UpdaterButton(Color color, Color UnEnableColor, String value) {
        this(color, value);
        this.unEnableColor = UnEnableColor;
    }

    public UpdaterButton(Color color, Color unEnableColor, Color foreground, String value) {
        this(color, unEnableColor, value);
        setForeground(foreground);
    }

    public UpdaterButton(Color color) {
        this.backgroundColor = color;
        setText(null);
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(this.backgroundColor);
    }

    public UpdaterButton() {
        setText(null);
        setContentAreaFilled(false);
        setOpaque(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paintComponent(Graphics g) {
        Rectangle rec = getVisibleRect();
        g.setColor(getBackground());
        g.fillRect(rec.x, rec.y, rec.width, rec.height);
        String text = getText();
        ButtonModel buttonModel = getModel();
        Color colorText = getForeground();
        if (buttonModel.isRollover() && this.unEnableColor != null && this.model.isEnabled()) {
            g.setColor(this.unEnableColor);
        }
        g.fillRect(rec.x, rec.y, rec.width, rec.height);
        g.setColor(colorText);
        if (text != null) {
            paintText(g, this, rec, text);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(getFont());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g2d);
        int x = (getWidth() - ((int) r.getWidth())) / 2;
        int y = ((getHeight() - ((int) r.getHeight())) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }
}
