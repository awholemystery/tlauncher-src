package org.tlauncher.tlauncher.ui.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.SpringLayout;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.BlockablePanel;
import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
import org.tlauncher.tlauncher.ui.swing.CheckBoxListener;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/VersionPanel.class */
public class VersionPanel extends BlockablePanel implements LoginProcessListener {
    public final VersionComboBox version;
    public final LocalizableCheckbox forceupdate;
    public final LoginForm loginForm;
    private boolean state;

    public VersionPanel(LoginForm lf) {
        this.loginForm = lf;
        this.version = new VersionComboBox(lf);
        ((ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class)).addGameListener(GameType.MODPACK, this.version);
        ((ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class)).addGameListener(GameType.MOD, this.version);
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        springLayout.putConstraint("North", this.version, 11, "North", this);
        springLayout.putConstraint("West", this.version, 0, "West", this);
        springLayout.putConstraint("South", this.version, 35, "North", this);
        springLayout.putConstraint("East", this.version, 0, "East", this);
        add((Component) this.version);
        this.forceupdate = new LocalizableCheckbox("loginform.checkbox.forceupdate");
        this.forceupdate.setForeground(Color.WHITE);
        this.forceupdate.setIconTextGap(14);
        this.forceupdate.setFocusPainted(false);
        springLayout.putConstraint("North", this.forceupdate, 44, "North", this);
        springLayout.putConstraint("South", this.forceupdate, -11, "South", this);
        springLayout.putConstraint("West", this.forceupdate, -4, "West", this);
        springLayout.putConstraint("East", this.forceupdate, 0, "East", this);
        add((Component) this.forceupdate);
        if (!OS.is(OS.WINDOWS)) {
            Font f = this.version.getFont();
            this.forceupdate.setFont(new Font(f.getFamily(), f.getStyle(), 11));
        }
        this.forceupdate.addItemListener(new CheckBoxListener() { // from class: org.tlauncher.tlauncher.ui.login.VersionPanel.1
            @Override // org.tlauncher.tlauncher.ui.swing.CheckBoxListener
            public void itemStateChanged(boolean newstate) {
                VersionPanel.this.state = newstate;
                VersionPanel.this.loginForm.play.updateState();
            }
        });
    }

    @Override // org.tlauncher.tlauncher.listeners.auth.LoginProcessListener
    public void validatePreGameLaunch() throws LoginException {
        VersionSyncInfo syncInfo = this.loginForm.versions.getVersion();
        if (syncInfo == null) {
            return;
        }
        boolean supporting = syncInfo.hasRemote();
        boolean installed = syncInfo.isInstalled();
        Version version = syncInfo.getLocal();
        if ((version instanceof CompleteVersion) && ((CompleteVersion) version).isModpack()) {
            supporting = true;
        }
        if (this.state) {
            if (!supporting) {
                Alert.showLocError("forceupdate.local");
                throw new LoginException("Cannot update local version!");
            } else if (installed && !Alert.showLocQuestion("forceupdate.question")) {
                throw new LoginException("User has cancelled force updating.");
            }
        }
    }
}
