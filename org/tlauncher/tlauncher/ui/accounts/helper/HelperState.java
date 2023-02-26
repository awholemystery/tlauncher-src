package org.tlauncher.tlauncher.ui.accounts.helper;

import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/helper/HelperState.class */
public enum HelperState {
    PREMIUM,
    FREE,
    HELP(false),
    NONE;
    
    public final LocalizableMenuItem item;
    public final boolean showInList;

    HelperState() {
        this(true);
    }

    HelperState(boolean showInList) {
        this.item = new LocalizableMenuItem("auth.helper." + toString());
        this.showInList = showInList;
    }

    @Override // java.lang.Enum
    public String toString() {
        return super.toString().toLowerCase();
    }
}
