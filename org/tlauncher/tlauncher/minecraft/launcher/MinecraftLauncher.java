package org.tlauncher.tlauncher.minecraft.launcher;

import ch.jamiete.mcping.MinecraftPing;
import ch.jamiete.mcping.MinecraftPingOptions;
import ch.qos.logback.core.CoreConstants;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Character;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.SwingUtilities;
import net.minecraft.launcher.process.JavaProcess;
import net.minecraft.launcher.process.JavaProcessLauncher;
import net.minecraft.launcher.process.JavaProcessListener;
import net.minecraft.launcher.updater.AssetIndex;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.ExtractRules;
import net.minecraft.launcher.versions.Library;
import net.minecraft.launcher.versions.LogClient;
import net.minecraft.launcher.versions.json.Argument;
import net.minecraft.launcher.versions.json.ArgumentType;
import net.minecraft.launcher.versions.json.DateTypeAdapter;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.cookie.ClientCookie;
import org.apache.log4j.spi.Configurator;
import org.tlauncher.tlauncher.component.LogClientConfigurationComponent;
import org.tlauncher.tlauncher.component.minecraft.JavaMinecraftComponent;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.downloader.AbortedDownloadException;
import org.tlauncher.tlauncher.downloader.DefaultDownloadableContainerHandler;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
import org.tlauncher.tlauncher.entity.server.RemoteServer;
import org.tlauncher.tlauncher.entity.server.Server;
import org.tlauncher.tlauncher.managers.AssetsManager;
import org.tlauncher.tlauncher.managers.ComponentManager;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.crash.CrashDescriptor;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.AdditionalFileAssistanceFactory;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.BackupWorldAssistant;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantInterface;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.service.XmlLogDeserialization;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.alert.Notification;
import org.tlauncher.tlauncher.ui.console.Console;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.settings.MinecraftSettings;
import org.tlauncher.tlauncher.ui.swing.notification.skin.SkinNotification;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;
import org.tlauncher.util.guice.LanguageAssistFactory;
import org.tlauncher.util.guice.SoundAssistFactory;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/MinecraftLauncher.class */
public class MinecraftLauncher implements JavaProcessListener {
    private final Thread parentThread;
    private final Gson gson;
    private final DateTypeAdapter dateAdapter;
    private final Downloader downloader;
    private final Configuration settings;
    private final boolean forceUpdate;
    private final boolean assistLaunch;
    private final VersionManager vm;
    private final AssetsManager am;
    private final ProfileManager pm;
    private final List<MinecraftListener> listeners;
    private final List<MinecraftExtendedListener> extListeners;
    private final List<MinecraftLauncherAssistantInterface> assistants;
    private boolean working;
    private boolean killed;
    @Named("console")
    @Inject
    private Console console;
    private final CrashDescriptor descriptor;
    private MinecraftLauncherStep step;
    @Inject
    private ModpackManager modpackManager;
    @Inject
    private SoundAssistFactory soundAssistFactory;
    @Inject
    private AdditionalFileAssistanceFactory additionalFileAssistanceFactory;
    @Inject
    private LanguageAssistFactory languageAssistFactory;
    @Inject
    JavaMinecraftComponent javaMinecraftComponent;
    @Inject
    private BackupWorldAssistant backupWorldAssistant;
    @Inject
    private LogClientConfigurationComponent logClientConfigurationComponent;
    @Inject
    private XmlLogDeserialization xmlLogDeserialization;
    private String versionName;
    private VersionSyncInfo versionSync;
    private CompleteVersion version;
    private CompleteVersion deJureVersion;
    private Account account;
    private int javaVersion;
    private MinecraftJava.CompleteMinecraftJava java;
    private File javaDir;
    private File gameDir;
    private File runningMinecraftDir;
    private File localAssetsDir;
    private File nativeDir;
    private File globalAssetsDir;
    private File assetsIndexesDir;
    private File assetsObjectsDir;
    private int[] windowSize;
    private boolean fullScreen;
    private int ramSize;
    private JavaProcessLauncher launcher;
    private String javaArgs;
    private String programArgs;
    private volatile boolean minecraftWorking;
    private long startupTime;
    private int exitCode;
    private RemoteServer server;
    private boolean clearNatives;
    private JavaProcess process;
    private boolean firstLine;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/MinecraftLauncher$ConsoleVisibility.class */
    public enum ConsoleVisibility {
        ALWAYS,
        ON_CRASH,
        NONE
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/MinecraftLauncher$MinecraftLauncherStep.class */
    public enum MinecraftLauncherStep {
        NONE,
        COLLECTING,
        DOWNLOADING,
        CONSTRUCTING,
        LAUNCHING,
        POSTLAUNCH
    }

    public int getJavaVersion() {
        return this.javaVersion;
    }

    public void setJavaVersion(int javaVersion) {
        this.javaVersion = javaVersion;
    }

    public MinecraftJava.CompleteMinecraftJava getJava() {
        return this.java;
    }

    public void setJava(MinecraftJava.CompleteMinecraftJava java) {
        this.java = java;
    }

    public File getJavaDir() {
        return this.javaDir;
    }

    public void setJavaDir(File javaDir) {
        this.javaDir = javaDir;
    }

    public long getStartupTime() {
        return this.startupTime;
    }

