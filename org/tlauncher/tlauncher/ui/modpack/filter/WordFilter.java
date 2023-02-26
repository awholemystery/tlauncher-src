package org.tlauncher.tlauncher.ui.modpack.filter;

import org.tlauncher.modpack.domain.client.GameEntityDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/WordFilter.class */
public class WordFilter extends NameFilter {
    private final String word;

    public WordFilter(String word) {
        super(word);
        this.word = word;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.NameFilter, org.tlauncher.tlauncher.ui.modpack.filter.GameEntityFilter, org.tlauncher.tlauncher.ui.modpack.filter.Filter
    public boolean isProper(GameEntityDTO gameEntity) {
        if (super.isProper(gameEntity) || gameEntity.getShortDescription().toLowerCase().contains(this.word.toLowerCase())) {
            return true;
        }
        return false;
    }
}
