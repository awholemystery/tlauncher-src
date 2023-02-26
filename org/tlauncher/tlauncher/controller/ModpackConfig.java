package org.tlauncher.tlauncher.controller;

import com.google.inject.Inject;
import java.util.Objects;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.share.ForgeStringComparator;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/controller/ModpackConfig.class */
public class ModpackConfig {
    @Inject
    private ModpackManager manager;

    public GameVersionDTO findGameVersion(String currentGameVersion) {
        for (GameVersionDTO dto : this.manager.getInfoMod().getGameVersions()) {
            if (dto.getName().equalsIgnoreCase(currentGameVersion) && dto.getForgeVersions().size() > 0) {
                dto.getForgeVersions().sort(new ForgeStringComparator());
                return dto;
            }
        }
        return null;
    }

    public void save(CompleteVersion version, String modpackName, boolean tlSkinCapeModBox, NameIdDTO ver) {
        if (!version.getID().equals(modpackName)) {
            this.manager.renameModpack(version, modpackName);
        } else {
            this.manager.resaveVersion(version);
        }
        ModpackVersionDTO mv = (ModpackVersionDTO) version.getModpack().getVersion();
        if (ModpackUtil.useSkinMod(version) && !tlSkinCapeModBox) {
            ModDTO m = new ModDTO();
            m.setId(ModDTO.TL_SKIN_CAPE_ID);
            this.manager.removeEntity(m, m.getVersion(), GameType.MOD, true);
        } else if (!ModpackUtil.useSkinMod(version) && tlSkinCapeModBox) {
            this.manager.installTLSkinCapeMod(mv);
        }
        if (Objects.nonNull(ver) && !Objects.equals(ver, mv.getMinecraftVersionName())) {
            try {
                this.manager.getGameVersion(mv);
                CompleteVersion v = this.manager.getCompleteVersionByMinecraftVersionTypeAndId(mv.getMinecraftVersionTypes().stream().findFirst().get(), ver);
                v.setSkinVersion(version.isSkinVersion());
                v.setID(version.getID());
                v.setModpackDTO(version.getModpack());
                ((ModpackVersionDTO) v.getModpack().getVersion()).setMinecraftVersionName(ver);
                this.manager.resaveVersionWithNewForge(v);
            } catch (Exception e) {
                U.log(e);
            }
        }
    }

    public void open(CompleteVersion version) {
        this.manager.openModpackFolder(version);
    }

    public void remove(CompleteVersion version) {
        this.manager.removeEntity(version.getModpack(), version.getModpack().getVersion(), GameType.MODPACK, false);
    }
}
