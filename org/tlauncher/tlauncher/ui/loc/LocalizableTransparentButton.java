package org.tlauncher.tlauncher.ui.loc;

import org.tlauncher.tlauncher.ui.swing.TransparentButton;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableTransparentButton.class */
public class LocalizableTransparentButton extends TransparentButton implements LocalizableComponent {
    private static final long serialVersionUID = -1357535949476677157L;
    private String path;
    private String[] variables;

    public LocalizableTransparentButton(String path, Object... vars) {
        setOpaque(false);
        setText(path, vars);
    }

    void setText(String path, Object... vars) {
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
