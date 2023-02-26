package org.tlauncher.tlauncher.ui.listener.mods;

import javax.swing.JTable;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/mods/VersionsAdjustmentListener.class */
public class VersionsAdjustmentListener extends ModpackAdjustmentListener {
    private CompleteSubEntityScene.FullGameEntity fullGameEntity;

    public VersionsAdjustmentListener(JTable gerp, CompleteSubEntityScene.FullGameEntity fullGameEntity) {
        super(gerp);
        this.fullGameEntity = fullGameEntity;
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.ModpackAdjustmentListener
    public void processed() {
        if (!this.fullGameEntity.isProcessingRequest()) {
            this.fullGameEntity.fillVersions();
        }
    }
}
