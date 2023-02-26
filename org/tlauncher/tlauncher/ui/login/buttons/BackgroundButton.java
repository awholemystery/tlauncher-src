package org.tlauncher.tlauncher.ui.login.buttons;

import java.awt.Color;
import java.awt.Graphics;
import org.tlauncher.tlauncher.ui.loc.LocalizableButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/BackgroundButton.class */
public class BackgroundButton extends LocalizableButton {
    private Color defaultColor;
    private Color clickColor;

    public BackgroundButton(Color background) {
        setContentAreaFilled(false);
        setOpaque(true);
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(this.clickColor);
        } else {
            g.setColor(this.defaultColor);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
