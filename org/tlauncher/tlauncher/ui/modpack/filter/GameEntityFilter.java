package org.tlauncher.tlauncher.ui.modpack.filter;

import org.tlauncher.modpack.domain.client.GameEntityDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/GameEntityFilter.class */
public interface GameEntityFilter extends Filter<GameEntityDTO> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.Filter
    boolean isProper(GameEntityDTO gameEntityDTO);
}
