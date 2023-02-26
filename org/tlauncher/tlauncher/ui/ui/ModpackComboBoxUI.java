package org.tlauncher.tlauncher.ui.ui;

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
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import net.minecraft.launcher.versions.CompleteVersion;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer;
import org.tlauncher.util.ColorUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/ModpackComboBoxUI.class */
public class ModpackComboBoxUI extends BasicComboBoxUI {
    protected int GUP_LEFT = 20;
    protected boolean centerText = false;

    protected JButton createArrowButton() {
        ActionListener[] actionListeners;
        final ImageUdaterButton button = new ImageUdaterButton(ColorUtil.BACKGROUND_COMBO_BOX_POPUP, "white-arrow-down.png");
        for (ActionListener l : button.getActionListeners()) {
            button.removeActionListener(l);
        }
        button.setBackground(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
        this.comboBox.addPopupMenuListener(new PopupMenuListener() { // from class: org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI.1
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                button.setImage(ImageCache.getBufferedImage("white-arrow-up.png"));
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                button.setImage(ImageCache.getBufferedImage("white-arrow-down.png"));
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        return button;
    }

    protected ComboPopup createPopup() {
        BasicComboPopup basic = new BasicComboPopup(this.comboBox) { // from class: org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI.2
            private static final long serialVersionUID = -3111049881837377991L;

            protected JScrollPane createScroller() {
                ModpackScrollBarUI barUI = new ModpackScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI.2.1
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
                scroller.setBackground(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
                return scroller;
            }
        };
        basic.setMaximumSize(new Dimension(172, 149));
        basic.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, ModpackComboxRenderer.LINE));
        return basic;
    }

    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        paintBackground(g, bounds);
        if (Objects.nonNull(this.comboBox.getSelectedItem())) {
            paintText(g, bounds, ((CompleteVersion) this.comboBox.getSelectedItem()).getID());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paintBackground(Graphics g, Rectangle bounds) {
        g.setColor(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paintText(Graphics g, Rectangle textRect, String text) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(this.comboBox.getFont());
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(text, g2d);
        g.setColor(Color.WHITE);
        int x = this.GUP_LEFT;
        int y = ((textRect.height - ((int) r.getHeight())) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
}
