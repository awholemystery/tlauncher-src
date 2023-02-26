package org.tlauncher.tlauncher.ui.settings;

import java.util.ArrayList;
import java.util.List;
import org.tlauncher.tlauncher.ui.editor.EditorField;
import org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/settings/HandlerSettings.class */
class HandlerSettings {
    private String key;
    private EditorField editorField;
    private List<EditorFieldChangeListener> listeners;

    public HandlerSettings(String key, EditorField editorField) {
        this.listeners = new ArrayList();
        this.key = key;
        this.editorField = editorField;
    }

    public HandlerSettings(String key, EditorField editorField, EditorFieldChangeListener listener) {
        this(key, editorField);
        this.listeners.add(listener);
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public EditorField getEditorField() {
        return this.editorField;
    }

    public void setEditorField(EditorField editorField) {
        this.editorField = editorField;
    }

    public void onChange(String oldValue, String newValue) {
        for (EditorFieldChangeListener editorFieldListener : this.listeners) {
            editorFieldListener.onChange(oldValue, newValue);
        }
    }
}
