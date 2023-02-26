package org.tlauncher.tlauncher.ui.loc;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.ui.images.ImageCache;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/UpdaterFullButton.class */
public class UpdaterFullButton extends UpdaterButton {
    private static final long serialVersionUID = 992760417140310208L;
    protected Color unEnableColor;
    protected Color backgroundColor;
    private Image image;
    private Image imageUp;

    public UpdaterFullButton(Color color, String value, String image) {
        super(color, value);
        this.backgroundColor = color;
        this.image = ImageCache.getImage(image);
        setForeground(Color.WHITE);
        setHorizontalTextPosition(4);
    }

    public UpdaterFullButton(Color color, final Color mouseUnder, String value, String image) {
        super(color, value);
        setHorizontalTextPosition(4);
        this.backgroundColor = color;
        this.image = ImageCache.getImage(image);
        setForeground(Color.WHITE);
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.loc.UpdaterFullButton.1
            public void mouseEntered(MouseEvent e) {
                UpdaterFullButton.this.setBackground(mouseUnder);
            }

            public void mouseExited(MouseEvent e) {
                UpdaterFullButton.this.setBackground(UpdaterFullButton.this.backgroundColor);
            }
        });
    }

    public UpdaterFullButton(Color color, final Color mouseUnder, String value, String image, String imageUp) {
        super(color, value);
        setHorizontalTextPosition(4);
        this.backgroundColor = color;
        this.image = ImageCache.getImage(image);
        this.imageUp = ImageCache.getImage(imageUp);
        setForeground(Color.WHITE);
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.loc.UpdaterFullButton.2
            public void mouseEntered(MouseEvent e) {
                UpdaterFullButton.this.setBackground(mouseUnder);
            }

            public void mouseExited(MouseEvent e) {
                UpdaterFullButton.this.setBackground(UpdaterFullButton.this.backgroundColor);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.loc.UpdaterButton
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle rec = getVisibleRect();
        String text = getText();
        paintBackground(rec, g);
        paintText(g, this, rec, text);
        paintPicture(g, this, rec, getModel().isRollover());
    }

    protected void paintPicture(Graphics g, JComponent c, Rectangle rect, boolean rollover) {
        if (this.image != null && getHorizontalTextPosition() == 4) {
            Graphics2D g2d = (Graphics2D) g;
            int x = getInsets().left;
            int y = (getHeight() - this.image.getHeight((ImageObserver) null)) / 2;
            if (rollover && this.imageUp != null) {
                g2d.drawImage(this.imageUp, x, y, (ImageObserver) null);
            } else {
                g2d.drawImage(this.image, x, y, (ImageObserver) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.loc.UpdaterButton
    public void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        if (text != null && getHorizontalTextPosition() == 4) {
            g.setColor(getForeground());
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(getFont());
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(text, g2d);
            int x = getInsets().left + this.image.getWidth((ImageObserver) null) + getIconTextGap();
            int y = (((getHeight() - ((int) r.getHeight())) / 2) + fm.getAscent()) - 1;
            g2d.drawString(text, x, y);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }

    protected void paintBackground(Rectangle rec, Graphics g) {
        ButtonModel buttonModel = getModel();
        g.setColor(getBackground());
        if (buttonModel.isPressed()) {
            g.setColor(getBackground());
            g.fillRect(rec.x, rec.y, rec.width, rec.height);
        } else if (!buttonModel.isEnabled()) {
            if (this.unEnableColor == null) {
                g.setColor(getForeground().darker());
            } else {
                g.setColor(this.unEnableColor);
            }
        } else if (buttonModel.isRollover()) {
            g.setColor(this.unEnableColor);
        }
        g.fillRect(rec.x, rec.y, rec.width, rec.height);
    }
}
