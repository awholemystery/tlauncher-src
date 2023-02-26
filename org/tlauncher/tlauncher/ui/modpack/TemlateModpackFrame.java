package org.tlauncher.tlauncher.ui.modpack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/TemlateModpackFrame.class */
public class TemlateModpackFrame extends JDialog {
    protected int upGap;
    public static final Color BLUE_COLOR = new Color(69, 168, (int) CompleteSubEntityScene.FullGameEntity.CompleteDescriptionGamePanel.SHADOW_PANEL);
    public static final Color BLUE_COLOR_UNDER = new Color(2, 161, 221);
    public static final Color COLOR_0_174_239 = new Color(0, 174, 239);
    public static final int LEFT_BORDER = 29;
    public static final int RIGHT_BORDER = 27;
    protected LocalizableLabel label;
    protected final JPanel baseContainer;
    private final JPanel paneContainer;

    public TemlateModpackFrame(JFrame parent, String title, Dimension size) {
        this(parent, title, size, false);
    }

    public TemlateModpackFrame(JFrame parent, String title, Dimension size, boolean noTransparentImage) {
        super(parent);
        this.upGap = 92;
        this.baseContainer = new JPanel((LayoutManager) null);
        this.paneContainer = new JPanel(new BorderLayout()) { // from class: org.tlauncher.tlauncher.ui.modpack.TemlateModpackFrame.1
            public synchronized void addMouseListener(MouseListener l) {
            }
        };
        setUndecorated(true);
        setTitle(Localizable.get(title));
        setLocationRelativeTo(null);
        setResizable(false);
        if (!noTransparentImage) {
            setBackground(new Color(0, 0, 0, 50));
            this.baseContainer.setOpaque(false);
        }
        this.baseContainer.add(this.paneContainer);
        add(this.baseContainer);
        JPanel panel = new JPanel(new FlowLayout(0, 0, 0));
        panel.setPreferredSize(new Dimension(size.width, 47));
        panel.setBackground(BLUE_COLOR);
        final JButton close = new ImageUdaterButton(BLUE_COLOR, "close-modpack.png");
        close.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.TemlateModpackFrame.2
            public void mouseEntered(MouseEvent e) {
                close.setBackground(new Color(60, 145, 193));
            }

            public void mouseExited(MouseEvent e) {
                close.setBackground(TemlateModpackFrame.BLUE_COLOR);
            }
        });
        this.label = new LocalizableLabel(title);
        SwingUtil.changeFontFamily(this.label, FontTL.ROBOTO_REGULAR, 18, ColorUtil.COLOR_248);
        this.label.setBorder(new EmptyBorder(0, 40, 0, 0));
        this.label.setHorizontalTextPosition(0);
        this.label.setHorizontalAlignment(0);
        this.label.setPreferredSize(new Dimension(size.width - 41, 47));
        close.setPreferredSize(new Dimension(41, 47));
        panel.add(this.label);
        panel.add(close);
        this.paneContainer.add(panel, "North");
        close.addActionListener(new ActionListener() { // from class: org.tlauncher.tlauncher.ui.modpack.TemlateModpackFrame.3
            public void actionPerformed(ActionEvent e) {
                TemlateModpackFrame.this.setVisible(false);
            }
        });
        Point point = parent.getContentPane().getLocationOnScreen();
        setBounds(point.x, point.y, MainPane.SIZE.width, MainPane.SIZE.height);
        setCenter(size);
        if (!TLauncher.DEBUG) {
            setAlwaysOnTop(true);
        }
    }

    public void addCenter(JComponent comp) {
        this.paneContainer.add(comp, "Center");
    }

    public void setCenter(Dimension size) {
        Point point = this.baseContainer.getLocation();
        this.paneContainer.setBounds(point.x + ((MainPane.SIZE.width - size.width) / 2), point.y + this.upGap, size.width, size.height);
        this.paneContainer.revalidate();
        this.paneContainer.repaint();
    }

    public void setVisible(boolean b) {
        if (b) {
            getParent().setEnabled(false);
        } else {
            getParent().setEnabled(true);
        }
        super.setVisible(b);
    }
}
