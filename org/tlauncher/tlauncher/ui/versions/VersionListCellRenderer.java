package org.tlauncher.tlauncher.ui.versions;

import java.awt.Color;
import java.awt.Component;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JList;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.images.ImageIcon;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.swing.VersionCellRenderer;
import org.tlauncher.tlauncher.ui.swing.border.VersionBorder;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionListCellRenderer.class */
public class VersionListCellRenderer extends VersionCellRenderer {
    private final VersionHandler handler;
    private final ImageIcon downloading = ImageCache.getIcon("down.png", 16, 16);
    private static final Color FOREGROUND_COLOR = new Color(76, 75, 74);
    private static final Color BACKGROUND_COLOR = new Color(235, 235, 235);

    @Override // org.tlauncher.tlauncher.ui.swing.VersionCellRenderer
    public /* bridge */ /* synthetic */ Component getListCellRendererComponent(JList jList, Object obj, int i, boolean z, boolean z2) {
        return getListCellRendererComponent((JList<? extends VersionSyncInfo>) jList, (VersionSyncInfo) obj, i, z, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionListCellRenderer(VersionList list) {
        this.handler = list.handler;
    }

    @Override // org.tlauncher.tlauncher.ui.swing.VersionCellRenderer
    public Component getListCellRendererComponent(JList<? extends VersionSyncInfo> list, VersionSyncInfo value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            return null;
        }
        JLabel label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (!isSelected) {
            if (value.isInstalled()) {
                label.setBackground(Color.WHITE);
            } else {
                label.setBackground(BACKGROUND_COLOR);
            }
        }
        SwingUtil.changeFontFamily(label, FontTL.ROBOTO_BOLD, 14, FOREGROUND_COLOR);
        label.setBorder(new VersionBorder(10, 20, 10, 0, VersionBorder.SEPARATOR_COLOR));
        if (value.isInstalled() && !value.isUpToDate()) {
            label.setText(label.getText() + ' ' + Localizable.get("version.list.needsupdate"));
        }
        if (this.handler.downloading != null) {
            Iterator<VersionSyncInfo> it = this.handler.downloading.iterator();
            if (it.hasNext()) {
                VersionSyncInfo compare = it.next();
                ImageIcon icon = compare.equals(value) ? this.downloading : null;
                label.setIcon(icon);
                label.setDisabledIcon(icon);
            }
        }
        return label;
    }
}
