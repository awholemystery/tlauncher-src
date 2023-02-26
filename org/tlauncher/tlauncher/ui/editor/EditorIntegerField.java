package org.tlauncher.tlauncher.ui.editor;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorIntegerField.class */
public class EditorIntegerField extends EditorTextField {
    private static final long serialVersionUID = -7930510655707946312L;

    public EditorIntegerField() {
    }

    public EditorIntegerField(String prompt) {
        super(prompt);
    }

    public int getIntegerValue() {
        try {
            return Integer.parseInt(getSettingsValue());
        } catch (Exception e) {
            return -1;
        }
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorTextField, org.tlauncher.tlauncher.ui.editor.EditorField
    public boolean isValueValid() {
        try {
            Integer.parseInt(getSettingsValue());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
