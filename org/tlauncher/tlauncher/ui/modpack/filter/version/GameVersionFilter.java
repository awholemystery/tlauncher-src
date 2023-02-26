package org.tlauncher.tlauncher.ui.modpack.filter.version;

import java.util.Objects;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/version/GameVersionFilter.class */
public class GameVersionFilter extends VersionFilter {
    public GameVersionFilter(GameEntityDTO entityDTO, GameType gameType, ModpackDTO modpackDTO) {
        super(entityDTO, gameType, modpackDTO);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.version.VersionFilter, org.tlauncher.tlauncher.ui.modpack.filter.Filter
    public boolean isProper(VersionDTO versionDTO) {
        GameVersionDTO gvdto = ((ModpackVersionDTO) this.modpackDTO.getVersion()).getGameVersionDTO();
        if (Objects.isNull(gvdto) || versionDTO.getGameVersionsDTO().isEmpty()) {
            return true;
        }
        return versionDTO.getGameVersionsDTO().stream().filter(e -> {
            return e.getId().equals(gvdto.getId());
        }).findAny().isPresent();
    }
}
