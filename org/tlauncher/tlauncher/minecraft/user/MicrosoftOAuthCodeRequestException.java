package org.tlauncher.tlauncher.minecraft.user;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/MicrosoftOAuthCodeRequestException.class */
public class MicrosoftOAuthCodeRequestException extends MinecraftAuthenticationException {
    public MicrosoftOAuthCodeRequestException(String message) {
        super(message);
    }

    public MicrosoftOAuthCodeRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return "microsoft_oauth_code_request";
    }
}
