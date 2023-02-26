package org.tlauncher.util;

import ch.qos.logback.core.CoreConstants;
import java.awt.Desktop;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import org.apache.http.protocol.HttpRequestExecutor;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/OS.class */
public enum OS {
    LINUX("linux", "unix"),
    WINDOWS("win"),
    OSX("mac"),
    SOLARIS("solaris", "sunos"),
    UNKNOWN("unknown");
    
    private final String name;
    private final String[] aliases;
    public static final String NAME = System.getProperty("os.name");
    public static final String VERSION = System.getProperty("os.version");
    public static final double JAVA_VERSION = getJavaVersion();
    public static final OS CURRENT = getCurrent();
    private static final Map<String, String> systemInfo = new HashMap();
    private static final String[] browsers = {"google-chrome", "firefox", "opera", "konqueror", "mozilla"};

    OS(String... aliases) {
        if (aliases == null) {
            throw new NullPointerException();
        }
        this.name = toString().toLowerCase(Locale.ROOT);
        this.aliases = aliases;
    }

    private static OS getCurrent() {
        OS[] values;
        String[] strArr;
        String osName = NAME.toLowerCase(Locale.ROOT);
        for (OS os : values()) {
            for (String alias : os.aliases) {
                if (osName.contains(alias)) {
                    return os;
                }
            }
        }
        return UNKNOWN;
    }

    private static double getJavaVersion() {
        String version = System.getProperty("java.version");
        int count = 0;
        int pos = 0;
        while (pos < version.length() && count < 2) {
            if (version.charAt(pos) == '.') {
                count++;
            }
            pos++;
        }
        String doubleVersion = version.substring(0, pos - 1);
        return Double.parseDouble(doubleVersion);
    }

    public static boolean isJava8() {
        return "8".equals(getJavaNumber());
    }

    public static boolean is(OS... any) {
        if (any == null) {
            throw new NullPointerException();
        }
        if (any.length == 0) {
            return false;
        }
        for (OS compare : any) {
            if (CURRENT == compare) {
                return true;
            }
        }
        return false;
    }

    public static String getJavaPathByHome(boolean appendBinFolder) {
        String path = System.getProperty("java.home");
        if (appendBinFolder) {
            path = appendToJVM(path);
        }
        return path;
    }

    public static String appendToJVM(String path) {
        char separator = File.separatorChar;
        StringBuilder b = new StringBuilder(path);
        b.append(separator);
        b.append("bin").append(separator).append("java");
        if (CURRENT == WINDOWS) {
            b.append("w.exe");
        }
        return b.toString();
    }

    public static String appendBootstrapperJvm(String path) {
        StringBuilder b = new StringBuilder();
        if (CURRENT == OSX && !path.toLowerCase().endsWith("jre") && !path.toLowerCase().endsWith("home")) {
            b.append("Contents").append(File.separatorChar).append("Home").append(File.separatorChar).append("jre").append(File.separatorChar);
        }
        return appendToJVM(new File(path, b.toString()).getPath());
    }

    public static String appendBootstrapperJvm1(String path) {
        StringBuilder b = new StringBuilder();
        if (CURRENT == OSX && !path.toLowerCase().endsWith("jre") && !path.toLowerCase().endsWith("home")) {
            b.append("jre.bundle").append(File.separatorChar).append("Contents").append(File.separatorChar).append("Home").append(File.separatorChar);
        }
        return appendToJVM(new File(path, b.toString()).getPath());
    }

    public static String appendBootstrapperJvm2(String path) {
        StringBuilder b = new StringBuilder();
        if (CURRENT == OSX) {
            b.append("Contents").append(File.separatorChar).append("Home").append(File.separatorChar);
        }
        return appendToJVM(new File(path, b.toString()).getPath());
    }

