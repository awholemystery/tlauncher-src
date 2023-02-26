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
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.loc.LocalizableRadioButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/GameRadioButton.class */
public class GameRadioButton extends LocalizableRadioButton {
    private Color selected;
    private Color under;
    private boolean mouseUnder;

    public GameRadioButton(String string) {
        super(string);
        this.selected = new Color(60, 170, 232);
        this.under = new Color(255, (int) HttpStatus.SC_ACCEPTED, 41);
        this.mouseUnder = false;
        setPreferredSize(new Dimension(149, 52));
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.swing.GameRadioButton.1
            public void mouseEntered(MouseEvent e) {
                GameRadioButton.this.mouseUnder = true;
            }

            public void mouseExited(MouseEvent e) {
                GameRadioButton.this.mouseUnder = false;
            }
        });
        setForeground(Color.BLACK);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle rec = getVisibleRect();
        int i = 242;
        Graphics2D g2 = (Graphics2D) g;
        for (int y = rec.y; y < rec.height + rec.y; y++) {
            g2.setColor(new Color(i, i, i));
            if (i != 255) {
                i++;
            }
            g2.drawLine(rec.x, y, rec.x + rec.width, y);
        }
        if (isSelected()) {
            g2.setColor(this.selected);
            for (int y2 = (rec.y + rec.height) - 3; y2 < rec.height + rec.y; y2++) {
                g2.drawLine(rec.x, y2, rec.x + rec.width, y2);
            }
        } else if (this.mouseUnder) {
            g2.setColor(this.under);
            for (int y3 = (rec.y + rec.height) - 3; y3 < rec.height + rec.y; y3++) {
                g2.drawLine(rec.x, y3, rec.x + rec.width, y3);
            }
        }
        paintText(g2, rec);
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
