package org.tlauncher.tlauncher.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.launcher.versions.ReleaseType;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.configuration.enums.BackupSetting;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.tlauncher.ui.settings.MinecraftSettings;
import org.tlauncher.tlauncher.updater.client.Notices;
import org.tlauncher.util.IntegerArray;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/ConfigurationDefaults.class */
public class ConfigurationDefaults {
    private static final int version = 3;
    private final Map<String, Object> d = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurationDefaults(InnerConfiguration inner) {
        Notices.NoticeType[] values;
        this.d.put("settings.version", 3);
        this.d.put("minecraft.gamedir", MinecraftUtil.getDefaultWorkingDirectory().getAbsolutePath());
        this.d.put("minecraft.size", new IntegerArray(925, 530));
        this.d.put("minecraft.fullscreen", false);
        Iterator<ReleaseType> it = ReleaseType.getDefault().iterator();
        while (it.hasNext()) {
            this.d.put("minecraft.versions." + it.next().name().toLowerCase(), true);
        }
        Iterator<ReleaseType.SubType> it2 = ReleaseType.SubType.getDefault().iterator();
        while (it2.hasNext()) {
            this.d.put("minecraft.versions.sub." + it2.next().name().toLowerCase(), true);
        }
        for (Notices.NoticeType type : Notices.NoticeType.values()) {
            if (type.isAdvert()) {
                this.d.put("gui.notice." + type.name().toLowerCase(), true);
            }
        }
        this.d.put(MinecraftSettings.MINECRAFT_SETTING_RAM, Integer.valueOf(OS.Arch.PREFERRED_MEMORY));
        this.d.put("minecraft.onlaunch", ActionOnLaunch.getDefault());
        this.d.put("gui.console", ConsoleType.getDefault());
        this.d.put("gui.console.width", 720);
        this.d.put("gui.console.height", 500);
        this.d.put("gui.console.x", 30);
        this.d.put("gui.console.y", 30);
        this.d.put("connection", ConnectionQuality.getDefault());
        this.d.put("client", UUID.randomUUID());
        this.d.put("gui.statistics.checkbox", false);
        this.d.put("gui.settings.guard.checkbox", true);
        this.d.put("gui.settings.servers.recommendation", true);
        this.d.put("gui.settings.servers.recommendation", true);
        this.d.put(BackupSetting.FREE_PARTITION_SIZE.toString(), Integer.valueOf(inner.getInteger(BackupSetting.FREE_PARTITION_SIZE.toString())));
        this.d.put(BackupSetting.SKIP_USER_BACKUP.toString(), Boolean.valueOf(inner.getBoolean(BackupSetting.SKIP_USER_BACKUP.toString())));
        this.d.put(BackupSetting.MAX_TIME_FOR_BACKUP.toString(), Integer.valueOf(inner.getInteger(BackupSetting.MAX_TIME_FOR_BACKUP.toString())));
        this.d.put(BackupSetting.REPEAT_BACKUP.toString(), Integer.valueOf(inner.getInteger(BackupSetting.REPEAT_BACKUP.toString())));
        this.d.put(BackupSetting.MAX_SIZE_FOR_WORLD.toString(), Integer.valueOf(inner.getInteger(BackupSetting.MAX_SIZE_FOR_WORLD.toString())));
    }

    public static int getVersion() {
        return 3;
    }

    public Map<String, Object> getMap() {
        return Collections.unmodifiableMap(this.d);
    }

    public Object get(String key) {
        return this.d.get(key);
    }
}
