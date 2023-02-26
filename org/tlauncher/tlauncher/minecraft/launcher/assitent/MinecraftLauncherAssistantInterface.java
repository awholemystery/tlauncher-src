package org.tlauncher.tlauncher.minecraft.launcher.assitent;

import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/assitent/MinecraftLauncherAssistantInterface.class */
public interface MinecraftLauncherAssistantInterface {
    void collectInfo(MinecraftLauncher minecraftLauncher) throws MinecraftException;

    void collectResources(MinecraftLauncher minecraftLauncher) throws MinecraftException;

    void constructJavaArguments(MinecraftLauncher minecraftLauncher) throws MinecraftException;

    void constructProgramArguments(MinecraftLauncher minecraftLauncher) throws MinecraftException;
}
