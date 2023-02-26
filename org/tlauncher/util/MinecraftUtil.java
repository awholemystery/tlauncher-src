package org.tlauncher.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/MinecraftUtil.class */
public class MinecraftUtil {
    public static File getWorkingDirectory() {
        if (TLauncher.getInstance() == null) {
            return getDefaultWorkingDirectory();
        }
        Configuration settings = TLauncher.getInstance().getConfiguration();
        String sdir = settings.get("minecraft.gamedir");
        return getWorkingDirectory(sdir);
    }

    public static Path buildWorkingPath(String... path) {
        return Paths.get(getWorkingDirectory().getAbsolutePath(), path);
    }

    public static File getWorkingDirectory(String sdir) {
        if (sdir == null) {
            return getDefaultWorkingDirectory();
        }
        File dir = new File(sdir);
        try {
            FileUtil.createFolder(dir);
            return dir;
        } catch (IOException e) {
            U.log("Cannot createScrollWrapper specified Minecraft folder:", dir.getAbsolutePath());
            return getDefaultWorkingDirectory();
        }
    }

    public static File getSystemRelatedFile(String path) {
        File file;
        String userHome = System.getProperty("user.home", ".");
        switch (OS.CURRENT) {
            case LINUX:
            case SOLARIS:
                file = new File(userHome, path);
                break;
            case WINDOWS:
                String applicationData = System.getenv("APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                file = new File(folder, path);
                break;
            case OSX:
                file = new File(userHome, "Library/Application Support/" + path);
                break;
            default:
                file = new File(userHome, path);
                break;
        }
        return file;
    }

    public static File getSystemRelatedDirectory(String path) {
        if (!OS.is(OS.OSX, OS.UNKNOWN)) {
            path = '.' + path;
        }
        return getSystemRelatedFile(path);
    }

    public static File getDefaultWorkingDirectory() {
        return getSystemRelatedDirectory(TLauncher.getFolder());
    }

    public static File getTLauncherFile(String path) {
        return new File(getSystemRelatedDirectory("tlauncher"), path);
    }

    public static boolean isUsernameValid(String username) {
        return username.length() > 2 && username.length() <= 16 && username.charAt(0) != '-';
    }

    public static void configureG1GC(List<String> list) {
        list.add("-XX:+UnlockExperimentalVMOptions");
        list.add("-XX:+UseG1GC");
        list.add("-XX:G1NewSizePercent=20");
        list.add("-XX:G1ReservePercent=20");
        list.add("-XX:MaxGCPauseMillis=50");
        list.add("-XX:G1HeapRegionSize=32M");
        list.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
        list.add("-Dfml.ignorePatchDiscrepancies=true");
        list.add("-Djava.net.preferIPv4Stack=true");
    }
}
