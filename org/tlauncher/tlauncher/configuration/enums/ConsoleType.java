package org.tlauncher.tlauncher.configuration.enums;

import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/enums/ConsoleType.class */
public enum ConsoleType {
    GLOBAL,
    NONE;

    public static boolean parse(String val) {
        ConsoleType[] values;
        if (val == null) {
            return false;
        }
        for (ConsoleType cur : values()) {
            if (cur.toString().equalsIgnoreCase(val)) {
                return true;
            }
        }
        return false;
    }

    public static ConsoleType get(String val) {
        ConsoleType[] values;
        for (ConsoleType cur : values()) {
            if (cur.toString().equalsIgnoreCase(val)) {
                return cur;
            }
        }
        throw new NullPointerException("not find console type " + val);
    }

    public MinecraftLauncher.ConsoleVisibility getVisibility() {
        return this == GLOBAL ? MinecraftLauncher.ConsoleVisibility.ALWAYS : MinecraftLauncher.ConsoleVisibility.ON_CRASH;
    }

    @Override // java.lang.Enum
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static ConsoleType getDefault() {
        return NONE;
    }
}
