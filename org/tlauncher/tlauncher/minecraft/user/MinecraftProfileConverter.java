package org.tlauncher.tlauncher.minecraft.user;

import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftOAuthProfile;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/MinecraftProfileConverter.class */
public class MinecraftProfileConverter {
    public MinecraftUser convertToMinecraftUser(MicrosoftOAuthToken microsoftToken, MinecraftServicesToken minecraftToken, MinecraftOAuthProfile profile) throws MinecraftProfileConversionException {
        try {
            return new MinecraftUser(profile, microsoftToken, minecraftToken);
        } catch (RuntimeException rE) {
            throw new MinecraftProfileConversionException(rE);
        }
    }
}