    public static String getSummary() {
        Configuration c = TLauncher.getInstance().getConfiguration();
        String bitMessage = c.get(TestEnvironment.WARMING_MESSAGE);
        String options = Objects.nonNull(System.getenv("_java_options")) ? System.lineSeparator() + "_java_options " + System.getenv("_java_options") : CoreConstants.EMPTY_STRING;
        StringBuilder builder = new StringBuilder();
        builder.append("-------------------------------------------------------").append(System.lineSeparator());
        builder.append(NAME).append(" ").append(VERSION).append(", Java").append(" ").append(System.getProperty("java.version")).append(", jvm bit ").append(getJavaBit()).append(", ").append(Arch.TOTAL_RAM_MB).append(" MB RAM");
        if (bitMessage != null && Arch.CURRENT == Arch.x32) {
            builder.append(", ").append(bitMessage);
        }
        builder.append(System.lineSeparator());
        builder.append("java path=").append(getJavaPathByHome(true));
        builder.append(options);
        String processInfo = c.get("process.info");
        if (Objects.nonNull(processInfo)) {
            builder.append(System.lineSeparator()).append(processInfo);
        }
        String gpu = c.get("gpu.info.full");
        if (Objects.nonNull(gpu)) {
            builder.append(System.lineSeparator()).append(gpu);
        }
        builder.append(System.lineSeparator()).append("-------------------------------------------------------");
        return builder.toString();
    }

    public static Arch getJavaBit() {
        String res = System.getProperty("sun.arch.data.model");
        if (res != null && res.equalsIgnoreCase("64")) {
            return Arch.x64;
        }
        return Arch.x32;
    }

    private static void rawOpenLink(URI uri) throws Throwable {
        if (!Desktop.isDesktopSupported()) {
            log("Your system doesnt'have a Desktop object");
        } else {
            Desktop.getDesktop().browse(uri);
        }
    }

    public static boolean openLink(URI uri, boolean alertError) {
        log("Trying to open link with default browser:", uri);
        try {
            if (is(LINUX)) {
                Runtime.getRuntime().exec("gnome-open " + uri);
                return true;
            } else if (!Desktop.isDesktopSupported()) {
                log("Your system doesnt'have a Desktop object");
                return false;
            } else {
                Desktop.getDesktop().browse(uri);
                return true;
            }
        } catch (Throwable th) {
            try {
                showDocument(uri.toString());
                return true;
            } catch (Throwable t) {
                log("Failed to open link with default browser:", uri, t);
                if (alertError) {
                    SwingUtilities.invokeLater(() -> {
                        Alert.showLocError("ui.error.openlink", uri);
                    });
                    return false;
                }
                return false;
            }
        }
    }

    public static boolean openLink(URI uri) {
        return openLink(uri, true);
    }

