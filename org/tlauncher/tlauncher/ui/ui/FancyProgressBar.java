package org.tlauncher.tlauncher.ui.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.http.HttpStatus;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/FancyProgressBar.class */
public class FancyProgressBar extends BasicProgressBarUI {
    public final Color border = new Color((int) TarConstants.LF_OFFSET, (int) TarConstants.PREFIXLEN, (int) TarConstants.PREFIXLEN);
    public final Color bottomBorderLine = new Color(146, 154, 140);
    public final Color REST_COLOR = new Color((int) HttpStatus.SC_OK, (int) HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, 199);
    public static final int PROGRESS_HEIGHT = 24;
    BufferedImage image;

    public FancyProgressBar(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    protected void paintDeterminate(Graphics g, JComponent c) {
        Graphics2D g2d = g.create();
        Rectangle rec = this.progressBar.getVisibleRect();
        double complete = this.progressBar.getPercentComplete();
        int width = this.progressBar.getWidth();
        int height = this.progressBar.getHeight();
        int completeWidth = (int) (complete * width);
        g2d.setColor(this.REST_COLOR);
        g2d.fillRect(rec.x + completeWidth, rec.y, width, height);
        g2d.setColor(this.border);
        g2d.drawLine(completeWidth, 0, rec.width, 0);
        g2d.drawLine(completeWidth, rec.height - 1, rec.width, rec.height - 1);
        g2d.drawLine((rec.x + rec.width) - 1, rec.y, (rec.x + rec.width) - 1, rec.y + rec.height);
        g2d.setColor(this.bottomBorderLine);
        g2d.drawLine(completeWidth, rec.height - 1, rec.width, rec.height - 1);
        if (completeWidth > 0) {
            g2d.drawImage(this.image.getSubimage(0, 0, completeWidth, 24), rec.x, rec.y, completeWidth, rec.height + 1, (ImageObserver) null);
        }
    }
}
