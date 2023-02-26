package org.tlauncher.tlauncher.ui.swing.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import org.apache.http.HttpStatus;
import org.tlauncher.modpack.domain.client.share.GameEntitySort;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/renderer/UserCategoryListRenderer.class */
public class UserCategoryListRenderer extends ModpackComboxRenderer {
    @Override // org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            return null;
        }
        String text = Localizable.get("modpack." + ((GameEntitySort) value).toString().toLowerCase(Locale.ROOT));
        return createElement(index, isSelected, text);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Component createElement(final int index, final boolean isSelected, String text) {
        JLabel label = new JLabel(text) { // from class: org.tlauncher.tlauncher.ui.swing.renderer.UserCategoryListRenderer.1
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (isSelected && index > -1) {
                    Rectangle r = g.getClipBounds();
                    r.height--;
                    g.setColor(new Color(121, (int) HttpStatus.SC_CREATED, 245));
                    g.drawLine(UserCategoryListRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent(), getFontMetrics(getFont()).stringWidth(getText()) + UserCategoryListRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent());
                    g.setColor(new Color(121, 211, 247));
                    g.drawLine(UserCategoryListRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent(), getFontMetrics(getFont()).stringWidth(getText()) + UserCategoryListRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent());
                }
            }
        };
        label.setForeground(Color.WHITE);
        SwingUtil.changeFontFamily(label, FontTL.ROBOTO_REGULAR, 14);
        label.setBorder(BorderFactory.createEmptyBorder(0, this.GUP_LEFT, 0, 0));
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(172, 30));
        p.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, LINE));
        p.setBackground(this.backgroundBox);
        p.add(label, "Center");
        return p;
    }
}
