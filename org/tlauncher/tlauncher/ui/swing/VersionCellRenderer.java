package org.tlauncher.tlauncher.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import net.minecraft.launcher.updater.LatestVersionSyncInfo;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.ReleaseType;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/VersionCellRenderer.class */
public class VersionCellRenderer implements ListCellRenderer<VersionSyncInfo> {
    private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    private final int averageColor = new Color(128, 128, 128, 255).getRGB();
    public static final VersionSyncInfo LOADING = VersionSyncInfo.createEmpty();
    public static final VersionSyncInfo EMPTY = VersionSyncInfo.createEmpty();
    public static final Color DARK_COLOR_TEXT = new Color(77, 77, 77);
    public static final Color OVER_ITEM = new Color(235, 235, 235);
    private static final Icon TLAUNCHER_ICON = ImageCache.getIcon("tlauncher-user.png");
    private static final Icon TLAUNCHER_USER_ICON_GRAY = ImageCache.getIcon("tlauncher-user-gray.png");

    public /* bridge */ /* synthetic */ Component getListCellRendererComponent(JList jList, Object obj, int i, boolean z, boolean z2) {
        return getListCellRendererComponent((JList<? extends VersionSyncInfo>) jList, (VersionSyncInfo) obj, i, z, z2);
    }

    public static String getLabelFor(VersionSyncInfo value) {
        String id;
        String label;
        LatestVersionSyncInfo asLatest = value instanceof LatestVersionSyncInfo ? (LatestVersionSyncInfo) value : null;
        ReleaseType type = value.getAvailableVersion().getReleaseType();
        if (asLatest == null) {
            id = value.getID();
            label = "version." + type;
        } else {
            id = asLatest.getVersionID();
            label = "version.latest." + type;
        }
        String label2 = Localizable.nget(label);
        if (type != null) {
            switch (type) {
                case OLD_ALPHA:
                    id = id.startsWith("a") ? id.substring(1) : id;
                    break;
                case OLD_BETA:
                    id = id.substring(1);
                    break;
            }
        }
        return label2 != null ? label2 + " " + id : id;
    }

    public Component getListCellRendererComponent(JList<? extends VersionSyncInfo> list, VersionSyncInfo value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel mainText = this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        mainText.setAlignmentY(0.5f);
        mainText.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
        mainText.setOpaque(true);
        if (isSelected) {
            mainText.setBackground(OVER_ITEM);
        } else {
            mainText.setBackground(Color.white);
        }
        mainText.setForeground(DARK_COLOR_TEXT);
        if (value == null) {
            mainText.setText("(null)");
            return mainText;
        }
        if (value.equals(LOADING)) {
            mainText.setText(Localizable.get("versions.loading"));
        } else if (value.equals(EMPTY)) {
            mainText.setText(Localizable.get("versions.notfound.tip"));
        } else {
            mainText.setText(getLabelFor(value));
            if (!value.isInstalled()) {
                mainText.setBackground(U.shiftColor(mainText.getBackground(), mainText.getBackground().getRGB() < this.averageColor ? 32 : -32));
            }
        }
        if (Objects.nonNull(value.getAvailableVersion())) {
            boolean skin = TLauncher.getInstance().getTLauncherManager().useTLauncherAccount(value.getAvailableVersion());
            boolean checkbox = TLauncher.getInstance().getConfiguration().getBoolean("skin.status.checkbox.state");
            if (skin && !checkbox) {
                mainText.setIcon(TLAUNCHER_USER_ICON_GRAY);
            } else if (skin) {
                mainText.setIcon(TLAUNCHER_ICON);
            }
        }
        return mainText;
    }

    public boolean getShowTLauncherVersions() {
        return false;
    }
}
