package org.tlauncher.tlauncher.ui.center;

import java.awt.Color;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/center/UpdateTheme.class */
public class UpdateTheme extends DefaultCenterPanelTheme {
    private final Color backGroundColorServer = new Color(252, 247, 244);

    @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getPanelBackground() {
        return this.backGroundColorServer;
    }

    @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getBorder() {
        return this.backGroundColorServer;
    }
}
