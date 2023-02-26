package org.tlauncher.tlauncher.configuration.enums;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/enums/BackupFrequency.class */
public enum BackupFrequency {
    EVERYTIME("settings.backup.frequency.always"),
    OFTEN("settings.backup.frequency.once");
    
    private final String value;

    BackupFrequency(String value) {
        this.value = value;
    }

    public static BackupFrequency get(String val) {
        if (val.equals("0") || val.equals(EVERYTIME.toString())) {
            return EVERYTIME;
        }
        if (val.equals("1") || val.equals(OFTEN.toString())) {
            return OFTEN;
        }
        return null;
    }

    public static String convert(BackupFrequency from) {
        if (from.equals(EVERYTIME)) {
            return "0";
        }
        if (from.equals(OFTEN)) {
            return "1";
        }
        return null;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.value;
    }

    public static BackupFrequency getDefault() {
        return OFTEN;
    }
}
