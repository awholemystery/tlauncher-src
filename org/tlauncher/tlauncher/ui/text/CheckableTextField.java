package org.tlauncher.tlauncher.ui.text;

import org.tlauncher.tlauncher.ui.center.CenterPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/text/CheckableTextField.class */
public abstract class CheckableTextField extends ExtendedTextField {
    private static final long serialVersionUID = 2835507963141686372L;
    private CenterPanel parent;

    protected abstract String check(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public CheckableTextField(CenterPanel panel, String placeholder, String value) {
        super(panel, placeholder, value);
        this.parent = panel;
    }

    public CheckableTextField(String placeholder, String value) {
        this(null, placeholder, value);
    }

    public CheckableTextField(String placeholder) {
        this(null, placeholder, null);
    }

    public CheckableTextField(CenterPanel panel) {
        this(panel, null, null);
    }

    boolean check() {
        String text = getValue();
        String result = check(text);
        if (result == null) {
            return setValid();
        }
        return setInvalid(result);
    }

    public boolean setInvalid(String reason) {
        setBackground(getTheme().getFailure());
        setForeground(getTheme().getFocus());
        if (this.parent != null) {
            this.parent.setError(reason);
            return false;
        }
        return false;
    }

    public boolean setValid() {
        setBackground(getTheme().getBackground());
        setForeground(getTheme().getFocus());
        if (this.parent != null) {
            this.parent.setError(null);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
    public void updateStyle() {
        super.updateStyle();
        check();
    }

    @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
    protected void onChange() {
        check();
    }
}
