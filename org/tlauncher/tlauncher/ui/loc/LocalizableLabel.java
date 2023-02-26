package org.tlauncher.tlauncher.ui.loc;

import org.tlauncher.tlauncher.ui.TLauncherFrame;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLabel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableLabel.class */
public class LocalizableLabel extends ExtendedLabel implements LocalizableComponent {
    private static final long serialVersionUID = 7628068160047735335L;
    protected String path;
    protected String[] variables;

    public LocalizableLabel(String path, Object... vars) {
        setText(path, vars);
        setFont(getFont().deriveFont(TLauncherFrame.fontSize));
    }

    public LocalizableLabel(String path) {
        this(path, Localizable.EMPTY_VARS);
    }

    public LocalizableLabel() {
        this((String) null);
    }

    public LocalizableLabel(int horizontalAlignment) {
        this((String) null);
        setHorizontalAlignment(horizontalAlignment);
    }

    public void setText(String path, Object... vars) {
        this.path = path;
        this.variables = Localizable.checkVariables(vars);
        String value = Localizable.get(path);
        for (int i = 0; i < this.variables.length; i++) {
            value = value.replace("%" + i, this.variables[i]);
        }
        setRawText(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRawText(String value) {
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
