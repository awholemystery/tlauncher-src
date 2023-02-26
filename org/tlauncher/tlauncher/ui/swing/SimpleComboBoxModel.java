package org.tlauncher.tlauncher.ui.swing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/SimpleComboBoxModel.class */
public class SimpleComboBoxModel<E> extends DefaultComboBoxModel<E> {
    private static final long serialVersionUID = 5950434966721171811L;
    protected List<E> objects;
    protected Object selectedObject;

    public SimpleComboBoxModel() {
        this.objects = Collections.synchronizedList(new ArrayList());
    }

    public SimpleComboBoxModel(E[] items) {
        this.objects = Collections.synchronizedList(new ArrayList());
        for (E e : items) {
            this.objects.add(e);
        }
        if (getSize() > 0) {
            this.selectedObject = getElementAt(0);
        }
    }

    public SimpleComboBoxModel(Vector<E> v) {
        this.objects = v;
        if (getSize() > 0) {
            this.selectedObject = getElementAt(0);
        }
    }

    public void setSelectedItem(Object anObject) {
        if ((this.selectedObject != null && !this.selectedObject.equals(anObject)) || (this.selectedObject == null && anObject != null)) {
            this.selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    public Object getSelectedItem() {
        return this.selectedObject;
    }

    public int getSize() {
        return this.objects.size();
    }

    public E getElementAt(int index) {
        if (index >= 0 && index < this.objects.size()) {
            return this.objects.get(index);
        }
        return null;
    }

    public int getIndexOf(Object anObject) {
        return this.objects.indexOf(anObject);
    }

    public void addElement(E anObject) {
        this.objects.add(anObject);
        int size = this.objects.size();
        int index = this.objects.size() - 1;
        fireIntervalAdded(this, index, index);
        if (size == 1 && this.selectedObject == null && anObject != null) {
            setSelectedItem(anObject);
        }
    }

    public void addElements(Collection<E> list) {
        if (list.size() == 0) {
            return;
        }
        int size = list.size();
        int index0 = this.objects.size();
        int index1 = (index0 + size) - 1;
        this.objects.addAll(list);
        fireIntervalAdded(this, index0, index1);
        if (this.selectedObject == null) {
            for (E elem : list) {
                if (elem != null) {
                    setSelectedItem(elem);
                    return;
                }
            }
        }
    }

    public void insertElementAt(E anObject, int index) {
        this.objects.add(index, anObject);
        fireIntervalAdded(this, index, index);
    }

    public void removeElementAt(int index) {
        if (getElementAt(index) == this.selectedObject) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            }
        }
        this.objects.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    public void removeElement(Object anObject) {
        int index = this.objects.indexOf(anObject);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    public void removeAllElements() {
        int size = this.objects.size();
        if (size > 0) {
            int lastIndex = size - 1;
            this.objects.clear();
            this.selectedObject = null;
            fireIntervalRemoved(this, 0, lastIndex);
            return;
        }
        this.selectedObject = null;
    }
}
