package org.tlauncher.tlauncher.ui.listener.mods;

import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/mods/GameEntityPanel.class */
public class GameEntityPanel extends ExtendedPanel implements GameEntityListener {
    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activationStarted(GameEntityDTO e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activation(GameEntityDTO e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void activationError(GameEntityDTO e, Throwable t) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void processingStarted(GameEntityDTO e, VersionDTO version) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installEntity(GameEntityDTO e, GameType type) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installEntity(CompleteVersion e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void removeEntity(GameEntityDTO e) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void populateStatus(GameEntityDTO status, GameType type, boolean state) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void updateVersion(CompleteVersion v, CompleteVersion newVersion) {
    }

    @Override // org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener
    public void removeCompleteVersion(CompleteVersion e) {
    }
}
