package org.tlauncher.tlauncher.ui.modpack.filter.version;

import java.util.Comparator;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModVersionDTO;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/version/ForgeModFilter.class */
public class ForgeModFilter extends VersionFilter {
    public ForgeModFilter(GameEntityDTO entityDTO, GameType gameType, ModpackDTO modpackDTO) {
        super(entityDTO, gameType, modpackDTO);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.version.VersionFilter, org.tlauncher.tlauncher.ui.modpack.filter.Filter
    public boolean isProper(VersionDTO versionDTO) {
        if (this.gameType != GameType.MOD && (versionDTO instanceof ModVersionDTO)) {
            ModVersionDTO dto = (ModVersionDTO) versionDTO;
            ForgeComparator f = new ForgeComparator();
            String version = ((ModpackVersionDTO) this.modpackDTO.getVersion()).getForgeVersion();
            if (dto.getDownForge() != null && f.compare(dto.getDownForge(), version) > 0) {
                return false;
            }
            if (dto.getUpForge() != null && f.compare(version, dto.getUpForge()) == 1) {
                return false;
            }
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/version/ForgeModFilter$ForgeComparator.class */
    public class ForgeComparator implements Comparator<String> {
        ForgeComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String o1, String o2) {
            String[] versions1 = o1.split("-")[1].split("\\.");
            String[] versions2 = o2.split("-")[1].split("\\.");
            for (int i = 0; i < versions1.length; i++) {
                int res = versions1[i].compareTo(versions2[i]);
                if (res != 0) {
                    return res;
                }
            }
            return 0;
        }
    }
}
