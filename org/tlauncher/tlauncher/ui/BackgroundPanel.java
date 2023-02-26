package org.tlauncher.tlauncher.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;
import org.tlauncher.tlauncher.ui.images.ImageCache;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/BackgroundPanel.class */
public class BackgroundPanel extends JPanel {
    private final BufferedImage image;

    public BackgroundPanel(String name) {
        this.image = ImageCache.get(name);
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(this.image, 0, 0, (ImageObserver) null);
    }
}
