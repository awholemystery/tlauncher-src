package org.tlauncher.tlauncher.ui.modpack.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.ui.modpack.filter.version.GameVersionFilter;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/filter/BaseModpackFilter.class */
public class BaseModpackFilter<T> {
    private final List<Filter<T>> filters = new ArrayList();

    public BaseModpackFilter(Filter<T>... filters) {
        this.filters.addAll(Arrays.asList(filters));
    }

    public boolean isProper(T entity) {
        try {
            for (Filter<T> filter : this.filters) {
                if (!filter.isProper(entity)) {
                    return false;
                }
            }
            return true;
        } catch (NullPointerException t) {
            U.log(entity);
            throw t;
        }
    }

    public void addFilter(Filter<T> filter) {
        this.filters.add(filter);
    }

    public List<T> findAll(List<? extends T> gameEntities) {
        List<T> res = new ArrayList<>();
        for (T g : gameEntities) {
            if (isProper(g)) {
                res.add(g);
            }
        }
        return res;
    }

    public String toString() {
        return getClass().getName() + "{filters=" + this.filters + '}';
    }

    public static BaseModpackFilter<VersionDTO> getBaseModpackStandardFilters(GameEntityDTO entity, GameType type, ModpackComboBox modpackComboBox) {
        BaseModpackFilter<VersionDTO> filter = new BaseModpackFilter<>(new Filter[0]);
        if (modpackComboBox.getSelectedIndex() > 0 && type != GameType.MODPACK) {
            ModpackDTO modpackDTO = modpackComboBox.getSelectedValue().getModpack();
            return getBaseModpackStandardFilters(entity, type, modpackDTO);
        }
        return filter;
    }

    public static BaseModpackFilter<VersionDTO> getBaseModpackStandardFilters(GameEntityDTO entity, GameType type, ModpackDTO modpackDTO) {
        BaseModpackFilter<VersionDTO> filter = new BaseModpackFilter<>(new Filter[0]);
        filter.addFilter(new GameVersionFilter(entity, type, modpackDTO));
        return filter;
    }
}
