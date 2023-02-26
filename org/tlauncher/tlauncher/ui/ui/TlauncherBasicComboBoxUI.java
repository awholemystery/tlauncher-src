package org.tlauncher.tlauncher.ui.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/ui/TlauncherBasicComboBoxUI.class */
public class TlauncherBasicComboBoxUI extends BasicComboBoxUI {
    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        Component c;
        ListCellRenderer renderer = this.comboBox.getRenderer();
        if (hasFocus && !isPopupVisible(this.comboBox)) {
            c = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, true, false);
        } else {
            c = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
            if (Objects.isNull(c)) {
                return;
            }
            c.setBackground(UIManager.getColor("ComboBox.background"));
        }
        c.setFont(this.comboBox.getFont());
        if (hasFocus && !isPopupVisible(this.comboBox)) {
            c.setForeground(Color.BLACK);
            c.setBackground(Color.WHITE);
        } else if (this.comboBox.isEnabled()) {
            c.setForeground(this.comboBox.getForeground());
            c.setBackground(this.comboBox.getBackground());
        } else {
            c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
            c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
        }
        boolean shouldValidate = false;
        if (c instanceof JPanel) {
            shouldValidate = true;
        }
        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;
        if (this.padding != null) {
            x = bounds.x + this.padding.left;
            y = bounds.y + this.padding.top;
            w = bounds.width - (this.padding.left + this.padding.right);
            h = bounds.height - (this.padding.top + this.padding.bottom);
        }
        this.currentValuePane.paintComponent(g, c, this.comboBox, x, y, w, h, shouldValidate);
    }

    protected JButton createArrowButton() {
        ActionListener[] actionListeners;
        JButton button = new ImageUdaterButton(Color.white, "black-arrow.png");
        for (ActionListener l : button.getActionListeners()) {
            button.removeActionListener(l);
        }
        return button;
    }

    protected ComboPopup createPopup() {
        BasicComboPopup basic = new BasicComboPopup(this.comboBox) { // from class: org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI.1
            private static final long serialVersionUID = -5424584744514133918L;

            protected JScrollPane createScroller() {
                VersionScrollBarUI barUI = new VersionScrollBarUI() { // from class: org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI.1.1
                    @Override // org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI
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
        basic.setBorder(BorderFactory.createEmptyBorder());
        return basic;
    }
}
