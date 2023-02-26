package org.tlauncher.util.guice;

import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/guice/MinecraftLauncherFactory.class */
public interface MinecraftLauncherFactory {
    MinecraftLauncher create(TLauncher tLauncher, boolean z);
}
