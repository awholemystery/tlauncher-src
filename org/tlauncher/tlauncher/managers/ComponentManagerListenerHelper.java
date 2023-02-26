package org.tlauncher.tlauncher.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.tlauncher.tlauncher.component.LauncherComponent;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ComponentManagerListenerHelper.class */
public class ComponentManagerListenerHelper extends LauncherComponent implements Blockable, VersionManagerListener, TlauncherManagerListener {
    private final List<ComponentManagerListener> listeners;

    public ComponentManagerListenerHelper(ComponentManager manager) throws Exception {
        super(manager);
        this.listeners = Collections.synchronizedList(new ArrayList());
        ((VersionManager) manager.getComponent(VersionManager.class)).addListener(this);
    }

    public void addListener(ComponentManagerListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        this.listeners.add(listener);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshing(VersionManager manager) {
        Blocker.block(this, manager);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshingFailed(VersionManager manager) {
        Blocker.unblock(this, manager);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshed(VersionManager manager) {
        Blocker.unblock(this, manager);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        for (ComponentManagerListener listener : this.listeners) {
            listener.onComponentsRefreshing(this.manager);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        for (ComponentManagerListener listener : this.listeners) {
            listener.onComponentsRefreshed(this.manager);
        }
    }

    @Override // org.tlauncher.tlauncher.managers.TlauncherManagerListener
    public void onTlauncherUpdating(TLauncherManager manager) {
        Blocker.block(this, manager);
    }

    @Override // org.tlauncher.tlauncher.managers.TlauncherManagerListener
    public void onTlauncherUpdated(TLauncherManager manager) {
        Blocker.unblock(this, manager);
    }
}
