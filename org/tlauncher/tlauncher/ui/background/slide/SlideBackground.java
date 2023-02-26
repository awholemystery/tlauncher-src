package org.tlauncher.tlauncher.ui.background.slide;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.image.ImageObserver;
import org.tlauncher.tlauncher.ui.background.Background;
import org.tlauncher.tlauncher.ui.background.BackgroundHolder;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/background/slide/SlideBackground.class */
public class SlideBackground extends Background {
    private static final long serialVersionUID = -4479685866688951989L;
    private final SlideBackgroundThread thread;
    final BackgroundHolder holder;
    final ExtendedComponentAdapter listener;
    private Image oImage;
    private int oImageWidth;
    private int oImageHeight;
    private Image vImage;
    private int vImageWidth;
    private int vImageHeight;

    public SlideBackground(BackgroundHolder holder) {
        super(holder, Color.black);
        this.holder = holder;
        this.thread = new SlideBackgroundThread(this);
        this.thread.setSlide(this.thread.defaultSlide, false);
        this.thread.refreshSlide(false);
        this.listener = new ExtendedComponentAdapter(this, 1000) { // from class: org.tlauncher.tlauncher.ui.background.slide.SlideBackground.1
            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter, org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener
            public void onComponentResized(ComponentEvent e) {
                SlideBackground.this.updateImage();
                SlideBackground.this.repaint();
            }
        };
        addComponentListener(this.listener);
    }

    public SlideBackgroundThread getThread() {
        return this.thread;
    }

    public Image getImage() {
        return this.oImage;
    }

    public void setImage(Image image) {
        if (image == null) {
            throw new NullPointerException();
        }
        this.oImage = image;
        this.oImageWidth = image.getWidth((ImageObserver) null);
        this.oImageHeight = image.getHeight((ImageObserver) null);
        updateImage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateImage() {
        double width;
        double height;
        double windowWidth = getWidth();
        double windowHeight = getHeight();
        double ratio = Math.min(this.oImageWidth / windowWidth, this.oImageHeight / windowHeight);
        if (ratio < 1.0d) {
            width = this.oImageWidth;
            height = this.oImageHeight;
        } else {
            width = this.oImageWidth / ratio;
            height = this.oImageHeight / ratio;
        }
        this.vImageWidth = (int) width;
        this.vImageHeight = (int) height;
        if (this.vImageWidth == 0 || this.vImageHeight == 0) {
            this.vImage = null;
        } else if (this.oImageWidth == this.vImageWidth && this.oImageHeight == this.vImageHeight) {
            this.vImage = this.oImage;
        } else {
            this.vImage = this.oImage.getScaledInstance(this.vImageWidth, this.vImageHeight, 4);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.background.Background
    public void paintBackground(Graphics g) {
        if (this.vImage == null) {
            updateImage();
        }
        if (this.vImage == null) {
            return;
        }
        double windowWidth = getWidth();
        double windowHeight = getHeight();
        double ratio = Math.min(this.vImageWidth / windowWidth, this.vImageHeight / windowHeight);
        double width = this.vImageWidth / ratio;
        double height = this.vImageHeight / ratio;
        double x = (windowWidth - width) / 2.0d;
        double y = (windowHeight - height) / 2.0d;
        g.drawImage(this.vImage, (int) x, (int) y, (int) width, (int) height, (ImageObserver) null);
    }
}
