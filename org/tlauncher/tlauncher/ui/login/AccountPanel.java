package org.tlauncher.tlauncher.ui.login;

import ch.qos.logback.core.CoreConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Objects;
import javafx.application.Platform;
import javax.swing.BorderFactory;
import javax.swing.SpringLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.block.BlockablePanel;
import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/AccountPanel.class */
public class AccountPanel extends BlockablePanel {
    private static final long serialVersionUID = 1;
    private static boolean helperOpen = false;
    public final AccountComboBox accountComboBox;
    public final LoginForm loginForm;
    public final UsernameField username;
    public final LocalizableCheckbox chooseTypeAccount;
    private final ExtendedPanel accountPanel;
    private Account.AccountType type;

    public AccountPanel(LoginForm lf, boolean officialAccountType) {
        this.loginForm = lf;
        ProfileManager profileManager = TLauncher.getInstance().getProfileManager();
        this.username = new UsernameField(CoreConstants.EMPTY_STRING);
        if (Objects.isNull(lf.global.get("login.account")) && profileManager.hasSelectedAccount()) {
            this.username.setValue((Object) profileManager.getSelectedAccount().getDisplayName());
        } else if (Objects.nonNull(lf.global.get("login.account"))) {
            this.username.setValue((Object) lf.global.get("login.account"));
        }
        this.username.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(), new EmptyBorder(0, 9, 0, 0)));
        this.accountComboBox = new AccountComboBox(lf);
        this.chooseTypeAccount = new LocalizableCheckbox("loginform.checkbox.account");
        this.chooseTypeAccount.setForeground(Color.WHITE);
        this.chooseTypeAccount.setIconTextGap(14);
        this.chooseTypeAccount.setFocusPainted(false);
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        this.accountPanel = new ExtendedPanel((LayoutManager) new GridLayout(1, 1));
        springLayout.putConstraint("North", this.accountPanel, 11, "North", this);
        springLayout.putConstraint("West", this.accountPanel, 0, "West", this);
        springLayout.putConstraint("South", this.accountPanel, 35, "North", this);
        springLayout.putConstraint("East", this.accountPanel, 0, "East", this);
        add((Component) this.accountPanel);
        if (officialAccountType) {
            this.accountPanel.add((Component) this.accountComboBox);
            this.type = Account.AccountType.MOJANG;
            lf.addLoginProcessListener(this.accountComboBox);
        } else {
            this.accountPanel.add((Component) this.username);
            this.type = Account.AccountType.FREE;
            lf.addLoginProcessListener(this.username);
        }
        if (!OS.is(OS.WINDOWS)) {
            Font f = this.chooseTypeAccount.getFont();
            this.chooseTypeAccount.setFont(new Font(f.getFamily(), f.getStyle(), 11));
        }
        springLayout.putConstraint("North", this.chooseTypeAccount, 44, "North", this);
        springLayout.putConstraint("South", this.chooseTypeAccount, -11, "South", this);
        springLayout.putConstraint("West", this.chooseTypeAccount, -4, "West", this.accountPanel);
        springLayout.putConstraint("East", this.chooseTypeAccount, 0, "East", this.accountPanel);
        add((Component) this.chooseTypeAccount);
        this.chooseTypeAccount.getModel().setSelected(officialAccountType);
        this.chooseTypeAccount.addItemListener(e -> {
            if (e.getStateChange() == 1) {
                Platform.runLater(new Runnable() { // from class: org.tlauncher.tlauncher.ui.login.AccountPanel.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountPanel.this.changeTypeAccount(Account.AccountType.SPECIAL);
                        TLauncher.getInstance().getConfiguration().set("chooser.type.account", (Object) true);
                        if (TLauncher.getInstance().getConfiguration().isFirstRun() && !AccountPanel.helperOpen) {
                            AccountPanel.this.loginForm.pane.openAccountEditor();
                            AccountPanel.this.loginForm.pane.accountEditor.setShownAccountHelper(true, true);
                            boolean unused = AccountPanel.helperOpen = true;
                            AccountPanel.this.loginForm.pane.accountEditor.addComponentListener(new ComponentListener() { // from class: org.tlauncher.tlauncher.ui.login.AccountPanel.1.1
                                public void componentShown(ComponentEvent e) {
                                }

                                public void componentResized(ComponentEvent e) {
                                }

                                public void componentMoved(ComponentEvent e) {
                                }

                                public void componentHidden(ComponentEvent e) {
                                    AccountPanel.this.loginForm.pane.accountEditor.setShownAccountHelper(false, false);
                                }
                            });
                        }
                    }
                });
                return;
            }
            changeTypeAccount(Account.AccountType.FREE);
            TLauncher.getInstance().getConfiguration().set("chooser.type.account", (Object) false);
        });
    }

    public void changeTypeAccount(Account.AccountType type) {
        if (type.equals(Account.AccountType.SPECIAL)) {
            if (this.accountPanel.contains(this.username)) {
                this.accountPanel.remove(this.username);
            }
            this.accountPanel.add((Component) this.accountComboBox);
            this.loginForm.addLoginProcessListener(this.accountComboBox, 0);
            this.loginForm.removeLoginProcessListener(this.username);
            this.type = Account.AccountType.SPECIAL;
        } else {
            if (this.accountPanel.contains(this.accountComboBox)) {
                this.accountPanel.remove(this.accountComboBox);
            }
            this.accountPanel.add((Component) this.username);
            this.loginForm.addLoginProcessListener(this.username, 0);
            this.loginForm.removeLoginProcessListener(this.accountComboBox);
            this.type = Account.AccountType.FREE;
        }
        revalidate();
        repaint();
    }

    public String getUsername() {
        if (this.type.equals(Account.AccountType.FREE)) {
            return this.username.getUsername();
        }
        return ((Account) this.accountComboBox.getSelectedItem()).getUsername();
    }

    public Account.AccountType getTypeAccountShow() {
        return this.type;
    }
}
