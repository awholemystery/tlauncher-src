package org.tlauncher.tlauncher.ui.swing.extended;

import javax.swing.Icon;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/HTMLLabel.class */
public class HTMLLabel extends ExtendedLabel {
    private static final long serialVersionUID = -509864367525835474L;

    public HTMLLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    public HTMLLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }

    public HTMLLabel(String text) {
        super(text);
    }

    public HTMLLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public HTMLLabel(Icon image) {
        super(image);
    }

    public HTMLLabel() {
    }

    public void setText(String text) {
        super.setText("<html>" + (text == null ? null : text.replace("\n", "<br/>")) + "</html>");
    }
}
