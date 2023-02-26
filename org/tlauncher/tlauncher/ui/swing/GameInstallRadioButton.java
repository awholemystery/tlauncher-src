package org.tlauncher.tlauncher.ui.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/GameInstallRadioButton.class */
public class GameInstallRadioButton extends GameRadioTextButton {
    public GameInstallRadioButton(String string) {
        super(string);
        this.defaultColor = Color.WHITE;
    }

    @Override // org.tlauncher.tlauncher.ui.swing.GameRadioTextButton
    protected void paintBackground(Graphics2D g2, Rectangle rec) {
        if (getModel().isSelected()) {
            g2.setColor(this.defaultColor);
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRect(rec.x, rec.y, rec.width, rec.height);
    }
}
