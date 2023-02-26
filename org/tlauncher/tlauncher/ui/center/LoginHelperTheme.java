package org.tlauncher.tlauncher.ui.center;

import java.awt.Color;
import org.apache.http.HttpStatus;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/center/LoginHelperTheme.class */
public class LoginHelperTheme extends TipPanelTheme {
    private final Color borderColor = new Color(96, (int) HttpStatus.SC_NO_CONTENT, 240);
    private final Color BACKROUND_COLOR = new Color(96, (int) HttpStatus.SC_NO_CONTENT, 240);
    private final Color FOREGROUND_COLOR = Color.WHITE;

    @Override // org.tlauncher.tlauncher.ui.center.TipPanelTheme, org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getBorder() {
        return this.borderColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getBackground() {
        return this.BACKROUND_COLOR;
    }

    public Color getForeground() {
        return this.FOREGROUND_COLOR;
    }
}
