package org.tlauncher.tlauncher.minecraft.user.xb.xsts;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/xsts/NoXboxAccountException.class */
public class NoXboxAccountException extends XSTSAuthenticationException {
    @Override // org.tlauncher.tlauncher.minecraft.user.xb.xsts.XSTSAuthenticationException, org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return super.getShortKey() + ".no_xbox_account";
    }
}
