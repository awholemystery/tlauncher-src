package org.tlauncher.tlauncher.ui.button;

import ch.qos.logback.core.CoreConstants;
import com.google.common.base.Strings;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.BorderFactory;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/button/RoundImageButton.class */
public class RoundImageButton extends LocalizableButton {
    private BufferedImage current;

    public RoundImageButton(final BufferedImage image, final BufferedImage mouseUnderImage) {
        super(CoreConstants.EMPTY_STRING);
        this.current = image;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.button.RoundImageButton.1
            public void mouseEntered(MouseEvent e) {
                RoundImageButton.this.current = mouseUnderImage;
                RoundImageButton.this.repaint();
            }

            public void mouseExited(MouseEvent e) {
                RoundImageButton.this.current = image;
                RoundImageButton.this.repaint();
            }
        });
    }

    public RoundImageButton(String image, String mouseUnderUrl) {
        this(ImageCache.loadImage(U.makeURL(image), true), ImageCache.loadImage(U.makeURL(mouseUnderUrl), true));
    }

    public Dimension getImageSize() {
        return new Dimension(this.current.getWidth(), this.current.getHeight());
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(this.current, 0, 0, (ImageObserver) null);
        String text = getText();
        if (!Strings.isNullOrEmpty(text)) {
            SwingUtil.paintText((Graphics2D) g, this, text);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(this.current.getWidth(), this.current.getHeight());
    }
}
