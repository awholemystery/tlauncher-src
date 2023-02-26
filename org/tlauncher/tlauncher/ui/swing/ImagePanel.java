package org.tlauncher.tlauncher.ui.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/ImagePanel.class */
public class ImagePanel extends ExtendedPanel {
    private static final long serialVersionUID = 1;
    public static final float DEFAULT_ACTIVE_OPACITY = 1.0f;
    public static final float DEFAULT_NON_ACTIVE_OPACITY = 0.75f;
    protected final Object animationLock;
    private Image originalImage;
    private Image image;
    private float activeOpacity;
    private float nonActiveOpacity;
    private boolean antiAlias;
    private int timeFrame;
    private float opacity;
    private boolean hover;
    private boolean shown;
    private boolean animating;

    public ImagePanel(String image, float activeOpacity, float nonActiveOpacity, boolean shown) {
        this((Image) ImageCache.getImage(image), activeOpacity, nonActiveOpacity, shown);
    }

    public ImagePanel(String image) {
        this(image, 1.0f, 0.75f, true);
    }

    public ImagePanel(Image image, float activeOpacity, float nonActiveOpacity, boolean shown) {
        this.animationLock = new Object();
        setImage(image);
        setActiveOpacity(activeOpacity);
        setNonActiveOpacity(nonActiveOpacity);
        this.shown = shown;
        this.opacity = shown ? nonActiveOpacity : 0.0f;
        this.timeFrame = 10;
        setBackground(new Color(0, 0, 0, 0));
        addMouseListenerOriginally(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.swing.ImagePanel.1
            public void mouseClicked(MouseEvent e) {
                ImagePanel.this.onClick();
            }

            public void mouseEntered(MouseEvent e) {
                ImagePanel.this.onMouseEntered();
            }

            public void mouseExited(MouseEvent e) {
                ImagePanel.this.onMouseExited();
            }
        });
    }

    public void setImage(Image image, boolean resetSize) {
        synchronized (this.animationLock) {
            this.originalImage = image;
            this.image = image;
            if (resetSize && image != null) {
                setSize(image.getWidth((ImageObserver) null), image.getHeight((ImageObserver) null));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setImage(Image image) {
        setImage(image, true);
    }

    protected void setActiveOpacity(float opacity) {
        if (opacity > 1.0f || opacity < 0.0f) {
            throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
        }
        this.activeOpacity = opacity;
    }

    protected void setNonActiveOpacity(float opacity) {
        if (opacity > 1.0f || opacity < 0.0f) {
            throw new IllegalArgumentException("Invalid opacity! Condition: 0.0F <= opacity (got: " + opacity + ") <= 1.0F");
        }
        this.nonActiveOpacity = opacity;
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel
    public void paintComponent(Graphics g0) {
        if (this.image == null) {
            return;
        }
        Graphics2D g = (Graphics2D) g0;
        Composite oldComp = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(3, this.opacity));
        g.drawImage(this.image, 0, 0, getWidth(), getHeight(), (ImageObserver) null);
        g.setComposite(oldComp);
    }

    public void show() {
        if (this.shown) {
            return;
        }
        this.shown = true;
        synchronized (this.animationLock) {
            this.animating = true;
            setVisible(true);
            this.opacity = 0.0f;
            float selectedOpacity = this.hover ? this.activeOpacity : this.nonActiveOpacity;
            while (this.opacity < selectedOpacity) {
                this.opacity += 0.01f;
                if (this.opacity > selectedOpacity) {
                    this.opacity = selectedOpacity;
                }
                repaint();
                U.sleepFor(this.timeFrame);
            }
            this.animating = false;
        }
    }

    public void hide() {
        if (!this.shown) {
            return;
        }
        this.shown = false;
        synchronized (this.animationLock) {
            this.animating = true;
            while (this.opacity > 0.0f) {
                this.opacity -= 0.01f;
                if (this.opacity < 0.0f) {
                    this.opacity = 0.0f;
                }
                repaint();
                U.sleepFor(this.timeFrame);
            }
            setVisible(false);
            this.animating = false;
        }
    }

    public void setPreferredSize() {
        if (this.image == null) {
            return;
        }
        setPreferredSize(new Dimension(this.image.getWidth((ImageObserver) null), this.image.getHeight((ImageObserver) null)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean onClick() {
        return this.shown;
    }

    protected void onMouseEntered() {
        this.hover = true;
        if (this.animating || !this.shown) {
            return;
        }
        this.opacity = this.activeOpacity;
        repaint();
    }

    protected void onMouseExited() {
        this.hover = false;
        if (this.animating || !this.shown) {
            return;
        }
        this.opacity = this.nonActiveOpacity;
        repaint();
    }
}
