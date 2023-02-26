package org.tlauncher.tlauncher.ui.versions;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPopupMenu;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.downloader.AbortedDownloadException;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.managers.VersionSyncInfoContainer;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Unblockable;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionDownloadButton.class */
public class VersionDownloadButton extends ImageUdaterButton implements VersionHandlerListener, Unblockable {
    private static final String SELECTION_BLOCK = "selection";
    private static final String PREFIX = "version.manager.downloader.";
    private static final String WARNING = "version.manager.downloader.warning.";
    private static final String WARNING_TITLE = "version.manager.downloader.warning.title";
    private static final String WARNING_FORCE = "version.manager.downloader.warning.force.";
    private static final String ERROR = "version.manager.downloader.error.";
    private static final String ERROR_TITLE = "version.manager.downloader.error.title";
    private static final String INFO = "version.manager.downloader.info.";
    private static final String INFO_TITLE = "version.manager.downloader.info.title";
    private static final String MENU = "version.manager.downloader.menu.";
    final VersionHandler handler;
    final Blockable blockable;
    private final JPopupMenu menu;
    private final LocalizableMenuItem ordinary;
    private final LocalizableMenuItem force;
    private ButtonState state;
    private boolean downloading;
    private boolean aborted;
    boolean forceDownload;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionDownloadButton(VersionList list) {
        super(Color.WHITE);
        this.handler = list.handler;
        this.blockable = new Blockable() { // from class: org.tlauncher.tlauncher.ui.versions.VersionDownloadButton.1
            @Override // org.tlauncher.tlauncher.ui.block.Blockable
            public void block(Object reason) {
                VersionDownloadButton.this.setEnabled(false);
            }

            @Override // org.tlauncher.tlauncher.ui.block.Blockable
            public void unblock(Object reason) {
                VersionDownloadButton.this.setEnabled(true);
            }
        };
        this.menu = new JPopupMenu();
        this.ordinary = new LocalizableMenuItem("version.manager.downloader.menu.ordinary");
        this.ordinary.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionDownloadButton.2
            public void actionPerformed(ActionEvent e) {
                VersionDownloadButton.this.forceDownload = false;
                VersionDownloadButton.this.onDownloadCalled();
            }
        });
        this.menu.add(this.ordinary);
        this.force = new LocalizableMenuItem("version.manager.downloader.menu.force");
        this.force.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionDownloadButton.3
            public void actionPerformed(ActionEvent e) {
            }
        });
        this.menu.add(this.force);
        addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionDownloadButton.4
            public void actionPerformed(ActionEvent e) {
                VersionDownloadButton.this.onPressed();
            }
        });
        setState(ButtonState.DOWNLOAD);
        this.handler.addListener(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setState(ButtonState state) {
        if (state == null) {
            throw new NullPointerException();
        }
        this.state = state;
        setImage(state.image);
    }

    void onPressed() {
        switch (this.state) {
            case DOWNLOAD:
                this.forceDownload = true;
                onDownloadCalled();
                return;
            case STOP:
                onStopCalled();
                return;
            default:
                return;
        }
    }

    void onDownloadPressed() {
        this.menu.show(this, 0, getHeight());
    }

    void onDownloadCalled() {
        if (this.state != ButtonState.DOWNLOAD) {
            throw new IllegalStateException();
        }
        this.handler.thread.startThread.iterate();
    }

    void onStopCalled() {
        if (this.state != ButtonState.STOP) {
            throw new IllegalStateException();
        }
        this.handler.thread.stopThread.iterate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startDownload() {
        String suffix;
        Object var;
        this.aborted = false;
        List<VersionSyncInfo> list = this.handler.getSelectedList();
        if (list == null || list.isEmpty()) {
            return;
        }
        int countLocal = 0;
        VersionSyncInfo local = null;
        for (VersionSyncInfo version : list) {
            if (this.forceDownload) {
                if (!version.hasRemote()) {
                    Alert.showError(Localizable.get(ERROR_TITLE), Localizable.get("version.manager.downloader.error.local", version.getID()));
                    return;
                } else if (version.isUpToDate() && version.isInstalled()) {
                    countLocal++;
                    local = version;
                }
            }
        }
        if (countLocal > 0) {
            String title = Localizable.get(WARNING_TITLE);
            if (countLocal == 1) {
                suffix = "single";
                var = local.getID();
            } else {
                suffix = "multiply";
                var = Integer.valueOf(countLocal);
            }
            if (!Alert.showQuestion(title, Localizable.get(WARNING_FORCE + suffix, var))) {
                return;
            }
        }
        List<VersionSyncInfoContainer> containers = new ArrayList<>();
        VersionManager manager = TLauncher.getInstance().getVersionManager();
        try {
            this.downloading = true;
            for (VersionSyncInfo version2 : list) {
                try {
                    version2.resolveCompleteVersion(manager, this.forceDownload);
                    VersionSyncInfoContainer container = manager.downloadVersion(version2, false, this.forceDownload);
                    if (this.aborted) {
                        this.downloading = false;
                        return;
                    } else if (!container.getList().isEmpty()) {
                        containers.add(container);
                    }
                } catch (Exception e) {
                    Alert.showError(Localizable.get(ERROR_TITLE), Localizable.get("version.manager.downloader.error.getting", version2.getID()), e);
                    this.downloading = false;
                    return;
                }
            }
            if (containers.isEmpty()) {
                Alert.showMessage(Localizable.get(INFO_TITLE), Localizable.get("version.manager.downloader.info.no-needed"));
                this.downloading = false;
                return;
            }
            if (containers.size() > 1) {
                DownloadableContainer.removeDuplicates(containers);
            }
            if (this.aborted) {
                this.downloading = false;
                return;
            }
            for (VersionSyncInfoContainer c : containers) {
                this.handler.downloader.add(c);
            }
            this.handler.downloading = list;
            this.handler.onVersionDownload(list);
            this.handler.downloader.startDownloadAndWait();
            this.downloading = false;
            this.handler.downloading.clear();
            for (VersionSyncInfoContainer container2 : containers) {
                List<Throwable> errors = container2.getErrors();
                VersionSyncInfo version3 = container2.getVersion();
                if (errors.isEmpty()) {
                    try {
                        manager.getLocalList().saveVersion(version3.getCompleteVersion(this.forceDownload));
                    } catch (IOException e2) {
                        Alert.showError(Localizable.get(ERROR_TITLE), Localizable.get("version.manager.downloader.error.saving", version3.getID()), e2);
                        return;
                    }
                } else if (!(errors.get(0) instanceof AbortedDownloadException)) {
                    Alert.showError(Localizable.get(ERROR_TITLE), Localizable.get("version.manager.downloader.error.downloading", version3.getID()), errors);
                }
            }
            this.handler.refresh();
        } catch (Throwable th) {
            this.downloading = false;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopDownload() {
        this.aborted = true;
        if (this.downloading) {
            this.handler.downloader.stopDownloadAndWait();
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionDownloadButton$ButtonState.class */
    public enum ButtonState {
        DOWNLOAD("down.png"),
        STOP("cancel.png");
        
        final Image image;

        ButtonState(String image) {
            this.image = ImageCache.getImage(image);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshing(VersionManager vm) {
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshed(VersionManager vm) {
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionSelected(List<VersionSyncInfo> versions) {
        if (!this.downloading) {
            this.blockable.unblock(SELECTION_BLOCK);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDeselected() {
        if (!this.downloading) {
            this.blockable.block(SELECTION_BLOCK);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDownload(List<VersionSyncInfo> list) {
    }
}
