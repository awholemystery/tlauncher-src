package org.tlauncher.tlauncher.minecraft.launcher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/MinecraftExtendedListener.class */
public interface MinecraftExtendedListener extends MinecraftListener {
    void onMinecraftCollecting();

    void onMinecraftComparingAssets();

    void onMinecraftDownloading();

    void onMinecraftReconstructingAssets();

    void onMinecraftUnpackingNatives();

    void onMinecraftDeletingEntries();

    void onMinecraftConstructing();

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    void onMinecraftLaunch();

    void onMinecraftPostLaunch();

    void onWorldBackup();
}
