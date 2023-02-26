package org.tlauncher.tlauncher.ui.login;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/UsernameField.class */
public class UsernameField extends LocalizableTextField implements Blockable, LoginProcessListener {
    private final ProfileManager profileManager;

    public UsernameField(String login) {
        super("account.username");
        this.profileManager = TLauncher.getInstance().getProfileManager();
        addFocusListener(new FocusListener() { // from class: org.tlauncher.tlauncher.ui.login.UsernameField.1
            public void focusGained(FocusEvent e) {
                UsernameField.this.setBackground(UsernameField.this.getTheme().getBackground());
            }

            public void focusLost(FocusEvent e) {
                UsernameField.this.profileManager.updateFreeAccountField(UsernameField.this.getUsername());
            }
        });
        setValue(login);
    }

    public String getUsername() {
        return getValue();
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        setEnabled(false);
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        setEnabled(true);
    }

    @Override // org.tlauncher.tlauncher.listeners.auth.LoginProcessListener
    public void validatePreGameLaunch() throws LoginException {
        String u = getUsername();
        if (u != null) {
            this.profileManager.updateFreeAccountField(u);
            return;
        }
        setBackground(getTheme().getFailure());
        Alert.showLocError("auth.error.nousername");
        throw new LoginException("Invalid user name!");
    }
}
