package org.tlauncher.tlauncher.minecraft.launcher.assitent;

import by.gdev.utils.service.FileMapperService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.configuration.enums.BackupSetting;
import org.tlauncher.tlauncher.entity.BackupWorldList;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.U;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/assitent/BackupWorldAssistant.class */
public class BackupWorldAssistant extends MinecraftLauncherAssistantWrapper {
    private static final Logger log = Logger.getLogger(BackupWorldAssistant.class);
    @Inject
    private TLauncher tLauncher;
    @Inject
    private FileMapperService fileMapperService;
    private BackupWorldList backupWorldList;
    @Inject
    private Gson gson;

    @Inject
    public void init() throws IOException {
        this.backupWorldList = new BackupWorldList();
        File worldJson = new File(MinecraftUtil.getWorkingDirectory(), PathAppUtil.WORLDS_BACKUP_CONFIGURATION);
        FileUtil.createFile(worldJson);
        this.backupWorldList = (BackupWorldList) this.fileMapperService.read(PathAppUtil.WORLDS_BACKUP_CONFIGURATION, BackupWorldList.class);
        if (Objects.isNull(this.backupWorldList)) {
            this.backupWorldList = new BackupWorldList();
        }
        if (log.isDebugEnabled()) {
            log.debug(this.gson.toJson(this.backupWorldList));
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantWrapper, org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantInterface
    public void constructJavaArguments(MinecraftLauncher launcher) throws MinecraftException {
        try {
            if (isBackupAvailable()) {
                this.tLauncher.getFrame().mp.getProgress().onWorldBackup();
                collectNewWorlds();
                checkIfWorldsAreChanged();
                checkIfModpackWorldsAreChanged();
                backupWorld();
                saveWorlds();
            }
            deleteOldBackupOrNotExistedWorlds();
        } catch (IOException e) {
            U.log(e.getMessage());
        }
    }

    private boolean isBackupAvailable() {
        boolean res = false;
        if (!this.tLauncher.getConfiguration().getBoolean(BackupSetting.SKIP_USER_BACKUP.toString())) {
            int requestFreeGB = this.tLauncher.getConfiguration().getInteger(BackupSetting.FREE_PARTITION_SIZE.toString());
            res = FileUtil.checkFreeSpace(MinecraftUtil.getWorkingDirectory(), requestFreeGB * 1024 * 1024);
            if (!res) {
                log.warn("Free space on partition doesn't enough for backup : " + (MinecraftUtil.getWorkingDirectory().getFreeSpace() / Math.pow(1024.0d, 3.0d)));
            }
        }
        log.info("backup world is active: " + res);
        return res;
    }

    private void saveWorlds() throws IOException {
        log.debug("saved backup world configuration");
        this.fileMapperService.write(this.backupWorldList, PathAppUtil.WORLDS_BACKUP_CONFIGURATION);
    }

    private void collectNewWorlds() throws IOException {
        Path simpleWorlds = MinecraftUtil.buildWorkingPath(PathAppUtil.DIRECTORY_WORLDS);
        Path versions = MinecraftUtil.buildWorkingPath(PathAppUtil.VERSION_DIRECTORY);
        Path destForSimpleWorld = MinecraftUtil.buildWorkingPath(PathAppUtil.BACKUP_DIRECTORY, PathAppUtil.DIRECTORY_WORLDS);
        Path destForModPackWorlds = MinecraftUtil.buildWorkingPath(PathAppUtil.BACKUP_DIRECTORY, PathAppUtil.DIRECTORY_MODPACK_WORLDS);
        Files.createDirectories(destForSimpleWorld, new FileAttribute[0]);
        Files.createDirectories(destForModPackWorlds, new FileAttribute[0]);
        addWorldToBackupSet(destForSimpleWorld.toFile(), Files.walk(simpleWorlds, 1, new FileVisitOption[0]));
        Files.walk(versions, 1, new FileVisitOption[0]).filter(version -> {
            return new File(version + "/" + PathAppUtil.DIRECTORY_WORLDS).exists();
        }).forEach(version2 -> {
            try {
                Stream<Path> walk2 = Files.walk(Paths.get(destForModPackWorlds.toString(), PathAppUtil.DIRECTORY_WORLDS), 1, new FileVisitOption[0]);
                addWorldToBackupSet(destForModPackWorlds.toFile(), walk2);
            } catch (IOException e) {
                U.log(e.getMessage());
            }
        });
    }

    private void addWorldToBackupSet(File dest, Stream<Path> walk) {
        walk.filter(world -> {
            return !world.toFile().getName().equals(PathAppUtil.DIRECTORY_WORLDS);
        }).forEach(world2 -> {
            try {
                LocalDateTime time = LocalDateTime.ofInstant(((FileTime) Files.getAttribute(dest, BackupSetting.LAST_MODIFIED_TIME.toString(), new LinkOption[0])).toInstant(), ZoneId.systemDefault());
                if (this.backupWorldList.getWorlds().stream().anyMatch(backup -> {
                    return backup.getSource().equals(dest.toString());
                })) {
                    return;
                }
                File destination = new File(dest, dest.getFileName().toString());
                this.backupWorldList.getWorlds().add(new BackupWorldList.BackupWorld(dest.toFile().getName(), dest.toString(), destination.getAbsolutePath(), time.withNano(0).toString(), false));
                log.info("Add new world to backup list: " + dest.toFile().getName());
            } catch (IOException e) {
                log.warn("exception", e);
            }
        });
    }

    private void backupWorld() {
        this.backupWorldList.getWorlds().stream().filter((v0) -> {
            return v0.isBackup();
        }).filter(b -> {
            return Files.isDirectory(Paths.get(b.getSource(), new String[0]), new LinkOption[0]);
        }).forEach(backupWorld -> {
            log.debug("backup world: " + backupWorld.getName());
            try {
                LocalDateTime time = LocalDateTime.ofInstant(((FileTime) Files.getAttribute(Paths.get(backupWorld.getSource(), new String[0]), BackupSetting.LAST_MODIFIED_TIME.toString(), new LinkOption[0])).toInstant(), ZoneId.systemDefault());
                backupWorld.setLastChanged(time.withNano(0).toString());
                backupWorld.setBackup(false);
                FileUtil.createFolder(new File(backupWorld.getDestination()));
                File dest = Paths.get(backupWorld.getDestination(), backupWorld.getName().concat("_").concat(backupWorld.getLastChanged().replaceAll(":", "_")).concat(".zip")).toFile();
                FileUtil.zipFolder(new File(backupWorld.getSource()), dest);
            } catch (Exception e) {
                log.warn("exception", e);
            }
        });
    }

    private void deleteOldBackupOrNotExistedWorlds() throws IOException {
        Files.walk(MinecraftUtil.buildWorkingPath(PathAppUtil.BACKUP_DIRECTORY), new FileVisitOption[0]).filter(backupWorld -> {
            return backupWorld.toFile().isFile();
        }).filter(backupWorld2 -> {
            try {
                LocalDateTime time = LocalDateTime.ofInstant(((FileTime) Files.getAttribute(Paths.get(backupWorld2.toString(), new String[0]), BackupSetting.LAST_MODIFIED_TIME.toString(), new LinkOption[0])).toInstant(), ZoneId.systemDefault());
                long daysBetween = ChronoUnit.DAYS.between(time, LocalDateTime.now());
                return daysBetween > ((long) this.tLauncher.getConfiguration().getInteger(BackupSetting.MAX_TIME_FOR_BACKUP.toString()));
            } catch (IOException e) {
                U.log(e.getMessage());
                return false;
            }
        }).forEach(oldWorld -> {
            log.info("Deleted world: " + oldWorld.toAbsolutePath());
            FileUtil.deleteFile(oldWorld.toFile());
        });
        Set<BackupWorldList.BackupWorld> existedWorlds = (Set) this.backupWorldList.getWorlds().stream().filter(backupWorld3 -> {
            return Paths.get(backupWorld3.getSource(), new String[0]).toFile().exists();
        }).collect(Collectors.toSet());
        if (existedWorlds.size() < this.backupWorldList.getWorlds().size()) {
            this.backupWorldList.setWorlds(existedWorlds);
            saveWorlds();
        }
    }

    private void checkIfModpackWorldsAreChanged() throws IOException {
        List<Path> versions = (List) Files.walk(MinecraftUtil.buildWorkingPath(PathAppUtil.VERSION_DIRECTORY), 1, new FileVisitOption[0]).filter(x$0 -> {
            return Files.isDirectory(x$0, new LinkOption[0]);
        }).collect(Collectors.toList());
        for (Path version : versions) {
            Path versionSaves = Paths.get(version.toString(), PathAppUtil.DIRECTORY_WORLDS);
            if (!Files.notExists(versionSaves, new LinkOption[0])) {
                checkIfWorldsAreChanged((List) Files.walk(versionSaves, 1, new FileVisitOption[0]).collect(Collectors.toList()));
            }
        }
    }

    private void checkIfWorldsAreChanged() throws IOException {
        Path source = MinecraftUtil.buildWorkingPath(PathAppUtil.DIRECTORY_WORLDS);
        checkIfWorldsAreChanged((List) Files.walk(source, 1, new FileVisitOption[0]).collect(Collectors.toList()));
    }

    private void checkIfWorldsAreChanged(List<Path> files) throws IOException {
        for (Path world : files) {
            FileTime creationTime = (FileTime) Files.getAttribute(world, BackupSetting.LAST_MODIFIED_TIME.toString(), new LinkOption[0]);
            LocalDateTime convertedFileTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault());
            Optional<BackupWorldList.BackupWorld> backupWorld1 = this.backupWorldList.getWorlds().stream().filter(backupWorld -> {
                return backupWorld.getSource().equals(world.toString());
            }).findAny();
            if (backupWorld1.isPresent()) {
                String worldTime = backupWorld1.get().getLastChanged();
                if (Paths.get(backupWorld1.get().getDestination(), new String[0]).toFile().listFiles() == null) {
                    backupWorld1.get().setBackup(true);
                } else if (!worldTime.equals(convertedFileTime.withNano(0).toString())) {
                    if (Paths.get(backupWorld1.get().getDestination(), new String[0]).toFile().listFiles() == null) {
                        backupWorld1.get().setBackup(true);
                    } else if (!isProperBackupWorld(backupWorld1.get())) {
                        backupWorld1.get().setBackup(true);
                    }
                }
            }
        }
    }

    private boolean isProperBackupWorld(BackupWorldList.BackupWorld backupWorld) throws IOException {
        long lastBackupSize = Paths.get(backupWorld.getDestination(), backupWorld.getName().concat("_").concat(backupWorld.getLastChanged().replaceAll(":", "_").concat(".zip"))).toFile().length();
        LocalDateTime backupLastChanged = LocalDateTime.parse(backupWorld.getLastChanged());
        long totalHoursPassed = Duration.between(backupLastChanged, LocalDateTime.now()).toHours();
        if (this.tLauncher.getConfiguration().getInteger(BackupSetting.REPEAT_BACKUP.toString()) == 1) {
            return totalHoursPassed < 24;
        } else if (lastBackupSize / Math.pow(1024.0d, 2.0d) > this.tLauncher.getConfiguration().getInteger(BackupSetting.MAX_SIZE_FOR_WORLD.toString())) {
            return true;
        } else {
            return false;
        }
    }
}
