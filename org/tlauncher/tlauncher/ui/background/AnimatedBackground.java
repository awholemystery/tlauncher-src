package org.tlauncher.tlauncher.ui.background;

import java.awt.Color;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/background/AnimatedBackground.class */
public abstract class AnimatedBackground extends Background {
    private static final long serialVersionUID = -7203733710324519015L;

    public abstract void startBackground();

    public abstract void stopBackground();

    public abstract void suspendBackground();

    public AnimatedBackground(BackgroundHolder holder, Color coverColor) {
        super(holder, coverColor);
    }
}
