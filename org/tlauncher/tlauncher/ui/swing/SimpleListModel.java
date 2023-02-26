package org.tlauncher.tlauncher.ui.swing;

import java.util.Collection;
import java.util.Vector;
import javax.swing.AbstractListModel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/SimpleListModel.class */
public class SimpleListModel<E> extends AbstractListModel<E> {
    private static final long serialVersionUID = 727845864028652893L;
    protected final Vector<E> vector = new Vector<>();

    public int getSize() {
        return this.vector.size();
    }

    public E getElementAt(int index) {
        if (index < 0 || index >= getSize()) {
            return null;
        }
        return this.vector.get(index);
    }

    public void add(E elem) {
        int index = this.vector.size();
        this.vector.add(elem);
        fireIntervalAdded(this, index, index);
    }

    public boolean remove(E elem) {
        int index = indexOf(elem);
        boolean rv = this.vector.removeElement(elem);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
    }

    public void addAll(Collection<E> elem) {
        int size = elem.size();
        if (size == 0) {
            return;
        }
        int index0 = this.vector.size();
        int index1 = (index0 + size) - 1;
        this.vector.addAll(elem);
        fireIntervalAdded(this, index0, index1);
    }

    public void clear() {
        int index1 = this.vector.size() - 1;
        this.vector.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }

    public boolean isEmpty() {
        return this.vector.isEmpty();
    }

    public boolean contains(E elem) {
        return this.vector.contains(elem);
    }

    public int indexOf(E elem) {
        return this.vector.indexOf(elem);
    }

    public int indexOf(E elem, int index) {
        return this.vector.indexOf(elem, index);
    }

    public E elementAt(int index) {
        return this.vector.elementAt(index);
    }

    public String toString() {
        return this.vector.toString();
    }
}
