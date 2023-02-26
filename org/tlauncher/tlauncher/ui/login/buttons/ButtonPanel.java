package org.tlauncher.tlauncher.ui.login.buttons;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/ButtonPanel.class */
public class ButtonPanel extends ExtendedPanel implements Blockable {
    public final Color mouseUnderButton = new Color(82, 127, 53);
    public final LoginForm loginForm;
    public final RefreshButton refresh;
    public final FolderButton folder;
    public final SettingsButton settings;
    private final ModpackButton mainImageButton;

    public ButtonPanel(LoginForm lf) {
        this.loginForm = lf;
        setLayout(new GridLayout(0, 4));
        this.mainImageButton = new ModpackButton(new Color(113, 169, 76), "modpack-tl-new.png", "modpack-tl-active-new.png");
        add((Component) this.mainImageButton);
        this.refresh = new RefreshButton(lf);
        add((Component) this.refresh);
        this.folder = new FolderButton(lf);
        add((Component) this.folder);
        this.settings = new SettingsButton(lf);
        add((Component) this.settings);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.block(this.mainImageButton, LoginForm.UPDATER_BLOCK);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblock(this.mainImageButton, LoginForm.UPDATER_BLOCK);
    }
}
