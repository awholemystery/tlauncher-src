package org.tlauncher.tlauncher.minecraft.user.preq;

import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/preq/MinecraftProfileRequestException.class */
public class MinecraftProfileRequestException extends MinecraftAuthenticationException {
    public MinecraftProfileRequestException(Throwable cause) {
        super(cause);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return "minecraft_profile_request";
    }
}
