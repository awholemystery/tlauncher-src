package org.tlauncher.tlauncher.managers;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.Library;
import net.minecraft.launcher.versions.Version;
import net.minecraft.launcher.versions.json.Argument;
import net.minecraft.launcher.versions.json.ArgumentType;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.component.RefreshableComponent;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.entity.TLauncherLib;
import org.tlauncher.tlauncher.entity.TLauncherVersionChanger;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.gson.DownloadUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/TLauncherManager.class */
public class TLauncherManager extends RefreshableComponent implements MinecraftListener {
    private static final Logger log = LoggerFactory.getLogger(TLauncherManager.class);
    public static final String CLIENT_TL_MANAGER = "_tl_manager";
    private TLauncherVersionChanger tLauncherVersionChanger;
    private final Gson gson;

    public TLauncherManager(ComponentManager manager) throws Exception {
        super(manager);
        this.gson = new Gson();
    }

    public boolean useTLauncherAccount(Version version) {
        return (Objects.nonNull(this.tLauncherVersionChanger) && this.tLauncherVersionChanger.getTlauncherSkinCapeVersion().contains(version.getID())) || version.isSkinVersion() || version.isActivateSkinCapeForUserVersion();
    }

    private List<TLauncherLib> findAddedLibraries(CompleteVersion complete) {
        String id = this.tLauncherVersionChanger.getVersionIDForUserSkinCapeVersion(complete);
        ArrayList<TLauncherLib> libList = new ArrayList<>();
        for (TLauncherLib tlauncherLib : this.tLauncherVersionChanger.getLibraries()) {
            if (tlauncherLib.isSupport(id)) {
                libList.add(tlauncherLib);
            } else {
                for (Library library : complete.getLibraries()) {
                    if (tlauncherLib.isApply(library, complete)) {
                        libList.add(tlauncherLib);
                    }
                }
            }
        }
        return libList;
    }

    public boolean applyTLauncherAccountLib(CompleteVersion original) {
        Configuration settings = TLauncher.getInstance().getConfiguration();
        Account ac = TLauncher.getInstance().getProfileManager().getSelectedAccount();
        if (Account.AccountType.OFFICIAL_ACCOUNTS.contains(ac.getType()) || !settings.getBoolean("skin.status.checkbox.state")) {
            return false;
        }
        return useTLauncherAccount(original);
    }

    public CompleteVersion addFilesForDownloading(CompleteVersion original, boolean customTexture) {
        if (Objects.isNull(this.tLauncherVersionChanger)) {
            return original;
        }
        log("add required libraries:", original.getID());
        CompleteVersion complete = original.fullCopy(new CompleteVersion());
        for (TLauncherLib lib : findAddedLibraries(original)) {
            complete.getLibraries().addAll(lib.getRequires());
            complete.getLibraries().add(lib);
            addMinecraftClientFiles(complete, lib, CLIENT_TL_MANAGER, false);
        }
        for (TLauncherLib l : this.tLauncherVersionChanger.getAddedMods(complete)) {
            complete.getLibraries().addAll(l.getRequires());
            complete.getLibraries().add(l);
        }
        return complete;
    }

    private void addMinecraftClientFiles(CompleteVersion complete, TLauncherLib lib, String postfix, boolean showLog) {
        if (Objects.nonNull(lib.getDownloads()) && Objects.nonNull(complete.getDownloads())) {
            for (Map.Entry<String, MetadataDTO> e : lib.getDownloads().entrySet()) {
                complete.getDownloads().put(e.getKey() + postfix, e.getValue());
                if (showLog) {
                    log(String.format("new client will be put: %s", e.getValue().getUrl()));
                }
            }
        }
    }

    @Override // org.tlauncher.tlauncher.component.RefreshableComponent
    protected boolean refresh() {
        try {
            this.tLauncherVersionChanger = (TLauncherVersionChanger) DownloadUtil.loadObjectByKey("skin.config.library", TLauncherVersionChanger.class);
            return true;
        } catch (Throwable e) {
            log("Failed to refresh TLancher manager", e);
            return false;
        }
    }

