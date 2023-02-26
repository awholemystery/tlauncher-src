package org.tlauncher.tlauncher.ui.versions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.managers.VersionManagerListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.scenes.VersionManagerScene;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionHandler.class */
public class VersionHandler implements Blockable, VersionHandlerListener {
    static final int ELEM_WIDTH = 500;
    public static final String REFRESH_BLOCK = "refresh";
    public static final String SINGLE_SELECTION_BLOCK = "single-select";
    public static final String START_DOWNLOAD = "start-download";
    public static final String STOP_DOWNLOAD = "stop-download";
    public static final String DELETE_BLOCK = "deleting";
    public final VersionManagerScene scene;
    final VersionHandlerThread thread;
    public final VersionList list;
    final VersionManager vm;
    final Downloader downloader;
    List<VersionSyncInfo> selected;
    private final VersionHandler instance = this;
    private final List<VersionHandlerListener> listeners = Collections.synchronizedList(new ArrayList());
    List<VersionSyncInfo> downloading = Collections.synchronizedList(new ArrayList());

    public VersionHandler(VersionManagerScene scene) {
        this.scene = scene;
        TLauncher launcher = TLauncher.getInstance();
        this.vm = launcher.getVersionManager();
        this.downloader = launcher.getDownloader();
        this.list = new VersionList(this);
        this.thread = new VersionHandlerThread(this);
        this.vm.addListener(new VersionManagerListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionHandler.1
            @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
            public void onVersionsRefreshing(VersionManager manager) {
                VersionHandler.this.instance.onVersionRefreshing(manager);
            }

            @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
            public void onVersionsRefreshed(VersionManager manager) {
                VersionHandler.this.instance.onVersionRefreshed(manager);
            }

            @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
            public void onVersionsRefreshingFailed(VersionManager manager) {
                onVersionsRefreshed(manager);
            }
        });
        onVersionDeselected();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addListener(VersionHandlerListener listener) {
        this.listeners.add(listener);
    }

    void update() {
        if (this.selected != null) {
            onVersionSelected(this.selected);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refresh() {
        this.vm.startRefresh(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void asyncRefresh() {
        this.vm.asyncRefresh();
    }

    public void stopRefresh() {
        this.vm.stopRefresh();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void exitEditor() {
        this.list.deselect();
        this.scene.getMainPane().openDefaultScene();
    }

    VersionSyncInfo getSelected() {
        if (this.selected == null || this.selected.size() != 1) {
            return null;
        }
        return this.selected.get(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<VersionSyncInfo> getSelectedList() {
        return this.selected;
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.block(reason, this.list, this.scene.getMainPane().defaultScene);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblock(reason, this.list, this.scene.getMainPane().defaultScene);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshing(VersionManager vm) {
        Blocker.block(this.instance, "refresh");
        for (VersionHandlerListener listener : this.listeners) {
            listener.onVersionRefreshing(vm);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshed(VersionManager vm) {
        Blocker.unblock(this.instance, "refresh");
        for (VersionHandlerListener listener : this.listeners) {
            listener.onVersionRefreshed(vm);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionSelected(List<VersionSyncInfo> version) {
        this.selected = version;
        if (version == null || version.isEmpty() || version.get(0).getID() == null) {
            onVersionDeselected();
            return;
        }
        for (VersionHandlerListener listener : this.listeners) {
            listener.onVersionSelected(version);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDeselected() {
        this.selected = null;
        for (VersionHandlerListener listener : this.listeners) {
            listener.onVersionDeselected();
        }
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDownload(List<VersionSyncInfo> list) {
        this.downloading = list;
        for (VersionHandlerListener listener : this.listeners) {
            listener.onVersionDownload(list);
        }
    }
}
