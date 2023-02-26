package org.tlauncher.tlauncher.ui.swing;

import javax.swing.JCheckBox;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.images.ImageIcon;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/OwnImageCheckBox.class */
public class OwnImageCheckBox extends JCheckBox {
    public OwnImageCheckBox(String text, String onText, String offText) {
        super(text);
        ImageIcon on = ImageCache.getIcon(onText);
        ImageIcon off = ImageCache.getIcon(offText);
        setSelectedIcon(on);
        setDisabledSelectedIcon(on);
        setPressedIcon(on);
        setIcon(off);
        setDisabledIcon(off);
        setOpaque(false);
        setFocusable(false);
    }
}
