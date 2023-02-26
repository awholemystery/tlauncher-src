package org.tlauncher.tlauncher.rmo;

import ch.qos.logback.core.CoreConstants;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.sothawo.mapjfx.cache.OfflineCache;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import joptsimple.OptionSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.log4j.Logger;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.tlauncher.configuration.ArgumentParser;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.InnerConfiguration;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.tlauncher.configuration.test.environment.JavaBitTestEnvironment;
import org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.entity.server.RemoteServer;
import org.tlauncher.tlauncher.handlers.ExceptionHandler;
import org.tlauncher.tlauncher.managers.AdditionalAssetsComponent;
import org.tlauncher.tlauncher.managers.ComponentManager;
import org.tlauncher.tlauncher.managers.ComponentManagerListenerHelper;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.managers.ServerListManager;
import org.tlauncher.tlauncher.managers.TLauncherManager;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.managers.VersionManagerListener;
import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedAdapter;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListenerAdapter;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.rmo.Bootstrapper;
import org.tlauncher.tlauncher.site.play.SitePlay;
import org.tlauncher.tlauncher.ui.TLauncherFrame;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.browser.BrowserHolder;
import org.tlauncher.tlauncher.ui.browser.JFXStartPageLoader;
import org.tlauncher.tlauncher.ui.console.Console;
import org.tlauncher.tlauncher.ui.explorer.FileExplorer;
import org.tlauncher.tlauncher.ui.listener.MinecraftUIListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.ui.progress.ProgressFrame;
import org.tlauncher.tlauncher.updater.client.AutoUpdater;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.TestInstallVersions;
import org.tlauncher.util.Time;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;
import org.tlauncher.util.guice.GuiceModule;
import org.tlauncher.util.guice.MinecraftLauncherFactory;
import org.tlauncher.util.statistics.GameRunningListener;
import org.tlauncher.util.statistics.InstallVersionListener;
import org.tlauncher.util.statistics.StatisticsUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/rmo/TLauncher.class */
public class TLauncher {
    public static boolean DEBUG = false;
    private static volatile TLauncher instance;
    private static String[] sargs;
    private static File directory;
    private static Gson gson;
    private static InnerConfiguration innerSettings;
    private static String family;
    private static Injector injector;
    @Named("console")
    @Inject
    private Console console;
    private final String defaultPrefix = getPagePrefix();
    private LangConfiguration lang;
    private final Configuration configuration;
    private final Downloader downloader;
    private final AutoUpdater updater;
    private TLauncherFrame frame;
    private final ComponentManager manager;
    private final VersionManager versionManager;
    private final ProfileManager profileManager;
    private final TLauncherManager tlauncherManager;
    private MinecraftLauncher launcher;
    private final MinecraftUIListener minecraftListener;
    private final AdditionalAssetsComponent additionalAssetsComponent;
    @Inject
    private MinecraftLauncherFactory minecraftLauncherFactory;
    private boolean ready;

