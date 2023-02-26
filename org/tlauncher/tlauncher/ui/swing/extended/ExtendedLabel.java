package org.tlauncher.tlauncher.ui.swing.extended;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.tlauncher.tlauncher.ui.TLauncherFrame;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/ExtendedLabel.class */
public class ExtendedLabel extends JLabel {
    private static final AlphaComposite disabledAlphaComposite = AlphaComposite.getInstance(3, 0.5f);

    public ExtendedLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        setFont(getFont().deriveFont(TLauncherFrame.fontSize));
        setOpaque(false);
    }

    public ExtendedLabel(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }

    public ExtendedLabel(String text) {
        this(text, null, 10);
    }

    public ExtendedLabel(Icon image, int horizontalAlignment) {
        this(null, image, horizontalAlignment);
    }

    public ExtendedLabel(Icon image) {
        this(null, image, 0);
    }

    public ExtendedLabel() {
        this(null, null, 10);
    }

    public void paintComponent(Graphics g0) {
        if (isEnabled()) {
            super.paintComponent(g0);
            return;
        }
        Graphics2D g = (Graphics2D) g0;
        Composite oldComposite = g.getComposite();
        g.setComposite(disabledAlphaComposite);
        super.paintComponent(g);
        g.setComposite(oldComposite);
    }
}
