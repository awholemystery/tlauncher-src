package org.tlauncher.util;

import java.io.File;
import java.io.IOException;
import net.minecraft.launcher.updater.LocalVersionList;
import net.minecraft.launcher.updater.VersionFilter;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/TestInstallVersions.class */
public class TestInstallVersions {
    private static final Logger log = Logger.getLogger(TestInstallVersions.class);

    public static void install(Configuration conf) {
        if (conf.getBoolean("run.all.tlauncher.versions") || conf.getBoolean("run.all.official.versions")) {
            U.sleepFor(5000L);
            log.info("******version should be updated******");
            FileUtil.deleteDirectory(new File(MinecraftUtil.getWorkingDirectory(), PathAppUtil.VERSION_DIRECTORY));
            FileUtil.createFolder(new File(MinecraftUtil.getWorkingDirectory(), PathAppUtil.VERSION_DIRECTORY));
            TLauncher.getInstance().getVersionManager().refresh();
        }
        if (conf.getBoolean("run.all.tlauncher.versions")) {
            log.info("******runAllTLauncherVersions = true******");
            LocalVersionList lvl = new LocalVersionList(MinecraftUtil.getWorkingDirectory());
            TLauncher t = TLauncher.getInstance();
            t.getVersionManager().getVersions().stream().filter(v -> {
                return v.getAvailableVersion().getSource().equals(ClientInstanceRepo.EXTRA_VERSION_REPO) || v.getAvailableVersion().getSource().equals(ClientInstanceRepo.SKIN_VERSION_REPO);
            }).forEach(v2 -> {
                log.info(v2.getID());
                VersionSyncInfo vsi = t.getVersionManager().getVersionSyncInfo(v2.getID());
                try {
                    lvl.saveVersion(vsi.getCompleteVersion(false).resolve(t.getVersionManager(), false));
                } catch (IOException e) {
                    log.warn("error", e);
                }
            });
        }
        if (conf.getBoolean("run.all.official.versions")) {
            log.info("******runAllOfficialVersions = true******");
            LocalVersionList lvl2 = new LocalVersionList(MinecraftUtil.getWorkingDirectory());
            FileUtil.deleteDirectory(MinecraftUtil.getWorkingDirectory(PathAppUtil.VERSION_DIRECTORY));
            TLauncher t2 = TLauncher.getInstance();
            t2.getVersionManager().getVersions(new VersionFilter(), false).stream().filter(v3 -> {
                return v3.getAvailableVersion().getSource().equals(ClientInstanceRepo.OFFICIAL_VERSION_REPO);
            }).forEach(v4 -> {
                log.info(v4.getID());
                VersionSyncInfo vsi = t2.getVersionManager().getVersionSyncInfo(v4.getID());
                try {
                    lvl2.saveVersion(vsi.getCompleteVersion(false).resolve(t2.getVersionManager(), false));
                } catch (IOException e) {
                    log.warn("error", e);
                }
            });
        }
    }
}
