package org.tlauncher.tlauncher.ui.menu.item;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
import org.tlauncher.util.ColorUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/menu/item/ModpackCategoryItem.class */
public class ModpackCategoryItem extends LocalizableMenuItem {
    public ModpackCategoryItem(String path) {
        super(path);
    }

    protected void paintComponent(Graphics g) {
        Rectangle r = getVisibleRect();
        g.setColor(ColorUtil.BLUE_MODPACK);
        g.fillRect(r.x, r.y, r.width, r.height);
        paintText(g, this, r, getText());
    }

    protected void paintText(Graphics g, JComponent c, Rectangle r, String text) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D textRect = fm.getStringBounds(text, g2d);
        g.setFont(getFont());
        g.setColor(Color.WHITE);
        int x = ((int) (r.getWidth() - textRect.getWidth())) / 2;
        int y = ((((int) (r.getHeight() - ((int) textRect.getHeight()))) / 2) + fm.getAscent()) - 1;
        g2d.drawString(text, x, y);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
}
