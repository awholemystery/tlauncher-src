package org.tlauncher.tlauncher.ui.accounts.helper;

import java.awt.Component;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.center.LoginHelperTheme;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.util.U;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/helper/HelperTip.class */
public class HelperTip extends CenterPanel {
    public final String name;
    public final LocalizableLabel label;
    public final Component component;
    public final Component parent;
    public final byte alignment;
    public final HelperState[] states;
    private static final LoginHelperTheme HelperTipTheme = new LoginHelperTheme();

    /* JADX INFO: Access modifiers changed from: package-private */
    public HelperTip(String name, Component component, Component parent, byte alignment, HelperState... states) {
        super(HelperTipTheme, smallSquareInsets);
        if (name == null) {
            throw new NullPointerException("Name is NULL");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name is empty");
        }
        if (component == null) {
            throw new NullPointerException("Component is NULL");
        }
        if (parent == null) {
            throw new NullPointerException("Parent is NULL");
        }
        if (alignment > 3) {
            throw new IllegalArgumentException("Unknown alignment");
        }
        if (states == null) {
            throw new NullPointerException("State array is NULL");
        }
        this.name = name;
        this.component = component;
        this.parent = parent;
        this.alignment = alignment;
        this.label = new LocalizableLabel();
        this.states = states;
        add((Component) this.label);
        setBackground(U.shiftAlpha(getTheme().getBackground(), 255));
    }
}
