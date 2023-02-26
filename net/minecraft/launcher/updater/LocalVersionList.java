package net.minecraft.launcher.updater;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.LogClient;
import net.minecraft.launcher.versions.ModifiedVersion;
import net.minecraft.launcher.versions.json.Argument;
import net.minecraft.launcher.versions.json.ArgumentType;
import org.apache.log4j.Logger;
import org.tlauncher.modpack.domain.client.share.ForgeStringComparator;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/LocalVersionList.class */
public class LocalVersionList extends StreamVersionList {
    private static final Logger log = Logger.getLogger(LocalVersionList.class);
    private File baseDirectory;
    private File baseVersionsDir;

    public LocalVersionList(File baseDirectory) throws IOException {
        setBaseDirectory(baseDirectory);
    }

    public File getBaseDirectory() {
        return this.baseDirectory;
    }

    public void setBaseDirectory(File directory) throws IOException {
        if (directory == null) {
            throw new IllegalArgumentException("Base directory is NULL!");
        }
        FileUtil.createFolder(directory);
        log("Base directory:", directory.getAbsolutePath());
        this.baseDirectory = directory;
        this.baseVersionsDir = new File(this.baseDirectory, PathAppUtil.VERSION_DIRECTORY);
    }

    @Override // net.minecraft.launcher.updater.VersionList
    public synchronized void refreshVersions() {
        clearCache();
        File[] files = this.baseVersionsDir.listFiles();
        if (files == null) {
            return;
        }
        for (File directory : files) {
            String id = directory.getName();
            File jsonFile = new File(directory, id + ".json");
            if (directory.isDirectory() && jsonFile.isFile()) {
                try {
                    CompleteVersion version = (CompleteVersion) this.gson.fromJson(getUrl("versions/" + id + "/" + id + ".json"), (Class<Object>) CompleteVersion.class);
                    if (version == null) {
                        log("JSON descriptor of version \"" + id + "\" in NULL, it won't be added in list as local.");
                    } else if (!"1.17".equals(version.getAssets()) || !Objects.isNull(version.getJavaVersion())) {
                        version.setID(id);
                        version.setSource(ClientInstanceRepo.LOCAL_VERSION_REPO);
                        version.setVersionList(this);
                        if (version.getID().equalsIgnoreCase(version.getInheritsFrom())) {
                            version = renameVersion(version, findNewVersionName(version.getID(), 0));
                        }
                        checkDoubleArgument(version);
                        fixedlog4j(version);
                        addVersion(version);
                    }
                } catch (Throwable ex) {
                    log("Error occurred while parsing local version", id, ex);
                    try {
                        SimpleDateFormat s = new SimpleDateFormat("dd_HH_mm_ss");
                        Files.move(directory.toPath(), Paths.get(directory.toString() + "_error_version_" + s.format(new Date()), new String[0]), new CopyOption[0]);
                    } catch (IOException e) {
                        U.log(e);
                    }
                }
            }
        }
    }

    private void fixedlog4j(CompleteVersion version) throws IOException {
        String id;
        LogClient l;
        Map<String, LogClient> map = version.getLogging();
        ForgeStringComparator comparator = new ForgeStringComparator();
        if (Objects.nonNull(version.getJar())) {
            id = version.getJar();
        } else {
            id = version.getAssets().replaceAll("pre-", CoreConstants.EMPTY_STRING).replaceAll("-af", CoreConstants.EMPTY_STRING).replaceAll("-aprilfools", CoreConstants.EMPTY_STRING).replaceAll("14w30c", "1.7.10").replaceAll("14w31a", "1.7.10").replaceAll("14w25a", "1.7.10");
            if (id.equals(AssetIndex.DEFAULT_ASSET_NAME)) {
                if (version.getID().split(" ").length == 2) {
                    id = version.getID().split(" ")[1];
                } else {
                    id = "1.7";
                }
            }
        }
        try {
            if (comparator.compare("1.6.9", id) == 1 && (Objects.isNull(map) || Objects.isNull(map.get("client")))) {
                Map<String, LogClient> map2 = new HashMap<>();
                if (comparator.compare("1.11.9", id) == -1) {
                    log.info(String.format("set logging %s version type %s, version id %s", "1.7.10.json", id, version.getID()));
                    l = (LogClient) this.gson.fromJson((Reader) new InputStreamReader(FileUtil.getResourceAppStream("/fix_log4j/1.7.10.json")), (Class<Object>) LogClient.class);
                } else {
                    log.info(String.format("set logging %s version type %s, version id %s", "1.12.json", id, version.getID()));
                    l = (LogClient) this.gson.fromJson((Reader) new InputStreamReader(FileUtil.getResourceAppStream("/fix_log4j/1.12.json")), (Class<Object>) LogClient.class);
                }
                map2.put("client", l);
                version.setLogging(map2);
                Path versionPath = MinecraftUtil.buildWorkingPath(PathAppUtil.VERSION_DIRECTORY, version.getID(), version.getID() + ".json");
                FileUtil.writeFile(versionPath.toFile(), this.gson.toJson((JsonElement) serializeVersion(version).getAsJsonObject()));
                saveVersion(version);
            }
        } catch (NumberFormatException e) {
            log.warn("can't patch fix log4j" + id);
        }
    }

    private void checkDoubleArgument(CompleteVersion version) throws IOException {
        Map<ArgumentType, List<Argument>> map = version.getArguments();
        if (Objects.nonNull(map)) {
            List<Argument> list = map.get(ArgumentType.GAME);
            if (!Objects.isNull(list) && list.size() > 30) {
                Set<String> set = new HashSet<>();
                list.removeIf(e -> {
                    String value = Arrays.toString(e.getValues());
                    if (set.contains(value)) {
                        U.log("removed arg " + e);
                        return true;
                    }
                    set.add(value);
                    return false;
                });
                saveVersion(version);
            }
        }
    }

