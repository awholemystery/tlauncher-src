package org.tlauncher.tlauncher.minecraft.launcher;

import org.tlauncher.tlauncher.minecraft.crash.Crash;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/MinecraftListener.class */
public interface MinecraftListener {
    void onMinecraftPrepare();

    void onMinecraftAbort();

    void onMinecraftLaunch();

    void onMinecraftClose();

    void onMinecraftError(Throwable th);

    void onMinecraftKnownError(MinecraftException minecraftException);

    void onMinecraftCrash(Crash crash);
}
