package org.tlauncher.tlauncher.minecraft.user.xb.auth;

import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/auth/XboxLiveAuthenticationException.class */
public class XboxLiveAuthenticationException extends MinecraftAuthenticationException {
    public XboxLiveAuthenticationException(Throwable cause) {
        super(cause);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return "xbox_live_authentication";
    }
}
