package org.tlauncher.tlauncher.minecraft.user.gos;

import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/gos/GameOwnershipValidationException.class */
public class GameOwnershipValidationException extends MinecraftAuthenticationException {
    private final boolean isKnownNotToOwn;

    public GameOwnershipValidationException(Throwable cause) {
        super(cause);
        this.isKnownNotToOwn = false;
    }

    public GameOwnershipValidationException(String message) {
        super(message);
        this.isKnownNotToOwn = true;
    }

    public boolean isKnownNotToOwn() {
        return this.isKnownNotToOwn;
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException
    public String getShortKey() {
        return "game_ownership";
    }
}
