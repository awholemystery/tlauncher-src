package org.tlauncher.tlauncher.ui.menu;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.swing.VersionCellRenderer;
import org.tlauncher.tlauncher.ui.swing.box.TlauncherCustomBox;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/menu/PopupMenuView.class */
public class PopupMenuView extends JPanel {
    private static final int MAX_SIZE_VERSION = 30;
    private static final Color BACKGROUND_TITLE = new Color(60, 170, 232);
    private static final Icon TLAUNCHER_USER_ICON = ImageCache.getIcon("tlauncher-user.png");
    private static final Icon TLAUNCHER_USER_ICON_GRAY = ImageCache.getIcon("tlauncher-user-gray.png");
    private static final Icon MOJANG_USER_ICON = ImageCache.getIcon("mojang-user.png");
    private final JLabel title;
    private final JComboBox<VersionSyncInfo> box;
    private final JLabel versionLabel;
    private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    private Point point;
    private final JLayeredPane defaultScene;
    private final PopupUpdaterButton start;
    private final PopupUpdaterButton copy;
    private final PopupUpdaterButton favorite;

    public PopupMenuView(JLayeredPane defaultScene) {
        setVisible(false);
        this.defaultScene = defaultScene;
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        setSize(290, 150);
        setBackground(Color.WHITE);
        this.versionLabel = new JLabel(Localizable.get().get("menu.version"));
        this.versionLabel.setForeground(ColorUtil.COLOR_77);
        this.box = new TlauncherCustomBox();
        this.box.setRenderer(list, value, index, isSelected, cellHasFocus -> {
            JLabel renderer = this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value == null) {
                return null;
            }
            boolean skin = TLauncher.getInstance().getTLauncherManager().useTLauncherAccount(value.getAvailableVersion());
            if (skin && TLauncher.getInstance().getConfiguration().getBoolean("skin.status.checkbox.state")) {
                renderer.setIcon(TLAUNCHER_USER_ICON);
            } else if (skin) {
                renderer.setIcon(TLAUNCHER_USER_ICON_GRAY);
            }
            renderer.setText(trimId(VersionCellRenderer.getLabelFor(value)));
            renderer.setAlignmentY(0.5f);
            renderer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
            renderer.setOpaque(true);
            if (isSelected) {
                renderer.setBackground(ColorUtil.COLOR_213);
            } else {
                renderer.setBackground(Color.white);
            }
            renderer.setForeground(ColorUtil.COLOR_77);
            return renderer;
        });
        this.box.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(149, 149, 149)));
        this.title = new JLabel();
        this.title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        springLayout.putConstraint("North", this.title, 0, "North", this);
        springLayout.putConstraint("West", this.title, 0, "West", this);
        springLayout.putConstraint("South", this.title, 30, "North", this);
        springLayout.putConstraint("East", this.title, 0, "East", this);
        add(this.title);
        this.title.setOpaque(true);
        this.title.setForeground(Color.WHITE);
        this.title.setBackground(BACKGROUND_TITLE);
        SwingUtil.setFontSize(this.title, 13.0f);
        this.start = new PopupUpdaterButton(Color.white, Localizable.get().get("menu.start"));
        this.start.setBorder(BorderFactory.createEmptyBorder());
        springLayout.putConstraint("North", this.start, 30, "North", this);
        springLayout.putConstraint("West", this.start, 0, "West", this);
        springLayout.putConstraint("South", this.start, 60, "North", this);
        springLayout.putConstraint("East", this.start, 0, "East", this);
        add(this.start);
        this.copy = new PopupUpdaterButton(Color.white, Localizable.get().get("menu.copy"));
        this.copy.setBorder(BorderFactory.createEmptyBorder());
        springLayout.putConstraint("North", this.copy, 60, "North", this);
        springLayout.putConstraint("West", this.copy, 0, "West", this);
        springLayout.putConstraint("South", this.copy, 90, "North", this);
        springLayout.putConstraint("East", this.copy, 0, "East", this);
        add(this.copy);
        this.favorite = new PopupUpdaterButton(Color.white, Localizable.get().get("menu.favorite"));
        this.favorite.setBorder(BorderFactory.createEmptyBorder());
        springLayout.putConstraint("North", this.favorite, 90, "North", this);
        springLayout.putConstraint("West", this.favorite, 0, "West", this);
        springLayout.putConstraint("South", this.favorite, 120, "North", this);
        springLayout.putConstraint("East", this.favorite, 0, "East", this);
        add(this.favorite);
        springLayout.putConstraint("North", this.versionLabel, 122, "North", this);
        springLayout.putConstraint("West", this.versionLabel, 10, "West", this);
        springLayout.putConstraint("South", this.versionLabel, 146, "North", this);
        springLayout.putConstraint("East", this.versionLabel, 90, "West", this);
        add(this.versionLabel);
        springLayout.putConstraint("North", this.box, 122, "North", this);
        springLayout.putConstraint("West", this.box, 90, "West", this);
        springLayout.putConstraint("South", this.box, 146, "North", this);
        springLayout.putConstraint("East", this.box, -10, "East", this);
        add(this.box);
        HotServerManager manager = (HotServerManager) TLauncher.getInjector().getInstance(HotServerManager.class);
        this.favorite.addActionListener(e -> {
            manager.addServerToList(true, (VersionSyncInfo) this.box.getSelectedItem());
        });
        this.copy.addActionListener(e2 -> {
            manager.copyAddress();
        });
        this.start.addActionListener(e3 -> {
            manager.launchGame((VersionSyncInfo) this.box.getSelectedItem());
            setVisible(false);
        });
        this.box.addPopupMenuListener(new PopupMenuListener() { // from class: org.tlauncher.tlauncher.ui.menu.PopupMenuView.1
            public void popupMenuWillBecomeVisible(PopupMenuEvent e4) {
                PopupMenuView.this.point = MouseInfo.getPointerInfo().getLocation();
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e4) {
            }

            public void popupMenuCanceled(PopupMenuEvent e4) {
            }
        });
        this.box.addActionListener(e4 -> {
            try {
                new Robot().mouseMove(this.point.x, this.point.y);
            } catch (AWTException e1) {
                log(e1);
            }
        });
        MouseListener mouseListener = new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.menu.PopupMenuView.2
            public void mouseExited(MouseEvent e5) {
                if (PopupMenuView.this.isShowing() && !PopupMenuView.this.mouseIsOverDisplayPanel(PopupMenuView.this)) {
                    PopupMenuView.this.setVisible(false);
                }
            }
        };
        addMouseListener(mouseListener);
        this.start.addMouseListener(mouseListener);
        this.copy.addMouseListener(mouseListener);
        this.favorite.addMouseListener(mouseListener);
        this.box.addMouseListener(mouseListener);
        this.box.addPopupMenuListener(new PopupMenuListener() { // from class: org.tlauncher.tlauncher.ui.menu.PopupMenuView.3
            public void popupMenuWillBecomeVisible(PopupMenuEvent e5) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e5) {
                if (PopupMenuView.this.isShowing() && !PopupMenuView.this.mouseIsOverDisplayPanel(PopupMenuView.this)) {
                    PopupMenuView.this.setVisible(false);
                }
            }

            public void popupMenuCanceled(PopupMenuEvent e5) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean mouseIsOverDisplayPanel(Component component) {
        if (this.box.isPopupVisible()) {
            return true;
        }
        Point c = component.getLocationOnScreen();
        Point m = MouseInfo.getPointerInfo().getLocation();
        return m.x >= c.x && m.x < (c.x + component.getWidth()) - 1 && m.y >= c.y && m.y <= c.y + component.getHeight();
    }

    private static String trimId(String version) {
        if (version.length() > 30) {
            return version.substring(0, 30);
        }
        return version;
    }

    private void log(Object e) {
        U.log("[PopupMenuView] ", e);
    }

    public void showSelectedModel(PopupMenuModel model) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.ui.menu.PopupMenuView A[D('this' org.tlauncher.tlauncher.ui.menu.PopupMenuView), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'model' org.tlauncher.tlauncher.ui.menu.PopupMenuModel A[D('model' org.tlauncher.tlauncher.ui.menu.PopupMenuModel), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.menu.PopupMenuView), (r1 I:org.tlauncher.tlauncher.ui.menu.PopupMenuModel) type: DIRECT call: org.tlauncher.tlauncher.ui.menu.PopupMenuView.lambda$showSelectedModel$5(org.tlauncher.tlauncher.ui.menu.PopupMenuModel):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.menu.PopupMenuView.showSelectedModel(org.tlauncher.tlauncher.ui.menu.PopupMenuModel):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/menu/PopupMenuView.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:272)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
            	at java.base/java.util.ArrayList.forEach(Unknown Source)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
            Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
            	at java.base/jdk.internal.util.Preconditions.outOfBounds(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.checkIndex(Unknown Source)
            	at java.base/java.util.Objects.checkIndex(Unknown Source)
            	at java.base/java.util.ArrayList.get(Unknown Source)
            	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:959)
            	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
            	... 15 more
            */
        /*
            this = this;
            r0 = r3
            r1 = r4
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$showSelectedModel$5(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.menu.PopupMenuView.showSelectedModel(org.tlauncher.tlauncher.ui.menu.PopupMenuModel):void");
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/menu/PopupMenuView$PopupUpdaterButton.class */
    private class PopupUpdaterButton extends UpdaterButton {
        PopupUpdaterButton(Color color, String value) {
            setText(value);
            setOpaque(true);
            setBackground(color);
            setForeground(ColorUtil.COLOR_77);
            addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.menu.PopupMenuView.PopupUpdaterButton.1
                public void mouseEntered(MouseEvent e) {
                    PopupUpdaterButton.this.setBackground(ColorUtil.COLOR_235);
                }

                public void mouseExited(MouseEvent e) {
                    PopupUpdaterButton.this.setBackground(Color.WHITE);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.tlauncher.tlauncher.ui.loc.UpdaterButton
        public void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(text, g2d);
            g.setFont(getFont());
            int y = ((getHeight() - ((int) r.getHeight())) / 2) + fm.getAscent();
            g2d.drawString(text, 10, y);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            Icon icon = getIcon();
            if (Objects.nonNull(icon)) {
                icon.paintIcon(c, g, (int) (r.getWidth() + 15.0d), (getHeight() - icon.getIconHeight()) / 2);
            }
        }
    }
}
