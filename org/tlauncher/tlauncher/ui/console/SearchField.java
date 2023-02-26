package org.tlauncher.tlauncher.ui.console;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
import org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme;
import org.tlauncher.tlauncher.ui.loc.LocalizableInvalidateTextField;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/SearchField.class */
class SearchField extends LocalizableInvalidateTextField {
    private static final long serialVersionUID = -6453744340240419870L;
    private static final CenterPanelTheme darkTheme = new DefaultCenterPanelTheme() { // from class: org.tlauncher.tlauncher.ui.console.SearchField.1
        public final Color backgroundColor = new Color(0, 0, 0, 255);
        public final Color focusColor = new Color(255, 255, 255, 255);
        public final Color focusLostColor = new Color(128, 128, 128, 255);
        public final Color successColor = this.focusColor;

        @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
        public Color getBackground() {
            return this.backgroundColor;
        }

        @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
        public Color getFocus() {
            return this.focusColor;
        }

        @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
        public Color getFocusLost() {
            return this.focusLostColor;
        }

        @Override // org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme, org.tlauncher.tlauncher.ui.center.CenterPanelTheme
        public Color getSuccess() {
            return this.successColor;
        }
    };

    SearchField(final SearchPanel sp) {
        super("console.search.placeholder");
        if (OS.WINDOWS.isCurrent()) {
            setTheme(darkTheme);
        }
        setText(null);
        setCaretColor(Color.white);
        addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.console.SearchField.2
            public void actionPerformed(ActionEvent e) {
                sp.search();
            }
        });
    }
}
