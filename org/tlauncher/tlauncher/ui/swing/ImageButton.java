package org.tlauncher.tlauncher.ui.swing;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/ImageButton.class */
public class ImageButton extends ExtendedButton {
    private static final long serialVersionUID = 1;
    protected Image image;
    protected ImageRotation rotation;
    private int margin;
    private boolean pressed;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/ImageButton$ImageRotation.class */
    public enum ImageRotation {
        LEFT,
        CENTER,
        RIGHT
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ImageButton() {
        this.rotation = ImageRotation.CENTER;
        this.margin = 4;
        initListeners();
    }

    public ImageButton(String label, Image image, ImageRotation rotation, int margin) {
        super(label);
        this.rotation = ImageRotation.CENTER;
        this.margin = 4;
        this.image = image;
        this.rotation = rotation;
        this.margin = margin;
        initImage();
        initListeners();
    }

    public ImageButton(String label, Image image, ImageRotation rotation) {
        this(label, image, rotation, 4);
    }

    public ImageButton(String label, Image image) {
        this(label, image, ImageRotation.CENTER);
    }

    public ImageButton(Image image) {
        this((String) null, image);
    }

    public ImageButton(String imagepath) {
        this((String) null, loadImage(imagepath));
    }

    public ImageButton(String label, String imagepath, ImageRotation rotation, int margin) {
        this(label, loadImage(imagepath), rotation, margin);
    }

    public ImageButton(String label, String imagepath, ImageRotation rotation) {
        this(label, loadImage(imagepath), rotation);
    }

    public ImageButton(String label, String imagepath) {
        this(label, loadImage(imagepath));
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
        initImage();
        repaint();
    }

    public ImageRotation getRotation() {
        return this.rotation;
    }

    public int getImageMargin() {
        return this.margin;
    }

    public void update(Graphics g) {
        super.update(g);
        paint(g);
    }

    public void paintComponent(Graphics g0) {
        int twidth;
        int ix;
        if (this.image == null) {
            return;
        }
        Graphics2D g = (Graphics2D) g0;
        String text = getText();
        boolean drawtext = text != null && text.length() > 0;
        FontMetrics fm = g.getFontMetrics();
        float opacity = isEnabled() ? 1.0f : 0.5f;
        int width = getWidth();
        int height = getHeight();
        int rmargin = this.margin;
        int offset = this.pressed ? 1 : 0;
        int iwidth = this.image.getWidth((ImageObserver) null);
        int iheight = this.image.getHeight((ImageObserver) null);
        int iy = (height / 2) - (iheight / 2);
        if (drawtext) {
            twidth = fm.stringWidth(text);
        } else {
            rmargin = 0;
            twidth = 0;
        }
        switch (this.rotation) {
            case LEFT:
                ix = (((width / 2) - (twidth / 2)) - iwidth) - rmargin;
                break;
            case CENTER:
                ix = (width / 2) - (iwidth / 2);
                break;
            case RIGHT:
                ix = (width / 2) + (twidth / 2) + rmargin;
                break;
            default:
                throw new IllegalStateException("Unknown rotation!");
        }
        Composite c = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(3, opacity));
        g.drawImage(this.image, ix + offset, iy + offset, (ImageObserver) null);
        g.setComposite(c);
        this.pressed = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Image loadImage(String path) {
        return ImageCache.getImage(path);
    }

    protected void initImage() {
        if (this.image == null) {
            return;
        }
        setPreferredSize(new Dimension(this.image.getWidth((ImageObserver) null) + 10, this.image.getHeight((ImageObserver) null) + 10));
    }

    private void initListeners() {
        initImage();
        addMouseListener(new MouseListener() { // from class: org.tlauncher.tlauncher.ui.swing.ImageButton.1
            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                ImageButton.this.pressed = true;
            }

            public void mouseReleased(MouseEvent e) {
            }
        });
        addKeyListener(new KeyListener() { // from class: org.tlauncher.tlauncher.ui.swing.ImageButton.2
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 32) {
                    ImageButton.this.pressed = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                ImageButton.this.pressed = false;
            }

            public void keyTyped(KeyEvent e) {
            }
        });
    }
}
