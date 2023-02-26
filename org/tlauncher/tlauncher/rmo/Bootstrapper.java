package org.tlauncher.tlauncher.rmo;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import com.google.gson.Gson;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import net.minecraft.launcher.process.JavaProcess;
import net.minecraft.launcher.process.JavaProcessLauncher;
import net.minecraft.launcher.process.JavaProcessListener;
import org.apache.log4j.LogManager;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.configuration.SimpleConfiguration;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponent;
import org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponentImpl;
import org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedBootInfo;
import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaConfig;
import org.tlauncher.tlauncher.updater.bootstrapper.model.LibraryConfig;
import org.tlauncher.tlauncher.updater.bootstrapper.view.DownloadingFrameElement;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.Reflect;
import org.tlauncher.util.StringUtil;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/rmo/Bootstrapper.class */
public final class Bootstrapper {
    private static final String linkUpdateErrorRu = "https://tlauncher.org/ru/error-kb4515384.html";
    private static final String linkUpdateErrorEn = "https://tlauncher.org/en/error-kb4515384.html";
    private static final String msiAfterBurnerRu = "https://tlauncher.org/ru/crash-afterburner.html";
    private static final String msiAfterBurnerEn = "https://tlauncher.org/en/crash-afterburner.html";
    public static SimpleConfiguration innerConfig;
    public static LangConfiguration langConfiguration;
    private static SimpleConfiguration launcherConfig;
    private static JavaConfig javaConfig;
    private static LibraryConfig libraryConfig;
    private final BootstrapperListener listener = new BootstrapperListener();
    private JavaProcessLauncher processLauncher;
    private static final String PROTECTION = "protection.txt";
    private JavaProcess process;
    private boolean started;
    private PreparedEnvironmentComponent preparedEnvironmentComponent;
    private File jvmFolder;
    private final String[] args;
    private FileLock lock;
    public static final File directory = new File(".");
    private static int i = 0;

    public Bootstrapper(String[] args) {
        this.args = args;
    }

