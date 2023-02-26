package org.tlauncher.tlauncher.ui.loc;

import org.tlauncher.tlauncher.ui.swing.extended.ExtendedButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableButton.class */
public class LocalizableButton extends ExtendedButton implements LocalizableComponent {
    private static final long serialVersionUID = 1073130908385613323L;
    private String path;
    private String[] variables;

    /* JADX INFO: Access modifiers changed from: protected */
    public LocalizableButton() {
    }

    public LocalizableButton(String path) {
        this();
        setText(path);
    }

    public LocalizableButton(String path, Object... vars) {
        this();
        setText(path, vars);
    }

    public void setText(String path, Object... vars) {
        this.path = path;
        this.variables = Localizable.checkVariables(vars);
        String value = Localizable.get(path);
        for (int i = 0; i < this.variables.length; i++) {
            value = value.replace("%" + i, this.variables[i]);
        }
        super.setText(value);
    }

    public void setText(String path) {
        setText(path, Localizable.EMPTY_VARS);
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setText(this.path, this.variables);
    }
}
