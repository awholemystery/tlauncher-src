package org.tlauncher.tlauncher.ui.swing;

import java.beans.PropertyChangeListener;
import javax.swing.Action;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/EmptyAction.class */
public abstract class EmptyAction implements Action {
    protected boolean enabled = true;

    public Object getValue(String key) {
        return null;
    }

    public void putValue(String key, Object value) {
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }
}
