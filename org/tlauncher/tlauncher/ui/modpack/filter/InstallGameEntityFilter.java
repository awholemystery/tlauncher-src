package org.tlauncher.tlauncher.ui.modpack.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.ui.modpack.filter.version.IncompatibleFilter;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/InstallGameEntityFilter.class */
public class InstallGameEntityFilter extends BaseModpackFilter<GameEntityDTO> {
    private ModpackComboBox modpackComboBox;
    private GameType type;

    public InstallGameEntityFilter(ModpackComboBox modpackComboBox, GameType type, Filter<GameEntityDTO>... filters) {
        super(filters);
        this.modpackComboBox = modpackComboBox;
        this.type = type;
        if (modpackComboBox.getSelectedIndex() > 0 && type != GameType.MODPACK) {
            ModpackDTO m = ((CompleteVersion) modpackComboBox.getSelectedItem()).getModpack();
            ListGameEntityImpl subElementVersionFilter = new ListGameEntityImpl(type, m);
            Set<Long> incompatible = new HashSet<>();
            ModpackUtil.extractIncompatible(m, incompatible);
            GameType.getSubEntities().forEach(t -> {
                ((List) ((ModpackVersionDTO) m.getVersion()).getByType(t).stream().collect(Collectors.toList())).forEach(e -> {
                    ModpackUtil.extractIncompatible(e, incompatible);
                });
            });
            IncompatibleFilter filter = new IncompatibleFilter(incompatible, ModpackUtil.getAllModpackIds(m));
            addFilter(filter);
            addFilter(subElementVersionFilter);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter
    public boolean isProper(GameEntityDTO entity) {
        if (!super.isProper((InstallGameEntityFilter) entity)) {
            return false;
        }
        if (this.type != GameType.MODPACK && this.modpackComboBox.getSelectedIndex() > 0) {
            BaseModpackFilter<VersionDTO> filter = BaseModpackFilter.getBaseModpackStandardFilters(entity, this.type, this.modpackComboBox);
            return filter.findAll(entity.getVersions()).size() != 0;
        }
        return true;
    }
}
