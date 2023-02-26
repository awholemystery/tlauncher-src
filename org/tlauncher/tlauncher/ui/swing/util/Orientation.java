package org.tlauncher.tlauncher.ui.swing.util;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/util/Orientation.class */
public enum Orientation {
    TOP(1),
    LEFT(2),
    BOTTOM(3),
    RIGHT(4),
    CENTER(0);
    
    private final int swingAlias;

    Orientation(int swingAlias) {
        this.swingAlias = swingAlias;
    }

    public int getSwingAlias() {
        return this.swingAlias;
    }

    public static Orientation fromSwingConstant(int orientation) {
        Orientation[] values;
        for (Orientation current : values()) {
            if (orientation == current.getSwingAlias()) {
                return current;
            }
        }
        return null;
    }
}
