package org.tlauncher.tlauncher.ui.modpack.filter;

import java.util.Collections;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/ListGameEntityImpl.class */
public class ListGameEntityImpl implements GameEntityFilter {
    private GameType gameType;
    private ModpackDTO modpackDTO;

    public ListGameEntityImpl(GameType gameType, ModpackDTO modpackDTO) {
        this.gameType = gameType;
        this.modpackDTO = modpackDTO;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.GameEntityFilter, org.tlauncher.tlauncher.ui.modpack.filter.Filter
    public boolean isProper(GameEntityDTO gameEntity) {
        for (VersionDTO v : gameEntity.getVersions()) {
            if (v.getGameVersions() != null && this.modpackDTO.getVersion().getGameVersions() != null && Collections.disjoint(v.getGameVersions(), this.modpackDTO.getVersion().getGameVersions())) {
                return true;
            }
        }
        return true;
    }
}
