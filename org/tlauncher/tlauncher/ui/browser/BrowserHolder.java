package org.tlauncher.tlauncher.ui.browser;

import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.block.Unblockable;
import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/browser/BrowserHolder.class */
public class BrowserHolder extends BorderPanel implements ResizeableComponent, Unblockable {
    private static BrowserHolder browserHolder;
    MainPane pane;
    final BrowserFallback fallback = new BrowserFallback(this);
    final BrowserPanel browser;

    private BrowserHolder() {
        BrowserPanel browser_ = null;
        try {
            browser_ = new BrowserPanel(this);
        } catch (Throwable e) {
            log("Cannot load BrowserPanel. Will show BrowserFallback panel.", e);
        }
        this.browser = browser_;
        setBrowserShown("fallback");
    }

    public void setBrowserShown(Object reason, boolean shown) {
        if (!shown && !Blocker.isBlocked(this.fallback)) {
            this.fallback.unblock(reason);
        } else {
            Blocker.setBlocked(this.fallback, reason, shown);
        }
    }

    public void setBrowserContentShown(Object reason, boolean shown) {
        if (this.browser != null) {
            Blocker.setBlocked(this.browser, reason, !shown);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        if (this.pane == null) {
            log("pane = null so it'c can't resize");
            return;
        }
        int width = this.pane.getWidth();
        int height = this.pane.getHeight();
        setSize(width, height);
    }

    private static void log(Object... o) {
        U.log("[BrowserHolder]", o);
    }

    public MainPane getPane() {
        return this.pane;
    }

    public void setPane(MainPane pane) {
        this.pane = pane;
    }

    public static synchronized BrowserHolder getInstance() {
        if (browserHolder == null) {
            browserHolder = new BrowserHolder();
        }
        return browserHolder;
    }

    public BrowserPanel getBrowser() {
        return this.browser;
    }

    public void setBrowserShown(String reason) {
        setBrowserShown(reason, this.browser != null);
    }
}
