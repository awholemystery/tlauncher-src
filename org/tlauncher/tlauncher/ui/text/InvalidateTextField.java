package org.tlauncher.tlauncher.ui.text;

import org.tlauncher.tlauncher.ui.center.CenterPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/text/InvalidateTextField.class */
public class InvalidateTextField extends CheckableTextField {
    private static final long serialVersionUID = -4076362911409776688L;

    /* JADX INFO: Access modifiers changed from: protected */
    public InvalidateTextField(CenterPanel panel, String placeholder, String value) {
        super(panel, placeholder, value);
    }

    public InvalidateTextField(CenterPanel panel) {
        this(panel, null, null);
    }

    @Override // org.tlauncher.tlauncher.ui.text.CheckableTextField
    protected String check(String text) {
        return null;
    }
}