    static {
        try {
            innerSettings = new InnerConfiguration(FileUtil.getResourceAppStream("/inner.tlauncher.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Inject
    public TLauncher(@Assisted Configuration configuration) throws Exception {
        this.configuration = configuration;
        Time.start(this);
        instance = this;
        gson = new Gson();
        File oldConfig = MinecraftUtil.getSystemRelatedDirectory(innerSettings.get("settings"));
        File newConfig = MinecraftUtil.getSystemRelatedDirectory(innerSettings.get("settings.new"));
        oldConfig = oldConfig.isFile() ? oldConfig : MinecraftUtil.getSystemRelatedDirectory(innerSettings.get("tlauncher.folder"));
        if (oldConfig.isFile() && !newConfig.isFile()) {
            boolean copied = true;
            try {
                FileUtil.createFile(newConfig);
                FileUtil.copyFile(oldConfig, newConfig, true);
            } catch (IOException ioE) {
                U.log("Cannot copy old configuration to the new place", oldConfig, newConfig, ioE);
                copied = false;
            }
            if (copied) {
                U.log("Old configuration successfully moved to the new place:", newConfig);
                FileUtil.deleteFile(oldConfig);
            }
        }
        U.setLoadingStep(Bootstrapper.LoadingStep.LOADING_CONFIGURATION);
        U.log("Machine info:", OS.getSummary());
        reloadLocale();
        U.setLoadingStep(Bootstrapper.LoadingStep.LOADING_CONSOLE);
        SwingUtil.init();
        if (!configuration.getLocale().getLanguage().equals(new Locale("zh").getLanguage())) {
            family = "Roboto";
        }
        if (configuration.get("ssl.deactivate.date") != null && LocalDate.parse(configuration.get("ssl.deactivate.date")).isAfter(LocalDate.now())) {
            TlauncherUtil.deactivateSSL();
        }
        this.downloader = new Downloader(configuration.getConnectionQuality());
        this.minecraftListener = new MinecraftUIListener(this);
        this.updater = new AutoUpdater(this);
        this.updater.asyncFindUpdate();
        JFXStartPageLoader.getInstance();
        BrowserHolder.getInstance();
        U.setLoadingStep(Bootstrapper.LoadingStep.LOADING_MANAGERS);
        this.manager = new ComponentManager(this);
        this.tlauncherManager = (TLauncherManager) this.manager.loadComponent(TLauncherManager.class);
        this.versionManager = (VersionManager) this.manager.loadComponent(VersionManager.class);
        this.profileManager = (ProfileManager) this.manager.loadComponent(ProfileManager.class);
        this.profileManager.refresh();
        ((ServerListManager) this.manager.loadComponent(ServerListManager.class)).asyncRefresh();
        this.manager.loadComponent(ComponentManagerListenerHelper.class);
        this.additionalAssetsComponent = (AdditionalAssetsComponent) this.manager.loadComponent(AdditionalAssetsComponent.class);
    }

    public static String getFamily() {
        return family;
    }

    public static void main(String[] args) {
        try {
            OptionSet set = ArgumentParser.parseArgs(args);
            Configuration configuration = Configuration.createConfiguration(set);
            U.initializeLoggerU(MinecraftUtil.getWorkingDirectory(configuration.get("minecraft.gamedir")), "tlauncher");
            U.setLoadingStep(Bootstrapper.LoadingStep.INITIALIZING);
            ExceptionHandler handler = ExceptionHandler.getInstance();
            Thread.setDefaultUncaughtExceptionHandler(handler);
            Thread.currentThread().setUncaughtExceptionHandler(handler);
            UIManager.put("FileChooser.useSystemExtensionHiding", false);
            System.setProperty("java.net.preferIPv4Stack", "true");
            Class.forName("org.apache.log4j.helpers.NullEnumeration");
            Class.forName("org.apache.http.impl.conn.CPool");
            TlauncherUtil.createTimeStart();
            initLookAndFeel();
            initUrlCache();
            launch(configuration);
            Configuration c = getInstance().getConfiguration();
            c.set(TestEnvironment.WARMING_MESSAGE, null, false);
            if (OS.Arch.CURRENT != OS.Arch.x64) {
                AsyncThread.execute(() -> {
                    OS.fillSystemInfo();
                    List<TestEnvironment> list = new ArrayList<TestEnvironment>() { // from class: org.tlauncher.tlauncher.rmo.TLauncher.1
                        {
                            add(new JavaBitTestEnvironment(Configuration.this));
                        }
                    };
                    for (TestEnvironment t : list) {
                        if (!t.testEnvironment()) {
                            t.fix();
                        }
                    }
                    c.store();
                });
            }
        } catch (Throwable e) {
            U.log("Error launching TLauncher:");
            U.log(e);
            if (Localizable.exists()) {
                TlauncherUtil.showCriticalProblem(Localizable.get("alert.error.not.run") + "<br><br>" + TlauncherUtil.getStringError(e));
            } else {
                TlauncherUtil.showCriticalProblem(e);
            }
        }
        Configuration conf = ((TLauncher) injector.getInstance(TLauncher.class)).getConfiguration();
        if (OS.is(OS.WINDOWS)) {
            String pr = OS.executeByTerminal("wmic CPU get NAME");
            String[] array = pr.split(System.lineSeparator());
            if (array.length == 4) {
                pr = array[2];
            }
            conf.set("process.info", pr.trim());
        }
        TlauncherUtil.fillGPUInfo(conf, true);
        Configuration c2 = getInstance().getConfiguration();
        StatisticsUtil.startSending("save/run/tlauncher", null, Maps.newHashMap());
        if (!enterGap(Long.valueOf(c2.getLong("sending.tlauncher.unique")))) {
            StatisticsUtil.sendMachineInfo(conf);
            c2.set("sending.tlauncher.unique", Long.valueOf(System.currentTimeMillis()));
        }
        TlauncherUtil.testNet();
        if (OS.is(OS.WINDOWS)) {
            boolean KB4515384Exists = OS.executeByTerminal("wmic qfe get HotFixID", 5).contains("KB4515384");
            if (KB4515384Exists) {
                conf.set("block.updater.message", (Object) true);
            }
        }
        TlauncherUtil.clearTimeLabel();
        TestInstallVersions.install(conf);
    }

    private static void initUrlCache() throws IOException {
        OfflineCache c = OfflineCache.INSTANCE;
        Path urlCache = MinecraftUtil.getTLauncherFile("cache" + File.separator + "requests").toPath();
        FileUtil.createFolder(urlCache.toFile());
        c.setCacheDirectory(urlCache);
        c.setCacheFilters(Sets.newHashSet(new String[]{".*png$", ".*gif$"}));
        c.setActive(true);
    }

    private static void initLookAndFeel() {
        LookAndFeel defaultLookAndFeel = null;
        try {
            defaultLookAndFeel = UIManager.getLookAndFeel();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new FileExplorer();
        } catch (Throwable t) {
            U.log("problem with ", t);
            if (Objects.nonNull(defaultLookAndFeel)) {
                try {
                    UIManager.setLookAndFeel(defaultLookAndFeel);
                } catch (Throwable e) {
                    U.log("problem with look and feel ", e);
                }
            }
        }
    }

    public static Gson getGson() {
        return gson;
    }

    public static void kill() {
        U.log("Good bye!");
        TlauncherUtil.clearTimeLabel();
        try {
            Class cl = Class.forName("org.tlauncher.tlauncher.managers.TLauncherManager$3");
            if (Objects.nonNull(cl) && Objects.nonNull(getInstance()) && Objects.nonNull(getInstance().tlauncherManager)) {
                String value = getInstance().getConfiguration().get("minecraft.onlaunch");
                if (!ActionOnLaunch.EXIT.name().equalsIgnoreCase(value)) {
                    getInstance().tlauncherManager.cleanMods();
                }
            }
        } catch (ClassNotFoundException e) {
        } catch (Throwable e2) {
            U.log(e2);
        }
        Logger.getLogger("main").info(U.FLUSH_MESSAGE);
        System.exit(0);
    }

    private static void launch(Configuration configuration) {
        Module guiceModule = new GuiceModule(configuration, innerSettings);
        injector = Guice.createInjector(new Module[]{guiceModule});
        guiceModule.setInjector(injector);
        U.log(String.format("Starting TLauncher %s %s", innerSettings.get(ClientCookie.VERSION_ATTR), innerSettings.get("type")));
        U.log("For more information, visit https://tlauncher.org/");
        U.log("Startup time:", DateFormat.getDateTimeInstance().format(new Date()));
        U.log("Running folder " + Paths.get(CoreConstants.EMPTY_STRING, new String[0]).toAbsolutePath().toString());
        U.log("---");
        ProgressFrame customBar = new ProgressFrame(innerSettings.get(ClientCookie.VERSION_ATTR));
        TLauncher t = (TLauncher) injector.getInstance(TLauncher.class);
        U.setLoadingStep(Bootstrapper.LoadingStep.LOADING_WINDOW);
        t.frame = new TLauncherFrame(t);
        t.init(customBar);
        t.getProfileManager().fireProfileRefreshed();
    }

    private static void log(String line) {
        U.log("[TLauncher] " + line);
    }

    public static InnerConfiguration getInnerSettings() {
        return innerSettings;
    }

    public static File getDirectory() {
        if (directory == null) {
            directory = new File(".");
        }
        return directory;
    }

    public static TLauncher getInstance() {
        return instance;
    }

    public static double getVersion() {
        return innerSettings.getDouble(ClientCookie.VERSION_ATTR);
    }

    public static String getFolder() {
        return innerSettings.get("folder");
    }

    public static String[] getUpdateRepos() {
        return innerSettings.getArrayAccess("update.repo");
    }

    public static Injector getInjector() {
        return injector;
    }

    private static boolean enterGap(Long last) {
        if (last.longValue() == 0) {
            return false;
        }
        Calendar start = Calendar.getInstance();
        start.set(10, 0);
        start.set(12, 0);
        start.set(13, 0);
        Calendar end = Calendar.getInstance();
        end.set(10, 23);
        end.set(12, 59);
        end.set(13, 59);
        return start.getTimeInMillis() < last.longValue() && last.longValue() < end.getTimeInMillis();
    }

    public Downloader getDownloader() {
        return this.downloader;
    }

    public LangConfiguration getLang() {
        return this.lang;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public AutoUpdater getUpdater() {
        return this.updater;
    }

    public TLauncherFrame getFrame() {
        return this.frame;
    }

    public ComponentManager getManager() {
        return this.manager;
    }

    public VersionManager getVersionManager() {
        return this.versionManager;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public TLauncherManager getTLauncherManager() {
        return this.tlauncherManager;
    }

    public MinecraftLauncher getLauncher() {
        return this.launcher;
    }

    public boolean isReady() {
        return this.ready;
    }

    public void reloadLocale() throws IOException {
        Locale locale = this.configuration.getLocale();
        U.log("Selected locale: " + locale);
        if (this.lang == null) {
            this.lang = new LangConfiguration(this.configuration.getLocales(), locale, "/lang/tlauncher/");
        } else {
            this.lang.setSelected(locale);
        }
        Localizable.setLang(this.lang);
        Alert.prepareLocal();
        if (this.console != null) {
            this.console.setName(this.lang.get("console"));
        }
    }

    public void launch(MinecraftListener listener, RemoteServer server, boolean forceupdate) {
        this.launcher = this.minecraftLauncherFactory.create(this, forceupdate);
        this.launcher.addListener(this.minecraftListener);
        this.launcher.addListener(listener);
        this.launcher.addListener(this.frame.mp.getProgress());
        this.launcher.addListener(getInstance().getTLauncherManager());
        this.launcher.addListener((MinecraftListener) injector.getInstance(HotServerManager.class));
        this.launcher.addListener(new GameRunningListener(this.launcher));
        this.launcher.addListener(new MinecraftListenerAdapter() { // from class: org.tlauncher.tlauncher.rmo.TLauncher.2
            @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListenerAdapter, org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
            public void onMinecraftKnownError(MinecraftException e) {
                if (e.getLangPath().equalsIgnoreCase("start")) {
                    TLauncher.this.frame.mp.setScene(TLauncher.this.frame.mp.settingsScene);
                }
            }
        });
        this.launcher.setServer(server);
        this.launcher.addListener(new MinecraftExtendedAdapter() { // from class: org.tlauncher.tlauncher.rmo.TLauncher.3
            @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedAdapter, org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
            public void onMinecraftDownloading() {
                if (TLauncher.this.launcher.getVersion().isModpack()) {
                    try {
                        ModpackVersionDTO v = (ModpackVersionDTO) TLauncher.this.launcher.getVersion().getModpack().getVersion();
                        boolean cleanOldDownloadFile = false;
                        for (GameType type : GameType.getSubEntities()) {
                            if (!type.equals(GameType.MAP)) {
                                for (int i = 0; i < v.getByType(type).size(); i++) {
                                    GameEntityDTO b = v.getByType(type).get(i);
                                    if (b.getVersion().getMetadata().getPath().endsWith(LoginForm.DOWNLOADER_BLOCK)) {
                                        GameEntityDTO replacer = (GameEntityDTO) ((ModpackManager) TLauncher.injector.getInstance(ModpackManager.class)).readFromServer(b.getClass(), b, b.getVersion());
                                        U.log(String.format("replace broken element %s %s", b.getVersion().getMetadata().getPath(), replacer.getVersion().getMetadata().getPath()));
                                        b.getVersion().setMetadata(replacer.getVersion().getMetadata());
                                        cleanOldDownloadFile = true;
                                    }
                                }
                                if (cleanOldDownloadFile) {
                                    Files.deleteIfExists(Paths.get(ModpackUtil.getPath(TLauncher.this.launcher.getVersion(), type).toString(), LoginForm.DOWNLOADER_BLOCK));
                                }
                            }
                        }
                    } catch (Throwable e) {
                        U.log("got problem with fixing download link", e);
                    }
                }
            }
        });
        this.launcher.addListener((MinecraftListener) injector.getInstance(ModpackManager.class));
        this.launcher.start();
    }

    public boolean isLauncherWorking() {
        return this.launcher != null && this.launcher.isWorking();
    }

    public void hide() {
        if (this.frame != null) {
            boolean doAgain = true;
            while (doAgain) {
                try {
                    this.frame.setVisible(false);
                    doAgain = false;
                } catch (Exception e) {
                }
            }
        }
        U.log("I'm hiding!");
    }

    public void show() {
        if (this.frame != null) {
            boolean doAgain = true;
            while (doAgain) {
                try {
                    this.frame.setVisible(true);
                    doAgain = false;
                } catch (Exception e) {
                    U.log(e);
                }
            }
        }
        U.log("Here I am!");
    }

    public String getPagePrefix() {
        if (this.defaultPrefix != null) {
            return this.defaultPrefix;
        }
        if (TlauncherUtil.hostAvailabilityCheck("https://page.tlauncher.org") == 200) {
            return "https://page.tlauncher.org/update/downloads/configs/client/";
        }
        if (TlauncherUtil.hostAvailabilityCheck("https://repo.tlauncher.org") == 200) {
            return "https://repo.tlauncher.org/update/downloads/configs/client/";
        }
        if (TlauncherUtil.hostAvailabilityCheck("https://advancedrepository.com") == 200) {
            return "https://advancedrepository.com/update/downloads/configs/client/";
        }
        return "127.0.0.1";
    }

    public void init(ProgressFrame customBar) {
        this.console.init(this.configuration, this.configuration.getConsoleType() == ConsoleType.GLOBAL);
        this.frame.afterInitProfile();
        U.setLoadingStep(Bootstrapper.LoadingStep.REFRESHING_INFO);
        this.versionManager.addListener((VersionManagerListener) injector.getInstance(SitePlay.class));
        this.versionManager.addListener((VersionManagerListener) injector.getInstance(HotServerManager.class));
        this.versionManager.asyncRefresh();
        this.additionalAssetsComponent.asyncRefresh();
        this.downloader.addListener(new InstallVersionListener());
        ModpackManager modpackManager = (ModpackManager) injector.getInstance(ModpackManager.class);
        U.log("Started! (" + Time.stop(this) + " ms.)");
        this.ready = true;
        U.setLoadingStep(Bootstrapper.LoadingStep.SUCCESS);
        customBar.setVisible(false);
        customBar.dispose();
        this.frame.setVisible(true);
        log("show tlauncher!!!");
        this.frame.showTips();
        if (!StringUtils.equals(System.getProperty("console"), Boolean.toString(true))) {
            U.removeConsoleAppender();
        }
    }

    public AdditionalAssetsComponent getAdditionalAssetsComponent() {
        return this.additionalAssetsComponent;
    }
}
