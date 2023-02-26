package org.tlauncher.tlauncher.ui.menu;

import java.awt.Color;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import org.tlauncher.modpack.domain.client.share.CategoryDTO;
import org.tlauncher.tlauncher.ui.menu.ModpackPopup;
import org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/menu/ModpackCategoryPopupMenu.class */
public class ModpackCategoryPopupMenu extends JPopupMenu {
    public ModpackCategoryPopupMenu(CategoryDTO c, JLabel label) {
        if (Objects.isNull(c)) {
            return;
        }
        setBorder(BorderFactory.createLineBorder(ModpackComboxRenderer.LINE));
        ModpackPopup.ModpackMenuItem localizableMenuItem = new ModpackPopup.ModpackMenuItem("modpack." + c.getName());
        SwingUtil.changeFontFamily(localizableMenuItem, FontTL.ROBOTO_REGULAR, 12);
        localizableMenuItem.setBackground(Color.WHITE);
        add(localizableMenuItem);
    }
}
