package org.tlauncher.tlauncher.ui.text;

import ch.qos.logback.core.CoreConstants;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextArea;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/text/LocalizableTextArea.class */
public class LocalizableTextArea extends JTextArea implements LocalizableComponent {
    private static final long serialVersionUID = -6319054735918918355L;
    private CenterPanelTheme theme;
    private String placeholder;

    public LocalizableTextArea(String placeholder, int rows, int columns) {
        super(rows, columns);
        this.theme = CenterPanel.defaultTheme;
        setLineWrap(true);
        this.placeholder = placeholder;
        addFocusListener(new FocusListener() { // from class: org.tlauncher.tlauncher.ui.text.LocalizableTextArea.1
            public void focusLost(FocusEvent e) {
                LocalizableTextArea.this.setPlaceholder();
            }

            public void focusGained(FocusEvent e) {
                LocalizableTextArea.this.setPlaceholder();
            }
        });
        setPlaceholder();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlaceholder() {
        if (StringUtils.isBlank(getText())) {
            setForeground(this.theme.getFocusLost());
            super.setText(Localizable.get(this.placeholder));
        } else if (Localizable.get(this.placeholder).equals(getText())) {
            super.setText(CoreConstants.EMPTY_STRING);
            setForeground(this.theme.getFocus());
        }
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setPlaceholder();
    }

    public void setText(String t) {
        setForeground(this.theme.getFocus());
        super.setText(t);
        if (StringUtils.isBlank(getText())) {
            setPlaceholder();
        }
    }
}
