package org.tlauncher.tlauncher.ui.editor;

import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorTextField.class */
public class EditorTextField extends LocalizableTextField implements EditorField {
    private static final long serialVersionUID = 3920711425159165958L;
    private final boolean canBeEmpty;

    public EditorTextField(String prompt, boolean canBeEmpty) {
        super(prompt);
        this.canBeEmpty = canBeEmpty;
        setColumns(1);
    }

    public EditorTextField(String prompt) {
        this(prompt, false);
    }

    public EditorTextField(boolean canBeEmpty) {
        this(null, canBeEmpty);
    }

    public EditorTextField() {
        this(false);
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public String getSettingsValue() {
        return getValue();
    }

    @Override // org.tlauncher.tlauncher.ui.editor.EditorField
    public void setSettingsValue(String value) {
        setText(value);
        setCaretPosition(0);
    }

    public boolean isValueValid() {
        String text = getValue();
        return text != null || this.canBeEmpty;
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        setEnabled(false);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        setEnabled(true);
    }
}
