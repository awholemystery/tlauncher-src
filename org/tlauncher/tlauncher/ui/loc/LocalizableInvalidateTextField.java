package org.tlauncher.tlauncher.ui.loc;

import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.text.InvalidateTextField;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableInvalidateTextField.class */
public class LocalizableInvalidateTextField extends InvalidateTextField implements LocalizableComponent {
    private static final long serialVersionUID = -3999545292427982797L;
    private String placeholderPath;

    private LocalizableInvalidateTextField(CenterPanel panel, String placeholderPath, String value) {
        super(panel, null, value);
        this.placeholderPath = placeholderPath;
        setValue(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LocalizableInvalidateTextField(String placeholderPath) {
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
