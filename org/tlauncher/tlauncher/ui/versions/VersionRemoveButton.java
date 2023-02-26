package org.tlauncher.tlauncher.ui.versions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPopupMenu;
import net.minecraft.launcher.updater.LocalVersionList;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionRemoveButton.class */
public class VersionRemoveButton extends ImageUdaterButton implements VersionHandlerListener, Blockable {
    private static final long serialVersionUID = 427368162418879141L;
    private static final String ILLEGAL_SELECTION_BLOCK = "illegal-selection";
    private static final String PREFIX = "version.manager.delete.";
    private static final String ERROR = "version.manager.delete.error.";
    private static final String ERROR_TITLE = "version.manager.delete.error.title";
    private static final String MENU = "version.manager.delete.menu.";
    private final VersionHandler handler;
    private final JPopupMenu menu;
    private final LocalizableMenuItem onlyJar;
    private final LocalizableMenuItem withLibraries;
    private boolean libraries;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionRemoveButton(VersionList list) {
        super(Color.WHITE, "delete-version.png");
        this.handler = list.handler;
        this.handler.addListener(this);
        this.menu = new JPopupMenu();
        this.onlyJar = new LocalizableMenuItem("version.manager.delete.menu.jar");
        this.onlyJar.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionRemoveButton.1
            public void actionPerformed(ActionEvent e) {
                VersionRemoveButton.this.onChosen(false);
            }
        });
        this.menu.add(this.onlyJar);
        this.withLibraries = new LocalizableMenuItem("version.manager.delete.menu.libraries");
        this.withLibraries.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionRemoveButton.2
            public void actionPerformed(ActionEvent e) {
                VersionRemoveButton.this.onChosen(true);
            }
        });
        this.menu.add(this.withLibraries);
        addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionRemoveButton.3
            public void actionPerformed(ActionEvent e) {
                VersionRemoveButton.this.onChosen(false);
            }
        });
    }

    void onPressed() {
        this.menu.show(this, 0, getHeight());
    }

    void onChosen(boolean removeLibraries) {
        this.libraries = removeLibraries;
        this.handler.thread.deleteThread.iterate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void delete() {
        if (this.handler.selected != null) {
            LocalVersionList localList = this.handler.vm.getLocalList();
            List<Throwable> errors = new ArrayList<>();
            for (VersionSyncInfo version : this.handler.selected) {
                if (version.isInstalled()) {
                    try {
                        localList.deleteVersion(version.getID(), this.libraries);
                    } catch (Throwable e) {
                        errors.add(e);
                    }
                }
            }
            if (!errors.isEmpty()) {
                String title = Localizable.get(ERROR_TITLE);
                String message = Localizable.get(ERROR + (errors.size() == 1 ? "single" : "multiply"), errors);
                Alert.showError(title, message);
            }
        }
        this.handler.refresh();
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshing(VersionManager vm) {
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshed(VersionManager vm) {
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionSelected(List<VersionSyncInfo> versions) {
        boolean onlyRemote = true;
        Iterator<VersionSyncInfo> it = versions.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            VersionSyncInfo version = it.next();
            if (version.isInstalled()) {
                onlyRemote = false;
                break;
            }
        }
        Blocker.setBlocked(this, ILLEGAL_SELECTION_BLOCK, onlyRemote);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDeselected() {
        Blocker.block(this, ILLEGAL_SELECTION_BLOCK);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDownload(List<VersionSyncInfo> list) {
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        setEnabled(false);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        setEnabled(true);
    }
}
