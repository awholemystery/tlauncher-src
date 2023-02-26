package org.tlauncher.tlauncher.ui.editor;

import org.tlauncher.tlauncher.ui.block.Blockable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorField.class */
public interface EditorField extends Blockable {
    String getSettingsValue();

    void setSettingsValue(String str);

    boolean isValueValid();
}
