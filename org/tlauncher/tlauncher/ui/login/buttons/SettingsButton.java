package org.tlauncher.tlauncher.ui.login.buttons;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.entity.ConfigEnum;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
import org.tlauncher.tlauncher.ui.login.LoginForm;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/SettingsButton.class */
public class SettingsButton extends MainImageButton implements Blockable {
    private static final long serialVersionUID = 1321382157134544911L;
    private final LoginForm lf;
    private final JPopupMenu popup;
    private final LocalizableMenuItem versionManager;
    private final LocalizableMenuItem settings;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettingsButton(LoginForm loginform) {
        super(DARK_GREEN_COLOR, "settings-mouse-under.png", "settings.png");
        this.lf = loginform;
        this.image = ImageCache.getImage("settings-mouse-under.png");
        this.popup = new JPopupMenu();
        this.settings = new LocalizableMenuItem("loginform.button.settings.launcher");
        this.settings.addActionListener(e -> {
            this.lf.pane.setScene(this.lf.pane.settingsScene);
        });
        this.popup.add(this.settings);
        this.versionManager = new LocalizableMenuItem("loginform.button.settings.version");
        this.versionManager.addActionListener(e2 -> {
            if (this.lf.scene.settingsForm.isVisible()) {
                this.lf.scene.setSidePanel(null);
            }
            this.lf.pane.setScene(this.lf.pane.versionManager);
        });
        this.popup.add(this.versionManager);
        Configuration c = TLauncher.getInstance().getConfiguration();
        if (c.getBoolean(ConfigEnum.UPDATER_LAUNCHER)) {
            LocalizableMenuItem updater = new LocalizableMenuItem("updater.update.now");
            updater.setForeground(Color.RED);
            this.popup.add(updater);
            updater.addActionListener(l -> {
                c.set("updater.delay", (Object) 0);
                TLauncher.getInstance().getUpdater().asyncFindUpdate();
            });
        }
        setPreferredSize(new Dimension(30, getHeight()));
        addActionListener(e3 -> {
            callPopup();
        });
    }

    void callPopup() {
        SwingUtilities.invokeLater(() -> {
            this.lf.defocus();
            this.popup.show(this, 0, getHeight());
        });
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        if (reason.equals(LoginForm.AUTH_BLOCK) || reason.equals(LoginForm.LAUNCH_BLOCK)) {
            Blocker.blockComponents(reason, this.versionManager);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblockComponents(reason, this.versionManager);
    }
}
