package org.tlauncher.tlauncher.ui.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.BasicMenuItemUI;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
import org.tlauncher.tlauncher.ui.modpack.AddedModpackStuffFrame;
import org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame;
import org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame;
import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/menu/ModpackPopup.class */
public class ModpackPopup extends JPopupMenu {
    private static final long serialVersionUID = 1985825573230509140L;
    public static Color BACKGROUND_ITEM = new Color(240, 240, 240);
    public static Color LINE = new Color((int) HttpStatus.SC_NO_CONTENT, (int) HttpStatus.SC_NO_CONTENT, (int) HttpStatus.SC_NO_CONTENT);

    public ModpackPopup() {
        setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, LINE));
    }

    public ModpackPopup(final ModpackComboBox localmodpacks) {
        this();
        ModpackMenuItem notFound = new ModpackMenuItem("modpack.backup.not.found");
        ModpackMenuItem hand = new ModpackMenuItem("modpack.backup.install.hand");
        ModpackMenuItem backup = new ModpackMenuItem("modpack.backup.settings");
        add(backup);
        add(notFound);
        add(hand);
        setPreferredSize(new Dimension(getPreferredSize().width, 97));
        notFound.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.menu.ModpackPopup.1
            public void actionPerformed(ActionEvent e) {
                AddedModpackStuffFrame addedModpackStuffView = new AddedModpackStuffFrame(TLauncher.getInstance().getFrame(), "modpack.added.request", "modpack.added.request.message");
                addedModpackStuffView.setVisible(true);
            }
        });
        hand.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.menu.ModpackPopup.2
            public void actionPerformed(ActionEvent e) {
                if (localmodpacks.getSelectedIndex() == 0) {
                    Alert.showLocMessage("modpack.select.modpack");
                    return;
                }
                HandleInstallModpackElementFrame frame = new HandleInstallModpackElementFrame(TLauncher.getInstance().getFrame(), localmodpacks.getSelectedValue());
                frame.setVisible(true);
            }
        });
        backup.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.menu.ModpackPopup.3
            public void actionPerformed(ActionEvent e) {
                ModpackBackupFrame frame = new ModpackBackupFrame(TLauncher.getInstance().getFrame(), localmodpacks);
                frame.setVisible(true);
            }
        });
    }

    protected void paintComponent(Graphics g) {
        Rectangle rec = getVisibleRect();
        g.setColor(LINE);
        g.fillRect(rec.x, rec.y, rec.width, rec.height);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/menu/ModpackPopup$ModpackMenuItem.class */
    public static class ModpackMenuItem extends LocalizableMenuItem {
        private static final long serialVersionUID = 371263248187501160L;
        int item_gup;

        public ModpackMenuItem(String name) {
            super(name);
            this.item_gup = 20;
            setBackground(ModpackPopup.BACKGROUND_ITEM);
            SwingUtil.changeFontFamily(this, FontTL.ROBOTO_REGULAR, 12, ColorUtil.COLOR_16);
            setUI(new BasicMenuItemUI() { // from class: org.tlauncher.tlauncher.ui.menu.ModpackPopup.ModpackMenuItem.1
                protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int gup) {
                    Rectangle rec = c.getVisibleRect();
                    ButtonModel model = ModpackMenuItem.this.getModel();
                    if (model.isPressed()) {
                        g.setColor(ColorUtil.COLOR_195);
                    } else if (model.isArmed()) {
                        g.setColor(ColorUtil.COLOR_215);
                    } else {
                        g.setColor(ModpackMenuItem.this.getBackground());
                    }
                    g.fillRect(rec.x, rec.y, rec.width, rec.height);
                    g.setColor(ModpackPopup.LINE);
                    ModpackMenuItem.this.paintBorder(g);
                    paintText(g, c, rec, ModpackMenuItem.this.getText());
                }

                protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    FontMetrics fm = g2d.getFontMetrics();
                    Rectangle2D r = fm.getStringBounds(text, g2d);
                    g.setFont(ModpackMenuItem.this.getFont());
                    g.setColor(Color.BLACK);
                    int x = ModpackMenuItem.this.item_gup;
                    int y = ((textRect.height - ((int) r.getHeight())) / 2) + fm.getAscent();
                    g2d.drawString(text, x, y);
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                }
            });
        }

        public void paint(Graphics g) {
            this.ui.paint(g, this);
        }

        protected void paintBorder(Graphics g) {
            Rectangle rec = getVisibleRect();
            g.setColor(ModpackPopup.LINE);
            g.drawLine(rec.x, (rec.y + rec.height) - 1, rec.x + rec.width, (rec.y + rec.height) - 1);
        }
    }
}
