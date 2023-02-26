package org.tlauncher.tlauncher.ui.scenes;

import java.awt.Component;
import java.awt.Dimension;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.SideNotifier;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.login.LoginFormHelper;
import org.tlauncher.tlauncher.ui.menu.PopupMenuView;
import org.tlauncher.tlauncher.ui.settings.SettingsPanel;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/DefaultScene.class */
public class DefaultScene extends PseudoScene {
    public static final Dimension SETTINGS_SIZE = new Dimension(700, 550);
    public static final int EDGE = 7;
    public final SideNotifier notifier;
    public final LoginForm loginForm;
    public final LoginFormHelper loginFormHelper;
    public final SettingsPanel settingsForm;
    private SidePanel sidePanel;
    private ExtendedPanel sidePanelComp;
    private PopupMenuView popupMenuView;

    public DefaultScene(MainPane main) {
        super(main);
        this.notifier = main.notifier;
        this.settingsForm = new SettingsPanel(this);
        this.settingsForm.setSize(SETTINGS_SIZE);
        this.settingsForm.setVisible(false);
        add((Component) this.settingsForm);
        this.loginForm = new LoginForm(this);
        this.popupMenuView = new PopupMenuView(this);
        add((Component) this.popupMenuView);
        add((Component) this.loginForm);
        this.loginFormHelper = new LoginFormHelper(this);
        add((Component) this.loginFormHelper);
    }

    public ExtendedPanel getSidePanelComp() {
        return this.sidePanelComp;
    }

    public void setSidePanelComp(ExtendedPanel sidePanelComp) {
        this.sidePanelComp = sidePanelComp;
    }

    public PopupMenuView getPopupMenuView() {
        return this.popupMenuView;
    }

    @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane, org.tlauncher.tlauncher.ui.swing.ResizeableComponent
    public void onResize() {
        if (this.parent == null) {
            return;
        }
        setBounds(0, 0, this.parent.getWidth(), this.parent.getHeight());
        updateCoords();
        this.loginFormHelper.onResize();
    }

    private void updateCoords() {
        int w = getWidth();
        int h = getHeight();
        int hw = w / 2;
        int y = h - this.loginForm.getHeight();
        this.loginForm.setLocation(0, y);
        if (this.sidePanel != null) {
            this.sidePanelComp.setLocation(hw - (this.sidePanelComp.getWidth() / 2), 7);
        }
    }

    public SidePanel getSidePanel() {
        return this.sidePanel;
    }

    public void setSidePanel(SidePanel side) {
        if (this.sidePanel == side) {
            return;
        }
        boolean noSidePanel = side == null;
        if (this.sidePanelComp != null) {
            this.sidePanelComp.setVisible(false);
        }
        this.sidePanel = side;
        this.sidePanelComp = noSidePanel ? null : getSidePanelComp(side);
        if (!noSidePanel) {
            this.sidePanelComp.setVisible(true);
        }
        getMainPane().browser.setBrowserContentShown("settings", side == null);
        updateCoords();
    }

    public void toggleSidePanel(SidePanel side) {
        if (this.sidePanel == side) {
            side = null;
        }
        setSidePanel(side);
    }

    public ExtendedPanel getSidePanelComp(SidePanel side) {
        if (side == null) {
            throw new NullPointerException("side");
        }
        switch (side) {
            case SETTINGS:
                return this.settingsForm;
            default:
                throw new RuntimeException("unknown side:" + side);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/scenes/DefaultScene$SidePanel.class */
    public enum SidePanel {
        SETTINGS;
        
        public final boolean requiresShow;

        SidePanel(boolean requiresShow) {
            this.requiresShow = requiresShow;
        }

        SidePanel() {
            this(false);
        }
    }
}
