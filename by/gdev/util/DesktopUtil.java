package by.gdev.util;

import by.gdev.util.OSInfo;
import by.gdev.util.model.download.Metadata;
import by.gdev.util.model.download.Repo;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import javax.swing.JFileChooser;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/DesktopUtil.class */
public class DesktopUtil {
    private static final String PROTECTION = "protection.txt";
    private static FileLock lock;
    private static final Logger log = LoggerFactory.getLogger(DesktopUtil.class);
    public static Set<PosixFilePermission> PERMISSIONS = new HashSet<PosixFilePermission>() { // from class: by.gdev.util.DesktopUtil.1
        {
            add(PosixFilePermission.OWNER_READ);
            add(PosixFilePermission.OWNER_WRITE);
            add(PosixFilePermission.OWNER_EXECUTE);
            add(PosixFilePermission.OTHERS_READ);
            add(PosixFilePermission.OTHERS_WRITE);
            add(PosixFilePermission.OTHERS_EXECUTE);
            add(PosixFilePermission.GROUP_READ);
            add(PosixFilePermission.GROUP_WRITE);
            add(PosixFilePermission.GROUP_EXECUTE);
        }
    };

    public static File getSystemPath(OSInfo.OSType type, String path) {
        File file;
        String userHome = System.getProperty("user.home", ".");
        switch (type) {
            case LINUX:
            case SOLARIS:
                file = new File(userHome, path);
                break;
            case WINDOWS:
                String applicationData = System.getenv("APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                file = new File(folder, path);
                break;
            case MACOSX:
                file = new File(userHome, "Library/Application Support/" + path);
                break;
            default:
                file = new File(userHome, path);
                break;
        }
        return file;
    }

    public static String getChecksum(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        byte[] b = createChecksum(file, algorithm);
        StringBuilder result = new StringBuilder();
        for (byte cb : b) {
            result.append(Integer.toString((cb & 255) + 256, 16).substring(1));
        }
        return result.toString();
    }

    private static byte[] createChecksum(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        int numRead;
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
        Throwable th = null;
        try {
            byte[] buffer = new byte[8192];
            MessageDigest complete = MessageDigest.getInstance(algorithm);
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            byte[] digest = complete.digest();
            if (fis != null) {
                if (0 != 0) {
                    try {
                        fis.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    fis.close();
                }
            }
            return digest;
        } finally {
        }
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
        if (OSInfo.getOSType().equals(OSInfo.OSType.WINDOWS)) {
            b.append("w.exe");
        }
        return b.toString();
    }

    public static <T> T uncheckCall(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, R> Function<T, R> wrap(CheckedFunction<T, R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static void sleep(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int numberOfAttempts(List<String> urls, int maxAttepmts, RequestConfig requestConfig, CloseableHttpClient httpclient) {
        for (String url : urls) {
            try {
                HttpHead http = new HttpHead(url);
                http.setConfig(requestConfig);
                httpclient.execute((HttpUriRequest) http);
                return maxAttepmts;
            } catch (IOException e) {
            }
        }
        return 1;
    }

    private static void createDirectory(File file) throws IOException {
        if (!file.isFile() && file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
    }

    public static void diactivateDoubleDownloadingResourcesLock() throws IOException {
        if (Objects.nonNull(lock)) {
            lock.release();
        }
    }

    public static String convertListToString(String del, List<Path> list) {
        StringBuilder b = new StringBuilder();
        for (Path string : list) {
            b.append(string).append(del);
        }
        return b.toString();
    }

    public static void activeDoubleDownloadingResourcesLock(String container) throws IOException {
        File f = new File(container, PROTECTION);
        createDirectory(f);
        if (f.exists()) {
            FileChannel ch2 = FileChannel.open(f.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            lock = ch2.tryLock();
            if (Objects.isNull(lock)) {
                log.warn("Lock could not be acquired ");
                System.exit(4);
            }
        }
    }

    public static String getJavaRun(Repo java) {
        String javaRun = null;
        for (Metadata s : java.getResources()) {
            if (s.isExecutable()) {
                javaRun = s.getPath();
            }
        }
        return javaRun;
    }

    public static String appendBootstrapperJvm2(String path) {
        StringBuilder b = new StringBuilder();
        if (OSInfo.getOSType() == OSInfo.OSType.MACOSX) {
            b.append("Contents").append(File.separatorChar).append("Home").append(File.separatorChar);
        }
        return appendToJVM(new File(b.toString()).getPath());
    }

    public static void openLink(OSInfo.OSType type, String uri) {
        try {
            Desktop.getDesktop().browse(new URI(uri));
        } catch (IOException | URISyntaxException e) {
            log.warn("can't open link", (Throwable) e);
            if (type.equals(OSInfo.OSType.LINUX)) {
                try {
                    Runtime.getRuntime().exec("gnome-open " + uri);
                } catch (IOException e2) {
                    log.warn("can't open link for linix", (Throwable) e);
                }
            }
        }
    }

    public static void initLookAndFeel() {
        LookAndFeel defaultLookAndFeel = null;
        try {
            defaultLookAndFeel = UIManager.getLookAndFeel();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new JFileChooser();
        } catch (Throwable t) {
            log.warn("problem with ", t);
            if (Objects.nonNull(defaultLookAndFeel)) {
                try {
                    UIManager.setLookAndFeel(defaultLookAndFeel);
                } catch (Throwable e) {
                    log.warn("coudn't set defualt look and feel", e);
                }
            }
        }
    }
}
