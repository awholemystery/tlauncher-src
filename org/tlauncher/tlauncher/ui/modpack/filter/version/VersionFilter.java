package org.tlauncher.tlauncher.ui.modpack.filter.version;

import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.ui.modpack.filter.Filter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/version/VersionFilter.class */
public abstract class VersionFilter implements Filter<VersionDTO> {
    protected GameEntityDTO entityDTO;
    protected GameType gameType;
    protected ModpackDTO modpackDTO;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.Filter
    public abstract boolean isProper(VersionDTO versionDTO);

    public VersionFilter(GameEntityDTO entityDTO, GameType gameType, ModpackDTO modpackDTO) {
        this.entityDTO = entityDTO;
        this.gameType = gameType;
        this.modpackDTO = modpackDTO;
    }
}
