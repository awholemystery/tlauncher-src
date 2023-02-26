package org.tlauncher.util.guice;

import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.LanguageAssistance;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/guice/LanguageAssistFactory.class */
public interface LanguageAssistFactory {
    LanguageAssistance create(MinecraftLauncher minecraftLauncher);
}
