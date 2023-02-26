package org.tlauncher.tlauncher.ui.versions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JPopupMenu;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionRefreshButton.class */
public class VersionRefreshButton extends ImageUdaterButton implements VersionHandlerListener, Blockable {
    private static final long serialVersionUID = -7148657244927244061L;
    private static final String PREFIX = "version.manager.refresher.";
    private static final String MENU = "version.manager.refresher.menu.";
    final VersionHandler handler;
    private final JPopupMenu menu;
    private final LocalizableMenuItem local;
    private final LocalizableMenuItem remote;
    private ButtonState state;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionRefreshButton(VersionList list) {
        super(GREEN_COLOR);
        this.handler = list.handler;
        this.menu = new JPopupMenu();
        this.local = new LocalizableMenuItem("version.manager.refresher.menu.local");
        this.local.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionRefreshButton.1
            public void actionPerformed(ActionEvent e) {
                VersionRefreshButton.this.handler.refresh();
            }
        });
        this.menu.add(this.local);
        this.remote = new LocalizableMenuItem("version.manager.refresher.menu.remote");
        this.remote.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionRefreshButton.2
            public void actionPerformed(ActionEvent e) {
                VersionRefreshButton.this.handler.asyncRefresh();
            }
        });
        this.menu.add(this.remote);
        addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionRefreshButton.3
            public void actionPerformed(ActionEvent e) {
                VersionRefreshButton.this.handler.asyncRefresh();
            }
        });
        setState(ButtonState.REFRESH);
        this.handler.addListener(this);
    }

    void onPressed() {
        switch (this.state) {
            case CANCEL:
                this.handler.stopRefresh();
                return;
            case REFRESH:
                this.menu.show(this, 0, getHeight());
                return;
            default:
                return;
        }
    }

    private void setState(ButtonState state) {
        if (state == null) {
            throw new NullPointerException();
        }
        this.state = state;
        setImage(state.image);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshing(VersionManager vm) {
        setState(ButtonState.CANCEL);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshed(VersionManager vm) {
        setState(ButtonState.REFRESH);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionSelected(List<VersionSyncInfo> versions) {
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDeselected() {
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDownload(List<VersionSyncInfo> list) {
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        if (!reason.equals("refresh")) {
            setEnabled(false);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        setEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionRefreshButton$ButtonState.class */
    public enum ButtonState {
        REFRESH("refresh.png"),
        CANCEL("cancel.png");
        
        final Image image;

        ButtonState(String image) {
            this.image = ImageCache.getImage(image);
        }
    }
}
