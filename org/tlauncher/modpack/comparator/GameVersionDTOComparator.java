package org.tlauncher.modpack.comparator;

import java.util.Comparator;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.share.ForgeStringComparator;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/comparator/GameVersionDTOComparator.class */
public class GameVersionDTOComparator implements Comparator<GameVersionDTO> {
    private ForgeStringComparator f = new ForgeStringComparator();

    @Override // java.util.Comparator
    public int compare(GameVersionDTO o1, GameVersionDTO o2) {
        return this.f.compare(o1.getName(), o2.getName());
    }
}
