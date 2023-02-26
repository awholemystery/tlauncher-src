package org.tlauncher.tlauncher.ui.editor;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorFieldChangeListener.class */
public abstract class EditorFieldChangeListener extends EditorFieldListener {
    public abstract void onChange(String str, String str2);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.editor.EditorFieldListener
    public void onChange(EditorHandler handler, String oldValue, String newValue) {
        if (newValue == null && oldValue == null) {
            return;
        }
        if (newValue != null && newValue.equals(oldValue)) {
            return;
        }
        onChange(oldValue, newValue);
    }
}
