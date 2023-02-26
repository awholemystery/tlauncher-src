package org.tlauncher.tlauncher.minecraft.auth;

import java.util.Objects;
import java.util.UUID;
import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/FreeAuthentication.class */
public class FreeAuthentication extends Authenticator {
    public FreeAuthentication(Account account) {
        super(account);
    }

    @Override // org.tlauncher.tlauncher.minecraft.auth.Authenticator
    protected void pass() throws AuthenticatorException {
        if (Objects.isNull(this.account.getUUID())) {
            this.account.setUUID(UUIDTypeAdapter.fromUUID(UUID.randomUUID()));
            this.account.setUserID(this.account.getUUID());
            this.account.setDisplayName(this.account.getUsername());
            return;
        }
        this.account.setDisplayName(this.account.getUsername());
    }
}
