package org.tlauncher.tlauncher.minecraft.user.xb.xsts;

import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/xsts/XSTSAuthenticationException.class */
public class XSTSAuthenticationException extends MinecraftAuthenticationException {
    public XSTSAuthenticationException() {
    }

    public XSTSAuthenticationException(Throwable cause) {
        super(cause);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return "xsts_authentication";
    }
}
