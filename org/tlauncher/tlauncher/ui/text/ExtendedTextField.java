package org.tlauncher.tlauncher.ui.text;

import ch.qos.logback.core.CoreConstants;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/text/ExtendedTextField.class */
public class ExtendedTextField extends JTextField {
    private static final long serialVersionUID = -1963422246993419362L;
    private CenterPanelTheme theme;
    private String placeholder;
    private String oldPlaceholder;

    /* JADX INFO: Access modifiers changed from: protected */
    public ExtendedTextField(CenterPanel panel, String placeholder, String value) {
        this.theme = panel == null ? CenterPanel.defaultTheme : panel.getTheme();
        this.placeholder = placeholder;
        addFocusListener(new FocusListener() { // from class: org.tlauncher.tlauncher.ui.text.ExtendedTextField.1
            public void focusGained(FocusEvent e) {
                ExtendedTextField.this.onFocusGained();
            }

            public void focusLost(FocusEvent e) {
                ExtendedTextField.this.onFocusLost();
            }
        });
        getDocument().addDocumentListener(new DocumentListener() { // from class: org.tlauncher.tlauncher.ui.text.ExtendedTextField.2
            public void insertUpdate(DocumentEvent e) {
                ExtendedTextField.this.onChange();
            }

            public void removeUpdate(DocumentEvent e) {
                ExtendedTextField.this.onChange();
            }

            public void changedUpdate(DocumentEvent e) {
                ExtendedTextField.this.onChange();
            }
        });
        setValue(value);
    }

    public ExtendedTextField(String placeholder, String value) {
        this(null, placeholder, value);
    }

    public ExtendedTextField(String placeholder) {
        this(null, placeholder, null);
    }

    @Deprecated
    public String getText() {
        return super.getText();
    }

    private String getValueOf(String value) {
        if (value == null || value.isEmpty() || value.equals(this.placeholder) || value.equals(this.oldPlaceholder)) {
            return null;
        }
        return value;
    }

    public String getValue() {
        return getValueOf(getText());
    }

    public void setText(String text) {
        String value = getValueOf(text);
        if (value == null) {
            setPlaceholder();
            return;
        }
        setForeground(this.theme.getFocus());
        setRawText(value);
    }

    private void setPlaceholder() {
        setForeground(this.theme.getFocusLost());
        setRawText(this.placeholder);
    }

    private void setEmpty() {
        setForeground(this.theme.getFocus());
        setRawText(CoreConstants.EMPTY_STRING);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateStyle() {
        setForeground(getValue() == null ? this.theme.getFocusLost() : this.theme.getFocus());
    }

    public void setValue(Object obj) {
        setText(obj == null ? null : obj.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setValue(String s) {
        setText(s);
    }

    protected void setRawText(String s) {
        super.setText(s);
        super.setCaretPosition(0);
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPlaceholder(String placeholder) {
        this.oldPlaceholder = this.placeholder;
        this.placeholder = placeholder;
        if (getValue() == null) {
            setPlaceholder();
        }
    }

    public CenterPanelTheme getTheme() {
        return this.theme;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTheme(CenterPanelTheme theme) {
        if (theme == null) {
            theme = CenterPanel.defaultTheme;
        }
        this.theme = theme;
        updateStyle();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onFocusGained() {
        if (getValue() == null) {
            setEmpty();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onFocusLost() {
        if (getValue() == null) {
            setPlaceholder();
        }
    }

    protected void onChange() {
    }
}
