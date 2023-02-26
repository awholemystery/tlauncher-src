package org.tlauncher.tlauncher.configuration.enums;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/enums/BackupSetting.class */
public enum BackupSetting {
    LAST_MODIFIED_TIME("lastModifiedTime"),
    MAX_TIME_FOR_BACKUP("max.time.for.backups"),
    FREE_PARTITION_SIZE("free.partition.size"),
    SKIP_USER_BACKUP("skip.user.backup"),
    MAX_SIZE_FOR_WORLD("max.size.for.world"),
    REPEAT_BACKUP("repeat.backup.hours");
    
    private String value;

    BackupSetting(String value) {
        this.value = value;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.value;
    }
}