    private String findNewVersionName(String name, int i) {
        String newName = name + " fixed " + i;
        File f = new File(this.baseVersionsDir, newName);
        if (f.exists()) {
            return findNewVersionName(name, i + 1);
        }
        return newName;
    }

    public synchronized void saveVersion(CompleteVersion version) throws IOException {
        fixedlog4j(version);
        Path versionPath = MinecraftUtil.buildWorkingPath(PathAppUtil.VERSION_DIRECTORY, version.getID(), version.getID() + ".json");
        Path modifiedVersionPath = MinecraftUtil.buildWorkingPath(PathAppUtil.VERSION_DIRECTORY, version.getID(), "TLauncherAdditional.json");
        ModifiedVersion mv = version.getModifiedVersion();
        if (Objects.nonNull(version.getRemoteVersion())) {
            FileUtil.writeFile(versionPath.toFile(), version.getRemoteVersion());
        } else if (Files.notExists(versionPath, new LinkOption[0]) || !ClientInstanceRepo.LOCAL_VERSION_REPO.equals(mv.getSource())) {
            FileUtil.writeFile(versionPath.toFile(), this.gson.toJson((JsonElement) serializeVersion(version).getAsJsonObject()));
        }
        mv.setSource(ClientInstanceRepo.LOCAL_VERSION_REPO);
        FileUtil.writeFile(modifiedVersionPath.toFile(), this.gson.toJson(mv));
    }

    public synchronized void deleteVersion(String id, boolean deleteLibraries) throws IOException {
        CompleteVersion version = getCompleteVersion(id);
        if (version == null) {
            throw new IllegalArgumentException("Version is not installed! id = " + id);
        }
        File dir = new File(this.baseVersionsDir, id + '/');
        if (!dir.isDirectory()) {
            throw new IOException("Cannot find directory: " + dir.getAbsolutePath());
        }
        FileUtil.deleteDirectory(dir);
        if (!deleteLibraries) {
            return;
        }
        for (File library : version.getClassPath(this.baseDirectory)) {
            FileUtil.deleteFile(library);
        }
        for (String nativeLib : version.getNatives()) {
            FileUtil.deleteFile(new File(this.baseDirectory, nativeLib));
        }
    }

    @Override // net.minecraft.launcher.updater.StreamVersionList
    protected InputStream getInputStream(String uri) throws IOException {
        return new FileInputStream(new File(this.baseDirectory, uri));
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0018  */
    @Override // net.minecraft.launcher.updater.VersionList
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean hasAllFiles(net.minecraft.launcher.versions.CompleteVersion r6, org.tlauncher.util.OS r7) {
        /*
            r5 = this;
            r0 = r6
            r1 = r7
            java.util.Set r0 = r0.getRequiredFiles(r1)
            r8 = r0
            r0 = r8
            java.util.Iterator r0 = r0.iterator()
            r9 = r0
        Le:
            r0 = r9
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L4a
            r0 = r9
            java.lang.Object r0 = r0.next()
            java.lang.String r0 = (java.lang.String) r0
            r10 = r0
            java.io.File r0 = new java.io.File
            r1 = r0
            r2 = r5
            java.io.File r2 = r2.baseDirectory
            r3 = r10
            r1.<init>(r2, r3)
            r11 = r0
            r0 = r11
            boolean r0 = r0.isFile()
            if (r0 == 0) goto L45
            r0 = r11
            long r0 = r0.length()
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L47
        L45:
            r0 = 0
            return r0
        L47:
            goto Le
        L4a:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: net.minecraft.launcher.updater.LocalVersionList.hasAllFiles(net.minecraft.launcher.versions.CompleteVersion, org.tlauncher.util.OS):boolean");
    }

    public synchronized CompleteVersion renameVersion(CompleteVersion version, String name) throws IOException {
        CompleteVersion newVersion = version.fullCopy(new CompleteVersion());
        newVersion.setID(name);
        if (Objects.nonNull(version.getModpack())) {
            newVersion.getModpack().setName(name);
        }
        File newFolder = new File(this.baseVersionsDir.toString(), newVersion.getID());
        if (newFolder.exists()) {
            throw new IOException("folder exists " + newFolder);
        }
        String oldName = version.getID();
        File oldJar = new File(this.baseVersionsDir.toString(), version.getID() + "/" + version.getID() + ".jar");
        File newJar = new File(this.baseVersionsDir.toString(), version.getID() + "/" + newVersion.getID() + ".jar");
        if (oldJar.exists() && !oldJar.renameTo(newJar)) {
            throw new IOException("can't rename from " + oldJar + "to " + newJar);
        }
        File oldFolder = new File(this.baseVersionsDir.toString(), oldName);
        if (!oldFolder.renameTo(newFolder)) {
            throw new IOException("can't rename from " + version.getID() + "to " + newVersion.getID());
        }
        FileUtil.deleteFile(new File(this.baseVersionsDir.toString(), newVersion.getID() + "/" + version.getID() + ".json"));
        saveVersion(newVersion);
        return newVersion;
    }

    public synchronized void refreshLocalVersion(CompleteVersion version) throws IOException {
        saveVersion(version);
        version.setSource(ClientInstanceRepo.LOCAL_VERSION_REPO);
        version.setVersionList(this);
        this.byName.put(version.getID(), version);
        for (int i = 0; i < this.versions.size(); i++) {
            if (this.versions.get(i).getID().equalsIgnoreCase(version.getID())) {
                this.versions.remove(i);
                this.versions.add(i, version);
                return;
            }
        }
    }
}
