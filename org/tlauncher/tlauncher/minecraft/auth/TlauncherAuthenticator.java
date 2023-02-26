package org.tlauncher.tlauncher.minecraft.auth;

import org.tlauncher.tlauncher.entity.auth.Response;
import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
import org.tlauncher.tlauncher.exceptions.auth.ServiceUnavailableException;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/TlauncherAuthenticator.class */
public class TlauncherAuthenticator extends StandardAuthenticator {
    public TlauncherAuthenticator(Account account) {
        super(account, TLauncher.getInnerSettings().get("skin.authentication.authenticate"), TLauncher.getInnerSettings().get("skin.authentication.refresh"));
    }

    @Override // org.tlauncher.tlauncher.minecraft.auth.StandardAuthenticator
    protected AuthenticatorException getException(Response result) {
        AuthenticatorException exception = super.getException(result);
        if (exception.getClass().equals(AuthenticatorException.class) && "ServiceUnavailableException".equals(result.getError())) {
            return new ServiceUnavailableException(result.getErrorMessage());
        }
        return exception;
    }
}
