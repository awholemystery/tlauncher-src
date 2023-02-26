package org.tlauncher.tlauncher.configuration.enums;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/enums/ActionOnLaunch.class */
public enum ActionOnLaunch {
    HIDE,
    EXIT,
    NOTHING;

    public static boolean parse(String val) {
        ActionOnLaunch[] values;
        if (val == null) {
            return false;
        }
        for (ActionOnLaunch cur : values()) {
            if (cur.toString().equalsIgnoreCase(val)) {
                return true;
            }
        }
        return false;
    }

    public static ActionOnLaunch get(String val) {
        ActionOnLaunch[] values;
        for (ActionOnLaunch cur : values()) {
            if (cur.toString().equalsIgnoreCase(val)) {
                return cur;
            }
        }
        return null;
    }

    @Override // java.lang.Enum
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static ActionOnLaunch getDefault() {
        return HIDE;
    }
}
