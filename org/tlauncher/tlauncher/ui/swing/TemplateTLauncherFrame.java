package org.tlauncher.tlauncher.ui.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.SwingUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/TemplateTLauncherFrame.class */
public class TemplateTLauncherFrame extends JFrame {
    protected JPanel contentPane;
    public static final int MAX_WIDTH_ELEMENT = 500;
    protected JFrame parent;

    public TemplateTLauncherFrame(JFrame parent, String title) {
        setBounds(100, 100, HttpStatus.SC_BAD_REQUEST, 366);
        this.parent = parent;
        setLocationRelativeTo(null);
        setAlwaysOnTop(false);
        setResizable(false);
        SwingUtil.setFavicons(this);
        setTitle(Localizable.get(title));
        parent.setEnabled(false);
    }

    public void setVisible(boolean b) {
        this.parent.setEnabled(!b);
        super.setVisible(b);
    }
}
