package org.tlauncher.tlauncher.ui.swing.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/ModpackComboxRenderer.class */
public class ModpackComboxRenderer extends DefaultListCellRenderer {
    public static final Color LINE = new Color(67, 187, 255);
    protected int GUP_LEFT = 18;
    protected Color backgroundBox = ColorUtil.BACKGROUND_COMBO_BOX_POPUP;

    public Component getListCellRendererComponent(JList<?> list, Object value, final int index, final boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            return null;
        }
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(172, 30));
        p.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, LINE));
        p.setBackground(this.backgroundBox);
        JLabel label = new JLabel(((CompleteVersion) value).getID()) { // from class: org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer.1
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (isSelected && index > -1) {
                    Rectangle r = getBounds();
                    r.height--;
                    g.setColor(new Color(121, (int) HttpStatus.SC_CREATED, 245));
                    g.drawLine(ModpackComboxRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent(), getFontMetrics(getFont()).stringWidth(getText()) + ModpackComboxRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent());
                    g.setColor(new Color(121, 211, 247));
                    g.drawLine(ModpackComboxRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent(), getFontMetrics(getFont()).stringWidth(getText()) + ModpackComboxRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent());
                }
            }
        };
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(0, this.GUP_LEFT, 0, 0));
        SwingUtil.changeFontFamily(label, FontTL.ROBOTO_REGULAR, 14);
        p.add(label, "Center");
        if (index > 0) {
            label = new JLabel(ImageCache.getNativeIcon("config-modpack.png"));
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
            SwingUtil.changeFontFamily(label, FontTL.ROBOTO_REGULAR, 14);
            p.add(label, "East");
        }
        if (index == 0 && list.getModel().getSize() < 2) {
            label.setText(Localizable.get("modpack.local.box.default.list"));
        }
        return p;
    }

    public void setBackground(Color color) {
        this.backgroundBox = color;
    }
}
