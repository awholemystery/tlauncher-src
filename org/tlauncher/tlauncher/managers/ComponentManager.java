package org.tlauncher.tlauncher.managers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.tlauncher.tlauncher.component.ComponentDependence;
import org.tlauncher.tlauncher.component.InterruptibleComponent;
import org.tlauncher.tlauncher.component.LauncherComponent;
import org.tlauncher.tlauncher.component.RefreshableComponent;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.async.LoopedThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ComponentManager.class */
public class ComponentManager {
    private TLauncher tlauncher;
    private final List<LauncherComponent> components = Collections.synchronizedList(new ArrayList());
    private final ComponentManagerRefresher refresher = new ComponentManagerRefresher(this);

    public ComponentManager(TLauncher tLauncher) {
        this.tlauncher = tLauncher;
        this.refresher.start();
    }

    public TLauncher getLauncher() {
        return this.tlauncher;
    }

    public <T extends LauncherComponent> T loadComponent(Class<T> classOfT) {
        Class<?>[] value;
        if (classOfT == null) {
            throw new NullPointerException();
        }
        if (hasComponent(classOfT)) {
            return (T) getComponent(classOfT);
        }
        ComponentDependence dependence = (ComponentDependence) classOfT.getAnnotation(ComponentDependence.class);
        if (dependence != null) {
            for (Class<?> requiredClass : dependence.value()) {
                rawLoadComponent(requiredClass);
            }
        }
        return (T) rawLoadComponent(classOfT);
    }

    private <T> T rawLoadComponent(Class<T> classOfT) {
        if (classOfT == null) {
            throw new NullPointerException();
        }
        if (!LauncherComponent.class.isAssignableFrom(classOfT)) {
            throw new IllegalArgumentException("Given class is not a LauncherComponent: " + classOfT.getSimpleName());
        }
        try {
            Constructor<T> constructor = classOfT.getConstructor(ComponentManager.class);
            try {
                T instance = constructor.newInstance(this);
                LauncherComponent component = (LauncherComponent) instance;
                this.components.add(component);
                return instance;
            } catch (Exception e) {
                throw new IllegalStateException("Cannot createScrollWrapper a new instance for component: " + classOfT.getSimpleName(), e);
            }
        } catch (Exception e2) {
            throw new IllegalStateException("Cannot get constructor for component: " + classOfT.getSimpleName(), e2);
        }
    }

    public <T extends LauncherComponent> T getComponent(Class<T> classOfT) {
        if (classOfT == null) {
            throw new NullPointerException();
        }
        Iterator<LauncherComponent> it = this.components.iterator();
        while (it.hasNext()) {
            T t = (T) it.next();
            if (classOfT.isInstance(t)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Cannot find the component!");
    }

    <T extends LauncherComponent> boolean hasComponent(Class<T> classOfT) {
        if (classOfT == null) {
            return false;
        }
        for (LauncherComponent component : this.components) {
            if (classOfT.isInstance(component)) {
                return true;
            }
        }
        return false;
    }

    public <T> List<T> getComponentsOf(Class<T> classOfE) {
        List<T> list = new ArrayList<>();
        if (classOfE == null) {
            return list;
        }
        for (LauncherComponent component : this.components) {
            if (classOfE.isInstance(component)) {
                list.add(component);
            }
        }
        return list;
    }

    public void startAsyncRefresh() {
        this.refresher.iterate();
    }

    void startRefresh() {
        for (LauncherComponent component : this.components) {
            if (component instanceof RefreshableComponent) {
                RefreshableComponent interruptible = (RefreshableComponent) component;
                interruptible.refreshComponent();
            }
        }
    }

    public void stopRefresh() {
        for (LauncherComponent component : this.components) {
            if (component instanceof InterruptibleComponent) {
                InterruptibleComponent interruptible = (InterruptibleComponent) component;
                interruptible.stopRefresh();
            }
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ComponentManager$ComponentManagerRefresher.class */
    class ComponentManagerRefresher extends LoopedThread {
        private final ComponentManager parent;

        ComponentManagerRefresher(ComponentManager manager) {
            super("ComponentManagerRefresher");
            this.parent = manager;
        }

        @Override // org.tlauncher.util.async.LoopedThread
        protected void iterateOnce() {
            this.parent.startRefresh();
        }
    }

    public void setTlauncher(TLauncher tlauncher) {
        this.tlauncher = tlauncher;
    }
}
