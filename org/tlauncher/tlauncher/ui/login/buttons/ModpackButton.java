package org.tlauncher.tlauncher.ui.login.buttons;

import java.awt.Color;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.managers.VersionManagerListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/ModpackButton.class */
public class ModpackButton extends MainImageButton implements Blockable, VersionManagerListener {
    public ModpackButton(Color color, String image, String mouseUnderImage) {
        super(color, image, mouseUnderImage);
        addActionListener(e -> {
            ModpackScene scene = TLauncher.getInstance().getFrame().mp.modpackScene;
            AsyncThread.execute(() -> {
                U.debug("memory status before loading" + U.memoryStatus());
                ((ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class)).loadInfo();
                SwingUtilities.invokeLater(() -> {
                    TLauncher.getInstance().getFrame().mp.setScene(scene);
                });
            });
        });
        Blocker.block((Blockable) this, (Object) false);
        ((VersionManager) TLauncher.getInstance().getManager().getComponent(VersionManager.class)).addListener(this);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        setEnabled(false);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        setEnabled(true);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshing(VersionManager manager) {
        block(false);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshingFailed(VersionManager manager) {
        unblock(true);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshed(VersionManager manager) {
        unblock(true);
    }
}
