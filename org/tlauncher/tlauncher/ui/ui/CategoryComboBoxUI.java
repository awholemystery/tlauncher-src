package org.tlauncher.tlauncher.ui.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.model.CategoryComboBoxModel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/CategoryComboBoxUI.class */
public class CategoryComboBoxUI extends CreationModpackComboBoxUI {
    @Override // org.tlauncher.tlauncher.ui.ui.CreationModpackComboBoxUI
    public String getText(Object value) {
        CategoryComboBoxModel ccbm = (CategoryComboBoxModel) value;
        if (!ccbm.getSelectedCategories().isEmpty()) {
            return Localizable.get("modpack.selected") + ": " + ccbm.getSelectedCategories().size();
        }
        return Localizable.get("modpack.all");
    }

    @Override // org.tlauncher.tlauncher.ui.ui.CreationModpackComboBoxUI
    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        g.setColor(Color.WHITE);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        paintText(g, bounds, getText(this.comboBox.getModel()));
    }
}
