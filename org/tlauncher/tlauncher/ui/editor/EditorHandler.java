package org.tlauncher.tlauncher.ui.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.ui.block.Blockable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/editor/EditorHandler.class */
public abstract class EditorHandler implements Blockable {
    private final String path;
    private String value;
    private final List<EditorFieldListener> listeners;

    public abstract boolean isValid();

    public abstract JComponent getComponent();

    public abstract String getValue();

    protected abstract void setValue0(String str);

    public EditorHandler(String path) {
        if (path == null) {
            throw new NullPointerException();
        }
        this.path = path;
        this.listeners = Collections.synchronizedList(new ArrayList());
    }

    public boolean addListener(EditorFieldListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return this.listeners.add(listener);
    }

    public boolean removeListener(EditorFieldListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return this.listeners.remove(listener);
    }

    public void onChange(String newvalue) {
        for (EditorFieldListener listener : this.listeners) {
            listener.onChange(this, this.value, newvalue);
        }
        this.value = newvalue;
    }

    public String getPath() {
        return this.path;
    }

    public void updateValue(Object obj) {
        String val = obj == null ? null : obj.toString();
        onChange(val);
        setValue0(this.value);
    }

    public void setValue(Object obj) {
        String val = obj == null ? null : obj.toString();
        setValue0(val);
    }

    public String toString() {
        return getClass().getSimpleName() + "{path='" + this.path + "', value='" + this.value + "'}";
    }
}
