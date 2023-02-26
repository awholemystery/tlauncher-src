package org.tlauncher.tlauncher.updater.client;

import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/PackageType.class */
public enum PackageType {
    EXE,
    JAR;
    
    public static final PackageType CURRENT;

    static {
        CURRENT = FileUtil.getRunningJar().toString().endsWith(".exe") ? EXE : JAR;
    }

    public String toLowerCase() {
        return name().toLowerCase();
    }

    public static boolean isCurrent(PackageType pt) {
        return pt == CURRENT;
    }
}