    public CompleteVersion createUpdatedVersion(CompleteVersion original, boolean showLog) {
        CompleteVersion complete = original.fullCopy(new CompleteVersion());
        transferVersionArgumentToModern(complete);
        if (Objects.isNull(this.tLauncherVersionChanger)) {
            return complete;
        }
        List<TLauncherLib> libList = findAddedLibraries(original);
        boolean tlLibraryType = applyTLauncherAccountLib(complete);
        for (TLauncherLib lib : libList) {
            if (lib.isProperAccountTypeLib(tlLibraryType)) {
                boolean added = false;
                List<Library> list = complete.getLibraries();
                int i = 0;
                while (true) {
                    if (i >= complete.getLibraries().size()) {
                        break;
                    }
                    Library current = complete.getLibraries().get(i);
                    if (!lib.isApply(current, complete)) {
                        i++;
                    } else {
                        if (showLog) {
                            log.info("library will be replaced: {} -> {}", current.getName(), lib.getName());
                        }
                        complete.getLibraries().remove(i);
                        complete.getLibraries().add(i, lib);
                        added = true;
                        setAdditionalFields(complete, lib);
                        List<Library> requiredLibraries = lib.getRequires();
                        addedRequiredLibraries(list, requiredLibraries, showLog);
                    }
                }
                if (!added) {
                    addedRequiredLibraries(complete.getLibraries(), lib.getRequires(), showLog);
                    complete.getLibraries().add(0, lib);
                    if (showLog) {
                        log("library will be added:", lib.getName());
                    }
                    setAdditionalFields(complete, lib);
                }
                addMinecraftClientFiles(complete, lib, CoreConstants.EMPTY_STRING, showLog);
            }
        }
        return complete;
    }

    private void transferVersionArgumentToModern(CompleteVersion completeVersion) {
        String s = completeVersion.getMinecraftArguments();
        if (Objects.isNull(s)) {
            return;
        }
        String[] array = s.replace("  ", " ").split(" ");
        List<Argument> arrayList = new ArrayList<>();
        Arrays.stream(array).forEach(e -> {
            arrayList.add(new Argument(new String[]{e}, null));
        });
        arrayList.add(new Argument(new String[]{"--width"}, null));
        arrayList.add(new Argument(new String[]{"${resolution_width}"}, null));
        arrayList.add(new Argument(new String[]{"--height"}, null));
        arrayList.add(new Argument(new String[]{"${resolution_height}"}, null));
        Map<ArgumentType, List<Argument>> map = new HashMap<>();
        map.put(ArgumentType.GAME, arrayList);
        List<Argument> jvm = new ArrayList<>();
        if (Objects.nonNull(completeVersion.getJVMArguments())) {
            String[] array2 = completeVersion.getJVMArguments().replace("  ", " ").split(" ");
            Arrays.stream(array2).forEach(e2 -> {
                jvm.add(new Argument(new String[]{e2}, null));
            });
        }
        jvm.add(new Argument(new String[]{"-Djava.library.path=${natives_directory}"}, null));
        jvm.add(new Argument(new String[]{"-cp"}, null));
        jvm.add(new Argument(new String[]{"${classpath}"}, null));
        map.put(ArgumentType.JVM, jvm);
        completeVersion.setArguments(map);
    }

    private void addedRequiredLibraries(List<Library> list, List<Library> requiredLibraries, boolean showLog) {
        if (Objects.isNull(requiredLibraries)) {
            return;
        }
        for (Library r : requiredLibraries) {
            boolean addedRequiredLib = false;
            int j = 0;
            while (true) {
                if (j >= list.size()) {
                    break;
                }
                Library l = list.get(j);
                if (!r.getPlainName().equals(l.getPlainName())) {
                    j++;
                } else {
                    if (showLog) {
                        log("library will be replaced as required:", r.getName());
                    }
                    list.remove(j);
                    list.add(j, r);
                    addedRequiredLib = true;
                }
            }
            if (!addedRequiredLib) {
                list.add(0, r);
                if (showLog) {
                    log("library will be added as required:", r.getName());
                }
            }
        }
    }

