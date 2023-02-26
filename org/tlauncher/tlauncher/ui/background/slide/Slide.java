package org.tlauncher.tlauncher.ui.background.slide;

import ch.qos.logback.core.joran.action.Action;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import org.tlauncher.util.Reflect;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/background/slide/Slide.class */
public class Slide {
    private final URL url;
    private Image image;

    public Slide(URL url) {
        if (url == null) {
            throw new NullPointerException();
        }
        this.url = url;
        if (isLocal()) {
            load();
        }
    }

    public boolean equals(Object o) {
        Slide slide;
        if (o == null || (slide = (Slide) Reflect.cast(o, Slide.class)) == null) {
            return false;
        }
        return this.url.equals(slide.url);
    }

    public URL getURL() {
        return this.url;
    }

    public boolean isLocal() {
        return this.url.getProtocol().equals(Action.FILE_ATTRIBUTE);
    }

    public Image getImage() {
        if (this.image == null) {
            load();
        }
        return this.image;
    }

    private void load() {
        log("Loading from:", this.url);
        try {
            BufferedImage tempImage = ImageIO.read(this.url);
            if (tempImage == null) {
                log("Image seems to be corrupted.");
                return;
            }
            log("Loaded successfully!");
            this.image = tempImage;
        } catch (Throwable e) {
            log("Cannot load slide!", e);
        }
    }

    protected void log(Object... w) {
        U.log("[" + getClass().getSimpleName() + "]", w);
    }
}
