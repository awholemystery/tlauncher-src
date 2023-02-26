package org.tlauncher.tlauncher.ui.modpack.filter.version;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameEntityDependencyDTO;
import org.tlauncher.modpack.domain.client.share.DependencyType;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.ui.modpack.filter.GameEntityFilter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/version/IncompatibleFilter.class */
public class IncompatibleFilter implements GameEntityFilter {
    private Set<Long> modpackIncompatible;
    private Set<Long> modapckIds;

    public IncompatibleFilter(Set<Long> modpackIncompatible, Set<Long> modapckIds) {
        this.modpackIncompatible = modpackIncompatible;
        this.modapckIds = modapckIds;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.modpack.filter.GameEntityFilter, org.tlauncher.tlauncher.ui.modpack.filter.Filter
    public boolean isProper(GameEntityDTO entity) {
        if (this.modpackIncompatible.contains(entity.getId())) {
            return false;
        }
        if (entity.getDependencies() != null) {
            for (GameEntityDependencyDTO d : entity.getDependencies()) {
                if (d.getDependencyType() == DependencyType.REQUIRED && this.modpackIncompatible.contains(d.getGameEntityId())) {
                    return false;
                }
            }
        }
        Set<Long> set = new HashSet<>();
        ModpackUtil.extractIncompatible(entity, set);
        return Collections.disjoint(this.modapckIds, set);
    }
}