    public static void main(String[] args) {
        File jvmFolder = null;
        try {
            System.setProperty("java.net.preferIPv4Stack", "true");
            initConfig();
            U.initializeLoggerU(MinecraftUtil.getWorkingDirectory(launcherConfig.get("minecraft.gamedir")), "boot");
            U.log(CoreConstants.EMPTY_STRING);
            U.log("-------------------------------------------------------------------");
            Class.forName("org.apache.log4j.helpers.NullEnumeration");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ExceptionInInitializerError error) {
                if ((error.getCause() instanceof IllegalArgumentException) && error.getCause().getMessage().contains("Text-specific LCD")) {
                    String link = "https://tlauncher.org/ru/font-error.html";
                    boolean ussr = launcherConfig.isUSSRLocale();
                    if (!ussr) {
                        link = "https://tlauncher.org/en/font-error.html";
                    }
                    OS.openLink(link);
                    System.exit(-1);
                }
            } catch (Throwable ex) {
                U.log(ex);
            }
            if (!testDoubleRunning()) {
                System.exit(0);
            }
            if (!checkFreeSpace(FileUtil.SIZE_200.longValue())) {
                showDiskProblem();
            }
            createAndValidateWorkDir();
            validateTempDir();
            valitdateKB4515384();
            boolean properJRE = false;
            if (launcherConfig.getBoolean("not.work.jfxwebkit.dll") || launcherConfig.getBoolean("fixed.gpu.jre.error")) {
                properJRE = true;
            }
            File jvm = getJVM(properJRE);
            jvmFolder = jvm.getParentFile();
            Bootstrapper bootstrapper = new Bootstrapper(args);
            bootstrapper.activeDoublePreparingJVM();
            Downloader downloader = new Downloader(ConnectionQuality.NORMAL);
            DownloadingFrameElement downloadingBarElement = new DownloadingFrameElement(langConfiguration);
            PreparedEnvironmentComponent prepareLauncher = new PreparedEnvironmentComponentImpl(libraryConfig, javaConfig, getWorkFolder(), jvmFolder, downloader);
            downloader.addListener(downloadingBarElement);
            DownloadedBootInfo info = prepareLauncher.validateLibraryAndJava();
            prepareLauncher.preparedLibrariesAndJava(info);
            bootstrapper.setPreparedEnvironmentComponent(prepareLauncher);
            bootstrapper.setJVMFolder(jvm);
            bootstrapper.diactivateDoublePreparingJVM();
            bootstrapper.start();
        } catch (AccessDeniedException e) {
            Alert.showErrorHtml(CoreConstants.EMPTY_STRING, langConfiguration.get("alert.access.denied.message", e.getFile(), e.getOtherFile()));
            TLauncher.kill();
        } catch (Throwable e2) {
            if ((e2 instanceof IOException) && Objects.nonNull(jvmFolder) && jvmFolder.toString().contains("jvms")) {
                fixedOnce(args, jvmFolder);
            } else if (e2 instanceof UnsupportedCharsetException) {
                Alert.showErrorHtml("not proper UnsupportedCharsetException", langConfiguration.get("not proper UnsupportedCharsetException"));
                TLauncher.kill();
            }
            e2.printStackTrace();
            String message = e2.getMessage();
            if (Objects.nonNull(message) && message.contains("GetIpAddrTable")) {
                Alert.showErrorHtml(CoreConstants.EMPTY_STRING, langConfiguration.get("addr.table.error"));
                TLauncher.kill();
            }
            U.log("problem with preparing boostrapper");
            TlauncherUtil.showCriticalProblem(e2);
            TLauncher.kill();
        }
    }

    private static boolean testDoubleRunning() throws InterruptedException {
        int value = TlauncherUtil.checkDoubleRunning();
        if (value > 0) {
            showWarningMessage(langConfiguration.get("double.running.title"), String.format(langConfiguration.get("double.running"), Integer.valueOf(value)));
            return false;
        }
        TlauncherUtil.createTimeStart();
        return true;
    }

    private static void valitdateKB4515384() {
        if (launcherConfig.getBoolean("block.updater.message")) {
            return;
        }
        if (!launcherConfig.getBoolean("retest.update")) {
            launcherConfig.set("retest.update", true, true);
        } else {
            findNotProperUpdater();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean findNotProperUpdater() {
        if (OS.is(OS.WINDOWS)) {
            boolean KB4515384Exists = OS.executeByTerminal("wmic qfe get HotFixID", 5).contains("KB4515384");
            if (KB4515384Exists) {
                showUpdateWinError();
                Alert.showErrorHtml(langConfiguration.get("warning.KB4515384.problem"), 500);
            }
            return KB4515384Exists;
        }
        return false;
    }

    private static void validateTempDir() throws IOException {
        try {
            Files.createTempFile("test", "txt", new FileAttribute[0]);
        } catch (IOException e) {
            try {
                if (Objects.nonNull(System.getProperty("java.io.tmpdir"))) {
                    Path folder = Paths.get(System.getProperty("java.io.tmpdir"), new String[0]);
                    if (Files.isRegularFile(folder, new LinkOption[0])) {
                        Files.delete(folder);
                    }
                    if (!Files.exists(folder, new LinkOption[0])) {
                        FileUtil.createFolder(folder.toFile());
                    }
                }
            } catch (IOException e1) {
                if (e1.getMessage().contains("createScrollWrapper")) {
                    Alert.showWarning(CoreConstants.EMPTY_STRING, langConfiguration.get("temp.dir.error"));
                    System.exit(-1);
                }
                throw e1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fixedOnce(String[] args, File jvmFolder) {
        if (i != 0) {
            return;
        }
        i++;
        FileUtil.deleteDirectory(jvmFolder);
        Alert.showErrorHtml(CoreConstants.EMPTY_STRING, langConfiguration.get("run.again.launcher"));
    }

    private static void createAndValidateWorkDir() throws AccessDeniedException {
        File workDir = getWorkFolder();
        if (workDir.exists()) {
            if (!Files.isWritable(workDir.toPath()) || !Files.isReadable(workDir.toPath())) {
                int random_number1 = 1 + ((int) (Math.random() * 100.0d));
                String workDirectory = String.valueOf(workDir) + random_number1;
                File correctFolder = new File(workDirectory);
                launcherConfig.set("minecraft.gamedir", correctFolder);
                throw new AccessDeniedException(String.valueOf(workDir), String.valueOf(correctFolder), CoreConstants.EMPTY_STRING);
            }
            return;
        }
        File file = new File(String.valueOf(workDir));
        if (!file.mkdir()) {
            launcherConfig.set("minecraft.gamedir", (Object) null);
        }
    }

    private static File getJVM(boolean properJVM) {
        File tlauncherFolder = MinecraftUtil.getSystemRelatedDirectory("tlauncher");
        return TlauncherUtil.getJVMFolder(javaConfig, tlauncherFolder);
    }

    private static void showDiskProblem() {
        String minecraftGamedir = launcherConfig.get("minecraft.gamedir");
        File minecraftFolder = minecraftGamedir == null ? MinecraftUtil.getSystemRelatedDirectory(innerConfig.get("folder")) : new File(minecraftGamedir);
        String url = launcherConfig.isUSSRLocale() ? "http://www.inetkomp.ru/uroki/488-osvobodit-mesto-na-diske-c.html" : "https://www.windowscentral.com/best-7-ways-free-hard-drive-space-windows-10";
        String path = minecraftFolder.toPath().getRoot().toString();
        String message = langConfiguration.get("place.disk.warning", path) + "<br><br>" + langConfiguration.get("alert.start.message", url);
        U.log(message);
        TlauncherUtil.showCriticalProblem(message);
        TLauncher.kill();
    }

    public static JavaProcessLauncher restartLauncher() {
        initConfig();
        File directory2 = new File(".");
        String path = OS.getJavaPathByHome(true);
        JavaProcessLauncher processLauncher = new JavaProcessLauncher(path, new String[0]);
        log("choose jvm for restart:" + path);
        String classPath = FileUtil.getRunningJar().getPath();
        processLauncher.directory(directory2);
        processLauncher.addCommand("-cp");
        processLauncher.addCommand(classPath + System.getProperty("path.separator"));
        processLauncher.addCommand(innerConfig.get("bootstrapper.class"));
        U.debug(processLauncher);
        return processLauncher;
    }

    private static File getWorkFolder() {
        String minecraftGamedir = launcherConfig.get("minecraft.gamedir");
        return minecraftGamedir == null ? MinecraftUtil.getSystemRelatedDirectory(innerConfig.get("folder")) : new File(minecraftGamedir);
    }

    private static void initConfig() {
        Gson g = new Gson();
        try {
            try {
                innerConfig = new SimpleConfiguration(Bootstrapper.class.getResourceAsStream("/inner.tlauncher.properties"));
            } catch (NullPointerException e) {
                String path = FileUtil.getRunningJar().toString();
                if (path.contains("!" + File.separator)) {
                    Alert.showError("Error", String.format("Java can't work with path that contains symbol '!', create new local user without characters '!'(use new local user for game) and use path to TLauncher without '!' characters \r\ncurrent: %s\r\n\r\nДжава не работает c путями в которых содержится восклицательный знак '!' , создайте новую учетную запись без '!' знаков(используйте её для игры) и используйте путь к файлу TLauncher без '!'\r\n текущий: %s", path, path));
                    System.exit(-2);
                }
            }
            launcherConfig = new SimpleConfiguration(new File(MinecraftUtil.getSystemRelatedDirectory(innerConfig.get("settings.new")).getCanonicalPath()));
            String locale = launcherConfig.get("locale");
            Locale locale2 = Locale.getDefault();
            if (locale != null) {
                locale2 = Configuration.getLocaleOf(locale);
            }
            List<Locale> listLocales = Configuration.getDefaultLocales(innerConfig);
            Locale selected = Configuration.findSuitableLocale(locale2, listLocales);
            langConfiguration = new LangConfiguration((Locale[]) listLocales.toArray(new Locale[0]), selected, innerConfig.get("bootstrapper.language.folder"));
            libraryConfig = (LibraryConfig) g.fromJson((Reader) new InputStreamReader(Bootstrapper.class.getResourceAsStream("/bootstrapper.libraries.json"), StandardCharsets.UTF_8), (Class<Object>) LibraryConfig.class);
            javaConfig = (JavaConfig) g.fromJson((Reader) new InputStreamReader(Bootstrapper.class.getResourceAsStream("/bootstrapper.jre.json"), StandardCharsets.UTF_8), (Class<Object>) JavaConfig.class);
        } catch (Throwable e1) {
            TlauncherUtil.showCriticalProblem(e1);
        }
    }

    private static int showQuestion(String title, String messae, String button, String button2) {
        return JOptionPane.showOptionDialog((Component) null, messae, title, 0, 2, (Icon) null, new Object[]{button, button2}, button2);
    }

    private static void showWarningMessage(String title, String message) {
        JOptionPane.showOptionDialog((Component) null, message, title, 1, 1, (Icon) null, new Object[]{"ok"}, 0);
    }

    private static boolean checkFreeSpace(long size) {
        String minecraftGamedir = launcherConfig.get("minecraft.gamedir");
        File minecraftFolder = minecraftGamedir == null ? MinecraftUtil.getSystemRelatedDirectory(innerConfig.get("folder")) : new File(minecraftGamedir);
        return FileUtil.checkFreeSpace(minecraftFolder, size);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(Object... s) {
        U.log("[Bootstrapper]", s);
    }

    private JavaProcessLauncher createLauncher(String[] args) {
        log("createLauncher");
        String jvm = OS.appendBootstrapperJvm(this.jvmFolder.getPath());
        log("choose jvm:" + jvm);
        JavaProcessLauncher processLauncher = new JavaProcessLauncher(jvm, new String[0]);
        processLauncher.directory(directory);
        processLauncher.addCommand("-Xmx" + innerConfig.get("max.memory") + ANSIConstants.ESC_END);
        processLauncher.addCommand("-Dfile.encoding=UTF8");
        String classPath = FileUtil.getRunningJar().getPath();
        String separator = File.pathSeparator;
        log("validate files");
        try {
            classPath = (classPath + separator) + StringUtil.convertListToString(separator, this.preparedEnvironmentComponent.getLibrariesForRunning());
        } catch (Throwable e) {
            U.log(e);
            TlauncherUtil.showCriticalProblem(new StringBuilder((langConfiguration.get("updater.download.fail", langConfiguration.get("java.reinstall")) + "<br>").replace("- problem1", CoreConstants.EMPTY_STRING)).toString());
            System.exit(-1);
        }
        log("end validated files");
        processLauncher.addCommand("-cp", classPath);
        processLauncher.addCommand(innerConfig.get("main.class"));
        if (args != null && args.length > 0) {
            processLauncher.addCommands(args);
        }
        return processLauncher;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void die(int status) {
        log("I can be terminated now: " + status);
        if (!this.started && this.process.isRunning()) {
            log("...started instance also will be terminated.");
            this.process.stop();
        }
        LogManager.shutdown();
        System.exit(status);
    }

    public void start() throws IOException {
        this.processLauncher = createLauncher(this.args);
        log("Starting launcher...");
        this.processLauncher.setListener(this.listener);
        TlauncherUtil.clearTimeLabel();
        this.process = this.processLauncher.start();
    }

    private void setPreparedEnvironmentComponent(PreparedEnvironmentComponent preparedEnvironmentComponent) {
        this.preparedEnvironmentComponent = preparedEnvironmentComponent;
    }

    private void setJVMFolder(File jvmFolder) {
        this.jvmFolder = jvmFolder;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/rmo/Bootstrapper$LoadingStep.class */
    public enum LoadingStep {
        INITIALIZING(21),
        LOADING_CONFIGURATION(35),
        LOADING_CONSOLE(41),
        LOADING_MANAGERS(51),
        LOADING_WINDOW(62),
        PREPARING_MAINPANE(77),
        POSTINIT_GUI(82),
        REFRESHING_INFO(91),
        SUCCESS(100);
        
        public static final String LOADING_PREFIX = "[Loading]";
        private final int percentage;

        LoadingStep(int percentage) {
            this.percentage = percentage;
        }

        public int getPercentage() {
            return this.percentage;
        }
    }

    private void activeDoublePreparingJVM() throws IOException {
        File tlauncherFolder = MinecraftUtil.getSystemRelatedDirectory("tlauncher");
        File f = new File(tlauncherFolder, PROTECTION);
        FileUtil.createFile(f);
        if (f.exists()) {
            FileChannel ch2 = FileChannel.open(f.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            this.lock = ch2.tryLock();
            if (Objects.isNull(this.lock)) {
                LogManager.shutdown();
                System.exit(4);
            }
        }
    }

    private void diactivateDoublePreparingJVM() throws IOException {
        if (Objects.nonNull(this.lock)) {
            this.lock.release();
        }
    }

    private static void showUpdateWinError() {
        boolean ussr = launcherConfig.isUSSRLocale();
        if (ussr) {
            OS.openLink(linkUpdateErrorRu);
        } else {
            OS.openLink(linkUpdateErrorEn);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/rmo/Bootstrapper$BootstrapperListener.class */
    public class BootstrapperListener implements JavaProcessListener {
        private final StringBuffer buffer;

        private BootstrapperListener() {
            this.buffer = new StringBuffer();
        }

        @Override // net.minecraft.launcher.process.JavaProcessListener
        public void onJavaProcessLog(JavaProcess jp, String line) {
            U.plog('>', line);
            this.buffer.append(line).append('\n');
            if (line.startsWith(LoadingStep.LOADING_PREFIX)) {
                if (line.length() < LoadingStep.LOADING_PREFIX.length() + 2) {
                    Bootstrapper.log("Cannot parse line: content is empty.");
                    return;
                }
                String content = line.substring(LoadingStep.LOADING_PREFIX.length() + 1);
                LoadingStep step = (LoadingStep) Reflect.parseEnum(LoadingStep.class, content);
                if (step == null) {
                    Bootstrapper.log("Cannot parse line: cannot parse step");
                } else if (step.getPercentage() == 100) {
                    Bootstrapper.this.started = true;
                    Bootstrapper.this.die(0);
                }
            }
        }

        @Override // net.minecraft.launcher.process.JavaProcessListener
        public void onJavaProcessEnded(JavaProcess jp) {
            int exit = jp.getExitCode();
            if (exit == 1 && Bootstrapper.this.jvmFolder.toString().contains("jvms")) {
                Bootstrapper.fixedOnce(Bootstrapper.this.args, Bootstrapper.this.jvmFolder);
            }
            switch (exit) {
                case -1073740791:
                    TlauncherUtil.showCriticalProblem(Bootstrapper.langConfiguration.get("alert.start.message", "https://tlauncher.org/ru/closed-minecraft-1073740791.html"));
                    TlauncherUtil.showCriticalProblem(Bootstrapper.langConfiguration.get("alert.start.message", "https://tlauncher.org/en/closed-minecraft-1073740791.html"));
                    Bootstrapper.this.die(exit);
                    break;
                case -1073740771:
                    if (!Bootstrapper.findNotProperUpdater()) {
                        boolean ussr = Bootstrapper.launcherConfig.isUSSRLocale();
                        if (ussr) {
                            OS.openLink(Bootstrapper.msiAfterBurnerRu);
                        } else {
                            OS.openLink(Bootstrapper.msiAfterBurnerEn);
                        }
                        TlauncherUtil.showCriticalProblem(Bootstrapper.langConfiguration.get("msi.after.burner.block"));
                    }
                    Bootstrapper.this.die(exit);
                    break;
            }
            if (exit != 0) {
                TlauncherUtil.showCriticalProblem(this.buffer.toString());
            }
            Bootstrapper.this.die(exit);
        }

        @Override // net.minecraft.launcher.process.JavaProcessListener
        public void onJavaProcessError(JavaProcess jp, Throwable e) {
            Bootstrapper.log(e);
        }
    }
}
