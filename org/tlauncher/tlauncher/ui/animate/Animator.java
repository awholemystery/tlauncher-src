package org.tlauncher.tlauncher.ui.animate;

import java.awt.Component;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/animate/Animator.class */
public class Animator {
    private static final int DEFAULT_TICK = 20;

    public static void move(Component comp, int destX, int destY, int tick) {
        comp.setLocation(destX, destY);
    }

    public static void move(Component comp, int destX, int destY) {
        move(comp, destX, destY, 20);
    }
}
