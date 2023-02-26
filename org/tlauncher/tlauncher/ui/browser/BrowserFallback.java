package org.tlauncher.tlauncher.ui.browser;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.images.ImageCache;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/browser/BrowserFallback.class */
public class BrowserFallback extends JPanel implements Blockable {
    private final BrowserHolder holder;
    private Image image;
    private int imageWidth;
    private int imageHeight;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BrowserFallback(BrowserHolder holder) {
        this.holder = holder;
    }

    private void updateImage() {
        this.image = ImageCache.getImage("plains.png");
        this.imageWidth = this.image.getWidth((ImageObserver) null);
        this.imageHeight = this.image.getHeight((ImageObserver) null);
    }

    public void paintComponent(Graphics g) {
        double width;
        double height;
        if (this.image == null) {
            return;
        }
        double windowWidth = getWidth();
        double windowHeight = getHeight();
        double ratio = Math.min(this.imageWidth / windowWidth, this.imageHeight / windowHeight);
        if (ratio < 1.0d) {
            width = this.imageWidth;
            height = this.imageHeight;
        } else {
            width = this.imageWidth / ratio;
            height = this.imageHeight / ratio;
        }
        double x = (windowWidth - width) / 2.0d;
        double y = (windowHeight - height) / 2.0d;
        g.drawImage(this.image, (int) x, (int) y, (int) width, (int) height, (ImageObserver) null);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        this.holder.removeAll();
        this.holder.setCenter(this.holder.browser);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        updateImage();
        this.holder.removeAll();
        this.holder.setCenter(this);
        if (this.holder.browser != null) {
            this.holder.browser.cleanupResources();
        }
    }
}
