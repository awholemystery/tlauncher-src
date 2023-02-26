package org.tlauncher.tlauncher.minecraft.user.mcsauth;

import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/mcsauth/MinecraftServicesAuthenticationException.class */
public class MinecraftServicesAuthenticationException extends MinecraftAuthenticationException {
    public MinecraftServicesAuthenticationException(Throwable cause) {
        super(cause);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return "minecraft_services_auth";
    }
}
