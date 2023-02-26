package org.tlauncher.tlauncher.ui.center;

import java.awt.Color;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/center/TipPanelTheme.class */
public class TipPanelTheme extends DefaultCenterPanelTheme {
    private final Color borderColor = this.failureColor;

    @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getBorder() {
        return this.borderColor;
    }
}
