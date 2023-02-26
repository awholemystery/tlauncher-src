package org.tlauncher.tlauncher.minecraft.user;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/MinecraftProfileConversionException.class */
public class MinecraftProfileConversionException extends MinecraftAuthenticationException {
    public MinecraftProfileConversionException(Throwable cause) {
        super(cause);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return "minecraft_profile_conversion";
    }
}
