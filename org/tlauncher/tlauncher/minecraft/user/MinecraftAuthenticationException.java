package org.tlauncher.tlauncher.minecraft.user;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/MinecraftAuthenticationException.class */
public abstract class MinecraftAuthenticationException extends Exception {
    public abstract String getShortKey();

    public MinecraftAuthenticationException() {
    }

    public MinecraftAuthenticationException(String message) {
        super(message);
    }

    public MinecraftAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinecraftAuthenticationException(Throwable cause) {
        super(cause);
    }
}
