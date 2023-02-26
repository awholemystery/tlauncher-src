package org.tlauncher.tlauncher.ui.loc.modpack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ButtonModel;
import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
import org.tlauncher.util.SwingUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/modpack/ShadowButton.class */
public class ShadowButton extends UpdaterFullButton {
    public ShadowButton(Color color, Color mouseUnder, String value, String image) {
        super(color, mouseUnder, value, image);
    }

    @Override // org.tlauncher.tlauncher.ui.loc.UpdaterFullButton
    protected void paintBackground(Rectangle rec, Graphics g) {
        ButtonModel buttonModel = getModel();
        if (buttonModel.isRollover()) {
            g.setColor(this.unEnableColor);
        } else {
            g.setColor(this.backgroundColor);
        }
        SwingUtil.paintShadowLine(rec, g, getBackground().getRed(), 12);
    }
}
