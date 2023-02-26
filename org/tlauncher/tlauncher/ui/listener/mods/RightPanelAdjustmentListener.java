package org.tlauncher.tlauncher.ui.listener.mods;

import org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/mods/RightPanelAdjustmentListener.class */
public class RightPanelAdjustmentListener extends ModpackAdjustmentListener {
    private ModpackScene scene;

    public RightPanelAdjustmentListener(GameEntityRightPanel gerp, ModpackScene scene) {
        super(gerp);
        this.scene = scene;
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.ModpackAdjustmentListener
    public void processed() {
        GameEntityRightPanel gerp = (GameEntityRightPanel) getTable();
        if (!gerp.isProcessingRequest()) {
            this.scene.fillGameEntitiesPanel(false);
        }
    }
}
