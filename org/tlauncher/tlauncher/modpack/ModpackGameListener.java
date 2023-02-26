package org.tlauncher.tlauncher.modpack;

import com.google.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/modpack/ModpackGameListener.class */
public class ModpackGameListener implements MinecraftListener {
    @Inject
    private ModpackManager modpackManager;
    @Inject
    private TLauncher tLauncher;

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftPrepare() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftAbort() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftLaunch() {
        U.log("copied to modpack servers.dat");
        copy(true);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftClose() {
        U.log("copy from modpack servers.dat ");
        copy(false);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftError(Throwable e) {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftKnownError(MinecraftException e) {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftCrash(Crash crash) {
    }

    private void copy(boolean toModpack) {
        Path modpackServers = ModpackUtil.getPathByVersion(this.tLauncher.getLauncher().getVersion(), "servers.dat");
        Path baseServers = FileUtil.getRelative("servers.dat");
        try {
            if (toModpack) {
                Files.copy(baseServers, modpackServers, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(modpackServers, baseServers, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            U.log(e);
        }
    }
}
