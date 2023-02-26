package org.tlauncher.tlauncher.ui.center;

import java.awt.Color;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/center/DefaultCenterPanelTheme.class */
public class DefaultCenterPanelTheme extends CenterPanelTheme {
    protected final Color backgroundColor = new Color(255, 255, 255, 255);
    protected final Color panelBackgroundColor = new Color(255, 255, 255, 128);
    protected final Color focusColor = new Color(0, 0, 0, 255);
    protected final Color focusLostColor = new Color(128, 128, 128, 255);
    protected final Color successColor = new Color(78, 196, 78, 255);
    protected final Color failureColor = Color.getHSBColor(0.0f, 0.3f, 1.0f);
    protected final Color borderColor = new Color(28, 128, 28, 255);
    protected final Color delPanelColor = this.successColor;

    @Override // org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getBackground() {
        return this.backgroundColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getPanelBackground() {
        return this.panelBackgroundColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getFocus() {
        return this.focusColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getFocusLost() {
        return this.focusLostColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getSuccess() {
        return this.successColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getFailure() {
        return this.failureColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getBorder() {
        return this.borderColor;
    }

    @Override // org.tlauncher.tlauncher.ui.center.CenterPanelTheme
    public Color getDelPanel() {
        return this.delPanelColor;
    }
}
