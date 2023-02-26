package org.tlauncher.tlauncher.ui.loc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.ui.swing.ImageButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/ImageUdaterButton.class */
public class ImageUdaterButton extends ImageButton {
    public final Color backroundColor;
    public final String defaultImage;
    private Color modelPressedColor;

    public ImageUdaterButton(Color color, String image) {
        super(image);
        this.defaultImage = image;
        this.backroundColor = color;
        setBackground(color);
    }

    public ImageUdaterButton(Color color) {
        this.backroundColor = color;
        this.defaultImage = null;
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(color);
    }

    public ImageUdaterButton(String image) {
        super(image);
        this.defaultImage = image;
        this.backroundColor = Color.BLACK;
    }

    public ImageUdaterButton(final Color color, final Color color1, final String s, final String s1) {
        this(color, s);
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.loc.ImageUdaterButton.1
            public void mouseEntered(MouseEvent e) {
                if (ImageUdaterButton.this.getModel().isEnabled()) {
                    ImageUdaterButton.super.setImage(ImageUdaterButton.loadImage(s1));
                    ImageUdaterButton.super.setBackground(color1);
                }
            }

            public void mouseExited(MouseEvent e) {
                ImageUdaterButton.super.setImage(ImageUdaterButton.loadImage(s));
                ImageUdaterButton.super.setBackground(color);
            }
        });
    }

    public ImageUdaterButton(final Color color, final Color color1, String s) {
        this(color, s);
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.loc.ImageUdaterButton.2
            public void mouseEntered(MouseEvent e) {
                if (ImageUdaterButton.this.getModel().isEnabled()) {
                    ImageUdaterButton.super.setBackground(color1);
                }
            }

            public void mouseExited(MouseEvent e) {
                ImageUdaterButton.super.setBackground(color);
            }
        });
    }

    @Override // org.tlauncher.tlauncher.ui.swing.ImageButton
    public void paintComponent(Graphics g) {
        Rectangle rec = getVisibleRect();
        g.setColor(getBackground());
        ButtonModel buttonModel = getModel();
        if (buttonModel.isPressed() && buttonModel.isEnabled() && this.modelPressedColor != null) {
            g.setColor(this.modelPressedColor);
        }
        g.fillRect(rec.x, rec.y, rec.width, rec.height);
        if (this.image != null) {
            paintPicture(g, this, rec);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paintPicture(Graphics g, JComponent c, Rectangle rect) {
        Graphics2D g2d = (Graphics2D) g;
        int x = (getWidth() - this.image.getWidth((ImageObserver) null)) / 2;
        int y = (getHeight() - this.image.getHeight((ImageObserver) null)) / 2;
        g2d.drawImage(this.image, x, y, (ImageObserver) null);
    }

    public String getDefaultImage() {
        return this.defaultImage;
    }

    public Color getBackroundColor() {
        return this.backroundColor;
    }

    public void setModelPressedColor(Color modelPressedColor) {
        this.modelPressedColor = modelPressedColor;
    }
}
