package org.tlauncher.tlauncher.ui.swing.extended;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.TLauncherFrame;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/ExtendedButton.class */
public class ExtendedButton extends JButton {
    private static final long serialVersionUID = -2009736184875993130L;
    public static final Color ORANGE_COLOR = new Color(235, 132, 46);
    public static final Color GREEN_COLOR = new Color(107, (int) HttpStatus.SC_ACCEPTED, 45);
    public static final Color DARK_GREEN_COLOR = new Color(113, 169, 76);
    public static final Color GRAY_COLOR = new Color(176, 177, 173);

    /* JADX INFO: Access modifiers changed from: protected */
    public ExtendedButton() {
        init();
    }

    public ExtendedButton(Icon icon) {
        super(icon);
        init();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ExtendedButton(String text) {
        super(text);
        init();
    }

    public ExtendedButton(Action a) {
        super(a);
        init();
    }

    public ExtendedButton(String text, Icon icon) {
        super(text, icon);
        init();
    }

    private void init() {
        setFont(getFont().deriveFont(TLauncherFrame.fontSize));
        setOpaque(false);
        addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.swing.extended.ExtendedButton.1
            public void actionPerformed(ActionEvent e) {
                Component parent = findRootParent(ExtendedButton.this.getParent());
                if (parent == null) {
                    return;
                }
                parent.requestFocusInWindow();
            }

            private Component findRootParent(Component comp) {
                if (comp == null) {
                    return null;
                }
                if (comp.getParent() == null) {
                    return comp;
                }
                return findRootParent(comp.getParent());
            }
        });
    }
}