    private MinecraftLauncher(ComponentManager manager, Downloader downloader, Configuration configuration, boolean forceUpdate, ConsoleVisibility visibility, boolean exit) {
        this.assistants = new ArrayList();
        this.javaVersion = 8;
        this.firstLine = true;
        if (manager == null) {
            throw new NullPointerException("Ti sovsem s duba ruhnul?");
        }
        if (downloader == null) {
            throw new NullPointerException("Downloader is NULL!");
        }
        if (configuration == null) {
            throw new NullPointerException("Configuration is NULL!");
        }
        if (visibility == null) {
            throw new NullPointerException("ConsoleVisibility is NULL!");
        }
        this.parentThread = Thread.currentThread();
        this.gson = new Gson();
        this.dateAdapter = new DateTypeAdapter();
        this.downloader = downloader;
        this.settings = configuration;
        this.vm = (VersionManager) manager.getComponent(VersionManager.class);
        this.am = (AssetsManager) manager.getComponent(AssetsManager.class);
        this.pm = (ProfileManager) manager.getComponent(ProfileManager.class);
        this.forceUpdate = forceUpdate;
        this.assistLaunch = !exit;
        this.descriptor = new CrashDescriptor(this);
        this.listeners = Collections.synchronizedList(new ArrayList());
        this.extListeners = Collections.synchronizedList(new ArrayList());
        this.step = MinecraftLauncherStep.NONE;
        log("Running under TLauncher " + TLauncher.getVersion());
    }

    @Inject
    public MinecraftLauncher(@Assisted TLauncher t, @Assisted boolean forceUpdate) {
        this(t.getManager(), t.getDownloader(), t.getConfiguration(), forceUpdate, t.getConfiguration().getConsoleType().getVisibility(), t.getConfiguration().getActionOnLaunch() == ActionOnLaunch.EXIT);
    }

    @Inject
    public void postInit() {
        this.assistants.add(this.soundAssistFactory.create(this));
        this.assistants.add(this.additionalFileAssistanceFactory.create(this));
        this.assistants.add(this.languageAssistFactory.create(this));
        this.assistants.add(this.javaMinecraftComponent);
        this.assistants.add(this.backupWorldAssistant);
    }

    public Downloader getDownloader() {
        return this.downloader;
    }

    public Configuration getConfiguration() {
        return this.settings;
    }

    public boolean isForceUpdate() {
        return this.forceUpdate;
    }

    public boolean isLaunchAssist() {
        return this.assistLaunch;
    }

    public CrashDescriptor getCrashDescriptor() {
        return this.descriptor;
    }

    public MinecraftLauncherStep getStep() {
        return this.step;
    }

    public boolean isWorking() {
        return this.working;
    }

    public void addListener(MinecraftListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (listener instanceof MinecraftExtendedListener) {
            this.extListeners.add((MinecraftExtendedListener) listener);
        }
        this.listeners.add(listener);
    }

    public void start() {
        checkWorking();
        this.working = true;
        try {
            collectInfo();
            downloadResources();
            checkExtraConditions();
            constructProcess();
            launchMinecraft();
            postLaunch();
        } catch (Throwable e) {
            log("Error occurred:", e);
            if (e instanceof MinecraftException) {
                if (!this.settings.getBoolean(Notification.MEMORY_NOTIFICATION) && !FileUtil.checkFreeSpace(MinecraftUtil.getWorkingDirectory(), FileUtil.SIZE_300.longValue())) {
                    String message = Localizable.get("memory.notification.message").replace("disk", MinecraftUtil.getWorkingDirectory().toPath().getRoot().toString());
                    Alert.showCustomMonolog(Localizable.get("memory.notification.title"), new Notification(message, Notification.MEMORY_NOTIFICATION));
                }
                MinecraftException me = (MinecraftException) e;
                for (MinecraftListener listener : this.listeners) {
                    listener.onMinecraftKnownError(me);
                }
            } else if (e instanceof MinecraftLauncherAborted) {
                for (MinecraftListener listener2 : this.listeners) {
                    listener2.onMinecraftAbort();
                }
            } else {
                for (MinecraftListener listener3 : this.listeners) {
                    listener3.onMinecraftError(e);
                }
            }
        }
        this.working = false;
        this.step = MinecraftLauncherStep.NONE;
        log("Launcher exited.");
    }

