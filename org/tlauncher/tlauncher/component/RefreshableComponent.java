package org.tlauncher.tlauncher.component;

import org.tlauncher.tlauncher.managers.ComponentManager;
import org.tlauncher.util.async.AsyncThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/component/RefreshableComponent.class */
public abstract class RefreshableComponent extends LauncherComponent {
    protected abstract boolean refresh();

    public RefreshableComponent(ComponentManager manager) throws Exception {
        super(manager);
    }

    public boolean refreshComponent() {
        return refresh();
    }

    public void asyncRefresh() {
        AsyncThread.execute(this::refresh);
    }
}
