package org.tlauncher.tlauncher.ui.listener.mods;

import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/mods/GameEntityListener.class */
public interface GameEntityListener {
    void activationStarted(GameEntityDTO gameEntityDTO);

    void activation(GameEntityDTO gameEntityDTO);

    void activationError(GameEntityDTO gameEntityDTO, Throwable th);

    void processingStarted(GameEntityDTO gameEntityDTO, VersionDTO versionDTO);

    void installEntity(GameEntityDTO gameEntityDTO, GameType gameType);

    void installEntity(CompleteVersion completeVersion);

    void removeEntity(GameEntityDTO gameEntityDTO);

    void removeCompleteVersion(CompleteVersion completeVersion);

    void installError(GameEntityDTO gameEntityDTO, VersionDTO versionDTO, Throwable th);

    void populateStatus(GameEntityDTO gameEntityDTO, GameType gameType, boolean z);

    void updateVersion(CompleteVersion completeVersion, CompleteVersion completeVersion2);

    default void updateVersionStorageAndScene(CompleteVersion v, CompleteVersion newVersion) {
    }
}