    public void stop() {
        if (this.step == MinecraftLauncherStep.NONE) {
            throw new IllegalStateException();
        }
        if (this.step == MinecraftLauncherStep.DOWNLOADING) {
            this.downloader.stopDownload();
        }
        this.working = false;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public CompleteVersion getVersion() {
        return this.version;
    }

    public int getExitCode() {
        return this.exitCode;
    }

    public Server getServer() {
        return this.server;
    }

    public void setServer(RemoteServer server) {
        checkWorking();
        this.server = server;
    }

    private void collectInfo() throws MinecraftException {
        checkStep(MinecraftLauncherStep.NONE, MinecraftLauncherStep.COLLECTING);
        log("Collecting info...");
        for (MinecraftListener listener : this.listeners) {
            listener.onMinecraftPrepare();
        }
        for (MinecraftExtendedListener listener2 : this.extListeners) {
            listener2.onMinecraftCollecting();
        }
        log("Force update:", Boolean.valueOf(this.forceUpdate));
        this.versionName = this.settings.get("login.version.game");
        if (this.versionName == null || this.versionName.isEmpty()) {
            throw new IllegalArgumentException("Version name is NULL or empty!");
        }
        log("Selected version:", this.versionName);
        this.account = this.pm.getSelectedAccount();
        if (Objects.isNull(this.account) || this.account.getUsername().isEmpty()) {
            throw new IllegalArgumentException("login is NULL or empty!");
        }
        log("Selected account:", this.account.toDebugString());
        this.versionSync = this.vm.getVersionSyncInfo(this.versionName);
        if (this.versionSync == null) {
            throw new IllegalArgumentException("Cannot find version " + this.version);
        }
        log("Version sync info:", this.versionSync);
        try {
            if (this.versionSync.getLocal() != null && ((CompleteVersion) this.versionSync.getLocal()).getDownloads() == null && this.versionSync.getRemote() != null) {
                this.versionSync.setLocal(null);
            }
            this.deJureVersion = this.versionSync.resolveCompleteVersion(this.vm, this.forceUpdate);
            if (this.deJureVersion == null) {
                throw new NullPointerException("Complete version is NULL");
            }
            this.version = this.deJureVersion;
            this.gameDir = new File(this.settings.get("minecraft.gamedir"));
            try {
                FileUtil.createFolder(this.gameDir);
                if (this.version.isModpack()) {
                    this.runningMinecraftDir = ModpackUtil.getPathByVersion(this.version).toFile();
                } else {
                    this.runningMinecraftDir = this.gameDir;
                }
                File serverResourcePacks = new File(this.runningMinecraftDir, "server-resource-packs");
                try {
                    FileUtil.createFolder(serverResourcePacks);
                    File modsFolder = new File(this.runningMinecraftDir, "mods");
                    try {
                        FileUtil.createFolder(modsFolder);
                        this.globalAssetsDir = new File(this.gameDir, "assets");
                        try {
                            FileUtil.createFolder(this.globalAssetsDir);
                            this.assetsIndexesDir = new File(this.globalAssetsDir, "indexes");
                            try {
                                FileUtil.createFolder(this.assetsIndexesDir);
                                this.assetsObjectsDir = new File(this.globalAssetsDir, "objects");
                                try {
                                    FileUtil.createFolder(this.assetsObjectsDir);
                                    this.nativeDir = new File(this.gameDir, "versions/" + this.version.getID() + "/natives");
                                    if (!OS.is(OS.WINDOWS) && this.nativeDir.getPath().chars().anyMatch(e -> {
                                        return Character.UnicodeBlock.of(e).equals(Character.UnicodeBlock.CYRILLIC);
                                    })) {
                                        try {
                                            this.nativeDir = Files.createTempDirectory("natives", new FileAttribute[0]).toFile();
                                            this.nativeDir.deleteOnExit();
                                            this.clearNatives = true;
                                        } catch (IOException e2) {
                                            throw new RuntimeException("Cannot createScrollWrapper native files directory!", e2);
                                        }
                                    }
                                    try {
                                        FileUtil.createFolder(this.nativeDir);
                                        this.programArgs = this.settings.get("minecraft.args");
                                        if (this.programArgs != null && this.programArgs.isEmpty()) {
                                            this.programArgs = null;
                                        }
                                        this.javaArgs = this.settings.get("minecraft.javaargs");
                                        if (this.javaArgs != null && this.javaArgs.isEmpty()) {
                                            this.javaArgs = null;
                                        }
                                        this.windowSize = this.settings.getClientWindowSize();
                                        if (this.windowSize[0] < 1) {
                                            throw new IllegalArgumentException("Invalid window width!");
                                        }
                                        if (this.windowSize[1] < 1) {
                                            throw new IllegalArgumentException("Invalid window height!");
                                        }
                                        this.fullScreen = this.settings.getBoolean("minecraft.fullscreen");
                                        if (this.version.isModpack() && this.version.getModpack().isModpackMemory()) {
                                            this.ramSize = this.version.getModpack().getMemory();
                                        } else {
                                            this.ramSize = this.settings.getInteger(MinecraftSettings.MINECRAFT_SETTING_RAM);
                                        }
                                        if (this.ramSize < 512) {
                                            throw new IllegalArgumentException("Invalid RAM size!");
                                        }
                                        for (MinecraftLauncherAssistantInterface assistant : this.assistants) {
                                            assistant.collectInfo(this);
                                        }
                                        log("Checking conditions...");
                                        if (!this.version.appliesToCurrentEnvironment()) {
                                            Alert.showLocWarning("launcher.warning.title", (Object) "launcher.warning.incompatible.environment");
                                        }
                                        if (this.version.getMinecraftArguments() == null && this.version.getArguments() == null) {
                                            throw new MinecraftException("Can't run version, missing minecraftArguments", "noArgs", new Object[0]);
                                        }
                                        if (this.version.getMinimumCustomLauncherVersion() > TLauncher.getInnerSettings().getInteger("tlauncher.game.version.compatible")) {
                                            throw new MinecraftException("Alternative launcher is incompatible with launching version!", "incompatible", new Object[0]);
                                        }
                                        String[] sh = new File(this.runningMinecraftDir, "shaderpacks").list();
                                        if (Objects.nonNull(sh) && sh.length > 0) {
                                            StringBuilder b = new StringBuilder("shaderpacks:");
                                            print(sh, b);
                                        }
                                        String[] rs = new File(this.runningMinecraftDir, "resourcepacks").list();
                                        if (Objects.nonNull(rs) && rs.length > 0) {
                                            StringBuilder b2 = new StringBuilder("resourcepacks:");
                                            print(rs, b2);
                                        }
                                    } catch (IOException e3) {
                                        throw new RuntimeException("Cannot createScrollWrapper native files directory!", e3);
                                    }
                                } catch (IOException e4) {
                                    throw new RuntimeException("Cannot createScrollWrapper assets objects directory!", e4);
                                }
                            } catch (IOException e5) {
                                throw new RuntimeException("Cannot createScrollWrapper assets indexes directory!", e5);
                            }
                        } catch (IOException e6) {
                            throw new RuntimeException("Cannot createScrollWrapper assets directory!", e6);
                        }
                    } catch (IOException e7) {
                        throw new RuntimeException("Can't create " + modsFolder, e7);
                    }
                } catch (IOException e8) {
                    throw new RuntimeException("Can't create " + serverResourcePacks, e8);
                }
            } catch (IOException e9) {
                throw new MinecraftException("Cannot createScrollWrapper working directory!", "folder-not-found", e9);
            }
        } catch (IOException e10) {
            Alert.showMessage(Localizable.get("version.manager.resolve.title"), Localizable.get("version.manager.resolve.message"));
            throw new MinecraftLauncherAborted(e10);
        }
    }

    private void print(String[] sh, StringBuilder b) {
        for (String f : sh) {
            b.append(f).append(",");
        }
        log(b.substring(0, b.length() - 1));
    }

    private void downloadResources() throws MinecraftException {
        checkStep(MinecraftLauncherStep.COLLECTING, MinecraftLauncherStep.DOWNLOADING);
        for (MinecraftExtendedListener listener : this.extListeners) {
            listener.onMinecraftComparingAssets();
        }
        List<AssetIndex.AssetObject> assets = compareAssets();
        for (MinecraftExtendedListener listener2 : this.extListeners) {
            listener2.onMinecraftDownloading();
        }
        try {
            DownloadableContainer execContainer = this.vm.downloadVersion(this.versionSync, this.settings.getBoolean("skin.status.checkbox.state"), this.forceUpdate);
            execContainer.addHandler(new DefaultDownloadableContainerHandler());
            DownloadableContainer assetsContainer = this.am.downloadResources(this.version, assets, this.forceUpdate);
            assetsContainer.addHandler(new DefaultDownloadableContainerHandler());
            DownloadableContainer modPackContainer = this.modpackManager.getContainer(this.version, this.forceUpdate);
            modPackContainer.addHandler(new DefaultDownloadableContainerHandler());
            for (MinecraftLauncherAssistantInterface assistant : this.assistants) {
                assistant.collectResources(this);
            }
            DownloadableContainer logContainer = this.logClientConfigurationComponent.getContainer(this.version, this.gameDir, this.forceUpdate);
            this.downloader.add(logContainer);
            this.downloader.add(assetsContainer);
            this.downloader.add(execContainer);
            this.downloader.add(modPackContainer);
            List<DownloadableContainer> list = Lists.newArrayList(this.downloader.getDownloadableContainers());
            this.downloader.startDownloadAndWait();
            for (DownloadableContainer container : list) {
                if (container.isAborted()) {
                    throw new MinecraftLauncherAborted(new AbortedDownloadException());
                }
                if (!container.getErrors().isEmpty() && container.isRequireAllFiles()) {
                    String message = LoginForm.DOWNLOADER_BLOCK;
                    if (Objects.nonNull(container.getErrors().get(0).getCause()) && (container.getErrors().get(0).getCause() instanceof FileSystemException)) {
                        message = "delete.file";
                    }
                    throw new MinecraftException(" ", message, container.getErrors().get(0));
                }
            }
            for (DownloadableContainer container2 : list) {
                if (!container2.getErrors().isEmpty() && !container2.isRequireAllFiles()) {
                    Alert.showWarning(Localizable.get("version.error.message.title"), Localizable.getByKeys("version.error.message", new Object[0]), container2.getErrors().get(0).getMessage() + " ->" + container2.getErrors().get(0).getCause().getMessage());
                }
            }
            try {
                this.vm.getLocalList().saveVersion(this.deJureVersion);
            } catch (IOException e) {
                log("Cannot save version!", e);
            }
        } catch (IOException e2) {
            throw new MinecraftException("Cannot download version!", "download-jar", e2);
        }
    }

    private void constructProcess() throws MinecraftException {
        this.version = TLauncher.getInstance().getTLauncherManager().createUpdatedVersion(this.version, true);
        for (MinecraftExtendedListener listener : this.extListeners) {
            listener.onMinecraftReconstructingAssets();
        }
        try {
            this.localAssetsDir = reconstructAssets();
            for (MinecraftExtendedListener listener2 : this.extListeners) {
                listener2.onMinecraftUnpackingNatives();
            }
            try {
                unpackNatives(this.forceUpdate);
                checkAborted();
                for (MinecraftExtendedListener listener3 : this.extListeners) {
                    listener3.onMinecraftDeletingEntries();
                }
                try {
                    deleteEntries();
                    try {
                        deleteLibraryEntries();
                        checkAborted();
                        log("Constructing process...");
                        for (MinecraftExtendedListener listener4 : this.extListeners) {
                            listener4.onMinecraftConstructing();
                        }
                        this.launcher = new JavaProcessLauncher(this.javaDir.getAbsolutePath(), new String[0]);
                        this.launcher.directory(this.runningMinecraftDir);
                        if (OS.OSX.isCurrent()) {
                            File icon = null;
                            try {
                                icon = getAssetObject("icons/minecraft.icns");
                            } catch (IOException e) {
                                log("Cannot get icon file from assets.", e);
                            }
                            if (icon != null) {
                                this.launcher.addCommand("-Xdock:icon=\"" + icon.getAbsolutePath() + "\"", "-Xdock:name=Minecraft");
                            }
                        }
                        this.launcher.addCommands(getJVMArguments());
                        if (this.javaArgs != null) {
                            this.launcher.addSplitCommands(this.javaArgs);
                        }
                        for (MinecraftLauncherAssistantInterface assistant : this.assistants) {
                            assistant.constructJavaArguments(this);
                        }
                        this.launcher.addCommand(this.version.getMainClass());
                        this.launcher.addCommands(getMinecraftArguments());
                        if (this.fullScreen) {
                            this.launcher.addCommand("--fullscreen");
                        }
                        if (this.server != null) {
                            String[] address = checkAndReplaceWrongAddress(StringUtils.split(this.server.getAddress(), ':'));
                            switch (address.length) {
                                case 2:
                                    this.launcher.addCommand("--port", address[1]);
                                case 1:
                                    this.launcher.addCommand("--server", address[0]);
                                    break;
                                default:
                                    log("Cannot recognize server:", this.server);
                                    break;
                            }
                            if (this.server.getName() == null) {
                                this.server = null;
                            }
                        }
                        if (this.programArgs != null) {
                            this.launcher.addSplitCommands(this.programArgs);
                        }
                        for (MinecraftLauncherAssistantInterface assistant2 : this.assistants) {
                            assistant2.constructProgramArguments(this);
                        }
                        log("Full command: " + this.launcher.getCommandsAsString());
                    } catch (Exception e2) {
                        throw new MinecraftException("Cannot delete library entries!", "delete-entries", e2);
                    }
                } catch (IOException e3) {
                    throw new MinecraftException("Cannot delete entries!", "delete-entries", e3);
                }
            } catch (IOException e4) {
                throw new MinecraftException("Cannot unpack natives!", "unpack-natives", e4);
            }
        } catch (IOException e5) {
            throw new MinecraftException("Cannot recounstruct assets!", "download-jar", e5);
        }
    }

    private String[] checkAndReplaceWrongAddress(String[] address) {
        try {
            InetAddress.getByName(address[0]);
            return address;
        } catch (UnknownHostException e) {
            MinecraftPing p = new MinecraftPing();
            MinecraftPingOptions options = new MinecraftPingOptions().setHostname(address[0]);
            if (address.length == 2) {
                options.setPort(Integer.parseInt(address[1]));
            }
            try {
                p.resolveDNS(options);
                return new String[]{options.getHostname(), String.valueOf(options.getPort())};
            } catch (Throwable e1) {
                U.log(e1);
            }
        }
    }

    private void checkExtraConditions() {
        checkStep(MinecraftLauncherStep.DOWNLOADING, MinecraftLauncherStep.CONSTRUCTING);
        Configuration conf = TLauncher.getInstance().getConfiguration();
        boolean skinVersion = TLauncher.getInstance().getTLauncherManager().useTLauncherAccount(this.version);
        if (skinVersion && !conf.getBoolean("skin.notification.off")) {
            if (!conf.getBoolean("skin.notification.off.temp")) {
                conf.set("skin.notification.off.temp", (Object) true);
                SwingUtilities.invokeLater(SkinNotification::showMessage);
                TLauncher.getInstance().getVersionManager().startRefresh(true);
                throw new MinecraftLauncherAborted("shown skin message");
            }
            conf.set("skin.notification.off.temp", (Object) false);
        } else if (this.account.getType().equals(Account.AccountType.TLAUNCHER) && !skinVersion && !conf.getBoolean("skin.not.work.notification.hide")) {
            String message = String.format(Localizable.get("skin.not.work.notification"), ImageCache.getRes("tlauncher-user.png").toExternalForm(), ImageCache.getRes("need-tl-version-for-skins.png").toExternalForm());
            if (Alert.showWarningMessageWithCheckBox("skin.notification.title", message, 500)) {
                conf.set("skin.not.work.notification.hide", (Object) true);
            }
        }
        if (Objects.nonNull(this.server) && this.server.isOfficialAccount() && !Account.AccountType.OFFICIAL_ACCOUNTS.contains(this.account.getType())) {
            int res = Alert.showConfirmDialog(0, 2, CoreConstants.EMPTY_STRING, Localizable.get("account.not.proper.warn"), null, Localizable.get("ui.go.no.matter"), Localizable.get("ui.no"));
            if (res == 1) {
                throw new MinecraftLauncherAborted("shown mojang skin message");
            }
        }
    }

    private File reconstructAssets() throws IOException {
        String assetVersion = this.version.getAssets() == null ? AssetIndex.DEFAULT_ASSET_NAME : this.version.getAssets();
        File indexFile = new File(this.assetsIndexesDir, assetVersion + ".json");
        File virtualRoot = new File(new File(this.globalAssetsDir, "virtual"), assetVersion);
        if (!indexFile.isFile()) {
            log("No assets index file " + virtualRoot + "; can't reconstruct assets");
            return virtualRoot;
        }
        AssetIndex index = (AssetIndex) this.gson.fromJson(FileUtil.readFile(indexFile), (Class<Object>) AssetIndex.class);
        if (index.isVirtual()) {
            log("Reconstructing virtual assets folder at " + virtualRoot);
            for (Map.Entry<String, AssetIndex.AssetObject> entry : index.getFileMap().entrySet()) {
                checkAborted();
                File target = new File(virtualRoot, entry.getKey());
                File original = new File(new File(this.assetsObjectsDir, entry.getValue().getHash().substring(0, 2)), entry.getValue().getHash());
                if (!original.isFile()) {
                    log("Skipped reconstructing:", original);
                } else if (this.forceUpdate || !target.isFile()) {
                    FileUtils.copyFile(original, target, false);
                    log(original, "->", target);
                }
            }
            FileUtil.writeFile(new File(virtualRoot, ".lastused"), this.dateAdapter.toString(new Date()));
        }
        return virtualRoot;
    }

    private File getAssetObject(String name) throws IOException {
        String assetVersion = this.version.getAssets();
        File indexFile = new File(this.assetsIndexesDir, assetVersion + ".json");
        AssetIndex index = (AssetIndex) this.gson.fromJson(FileUtil.readFile(indexFile), (Class<Object>) AssetIndex.class);
        if (index.getFileMap() == null) {
            throw new IOException("Cannot get filemap!");
        }
        if (Objects.isNull(index.getFileMap().get(name))) {
            return null;
        }
        String hash = index.getFileMap().get(name).getHash();
        return new File(this.assetsObjectsDir, hash.substring(0, 2) + "/" + hash);
    }

    private void unpackNatives(boolean force) throws IOException {
        log("Unpacking natives...");
        Collection<Library> libraries = this.version.getRelevantLibraries();
        OS os = OS.CURRENT;
        if (force) {
            this.nativeDir.delete();
        }
        for (Library library : libraries) {
            Map<OS, String> nativesPerOs = library.getNatives();
            if (nativesPerOs != null && nativesPerOs.get(os) != null) {
                File file = new File(MinecraftUtil.getWorkingDirectory(), "libraries/" + library.getArtifactPath(nativesPerOs.get(os)));
                if (!file.isFile()) {
                    throw new IOException("Required archive doesn't exist: " + file.getAbsolutePath());
                }
                try {
                    ZipFile zip = new ZipFile(file);
                    ExtractRules extractRules = library.getExtractRules();
                    Enumeration<? extends ZipEntry> entries = zip.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        if (extractRules == null || extractRules.shouldExtract(entry.getName())) {
                            File targetFile = new File(this.nativeDir, entry.getName());
                            if (force || !targetFile.isFile()) {
                                if (targetFile.getParentFile() != null) {
                                    targetFile.getParentFile().mkdirs();
                                }
                                if (!entry.isDirectory()) {
                                    BufferedInputStream inputStream = new BufferedInputStream(zip.getInputStream(entry));
                                    byte[] buffer = new byte[2048];
                                    FileOutputStream outputStream = new FileOutputStream(targetFile);
                                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                                    while (true) {
                                        int length = inputStream.read(buffer, 0, buffer.length);
                                        if (length == -1) {
                                            break;
                                        }
                                        bufferedOutputStream.write(buffer, 0, length);
                                    }
                                    inputStream.close();
                                    bufferedOutputStream.close();
                                }
                            }
                        }
                    }
                    zip.close();
                } catch (IOException e) {
                    throw new IOException("Error opening ZIP archive: " + file.getAbsolutePath(), e);
                }
            }
        }
    }

