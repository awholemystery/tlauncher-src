package org.tlauncher.tlauncher.ui.login.buttons;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/MainImageButton.class */
public class MainImageButton extends ImageUdaterButton {
    private static final Color mouseUnderColor = new Color(82, 127, 53);
    protected MouseAdapter adapter;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MainImageButton(Color color, String image, String mouseUnderImage) {
        super(color, mouseUnderColor, image, mouseUnderImage);
        setBorder(BorderFactory.createEmptyBorder());
    }
}
