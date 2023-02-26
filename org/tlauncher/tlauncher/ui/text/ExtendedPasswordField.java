package org.tlauncher.tlauncher.ui.text;

import ch.qos.logback.core.CoreConstants;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/text/ExtendedPasswordField.class */
public class ExtendedPasswordField extends JPasswordField {
    private static final long serialVersionUID = 3175896797135831502L;
    private static final String DEFAULT_PLACEHOLDER = "password";
    private CenterPanelTheme theme;
    private String placeholder;

    private ExtendedPasswordField(CenterPanel panel, String placeholder) {
        this.theme = panel == null ? CenterPanel.defaultTheme : panel.getTheme();
        this.placeholder = placeholder == null ? DEFAULT_PLACEHOLDER : placeholder;
        addFocusListener(new FocusListener() { // from class: org.tlauncher.tlauncher.ui.text.ExtendedPasswordField.1
            public void focusGained(FocusEvent e) {
                ExtendedPasswordField.this.onFocusGained();
            }

            public void focusLost(FocusEvent e) {
                ExtendedPasswordField.this.onFocusLost();
            }
        });
        getDocument().addDocumentListener(new DocumentListener() { // from class: org.tlauncher.tlauncher.ui.text.ExtendedPasswordField.2
            public void insertUpdate(DocumentEvent e) {
                ExtendedPasswordField.this.onChange();
            }

            public void removeUpdate(DocumentEvent e) {
                ExtendedPasswordField.this.onChange();
            }

            public void changedUpdate(DocumentEvent e) {
                ExtendedPasswordField.this.onChange();
            }
        });
        setText(null);
    }

    public ExtendedPasswordField() {
        this(null, null);
    }

    private String getValueOf(String value) {
        if (value == null || value.isEmpty() || value.equals(this.placeholder)) {
            return null;
        }
        return value;
    }

    @Deprecated
    public String getText() {
        return super.getText();
    }

    public char[] getPassword() {
        String value = getValue();
        if (value == null) {
            return new char[0];
        }
        return value.toCharArray();
    }

    public boolean hasPassword() {
        return getValue() != null;
    }

    private String getValue() {
        return getValueOf(getText());
    }

    public void setText(String text) {
        String value = getValueOf(text);
        if (value == null) {
            setPlaceholder();
            return;
        }
        setForeground(this.theme.getFocus());
        super.setText(value);
    }

    private void setPlaceholder() {
        setForeground(this.theme.getFocusLost());
        super.setText(this.placeholder);
    }

    private void setEmpty() {
        setForeground(this.theme.getFocus());
        super.setText(CoreConstants.EMPTY_STRING);
    }

    void updateStyle() {
        setForeground(getValue() == null ? this.theme.getFocusLost() : this.theme.getFocus());
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder == null ? DEFAULT_PLACEHOLDER : placeholder;
        if (getValue() == null) {
            setPlaceholder();
        }
    }

    public CenterPanelTheme getTheme() {
        return this.theme;
    }

    public void setTheme(CenterPanelTheme theme) {
        if (theme == null) {
            theme = CenterPanel.defaultTheme;
        }
        this.theme = theme;
        updateStyle();
    }

    protected void onFocusGained() {
        if (getValue() == null) {
            setEmpty();
        }
    }

    protected void onFocusLost() {
        if (getValue() == null) {
            setPlaceholder();
        }
    }

    protected void onChange() {
    }
}
