package org.tlauncher.tlauncher.ui.center;

import java.awt.Color;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/center/SettingsPanelTheme.class */
public class SettingsPanelTheme extends DefaultCenterPanelTheme {
    protected final Color panelBackgroundColor = new Color(246, 244, 243);
    protected final Color borderColor = new Color(172, 172, 172, 255);
    protected final Color delPanelColor = new Color(50, 80, 190, 255);

    @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getPanelBackground() {
        return this.panelBackgroundColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getBorder() {
        return this.borderColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getDelPanel() {
        return this.delPanelColor;
    }
}
