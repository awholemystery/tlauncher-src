package org.tlauncher.tlauncher.ui.editor;

import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorCheckBox.class */
public class EditorCheckBox extends LocalizableCheckbox implements EditorField {
    private static final long serialVersionUID = -2540132118355226609L;

    public EditorCheckBox(String path) {
        super(path, LocalizableCheckbox.PANEL_TYPE.SETTINGS);
        setFocusable(false);
        setIconTextGap(10);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public String getSettingsValue() {
        return isSelected() ? "true" : "false";
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public void setSettingsValue(String value) {
        setSelected(Boolean.parseBoolean(value));
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public boolean isValueValid() {
        return true;
    }

    public void block(Object reason) {
        setEnabled(false);
    }

    public void unblock(Object reason) {
        setEnabled(true);
    }
}
