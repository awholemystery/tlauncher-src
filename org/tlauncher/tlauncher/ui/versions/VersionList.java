package org.tlauncher.tlauncher.ui.versions;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.server.BackPanel;
import org.tlauncher.tlauncher.ui.swing.ImageButton;
import org.tlauncher.tlauncher.ui.swing.ScrollPane;
import org.tlauncher.tlauncher.ui.swing.SimpleListModel;
import org.tlauncher.tlauncher.ui.swing.VersionCellRenderer;
import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionList.class */
public class VersionList extends CenterPanel implements VersionHandlerListener {
    private static final long serialVersionUID = -7192156096621636270L;
    final VersionHandler handler;
    public final SimpleListModel<VersionSyncInfo> model;
    public final JList<VersionSyncInfo> list;
    VersionDownloadButton download;
    VersionRemoveButton remove;
    public final ImageButton refresh;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionList(VersionHandler h) {
        super(squareInsets);
        this.handler = h;
        BorderPanel panel = new BorderPanel(0, 0);
        BackPanel backPanel = new BackPanel("version.manager.list", new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.versions.VersionList.1
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    VersionList.this.handler.exitEditor();
                }
            }
        }, ImageCache.getIcon("back-arrow.png"));
        panel.setNorth(backPanel);
        this.model = new SimpleListModel<>();
        this.list = new JList<>(this.model);
        this.list.setCellRenderer(new VersionListCellRenderer(this));
        this.list.setSelectionMode(2);
        this.list.addListSelectionListener(new ListSelectionListener() { // from class: org.tlauncher.tlauncher.ui.versions.VersionList.2
            public void valueChanged(ListSelectionEvent e) {
                VersionList.this.handler.onVersionSelected(VersionList.this.list.getSelectedValuesList());
            }
        });
        ScrollPane pane = new ScrollPane((Component) this.list, ScrollPane.ScrollBarPolicy.AS_NEEDED, ScrollPane.ScrollBarPolicy.NEVER);
        pane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        pane.getVerticalScrollBar().setUI(new VersionScrollBarUI());
        panel.setCenter(pane);
        ExtendedPanel buttons = new ExtendedPanel((LayoutManager) new GridLayout(0, 3));
        buttons.setInsets(0, -1, 0, -1);
        this.refresh = new VersionRefreshButton(this);
        buttons.add((Component) this.refresh);
        this.download = new VersionDownloadButton(this);
        buttons.add((Component) this.download);
        this.remove = new VersionRemoveButton(this);
        buttons.add((Component) this.remove);
        panel.setSouth(buttons);
        add((Component) panel);
        this.handler.addListener(this);
        setSize(500, HttpStatus.SC_BAD_REQUEST);
    }

    void select(List<VersionSyncInfo> list) {
        if (list == null) {
            return;
        }
        int size = list.size();
        int[] indexes = new int[list.size()];
        for (int i = 0; i < size; i++) {
            indexes[i] = this.model.indexOf(list.get(i));
        }
        this.list.setSelectedIndices(indexes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deselect() {
        this.list.clearSelection();
    }

    void refreshFrom(VersionManager manager) {
        setRefresh(false);
        List<VersionSyncInfo> list = manager.getVersions(null, false);
        this.model.addAll(list);
    }

    void setRefresh(boolean refresh) {
        this.model.clear();
        if (refresh) {
            this.model.add(VersionCellRenderer.LOADING);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.BlockablePanel, org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.blockComponents(reason, this.list, this.refresh, this.remove);
    }

    @Override // org.tlauncher.tlauncher.ui.block.BlockablePanel, org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents(reason, this.list, this.refresh, this.remove);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshing(VersionManager vm) {
        setRefresh(true);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionRefreshed(VersionManager vm) {
        refreshFrom(vm);
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionSelected(List<VersionSyncInfo> version) {
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDeselected() {
    }

    @Override // org.tlauncher.tlauncher.ui.versions.VersionHandlerListener
    public void onVersionDownload(List<VersionSyncInfo> list) {
        select(list);
    }
}
