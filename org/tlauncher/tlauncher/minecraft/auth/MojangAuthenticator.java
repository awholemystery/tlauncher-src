package org.tlauncher.tlauncher.minecraft.auth;

import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/MojangAuthenticator.class */
class MojangAuthenticator extends StandardAuthenticator {
    public MojangAuthenticator(Account account, String url) {
        super(account, url + "authenticate", url + "refresh");
    }

    @Override // org.tlauncher.tlauncher.minecraft.auth.StandardAuthenticator, org.tlauncher.tlauncher.minecraft.auth.Authenticator
    protected void pass() throws AuthenticatorException {
        super.pass();
        if (getAccount().getProfiles().length == 0) {
            throw new AuthenticatorException("not proper type", "not.mojang.account");
        }
    }
}
