package org.tlauncher.tlauncher.listeners.auth;

import org.tlauncher.tlauncher.ui.login.LoginException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/listeners/auth/LoginProcessListener.class */
public interface LoginProcessListener {
    default void preValidate() {
    }

    default void validatePreGameLaunch() throws LoginException {
    }

    default void loginFailed() {
    }

    default void loginSucceed() {
    }
}
