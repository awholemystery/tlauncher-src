package org.tlauncher.util;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/Direction.class */
public enum Direction {
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    CENTER_LEFT,
    CENTER,
    CENTER_RIGHT,
    BOTTOM_LEFT,
    BOTTOM,
    BOTTOM_RIGHT;
    
    private final String lower = name().toLowerCase();

    Direction() {
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.lower;
    }
}
