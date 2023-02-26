package org.tlauncher.tlauncher.ui.login;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javafx.application.Platform;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.BlockablePanel;
import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
import org.tlauncher.tlauncher.ui.swing.CheckBoxListener;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/CheckBoxPanel.class */
public class CheckBoxPanel extends BlockablePanel implements LoginProcessListener {
    private static final long serialVersionUID = 768489049585749260L;
    public final LocalizableCheckbox forceupdate = new LocalizableCheckbox("loginform.checkbox.forceupdate");
    private static boolean helperOpen = false;
    private final LocalizableCheckbox chooseTypeAccount;
    private final Configuration settings;
    private boolean state;
    private final LoginForm loginForm;
    private final InputPanel inputPanel;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CheckBoxPanel(LoginForm lf, InputPanel input, boolean chooserState) {
        this.loginForm = lf;
        this.inputPanel = input;
        this.forceupdate.setFocusable(false);
        this.chooseTypeAccount = new LocalizableCheckbox("loginform.checkbox.account");
        this.settings = TLauncher.getInstance().getConfiguration();
        if (!OS.is(OS.WINDOWS)) {
            Font f = this.chooseTypeAccount.getFont();
            this.chooseTypeAccount.setFont(new Font(f.getFamily(), f.getStyle(), 11));
            this.forceupdate.setFont(new Font(f.getFamily(), f.getStyle(), 11));
        }
        add((Component) this.forceupdate);
        add((Component) this.chooseTypeAccount);
        this.chooseTypeAccount.getModel().setSelected(chooserState);
        this.forceupdate.addItemListener(new CheckBoxListener() { // from class: org.tlauncher.tlauncher.ui.login.CheckBoxPanel.1
            @Override // org.tlauncher.tlauncher.ui.swing.CheckBoxListener
            public void itemStateChanged(boolean newstate) {
                CheckBoxPanel.this.state = newstate;
                CheckBoxPanel.this.loginForm.play.updateState();
            }
        });
        this.chooseTypeAccount.addItemListener(e -> {
            if (e.getStateChange() == 1) {
                Platform.runLater(new Runnable() { // from class: org.tlauncher.tlauncher.ui.login.CheckBoxPanel.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CheckBoxPanel.this.inputPanel.changeTypeAccount(Account.AccountType.SPECIAL);
                        TLauncher.getInstance().getConfiguration().set("chooser.type.account", (Object) true);
                        if (CheckBoxPanel.this.settings.isFirstRun() && !CheckBoxPanel.helperOpen) {
                            CheckBoxPanel.this.loginForm.pane.openAccountEditor();
                            CheckBoxPanel.this.loginForm.pane.accountEditor.setShownAccountHelper(true, true);
                            boolean unused = CheckBoxPanel.helperOpen = true;
                            CheckBoxPanel.this.loginForm.pane.accountEditor.addComponentListener(new ComponentListener() { // from class: org.tlauncher.tlauncher.ui.login.CheckBoxPanel.2.1
                                public void componentShown(ComponentEvent e) {
                                }

                                public void componentResized(ComponentEvent e) {
                                }

                                public void componentMoved(ComponentEvent e) {
                                }

                                public void componentHidden(ComponentEvent e) {
                                    CheckBoxPanel.this.loginForm.pane.accountEditor.setShownAccountHelper(false, false);
                                }
                            });
                        }
                    }
                });
                return;
            }
            this.inputPanel.changeTypeAccount(Account.AccountType.FREE);
            TLauncher.getInstance().getConfiguration().set("chooser.type.account", (Object) false);
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
