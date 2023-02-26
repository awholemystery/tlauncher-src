package org.tlauncher.tlauncher.ui.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Objects;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.http.HttpStatus;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/PreloaderProgressUI.class */
public class PreloaderProgressUI extends BasicProgressBarUI {
    public final Color border = new Color((int) TarConstants.LF_OFFSET, (int) TarConstants.PREFIXLEN, (int) TarConstants.PREFIXLEN);
    public final Color bottomBorderLine = new Color(146, 154, 140);
    public final Color REST_COLOR = new Color((int) HttpStatus.SC_OK, (int) HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, 199);
    public static final int PROGRESS_HEIGHT = 24;
    public static final int PROGRESS_BAR_WIDTH = 40;
    BufferedImage bottom;
    BufferedImage top;

    public PreloaderProgressUI(BufferedImage bottom, BufferedImage top) {
        this.bottom = bottom;
        this.top = top;
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
            g2d.drawImage(this.bottom.getSubimage(0, 0, completeWidth, 24), rec.x, rec.y, completeWidth, rec.height + 1, (ImageObserver) null);
        }
    }

    protected void paintIndeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }
        Rectangle rec = null;
        try {
            Graphics2D g2d = (Graphics2D) g;
            rec = this.progressBar.getVisibleRect();
            this.boxRect = getBox(this.boxRect);
            g2d.drawImage(this.bottom, rec.x, rec.y, this.bottom.getWidth(), this.bottom.getHeight(), (ImageObserver) null);
            g2d.drawImage(this.top, this.boxRect.x, this.boxRect.y, this.boxRect.width, this.boxRect.height, (ImageObserver) null);
        } catch (NullPointerException e) {
            U.log("bottom is null " + Objects.isNull(this.bottom));
            U.log("rec is null " + Objects.isNull(rec));
        }
    }

    protected Rectangle getBox(Rectangle r) {
        r.x += 4;
        if (r.x > this.progressBar.getWidth()) {
            r.x = 0;
        }
        r.height = 24;
        r.width = 40;
        return r;
    }
}
