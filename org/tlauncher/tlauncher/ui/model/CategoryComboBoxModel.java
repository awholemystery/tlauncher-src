package org.tlauncher.tlauncher.ui.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import org.tlauncher.modpack.domain.client.share.CategoryDTO;
import org.tlauncher.tlauncher.ui.alert.Alert;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/model/CategoryComboBoxModel.class */
public class CategoryComboBoxModel extends DefaultComboBoxModel<CategoryDTO> {
    private static final long serialVersionUID = -216867993953483715L;
    private Set<CategoryDTO> set;

    public CategoryComboBoxModel(CategoryDTO[] items) {
        super(items);
        this.set = Collections.synchronizedSet(new HashSet());
    }

    public void setSelectedItem(Object anObject) {
        if (Objects.nonNull(anObject)) {
            CategoryDTO c = (CategoryDTO) anObject;
            if (c.getId().longValue() == 0) {
                this.set.clear();
            } else if (this.set.size() > 4 && !this.set.contains(c)) {
                SwingUtilities.invokeLater(() -> {
                    Alert.showLocMessageWithoutTitle("modpack.selected.so.much");
                });
                return;
            } else if (this.set.contains(c)) {
                this.set.remove(c);
            } else {
                this.set.add(c);
            }
            super.fireContentsChanged(this, 0, getSize());
        }
        super.setSelectedItem((Object) null);
    }

    public Set<CategoryDTO> getSelectedCategories() {
        return this.set;
    }

    public void cleanAllSelection() {
        this.set.clear();
    }
}
