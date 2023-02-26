package org.tlauncher.tlauncher.ui.swing.renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.CompoundBorder;
import org.tlauncher.modpack.domain.client.share.CategoryDTO;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/CategoryListRenderer.class */
public class CategoryListRenderer extends ModpackComboBoxRendererBasic {
    private JComboBox<CategoryDTO> categoriesBox;

    public CategoryListRenderer(JComboBox<CategoryDTO> categoriesBox) {
        this.categoriesBox = categoriesBox;
    }

    @Override // org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboBoxRendererBasic
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            return null;
        }
        Set<CategoryDTO> set = this.categoriesBox.getModel().getSelectedCategories();
        CategoryDTO cat = (CategoryDTO) value;
        JComponent c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (set.contains(cat)) {
            c.setBackground(new Color(225, 225, 225));
        }
        if (cat.getNesting() != 0) {
            CompoundBorder border = new CompoundBorder(c.getBorder(), BorderFactory.createEmptyBorder(0, cat.getNesting() == 1 ? 15 : 30, 0, 0));
            c.setBorder(border);
        }
        return c;
    }

    @Override // org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboBoxRendererBasic
    public String getRenderText(Object value) {
        CategoryDTO cat = (CategoryDTO) value;
        return Localizable.get("modpack." + cat.getName());
    }
}
