package org.tlauncher.tlauncher.configuration.enums;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/enums/BackupToolTips.class */
public enum BackupToolTips {
    TITLE("settings.backup.title.tooltip"),
    DO_BACKUPS("settings.doBackup.tooltip"),
    MAX_BACKUP_SIZE("settings.max.backup.size.tooltip"),
    MAX_BACKUP_SAVE_TIME("settings.max.backup.time.tooltip");
    
    private String value;

    BackupToolTips(String value) {
        this.value = value;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.value;
    }
}
