package org.tlauncher.tlauncher.ui.background.slide;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.explorer.filters.ImageFileFilter;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.async.LoopedThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/background/slide/SlideBackgroundThread.class */
public class SlideBackgroundThread extends LoopedThread {
    private static final Pattern extensionPattern = ImageFileFilter.extensionPattern;
    private static final String defaultImageName = "plains.jpg";
    private final SlideBackground background;
    final Slide defaultSlide;
    private Slide currentSlide;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SlideBackgroundThread(SlideBackground background) {
        super("SlideBackgroundThread");
        this.background = background;
        this.defaultSlide = new Slide(ImageCache.getRes(defaultImageName));
        startAndWait();
    }

    public SlideBackground getBackground() {
        return this.background;
    }

    public Slide getSlide() {
        return this.currentSlide;
    }

    public synchronized void refreshSlide(boolean animate) {
        String path = TLauncher.getInstance().getConfiguration().get("gui.background");
        URL url = getImageURL(path);
        Slide slide = url == null ? this.defaultSlide : new Slide(url);
        setSlide(slide, animate);
    }

    public void asyncRefreshSlide() {
        iterate();
    }

    public synchronized void setSlide(Slide slide, boolean animate) {
        if (slide == null) {
            throw new NullPointerException();
        }
        if (slide.equals(this.currentSlide)) {
            return;
        }
        Image image = slide.getImage();
        if (image == null) {
            slide = this.defaultSlide;
            image = slide.getImage();
        }
        this.currentSlide = slide;
        if (image == null) {
            log("Default image is NULL. Check accessibility to the JAR file of TLauncher.");
            return;
        }
        this.background.holder.cover.makeCover(animate);
        this.background.setImage(image);
        U.sleepFor(500L);
        this.background.holder.cover.removeCover(animate);
    }

    @Override // org.tlauncher.util.async.LoopedThread
    protected void iterateOnce() {
        refreshSlide(true);
    }

    private URL getImageURL(String path) {
        log("Trying to resolve path:", path);
        if (path == null) {
            log("Na NULL i suda NULL.");
            return null;
        }
        URL asURL = U.makeURL(path);
        if (asURL != null) {
            log("Path resolved as an URL:", asURL);
            return asURL;
        }
        File asFile = new File(path);
        if (asFile.isFile()) {
            String absPath = asFile.getAbsolutePath();
            log("Path resolved as a file:", absPath);
            String ext = FileUtil.getExtension(asFile);
            if (ext == null || !extensionPattern.matcher(ext).matches()) {
                log("This file doesn't seem to be an image. It should have JPG or PNG format.");
                return null;
            }
            try {
                return asFile.toURI().toURL();
            } catch (IOException e) {
                log("Cannot covert this file into URL.", e);
                return null;
            }
        }
        log("Cannot resolve this path.");
        return null;
    }

    protected void log(Object... w) {
        U.log("[" + getClass().getSimpleName() + "]", w);
    }
}
