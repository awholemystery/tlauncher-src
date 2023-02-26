package org.tlauncher.tlauncher.ui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.LocalizableRadioButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/GameRadioTextButton.class */
public class GameRadioTextButton extends LocalizableRadioButton {
    protected Color defaultColor;
    private Color under;
    private boolean mouseUnder;

    public GameRadioTextButton(String string) {
        super(string);
        this.defaultColor = new Color(60, 170, 232);
        this.under = new Color(255, (int) HttpStatus.SC_ACCEPTED, 41);
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.swing.GameRadioTextButton.1
            public void mouseEntered(MouseEvent e) {
                GameRadioTextButton.this.mouseUnder = true;
                GameRadioTextButton.this.repaint();
            }

            public void mouseExited(MouseEvent e) {
                GameRadioTextButton.this.mouseUnder = false;
                GameRadioTextButton.this.repaint();
            }
        });
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(129, 55));
    }

    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Rectangle rec = getVisibleRect();
        Graphics2D g2 = (Graphics2D) gr;
        paintBackground(g2, rec);
        paintText(g2, rec);
    }

    protected void paintBackground(Graphics2D g2, Rectangle rec) {
        int i = rec.y;
        g2.drawImage(ImageCache.getBufferedImage("modpack-radio-button-background.png"), 0, 0, (ImageObserver) null);
        if (isSelected() || this.mouseUnder) {
            g2.setColor(this.under);
            for (int y = (rec.y + rec.height) - 9; y < rec.height + rec.y; y++) {
                g2.drawLine(rec.x, y, rec.x + rec.width, y);
            }
            return;
        }
        g2.setColor(this.defaultColor);
        for (int y2 = (rec.y + rec.height) - 9; y2 < rec.height + rec.y; y2++) {
            g2.drawLine(rec.x, y2, rec.x + rec.width, y2);
        }
    }

    protected void paintText(Graphics2D g, Rectangle textRect) {
        g.setColor(getForeground());
        String text = getText();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g);
        g.setFont(getFont());
        int x = (getWidth() - ((int) r.getWidth())) / 2;
        int y = ((getHeight() - ((int) r.getHeight())) / 2) + fm.getAscent();
        g.drawString(text, x, y);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
}
