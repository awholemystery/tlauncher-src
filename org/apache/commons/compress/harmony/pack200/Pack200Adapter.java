package org.apache.commons.compress.harmony.pack200;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.compress.java.util.jar.Pack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Pack200Adapter.class */
public abstract class Pack200Adapter {
    protected static final int DEFAULT_BUFFER_SIZE = 8192;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final SortedMap<String, String> properties = new TreeMap();

    public SortedMap<String, String> properties() {
        return this.properties;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.support.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.support.removePropertyChangeListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void completed(double value) {
        firePropertyChange(Pack200.Packer.PROGRESS, null, String.valueOf((int) (100.0d * value)));
    }
}
