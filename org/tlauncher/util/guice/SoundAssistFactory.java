package org.tlauncher.util.guice;

import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.SoundAssist;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/guice/SoundAssistFactory.class */
public interface SoundAssistFactory {
    SoundAssist create(MinecraftLauncher minecraftLauncher);
}
