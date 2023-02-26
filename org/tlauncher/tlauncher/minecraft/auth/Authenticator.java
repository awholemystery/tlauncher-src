package org.tlauncher.tlauncher.minecraft.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
import org.tlauncher.tlauncher.listeners.auth.AuthenticatorSaveListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/Authenticator.class */
public abstract class Authenticator implements Runnable {
    protected final Account account;
    private final String logPrefix = '[' + getClass().getSimpleName() + ']';
    private final List<AuthenticatorListener> listeners = new ArrayList();

    protected abstract void pass() throws AuthenticatorException;

    public final Account getAccount() {
        return this.account;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Authenticator(Account account) {
        if (account == null) {
            throw new NullPointerException("account");
        }
        this.account = account;
    }

    public static Authenticator instanceFor(Account account) {
        if (account == null) {
            throw new NullPointerException("account");
        }
        switch (account.getType()) {
            case TLAUNCHER:
                return new TlauncherAuthenticator(account);
            case MOJANG:
                return new MojangAuthenticator(account, TLauncher.getInnerSettings().get("authserver.mojang"));
            case MICROSOFT:
                return new MicrosoftAuthenticator(account);
            case FREE:
                return new FreeAuthentication(account);
            default:
                throw new IllegalArgumentException("illegal account type");
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        this.listeners.forEach(e -> {
            e.onAuthPassing(this);
        });
        try {
            pass();
            this.listeners.forEach(e2 -> {
                e2.onAuthPassed(this);
            });
        } catch (Exception error) {
            log("Cannot authenticate:", error);
            this.listeners.forEach(e3 -> {
                error.onAuthPassingError(this, error);
            });
        }
    }

    public static UUID getClientToken() {
        return TLauncher.getInstance().getProfileManager().getClientToken();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void log(Object... o) {
        U.log(this.logPrefix, o);
    }

    public void addListener(AuthenticatorListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(AuthenticatorListener listener) {
        this.listeners.remove(listener);
    }

    public static void authenticate(Account acc, AuthenticatorListener authenticatorListener) {
        Authenticator authenticator = instanceFor(acc);
        authenticator.addListener(new AuthenticatorSaveListener());
        if (Objects.nonNull(authenticatorListener)) {
            authenticator.addListener(authenticatorListener);
        }
        CompletableFuture.runAsync(authenticator);
    }
}
