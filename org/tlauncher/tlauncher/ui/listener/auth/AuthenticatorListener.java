package org.tlauncher.tlauncher.ui.listener.auth;

import org.tlauncher.tlauncher.minecraft.auth.Authenticator;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/auth/AuthenticatorListener.class */
public interface AuthenticatorListener {
    void onAuthPassing(Authenticator authenticator);

    void onAuthPassingError(Authenticator authenticator, Exception exc);

    void onAuthPassed(Authenticator authenticator);
}
