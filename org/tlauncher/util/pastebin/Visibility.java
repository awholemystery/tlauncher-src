package org.tlauncher.util.pastebin;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/pastebin/Visibility.class */
public enum Visibility {
    PUBLIC(0),
    NOT_LISTED(1);
    
    private final int value;

    Visibility(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
