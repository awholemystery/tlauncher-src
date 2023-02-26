package org.tlauncher.tlauncher.ui.scenes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.accounts.AccountEditor;
import org.tlauncher.tlauncher.ui.accounts.AccountList;
import org.tlauncher.tlauncher.ui.accounts.AccountTip;
import org.tlauncher.tlauncher.ui.accounts.helper.AccountEditorHelper;
import org.tlauncher.tlauncher.ui.accounts.helper.HelperState;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.server.BackPanel;
import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/AccountEditorScene.class */
public class AccountEditorScene extends PseudoScene {
    private static final long serialVersionUID = -151325577614420989L;
    private final int WIDTH = 562;
    private final int HEIGHT = 365;
    public final AccountEditor editor;
    public final AccountList list;
    public final AccountTip tip;
    public final AccountEditorHelper helper;
    public final ExtendedPanel panel;
    public static final Color BACKGROUND_ACCOUNT_COLOR = new Color(248, 246, 244);
    private final FlexibleEditorPanel flex;
    private final ProfileManager profileManager;

    public AccountEditorScene(MainPane main) {
        super(main);
        this.WIDTH = 562;
        this.HEIGHT = 365;
        this.panel = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
        this.flex = new FlexibleEditorPanel("text/html", "auth.tip.tlauncher", 537);
        Component extendedPanel = new ExtendedPanel();
        this.profileManager = TLauncher.getInstance().getProfileManager();
        Component backPanel = new BackPanel("account.config", new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.scenes.AccountEditorScene.1
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    AccountEditorScene.this.profileManager.refresh();
                    AccountEditorScene.this.getMainPane().openDefaultScene();
                }
            }
        }, ImageCache.getIcon("back-arrow.png"));
        this.panel.setOpaque(true);
        this.panel.setSize(562, 365);
        this.flex.setPreferredSize(new Dimension(546, 99));
        this.flex.setMargin(new Insets(20, 20, 22, 16));
        backPanel.setPreferredSize(new Dimension(562, 25));
        this.panel.setBackground(BACKGROUND_ACCOUNT_COLOR);
        extendedPanel.setLayout(new BoxLayout(extendedPanel, 0));
        extendedPanel.setPreferredSize(new Dimension(562, 191));
        extendedPanel.setInsets(new Insets(20, 20, 0, 16));
        Component extendedPanel2 = new ExtendedPanel((LayoutManager) new FlowLayout(0, 0, 0));
        JPanel gap = new JPanel(new FlowLayout(1, 0, 0));
        gap.setPreferredSize(new Dimension(1, 170));
        gap.setBackground(new Color(172, 171, 170));
        extendedPanel2.setPreferredSize(new Dimension(41, 171));
        extendedPanel2.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        extendedPanel2.add(Box.createHorizontalStrut(20));
        extendedPanel2.add(gap);
        extendedPanel2.add(Box.createHorizontalStrut(20));
        this.editor = new AccountEditor(this, this.flex);
        this.editor.setOpaque(true);
        this.list = new AccountList(this);
        this.profileManager.addListener(this.list);
        extendedPanel.add(this.editor);
        extendedPanel.add(extendedPanel2);
        extendedPanel.add(this.list);
        this.panel.add(backPanel, "North");
        this.panel.add(extendedPanel, "Center");
        this.panel.add((Component) this.flex, (Object) "South");
        add((Component) this.panel);
        this.tip = new AccountTip(this);
        this.tip.setAccountType(Account.AccountType.TLAUNCHER);
        this.editor.setSelectedAccountType(Account.AccountType.TLAUNCHER);
        addComponentListener(new ComponentListener() { // from class: org.tlauncher.tlauncher.ui.scenes.AccountEditorScene.2
            public void componentShown(ComponentEvent e) {
                if (AccountEditorScene.this.list.model.getSize() == 0) {
                    AccountEditorScene.this.list.addTempToList();
                }
            }

            public void componentResized(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }
        });
        this.tip.setVisible(true);
        this.helper = new AccountEditorHelper(this);
        add((Component) this.helper);
        this.helper.setState(HelperState.NONE);
    }

    public void setShownAccountHelper(boolean shown, boolean animate) {
        super.setShown(shown, animate);
        if (!shown || !this.list.model.isEmpty()) {
            this.helper.setState(HelperState.NONE);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        super.onResize();
        int hw = getWidth() / 2;
        int hh = getHeight() / 2;
        this.panel.setLocation(hw - (this.panel.getWidth() / 2), hh - (this.panel.getHeight() / 2));
    }
}