    private void deleteEntries() throws IOException {
        List<String> entries = this.version.getDeleteEntries();
        if (entries == null || entries.size() == 0) {
            return;
        }
        log("Removing entries...");
        File file = this.version.getFile(this.gameDir);
        removeFrom(file, entries);
    }

    private void deleteLibraryEntries() throws IOException {
        for (Library lib : this.version.getLibraries()) {
            List<String> entries = lib.getDeleteEntriesList();
            if (entries != null && !entries.isEmpty()) {
                log("Processing entries of", lib.getName());
                removeFrom(new File(this.gameDir, "libraries/" + lib.getArtifactPath()), entries);
            }
        }
    }

    private String constructClassPath(CompleteVersion version) throws MinecraftException {
        log("Constructing classpath...");
        StringBuilder result = new StringBuilder();
        Collection<File> classPath = version.getClassPath(OS.CURRENT, this.gameDir);
        String separator = System.getProperty("path.separator");
        for (File file : classPath) {
            if (!file.isFile()) {
                throw new MinecraftException("Classpath is not found: " + file, "classpath", file);
            }
            if (result.length() > 0) {
                result.append(separator);
            }
            result.append(file.getAbsolutePath());
        }
        return result.toString();
    }

    private String[] getMinecraftArguments() {
        log("Getting Minecraft arguments...");
        List<String> list = new ArrayList<>();
        List<Argument> arguments = this.version.getArguments().get(ArgumentType.GAME);
        for (Argument argument : arguments) {
            if (argument.appliesToCurrentEnvironment()) {
                list.addAll(Arrays.asList(argument.getValues()));
            }
        }
        Map<String, String> map = new HashMap<>();
        String assets = this.version.getAssets();
        map.put("auth_username", this.account.getUsername());
        map.put("auth_session", String.format("token:%s:%s", this.account.getAccessToken(), this.account.getProfile().getId()));
        if (Account.AccountType.OFFICIAL_ACCOUNTS.contains(this.account.getType())) {
            map.put("auth_access_token", this.account.getAccessToken());
        } else {
            map.put("auth_access_token", Configurator.NULL);
        }
        map.put("user_properties", this.gson.toJson(this.account.getProperties()));
        if (this.settings.getBoolean("skip.account.property")) {
            map.put("user_properties", "{}");
        }
        map.put("auth_uuid", this.account.getUUID());
        if (Account.AccountType.MICROSOFT.equals(this.account.getType())) {
            map.put("user_type", "msa");
        } else {
            map.put("user_type", "mojang");
        }
        map.put("profile_name", this.account.getProfile().getName());
        map.put("auth_player_name", this.account.getDisplayName());
        map.put("version_type", this.version.getReleaseType().toString());
        map.put("version_name", this.version.getID());
        map.put("game_directory", this.runningMinecraftDir.getAbsolutePath());
        map.put("game_assets", this.localAssetsDir.getAbsolutePath());
        map.put("assets_root", this.globalAssetsDir.getAbsolutePath());
        map.put("assets_index_name", assets == null ? AssetIndex.DEFAULT_ASSET_NAME : assets);
        map.put("resolution_width", String.valueOf(this.windowSize[0]));
        map.put("resolution_height", String.valueOf(this.windowSize[1]));
        StrSubstitutor substitutor = new StrSubstitutor(map);
        String[] split = (String[]) list.toArray(new String[0]);
        for (int i = 0; i < split.length; i++) {
            split[i] = substitutor.replace(split[i]);
        }
        return split;
    }

