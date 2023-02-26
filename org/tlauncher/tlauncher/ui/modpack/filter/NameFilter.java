package org.tlauncher.tlauncher.ui.modpack.filter;

import org.tlauncher.modpack.domain.client.GameEntityDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/NameFilter.class */
public class NameFilter implements GameEntityFilter {
    private String name;

    public NameFilter(String name) {
        this.name = name;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.GameEntityFilter, org.tlauncher.tlauncher.ui.modpack.filter.Filter
    public boolean isProper(GameEntityDTO gameEntity) {
        return gameEntity.getName().toLowerCase().contains(this.name.toLowerCase());
    }
}