    public static boolean openLink(URL url, boolean alertError) {
        log("Trying to open URL with default browser:", url);
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            U.log("error", e);
        }
        return openLink(uri, alertError);
    }

    public static boolean openLink(URL url) {
        return openLink(url, true);
    }

    public static boolean openLink(String url) {
        try {
            return openLink(new URI(url), true);
        } catch (URISyntaxException e) {
            U.log(e);
            return false;
        }
    }

    private static void openPath(File path, boolean appendSeparator) throws Throwable {
        String absPath = path.getAbsolutePath() + File.separatorChar;
        Runtime r = Runtime.getRuntime();
        Throwable t = null;
        switch (CURRENT) {
            case OSX:
                String[] cmdArr = {"/usr/bin/open", absPath};
                try {
                    r.exec(cmdArr);
                    return;
                } catch (Throwable e) {
                    t = e;
                    log("Cannot open folder using:\n", cmdArr, e);
                    break;
                }
            case WINDOWS:
                String cmd = String.format("cmd.exe /C start \"Open path\" \"%s\"", absPath);
                try {
                    r.exec(cmd);
                    return;
                } catch (Throwable e2) {
                    t = e2;
                    log("Cannot open folder using CMD.exe:\n", cmd, e2);
                    break;
                }
            case LINUX:
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(absPath));
                    return;
                }
            default:
                log("... will use desktop");
                break;
        }
        try {
            rawOpenLink(path.toURI());
        } catch (Throwable e3) {
            t = e3;
        }
        if (t == null) {
            return;
        }
        throw t;
    }

    public static boolean openFolder(File folder, boolean alertError) {
        log("Trying to open folder:", folder);
        if (!folder.isDirectory()) {
            log("This path is not a directory, sorry.");
            return false;
        }
        try {
            openPath(folder, true);
            return true;
        } catch (Throwable e) {
            log("Failed to open folder:", e);
            if (alertError) {
                Alert.showLocError("ui.error.openfolder", folder);
                return false;
            }
            return false;
        }
    }

    public static boolean openFolder(File folder) {
        return openFolder(folder, true);
    }

    public static boolean openFile(File file, boolean alertError) {
        log("Trying to open file:", file);
        if (!file.isFile()) {
            log("This path is not a file, sorry.");
            return false;
        }
        try {
            openPath(file, false);
            return true;
        } catch (Throwable e) {
            log("Failed to open file:", e);
            if (alertError) {
                Alert.showLocError("ui.error.openfolder", file);
                return false;
            }
            return false;
        }
    }

    public static boolean openFile(File file) {
        return openFile(file, true);
    }

    protected static void log(Object... o) {
        U.log("[OS]", o);
    }

    public static String getJavaNumber() {
        if (String.valueOf(JAVA_VERSION).startsWith("10")) {
            return "10";
        }
        if (String.valueOf(JAVA_VERSION).startsWith("11")) {
            return "11";
        }
        if (JAVA_VERSION > 2.0d) {
            return String.valueOf(JAVA_VERSION).substring(0, 1);
        }
        return String.valueOf(JAVA_VERSION).substring(2, 3);
    }

    public static void fillSystemInfo() {
        int first;
        try {
            if (getCurrent() == WINDOWS) {
                Process p = Runtime.getRuntime().exec("cmd.exe /C chcp 437 & systeminfo");
                p.waitFor(30L, TimeUnit.SECONDS);
                String res = FileUtil.readStream(p.getInputStream(), "cp866");
                String[] array = res.split("\r\n");
                int i = 0;
                while (i < array.length) {
                    String r = array[i];
                    if (!r.isEmpty() && (first = r.indexOf(":")) > 0) {
                        U.debug(r);
                        if (r.substring(0, first).equalsIgnoreCase("Processor(s)")) {
                            i++;
                            systemInfo.put(r.substring(0, first), r.substring(first + 1) + array[i]);
                        } else {
                            systemInfo.put(r.substring(0, first), r.substring(first + 1));
                        }
                    }
                    i++;
                }
            }
        } catch (Throwable e) {
            U.log(e);
        }
    }

    public static String executeByTerminal(String command, int time) {
        String res = CoreConstants.EMPTY_STRING;
        try {
            if (getCurrent() == WINDOWS) {
                Process p = Runtime.getRuntime().exec("cmd.exe /C chcp 437 & " + command);
                p.waitFor(time, TimeUnit.SECONDS);
                res = FileUtil.readStream(p.getInputStream(), "IBM437");
            } else if (is(LINUX)) {
                Process p2 = Runtime.getRuntime().exec(command);
                p2.waitFor(time, TimeUnit.SECONDS);
                res = FileUtil.readStream(p2.getInputStream());
            } else if (is(OSX)) {
                Process p3 = Runtime.getRuntime().exec(command);
                p3.waitFor(time, TimeUnit.SECONDS);
                res = FileUtil.readStream(p3.getInputStream());
            }
        } catch (Throwable e) {
            log(e);
        }
        return res;
    }

    public static String executeByTerminal(String command) {
        return executeByTerminal(command, 30);
    }

    public static String getSystemInfo(String key) {
        return systemInfo.get(key);
    }

    public String getName() {
        return this.name;
    }

    public boolean isUnsupported() {
        return this == UNKNOWN;
    }

    public boolean isCurrent() {
        return this == CURRENT;
    }

    private static void showDocument(String var1) {
        try {
            if (is(OSX)) {
                Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", String.class).invoke(null, var1);
            } else if (is(WINDOWS)) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + var1);
            } else {
                String var3 = null;
                String[] var4 = browsers;
                for (String var7 : var4) {
                    if (var3 == null && Runtime.getRuntime().exec(new String[]{"which", var7}).getInputStream().read() != -1) {
                        Runtime var10000 = Runtime.getRuntime();
                        var3 = var7;
                        String[] var10001 = {var7, var1};
                        var10000.exec(var10001);
                    }
                }
                if (var3 == null) {
                    throw new Exception("No web browser found");
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/util/OS$Arch.class */
    public enum Arch {
        x32,
        x64,
        UNKNOWN;
        
        public static final int MIN_MEMORY = 512;
        private final String asString = toString().substring(1);
        private final int asInt;
        public static final Arch CURRENT = getCurrent();
        public static final long TOTAL_RAM = getTotalRam();
        public static final int TOTAL_RAM_MB = (int) ((TOTAL_RAM / 1024) / 1024);
        public static final int MAX_MEMORY = getMaximumMemory();
        public static final int PREFERRED_MEMORY = getPreferWrapper();

        Arch() {
            int asInt_temp = 0;
            try {
                asInt_temp = Integer.parseInt(this.asString);
            } catch (RuntimeException e) {
            }
            this.asInt = asInt_temp;
        }

        private static Arch getCurrent() {
            Arch[] values;
            String curArch = System.getProperty("sun.arch.data.model");
            for (Arch arch : values()) {
                if (arch.asString.equals(curArch)) {
                    return arch;
                }
            }
            return UNKNOWN;
        }

        private static long getTotalRam() {
            long m = 0;
            try {
                m = ManagementFactory.getOperatingSystemMXBean().getTotalPhysicalMemorySize();
                if (m == 0) {
                    m = 10000000000L;
                }
            } catch (Throwable e) {
                try {
                    U.log("[ERROR] Cannot allocate total physical memory size!", e);
                    if (m == 0) {
                        m = 10000000000L;
                    }
                } catch (Throwable th) {
                    if (m == 0) {
                    }
                    throw th;
                }
            }
            return m;
        }

        private static int getPreferWrapper() {
            switch (CURRENT) {
                case x64:
                    if (TOTAL_RAM_MB > 6000) {
                        return HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE;
                    }
                    if (TOTAL_RAM_MB > 3000) {
                        return TOTAL_RAM_MB - 1024;
                    }
                    if (TOTAL_RAM_MB > 2000) {
                        return TOTAL_RAM_MB - 512;
                    }
                    return TOTAL_RAM_MB;
                case x32:
                    if (TOTAL_RAM_MB > 1500) {
                        return 1500;
                    }
                    if (TOTAL_RAM_MB > 1024) {
                        return 750;
                    }
                    return 512;
                default:
                    return 512;
            }
        }

        private static int getMaximumMemory() {
            switch (CURRENT) {
                case x64:
                    return TOTAL_RAM_MB;
                case x32:
                    if (TOTAL_RAM_MB > 1500) {
                        return 1024;
                    }
                    return 512;
                default:
                    return 512;
            }
        }

        public String asString() {
            return this == UNKNOWN ? toString() : this.asString;
        }

        public int asInteger() {
            return this.asInt;
        }

        public boolean isCurrent() {
            return this == CURRENT;
        }
    }

    public static String buildJVMKey() {
        StringBuilder b = new StringBuilder();
        switch (CURRENT) {
            case OSX:
                b.append("mac-os");
                break;
            case WINDOWS:
            case LINUX:
                b.append(CURRENT.name);
                break;
        }
        switch (Arch.CURRENT) {
            case x64:
                if (CURRENT.equals(WINDOWS)) {
                    b.append("-x64");
                    break;
                }
                break;
            case x32:
                if (CURRENT.equals(LINUX)) {
                    b.append("-i386");
                    break;
                } else if (CURRENT.equals(WINDOWS)) {
                    b.append("-x86");
                    break;
                }
                break;
        }
        return b.toString();
    }

    public static Path buildJAVAFolder() {
        Path p = null;
        switch (CURRENT) {
            case OSX:
                p = Paths.get("/Library/Java/JavaVirtualMachines", new String[0]);
                break;
            case WINDOWS:
                switch (Arch.CURRENT) {
                    case x64:
                        p = Paths.get("C:\\Program Files\\Java", new String[0]);
                        break;
                    case x32:
                        p = Paths.get("C:\\Program Files (x86)\\Java", new String[0]);
                        break;
                }
            case LINUX:
                p = Paths.get("/usr/lib/jvm", new String[0]);
                break;
        }
        if (Files.notExists(p, new LinkOption[0]) || Objects.isNull(p)) {
            return Paths.get(".", new String[0]);
        }
        return p;
    }
}
