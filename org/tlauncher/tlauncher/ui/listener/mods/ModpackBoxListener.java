package org.tlauncher.tlauncher.ui.listener.mods;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.login.VersionComboBox;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/mods/ModpackBoxListener.class */
public class ModpackBoxListener implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
        if (1 == e.getStateChange()) {
            CompleteVersion completeVersion = ((ModpackComboBox) e.getSource()).getSelectedValue();
            VersionComboBox versions = TLauncher.getInstance().getFrame().mp.defaultScene.loginForm.versions;
            for (int i = 0; i < versions.getModel().getSize(); i++) {
                VersionSyncInfo vsi = (VersionSyncInfo) versions.getModel().getElementAt(i);
                if (Objects.nonNull(vsi) && Objects.equals(vsi.getID(), completeVersion.getID())) {
                    versions.getModel().setSelectedItem(vsi);
                    return;
                }
            }
        }
    }
}
