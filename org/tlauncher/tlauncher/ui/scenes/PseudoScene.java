package org.tlauncher.tlauncher.ui.scenes;

import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.swing.AnimatedVisibility;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/PseudoScene.class */
public abstract class PseudoScene extends ExtendedLayeredPane implements AnimatedVisibility {
    private static final long serialVersionUID = -1;
    private final MainPane main;
    private boolean shown;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PseudoScene(MainPane main) {
        super(main);
        this.shown = true;
        this.main = main;
        setSize(main.getWidth(), main.getHeight());
    }

    public MainPane getMainPane() {
        return this.main;
    }

    public void setShown(boolean shown) {
        setShown(shown, true);
    }

    public void setShown(boolean shown, boolean animate) {
        if (this.shown == shown) {
            return;
        }
        this.shown = shown;
        setVisible(shown);
    }
}