    private String[] getJVMArguments() throws MinecraftException {
        List<String> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        if (OS.CURRENT == OS.WINDOWS && System.getProperty("os.version").startsWith("10.")) {
            list.add("-Dos.name=Windows 10");
            list.add("-Dos.version=10.0");
        }
        List<Argument> arguments = this.version.getArguments().get(ArgumentType.JVM);
        for (Argument argument : arguments) {
            if (argument.appliesToCurrentEnvironment()) {
                list.addAll(Arrays.asList(argument.getValues()));
            }
        }
        list.add("-Xmx" + this.ramSize + "M");
        map.put("game_directory", this.runningMinecraftDir.getAbsolutePath());
        if (OS.Arch.TOTAL_RAM_MB < 5000 && getJavaVersion() == 8) {
            list.add("-XX:+UseConcMarkSweepGC");
        } else if (Objects.nonNull(this.java)) {
            list.addAll(this.java.getArgs());
        } else {
            MinecraftUtil.configureG1GC(list);
        }
        list.add("-Dminecraft.applet.TargetDirectory=${game_directory}");
        if (this.logClientConfigurationComponent.isNotNullLogClient(this.version)) {
            LogClient f = this.logClientConfigurationComponent.getLogClient(this.version);
            list.add(f.getArgument());
            map.put(ClientCookie.PATH_ATTR, this.logClientConfigurationComponent.buildPathLogXmlConfiguration(this.gameDir, f).toString());
        }
        if (this.version.isModpack()) {
            list.add("-DLibLoader.modsFolder=" + ModpackUtil.getPathByVersion(this.version, "mods"));
        }
        String[] split = (String[]) list.toArray(new String[0]);
        map.put("natives_directory", this.nativeDir.toString());
        String classpath = constructClassPath(this.version);
        map.put("classpath", classpath);
        map.put("legacyClassPath", classpath);
        map.put("launcher_name", "minecraft-launcher");
        map.put("launcher_version", "2.3.173");
        map.put("classpath_separator", File.pathSeparator);
        map.put("library_directory", String.join(File.separator, this.gameDir.getAbsolutePath(), "libraries"));
        map.put("version_name", this.versionName);
        StrSubstitutor substitutor = new StrSubstitutor(map);
        for (int i = 0; i < split.length; i++) {
            split[i] = substitutor.replace(split[i]);
        }
        return split;
    }

