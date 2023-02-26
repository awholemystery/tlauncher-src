package org.tlauncher.tlauncher.listeners.auth;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.tlauncher.tlauncher.exceptions.auth.NotCorrectTokenOrIdException;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.login.LoginException;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/listeners/auth/ValidateAccountToken.class */
public class ValidateAccountToken implements LoginProcessListener {
    private final RefreshTokenListener refreshTokenListener = new RefreshTokenListener(this);

    @Override // org.tlauncher.tlauncher.listeners.auth.LoginProcessListener
    public void validatePreGameLaunch() throws LoginException {
        ProfileManager profile = TLauncher.getInstance().getProfileManager();
        Account ac = profile.getSelectedAccount();
        Authenticator authenticator = Authenticator.instanceFor(ac);
        authenticator.addListener(new AuthenticatorSaveListener());
        authenticator.addListener(this.refreshTokenListener);
        try {
            synchronized (this.refreshTokenListener) {
                CompletableFuture.runAsync(authenticator);
                this.refreshTokenListener.wait();
            }
        } catch (InterruptedException e) {
            U.log(e);
        }
        if (this.refreshTokenListener.getException() instanceof NotCorrectTokenOrIdException) {
            try {
                profile.remove(ac);
            } catch (IOException e2) {
                U.log(e2);
            }
            throw new LoginException(this.refreshTokenListener.getException().getMessage());
        }
    }
}
