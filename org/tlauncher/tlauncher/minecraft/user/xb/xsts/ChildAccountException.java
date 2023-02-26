package org.tlauncher.tlauncher.minecraft.user.xb.xsts;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/xb/xsts/ChildAccountException.class */
public class ChildAccountException extends XSTSAuthenticationException {
    @Override // org.tlauncher.tlauncher.minecraft.user.xb.xsts.XSTSAuthenticationException, org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return super.getShortKey() + ".child_account";
    }
}
