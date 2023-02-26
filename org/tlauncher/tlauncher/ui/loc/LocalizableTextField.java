package org.tlauncher.tlauncher.ui.loc;

import org.tlauncher.tlauncher.ui.TLauncherFrame;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.text.ExtendedTextField;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableTextField.class */
public class LocalizableTextField extends ExtendedTextField implements LocalizableComponent {
    private static final long serialVersionUID = 359096767189321072L;
    protected String placeholderPath;
    protected String[] variables;

    public LocalizableTextField(CenterPanel panel, String placeholderPath, String value) {
        super(panel, null, value);
        setValue(value);
        setPlaceholder(placeholderPath);
        setFont(getFont().deriveFont(TLauncherFrame.fontSize));
    }

    public LocalizableTextField(CenterPanel panel, String placeholderPath) {
        this(panel, placeholderPath, null);
    }

    public LocalizableTextField(String placeholderPath) {
        this(null, placeholderPath, null);
    }

    public LocalizableTextField() {
        this(null, null, null);
    }

    public void setPlaceholder(String placeholderPath, Object... vars) {
        this.placeholderPath = placeholderPath;
        this.variables = Localizable.checkVariables(vars);
        String value = Localizable.get(placeholderPath);
        for (int i = 0; i < this.variables.length; i++) {
            value = value.replace("%" + i, this.variables[i]);
        }
        super.setPlaceholder(value);
    }

    @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
    public void setPlaceholder(String placeholderPath) {
        setPlaceholder(placeholderPath, Localizable.EMPTY_VARS);
    }

    public String getPlaceholderPath() {
        return this.placeholderPath;
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setPlaceholder(this.placeholderPath, this.variables);
    }
}
