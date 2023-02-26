package org.tlauncher.util.async;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/async/AsyncObjectContainer.class */
public class AsyncObjectContainer<T> {
    private final List<AsyncObject<T>> objects;
    private final Map<AsyncObject<T>, T> values;
    private boolean executionLock;

    public AsyncObjectContainer() {
        this.objects = new ArrayList();
        this.values = new LinkedHashMap();
    }

    public AsyncObjectContainer(AsyncObject<T>[] asyncObjects) {
        this();
        for (AsyncObject<T> object : asyncObjects) {
            add(object);
        }
    }

    public Map<AsyncObject<T>, T> execute() {
        this.executionLock = true;
        this.values.clear();
        synchronized (this.objects) {
            int i = 0;
            int size = this.objects.size();
            for (AsyncObject<T> object : this.objects) {
                object.start();
            }
            while (i < size) {
                for (AsyncObject<T> object2 : this.objects) {
                    try {
                    } catch (AsyncObjectGotErrorException e) {
                        this.values.put(object2, null);
                        i++;
                    } catch (AsyncObjectNotReadyException e2) {
                    }
                    if (!this.values.containsKey(object2)) {
                        this.values.put(object2, object2.getValue());
                        i++;
                    }
                }
            }
        }
        this.executionLock = false;
        return this.values;
    }

    public void add(AsyncObject<T> object) {
        if (object == null) {
            throw new NullPointerException();
        }
        synchronized (this.objects) {
            if (this.executionLock) {
                throw new AsyncContainerLockedException();
            }
            this.objects.add(object);
        }
    }

    public void remove(AsyncObject<T> object) {
        if (object == null) {
            throw new NullPointerException();
        }
        synchronized (this.objects) {
            if (this.executionLock) {
                throw new AsyncContainerLockedException();
            }
            this.objects.remove(object);
        }
    }

    public void removeAll() {
        synchronized (this.objects) {
            if (this.executionLock) {
                throw new AsyncContainerLockedException();
            }
            this.objects.clear();
        }
    }
}
