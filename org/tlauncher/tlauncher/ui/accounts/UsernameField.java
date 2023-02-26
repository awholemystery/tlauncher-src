package org.tlauncher.tlauncher.ui.accounts;

import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/UsernameField.class */
public class UsernameField extends LocalizableTextField {
    private static final long serialVersionUID = -5813187607562947592L;
    private UsernameState state;
    String username;

    public UsernameField(CenterPanel pan, UsernameState state) {
        super(pan, "account.username");
        setState(state);
    }

    public UsernameState getState() {
        return this.state;
    }

    public void setState(UsernameState state) {
        if (state == null) {
            throw new NullPointerException();
        }
        this.state = state;
        setPlaceholder(state.placeholder);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/accounts/UsernameField$UsernameState.class */
    public enum UsernameState {
        USERNAME("account.username"),
        EMAIL_LOGIN("account.email"),
        EMAIL("account.email.restrict");
        
        private final String placeholder;

        UsernameState(String placeholder) {
            this.placeholder = placeholder;
        }

        public String getPlaceholder() {
            return this.placeholder;
        }
    }
}
