package org.tlauncher.tlauncher.component;

import org.tlauncher.tlauncher.managers.ComponentManager;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/component/LauncherComponent.class */
public abstract class LauncherComponent {
    protected final ComponentManager manager;

    public LauncherComponent(ComponentManager manager) throws Exception {
        if (manager == null) {
            throw new NullPointerException();
        }
        this.manager = manager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void log(Object... w) {
        U.log("[" + getClass().getSimpleName() + "]", w);
    }
}
