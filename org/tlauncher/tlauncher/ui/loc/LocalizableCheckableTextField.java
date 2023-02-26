package org.tlauncher.tlauncher.ui.loc;

import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.text.CheckableTextField;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableCheckableTextField.class */
public abstract class LocalizableCheckableTextField extends CheckableTextField implements LocalizableComponent {
    private static final long serialVersionUID = 1;
    private String placeholderPath;

    private LocalizableCheckableTextField(CenterPanel panel, String placeholderPath, String value) {
        super(panel, null, null);
        this.placeholderPath = placeholderPath;
        setValue(value);
    }

    public LocalizableCheckableTextField(CenterPanel panel, String placeholderPath) {
        this(panel, placeholderPath, null);
    }

    public LocalizableCheckableTextField(String placeholderPath, String value) {
        this(null, placeholderPath, value);
    }

    public LocalizableCheckableTextField(String placeholderPath) {
        this(null, placeholderPath, null);
    }

    @Override // org.tlauncher.tlauncher.ui.text.ExtendedTextField
    public void setPlaceholder(String placeholderPath) {
        this.placeholderPath = placeholderPath;
        super.setPlaceholder(Localizable.get() == null ? placeholderPath : Localizable.get().get(placeholderPath));
    }

    public String getPlaceholderPath() {
        return this.placeholderPath;
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setPlaceholder(this.placeholderPath);
    }
}