    private List<AssetIndex.AssetObject> compareAssets() {
        migrateOldAssets();
        log("Comparing assets...");
        long start = System.currentTimeMillis();
        List<AssetIndex.AssetObject> result = this.am.checkResources(this.version, !this.forceUpdate);
        log("finished comparing assets: " + (System.currentTimeMillis() - start) + " ms.");
        return result;
    }

    private void migrateOldAssets() {
        if (!this.globalAssetsDir.isDirectory()) {
            return;
        }
        File skinsDir = new File(this.globalAssetsDir, "skins");
        if (skinsDir.isDirectory()) {
            FileUtil.deleteDirectory(skinsDir);
        }
        IOFileFilter migratableFilter = FileFilterUtils.notFileFilter(FileFilterUtils.or(FileFilterUtils.nameFileFilter("indexes"), FileFilterUtils.nameFileFilter(PathAppUtil.LOG_CONFIGS), FileFilterUtils.nameFileFilter("objects"), FileFilterUtils.nameFileFilter("virtual")));
        Iterator it = new TreeSet(FileUtils.listFiles(this.globalAssetsDir, TrueFileFilter.TRUE, migratableFilter)).iterator();
        while (it.hasNext()) {
            File file = (File) it.next();
            String hash = FileUtil.getDigest(file, MessageDigestAlgorithms.SHA_1, 40);
            if (hash != null) {
                File destinationFile = new File(this.assetsObjectsDir, hash.substring(0, 2) + "/" + hash);
                if (!destinationFile.exists()) {
                    log("Migrated old asset", file, "into", destinationFile);
                    try {
                        FileUtils.copyFile(file, destinationFile);
                    } catch (IOException e) {
                        log("Couldn't migrate old asset", e);
                    }
                }
            }
            FileUtils.deleteQuietly(file);
        }
        File[] assets = this.globalAssetsDir.listFiles();
        if (assets != null) {
            for (File file2 : assets) {
                if (!file2.getName().equals("indexes") && !file2.getName().equals("objects") && !file2.getName().equals(PathAppUtil.LOG_CONFIGS) && !file2.getName().equals("virtual")) {
                    log("Cleaning up old assets directory", file2, "after migration");
                    FileUtils.deleteQuietly(file2);
                }
            }
        }
    }

