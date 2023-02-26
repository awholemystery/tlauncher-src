package org.tlauncher.tlauncher.ui.modpack.filter;

import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.Category;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/CategoryFilter.class */
public class CategoryFilter implements GameEntityFilter {
    private Category category;

    public CategoryFilter(Category category) {
        this.category = category;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.GameEntityFilter, org.tlauncher.tlauncher.ui.modpack.filter.Filter
    public boolean isProper(GameEntityDTO gameEntity) {
        return true;
    }
}