    private void setAdditionalFields(CompleteVersion complete, TLauncherLib lib) {
        Map<ArgumentType, List<Argument>> arguments = lib.getArguments();
        if (Objects.nonNull(arguments)) {
            for (Map.Entry<ArgumentType, List<Argument>> arg : arguments.entrySet()) {
                ArgumentType type = arg.getKey();
                List<Argument> libArguments = arg.getValue();
                List<Argument> versionArguments = complete.getArguments().get(type);
                for (Argument a : libArguments) {
                    boolean added = false;
                    int i = 0;
                    while (true) {
                        if (i < versionArguments.size()) {
                            if (!Arrays.equals(a.getValues(), versionArguments.get(i).getValues())) {
                                i++;
                            } else {
                                versionArguments.remove(i);
                                versionArguments.add(i, a);
                                added = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!added) {
                        versionArguments.add(a);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(lib.getMainClass())) {
            complete.setMainClass(lib.getMainClass());
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftPrepare() {
        cleanMods();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftAbort() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftLaunch() {
        File optifineTempFile = new File(MinecraftUtil.getWorkingDirectory(), TLauncher.getInnerSettings().get("skin.config.temp.optifine.file.new"));
        Path mods = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), "mods");
        CompleteVersion completeVersion = TLauncher.getInstance().getLauncher().getVersion();
        if (completeVersion.isModpack()) {
            mods = ModpackUtil.getPathByVersion(completeVersion, "mods");
        }
        if (!Files.exists(mods, new LinkOption[0])) {
            try {
                Files.createDirectory(mods, new FileAttribute[0]);
            } catch (IOException e1) {
                log(e1);
            }
        }
        List<String> filesMods = readMods();
        if (completeVersion.getID().startsWith("ForgeOptiFine")) {
            Iterator<Library> it = completeVersion.getLibraries().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Library library = it.next();
                if (library.getName().contains("optifine:OptiFine")) {
                    Path out = copyMods(library);
                    filesMods.add(out.toString());
                    break;
                }
            }
        }
        if (completeVersion.getModsLibraries() != null) {
            List<Library> libraries = completeVersion.getModsLibraries();
            copyListMods(filesMods, libraries);
        }
        if (Objects.nonNull(this.tLauncherVersionChanger)) {
            List<TLauncherLib> libraries2 = this.tLauncherVersionChanger.getAddedMods(completeVersion, applyTLauncherAccountLib(completeVersion));
            copyListMods(filesMods, libraries2);
        }
        try {
            String result = this.gson.toJson(filesMods, new TypeToken<ArrayList<String>>() { // from class: org.tlauncher.tlauncher.managers.TLauncherManager.1
            }.getType());
            FileUtil.writeFile(optifineTempFile, result);
        } catch (IOException e) {
            log(e);
        }
        printModsFiles("mods after", mods);
    }

    private void copyListMods(List<String> filesMods, List<? extends Library> libraries) {
        for (Library lib : libraries) {
            filesMods.add(copyMods(lib).toString());
        }
    }

    private Path copyMods(Library library) {
        Path in = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), "libraries", library.getArtifactPath());
        Path out = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), "mods", FilenameUtils.getName(library.getArtifactPath()));
        try {
            Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log(e);
        }
        return out;
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftClose() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftError(Throwable e) {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftKnownError(MinecraftException e) {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftCrash(Crash crash) {
    }

    public void cleanMods() {
        Path mods1 = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), "mods");
        printModsFiles("before clearLibrary", mods1);
        Path old = FileUtil.getRelativeConfig("skin.config.temp.optifine.file");
        Path mods = FileUtil.getRelativeConfig("skin.config.temp.optifine.file.new");
        if (Files.exists(old, new LinkOption[0])) {
            try {
                log("clear old library");
                Files.delete(Paths.get(FileUtil.readFile(old.toFile()), new String[0]));
                Files.delete(old);
            } catch (IOException exception) {
                log(exception);
            } catch (InvalidPathException e) {
                try {
                    Files.delete(old);
                } catch (IOException e2) {
                    log(e2);
                }
            }
        }
        cleanMods(mods);
        printModsFiles("after clearLibrary", mods1);
    }

    private void cleanMods(Path mods) {
        if (Files.exists(mods, new LinkOption[0])) {
            List<String> list = readMods();
            if (!list.isEmpty()) {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    String file = it.next();
                    try {
                        Files.delete(Paths.get(file, new String[0]));
                        it.remove();
                    } catch (IOException exception) {
                        log(exception.getMessage());
                    } catch (InvalidPathException | NoSuchFileException e) {
                        it.remove();
                        log(e);
                    }
                }
            }
            writeStateMod(list);
        }
    }

    private void writeStateMod(List<String> list) {
        try {
            String result = this.gson.toJson(list, new TypeToken<ArrayList<String>>() { // from class: org.tlauncher.tlauncher.managers.TLauncherManager.2
            }.getType());
            FileUtil.writeFile(FileUtil.getRelativeConfigFile("skin.config.temp.optifine.file.new"), result);
            log("written: ", result);
        } catch (IOException e) {
            log(e);
        }
    }

    private void printModsFiles(String state, Path mods) {
        File[] files = mods.toFile().listFiles((v0) -> {
            return v0.isFile();
        });
        if (Objects.nonNull(files)) {
            log(state, files);
        }
    }

    private List<String> readMods() {
        Path mods = FileUtil.getRelativeConfig("skin.config.temp.optifine.file.new");
        List<String> list = null;
        if (Files.exists(mods, new LinkOption[0])) {
            try {
                list = (List) this.gson.fromJson(FileUtil.readFile(mods.toFile(), "utf-8"), new TypeToken<ArrayList<String>>() { // from class: org.tlauncher.tlauncher.managers.TLauncherManager.3
                }.getType());
            } catch (JsonSyntaxException | IOException e) {
                log(e);
            }
        }
        return list == null ? new ArrayList() : list;
    }
}