    private void launchMinecraft() throws MinecraftException {
        checkStep(MinecraftLauncherStep.CONSTRUCTING, MinecraftLauncherStep.LAUNCHING);
        log("Launching Minecraft...");
        for (MinecraftListener listener : this.listeners) {
            listener.onMinecraftLaunch();
        }
        log("skin system is activated: " + getConfiguration().getBoolean("skin.status.checkbox.state"));
        log("Starting Minecraft " + this.versionName + "...");
        log("Launching in:", this.runningMinecraftDir.getAbsolutePath());
        this.startupTime = System.currentTimeMillis();
        this.console.setLauncherToKillProcess(this);
        U.gc();
        try {
            this.launcher.setListener(this);
            this.process = this.launcher.start();
            this.minecraftWorking = true;
        } catch (Exception e) {
            notifyClose();
            throw new MinecraftException("Cannot start the game!", "start", e);
        }
    }

    private void postLaunch() {
        checkStep(MinecraftLauncherStep.LAUNCHING, MinecraftLauncherStep.POSTLAUNCH);
        log("Processing post-launch actions. Assist launch:", Boolean.valueOf(this.assistLaunch));
        for (MinecraftExtendedListener listener : this.extListeners) {
            listener.onMinecraftPostLaunch();
        }
        if (this.assistLaunch) {
            waitForClose();
            return;
        }
        U.sleepFor(30000L);
        if (this.minecraftWorking) {
            TLauncher.kill();
        }
    }

