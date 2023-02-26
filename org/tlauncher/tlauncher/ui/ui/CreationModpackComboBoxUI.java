package org.tlauncher.tlauncher.ui.ui;

import ch.qos.logback.core.CoreConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.util.ColorUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/CreationModpackComboBoxUI.class */
public abstract class CreationModpackComboBoxUI extends BasicComboBoxUI {
    public abstract String getText(Object obj);

    protected JButton createArrowButton() {
        ActionListener[] actionListeners;
        ImageUdaterButton button = new ImageUdaterButton(Color.WHITE, "gray-combobox-array.png");
        for (ActionListener l : button.getActionListeners()) {
            button.removeActionListener(l);
        }
        button.setModelPressedColor(ColorUtil.COLOR_195);
        return button;
    }

    protected ComboPopup createPopup() {
        BasicComboPopup basic = new BasicComboPopup(this.comboBox) { // from class: org.tlauncher.tlauncher.ui.ui.CreationModpackComboBoxUI.1
            private static final long serialVersionUID = -1200285237129861017L;

            protected JScrollPane createScroller() {
                ModpackScrollBarUI barUI = new ModpackScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.ui.CreationModpackComboBoxUI.1.1
                    @Override // org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI
                    protected Dimension getMinimumThumbSize() {
                        return new Dimension(10, 40);
                    }

                    public Dimension getMaximumSize(JComponent c) {
                        Dimension dim = super.getMaximumSize(c);
                        dim.setSize(10.0d, dim.getHeight());
                        return dim;
                    }

                    public Dimension getPreferredSize(JComponent c) {
                        Dimension dim = super.getPreferredSize(c);
                        dim.setSize(13.0d, dim.getHeight());
                        return dim;
                    }
                };
                barUI.setGapThubm(5);
                JScrollPane scroller = new JScrollPane(this.list, 20, 31);
                scroller.getVerticalScrollBar().setUI(barUI);
                return scroller;
            }
        };
        basic.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(149, 149, 149)));
        return basic;
    }

    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        super.paintCurrentValue(g, bounds, hasFocus);
        g.setColor(Color.WHITE);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        Object ob = this.comboBox.getSelectedItem();
        paintText(g, bounds, Objects.isNull(ob) ? CoreConstants.EMPTY_STRING : getText(ob));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paintText(Graphics g, Rectangle textRect, String text) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g2d);
        g.setFont(this.comboBox.getFont());
        g.setColor(new Color(25, 25, 25));
        int y = ((textRect.height - ((int) r.getHeight())) / 2) + fm.getAscent();
        g2d.drawString(text, 14, y);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
}
