package org.tlauncher.tlauncher.minecraft.user.oauth.exchange;

import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/oauth/exchange/MicrosoftOAuthCodeExchangeException.class */
public class MicrosoftOAuthCodeExchangeException extends MinecraftAuthenticationException {
    public MicrosoftOAuthCodeExchangeException(Throwable cause) {
        super(cause);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return "microsoft_oauth_code_exchange";
    }
}