    public void killProcess() {
        if (!this.minecraftWorking) {
            throw new IllegalStateException();
        }
        log("Killing Minecraft forcefully");
        this.killed = true;
        this.process.stop();
    }

    public void log(Object... o) {
        U.log("[Launcher]", o);
    }

    private void checkThread() {
        if (!Thread.currentThread().equals(this.parentThread)) {
            throw new IllegalStateException("Illegal thread!");
        }
    }

    private void checkStep(MinecraftLauncherStep prevStep, MinecraftLauncherStep currentStep) {
        checkAborted();
        if (prevStep == null || currentStep == null) {
            throw new NullPointerException("NULL: " + prevStep + " " + currentStep);
        }
        if (!this.step.equals(prevStep)) {
            throw new IllegalStateException("Called from illegal step: " + this.step);
        }
        checkThread();
        this.step = currentStep;
    }

    private void checkAborted() {
        if (!this.working) {
            throw new MinecraftLauncherAborted("Aborted at step: " + this.step);
        }
    }

    private void checkWorking() {
        if (this.working) {
            throw new IllegalStateException("Launcher is working!");
        }
    }

    @Override // net.minecraft.launcher.process.JavaProcessListener
    public void onJavaProcessLog(JavaProcess jp, String line) {
        if (this.firstLine) {
            this.firstLine = false;
            U.plog("===============================================================================================");
        }
        this.xmlLogDeserialization.addToLog(line);
    }

    @Override // net.minecraft.launcher.process.JavaProcessListener
    public void onJavaProcessEnded(JavaProcess jp) {
        notifyClose();
        this.console.setLauncherToKillProcess(null);
        int exit = jp.getExitCode();
        log("Minecraft closed with exit code: " + exit);
        this.exitCode = exit;
        Crash crash = this.killed ? null : this.descriptor.scan();
        if (crash == null) {
            if (!this.assistLaunch) {
                TLauncher.kill();
                return;
            }
            return;
        }
        this.console.show(true);
        for (MinecraftListener listener : this.listeners) {
            listener.onMinecraftCrash(crash);
        }
    }

    @Override // net.minecraft.launcher.process.JavaProcessListener
    public void onJavaProcessError(JavaProcess jp, Throwable e) {
        notifyClose();
        for (MinecraftListener listener : this.listeners) {
            listener.onMinecraftError(e);
        }
    }

    private synchronized void waitForClose() {
        while (this.minecraftWorking) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    private synchronized void notifyClose() {
        this.minecraftWorking = false;
        if (System.currentTimeMillis() - this.startupTime < 5000) {
            U.sleepFor(1000L);
        }
        notifyAll();
        for (MinecraftListener listener : this.listeners) {
            listener.onMinecraftClose();
        }
        if (this.clearNatives && Files.exists(this.nativeDir.toPath(), new LinkOption[0])) {
            FileUtil.deleteDirectory(this.nativeDir);
        }
    }

    private void removeFrom(File zipFile, List<String> entries) throws IOException {
        File tempFile = File.createTempFile(zipFile.getName(), null);
        tempFile.delete();
        tempFile.deleteOnExit();
        Path moved = Files.move(zipFile.toPath(), tempFile.toPath(), new CopyOption[0]);
        if (Files.notExists(moved, new LinkOption[0])) {
            throw new IOException("Could not move the file " + zipFile.getAbsolutePath() + " -> " + tempFile.getAbsolutePath());
        }
        log("Removing entries from", zipFile);
        byte[] buf = new byte[1024];
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(tempFile)), StandardCharsets.UTF_8);
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)), StandardCharsets.UTF_8);
        ZipEntry nextEntry = zin.getNextEntry();
        while (true) {
            ZipEntry entry = nextEntry;
            if (entry != null) {
                String name = entry.getName();
                if (entries.contains(name)) {
                    log("Removed:", name);
                } else {
                    zout.putNextEntry(new ZipEntry(name));
                    while (true) {
                        int len = zin.read(buf);
                        if (len > 0) {
                            zout.write(buf, 0, len);
                        }
                    }
                }
                nextEntry = zin.getNextEntry();
            } else {
                zin.close();
                zout.close();
                tempFile.delete();
                return;
            }
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/MinecraftLauncher$MinecraftLauncherAborted.class */
    public static class MinecraftLauncherAborted extends RuntimeException {
        private static final long serialVersionUID = -3001265213093607559L;

        MinecraftLauncherAborted(String message) {
            super(message);
        }

        MinecraftLauncherAborted(Throwable cause) {
            super(cause);
        }
    }

    public File getRunningMinecraftDir() {
        return this.runningMinecraftDir;
    }
}
