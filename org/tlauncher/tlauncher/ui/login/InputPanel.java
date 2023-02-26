package org.tlauncher.tlauncher.ui.login;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JButton;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.swing.extended.VPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/InputPanel.class */
public class InputPanel extends VPanel {
    private static final long serialVersionUID = -4418780541141471184L;
    public final LoginForm loginForm;
    public final UsernameField username;
    public final VersionComboBox version;
    public final CheckBoxPanel checkbox;
    public final AccountComboBox accountComboBox;
    private Account.AccountType type;

    public InputPanel(LoginForm lf) {
        boolean chooser = TLauncher.getInstance().getConfiguration().getBoolean("chooser.type.account");
        this.loginForm = lf;
        this.username = new UsernameField("test");
        this.accountComboBox = new AccountComboBox(lf);
        if (chooser) {
            add((Component) this.accountComboBox, Box.createRigidArea(new Dimension(1, 3)));
            this.type = Account.AccountType.MOJANG;
            lf.addLoginProcessListener(this.accountComboBox);
        } else {
            add((Component) this.username, Box.createRigidArea(new Dimension(1, 3)));
            this.type = Account.AccountType.FREE;
            lf.addLoginProcessListener(this.username);
        }
        this.version = new VersionComboBox(lf);
        add((Component) this.version);
        this.checkbox = new CheckBoxPanel(lf, this, chooser);
        add((Component) this.checkbox);
        add((Component) new JButton("test"));
    }

    public void changeTypeAccount(Account.AccountType type) {
        if (type.equals(Account.AccountType.SPECIAL)) {
            if (contains(this.username)) {
                remove(this.username);
            }
            add((Component) this.accountComboBox, 0);
            this.loginForm.addLoginProcessListener(this.accountComboBox);
            this.loginForm.removeLoginProcessListener(this.username);
            this.type = Account.AccountType.SPECIAL;
        } else {
            if (contains(this.accountComboBox)) {
                remove(this.accountComboBox);
            }
            add((Component) this.username, 0);
            this.loginForm.addLoginProcessListener(this.username);
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
