package org.tlauncher.util;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.X509TrustManager;
import net.minecraft.launcher.Http;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.ModifiedVersion;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.InnerConfiguration;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.log.LogFrame;
import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaConfig;
import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaDownloadedElement;
import org.tlauncher.util.OS;
import org.tlauncher.util.async.AsyncThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/TlauncherUtil.class */
public class TlauncherUtil {
    public static final String PROTECTED_DOUBLE_RUNNING_FILE = "doubleRunningProtection.txt";
    private static final int CAN_RUNNING_AFTER = 1;
    public static final String LOG_CHARSET = defineCharsetString("cp1251");
    public static final String TLAUNCHER_ADDITIONAL_CONFIG = "TLauncherAdditional";

    public static void sendLog(Throwable e) {
        if (TLauncher.DEBUG) {
            return;
        }
        if (Localizable.get() == null) {
            try {
                Configuration settings = Configuration.createConfiguration();
                Locale locale = settings.getLocale();
                InnerConfiguration innerConfig = new InnerConfiguration(FileUtil.getResourceAppStream("/inner.tlauncher.properties"));
                Localizable.setLang(new LangConfiguration(settings.getLocales(), locale, innerConfig.get("tlauncher.language.folder")));
            } catch (IOException e1) {
                e1.addSuppressed(e);
                e = e1;
                e1.printStackTrace();
            }
        }
        new LogFrame(TLauncher.getInstance().getFrame(), e).setVisible(true);
    }

    public static void checkRedirect() {
    }

    public static int hostAvailabilityCheck(String host) {
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        try {
            URL url = new URL(host);
            HttpURLConnection httpConn = Downloadable.setUp(url.openConnection(), true);
            httpConn.setRequestMethod(HttpHead.METHOD_NAME);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.connect();
            U.debug(host + " : " + httpConn.getResponseCode());
            return httpConn.getResponseCode();
        } catch (Throwable th) {
            U.debug(host + " is down ");
            return 500;
        }
    }

    public static void testNet() {
        try {
            Http.performGet("https://tlauncher.org/repo/update/lch/additional_hot_servers.json");
            Http.performGet("https://dl2.fastrepo.org/not_remove_test_file.txt");
            testNet1();
        } catch (Throwable e) {
            if (e instanceof SSLHandshakeException) {
                Alert.showLocWarning(CoreConstants.EMPTY_STRING, "block.doctor.web", null);
            } else {
                testNet1();
            }
            U.log("error", e);
        }
    }

    private static void testNet1() {
        int code = hostAvailabilityCheck("http://page.tlauncher.org");
        if (code == 503 || code == 403) {
            Alert.showErrorHtml(CoreConstants.EMPTY_STRING, "alert.block.ip");
        }
    }

    public static void deactivateSSL() {
        Configuration c = TLauncher.getInstance().getConfiguration();
        if (c.get("ssl.deactivate.date") == null || LocalDate.parse(c.get("ssl.deactivate.date")).isBefore(LocalDate.now())) {
            TLauncher.getInstance().getConfiguration().set("ssl.deactivate.date", LocalDate.now().plusMonths(1L));
            try {
                TLauncher.getInstance().getConfiguration().save();
            } catch (IOException e1) {
                U.log(e1);
            }
        }
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new X509TrustManager[]{new X509TrustManager() { // from class: org.tlauncher.util.TlauncherUtil.1
                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Throwable var1) {
            U.log(var1);
        }
        HttpsURLConnection.setDefaultHostnameVerifier(s, sslSession -> {
            return true;
        });
    }

    public static void checkServersAvalability(String[] array) {
        List<CompletableFuture<Integer>> servers = (List) Arrays.stream(array).map(link -> {
            return CompletableFuture.supplyAsync(() -> {
                if (hostAvailabilityCheck(link) == 503) {
                    U.log("     server code 503 -> " + link);
                    return Integer.valueOf((int) HttpStatus.SC_SERVICE_UNAVAILABLE);
                }
                return Integer.valueOf((int) HttpStatus.SC_OK);
            }, AsyncThread.getService());
        }).collect(Collectors.toList());
        try {
            CompletableFuture.allOf((CompletableFuture[]) servers.toArray(new CompletableFuture[0])).get();
            if (servers.stream().allMatch(e -> {
                try {
                    return ((Integer) e.get()).equals(Integer.valueOf((int) HttpStatus.SC_OK));
                } catch (Exception e) {
                    return false;
                }
            })) {
                U.log("#####    all servers are available   ######");
            }
        } catch (Exception e2) {
        }
    }

    public static int checkDoubleRunning() {
        File f = MinecraftUtil.getTLauncherFile(PROTECTED_DOUBLE_RUNNING_FILE);
        if (Files.exists(f.toPath(), new LinkOption[0])) {
            try {
                Date start = new Date(Long.parseLong(FileUtil.readFile(f)));
                Date end = new Date(start.getTime() + TimeUnit.MINUTES.toMillis(1L));
                Date current = new Date();
                if (current.after(start) && current.before(end)) {
                    return (int) (TimeUnit.MINUTES.toSeconds(1L) - ((current.getTime() - start.getTime()) / 1000));
                }
                return 0;
            } catch (Throwable e) {
                U.log(e);
                return 0;
            }
        }
        return 0;
    }

    public static String resolveHostName(String path) throws MalformedURLException, UnknownHostException {
        URL url = new URL(path);
        return url.getProtocol() + "://" + InetAddress.getByName(url.getHost()).getHostAddress() + ":" + url.getPort() + url.getFile();
    }

    public static boolean isAdmin() {
        if (!OS.is(OS.WINDOWS)) {
            return true;
        }
        try {
            Class<?> cl = Class.forName("com.sun.security.auth.module.NTSystem");
            if (Objects.isNull(cl)) {
                return true;
            }
            Method method = cl.getMethod("getGroupIDs", new Class[0]);
            String[] groups = (String[]) method.invoke(cl.newInstance(), new Object[0]);
            for (String group : groups) {
                if (group.equals("S-1-5-32-544")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            U.log(e);
            return true;
        }
    }

    public static String getPageLanguage() {
        if (Objects.nonNull(TLauncher.getInstance()) && Objects.nonNull(TLauncher.getInstance().getConfiguration()) && TLauncher.getInstance().getConfiguration().isUSSRLocale()) {
            return "ru";
        }
        return "en";
    }

    public static String findJavaOptionAndGetName() {
        for (Map.Entry<String, String> e : System.getenv().entrySet()) {
            if (e.getKey().equalsIgnoreCase("_java_options")) {
                return e.getKey();
            }
        }
        return null;
    }

    public static String getStringError(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString().replaceAll(System.lineSeparator(), "<br>");
    }

    public static File getJVMFolder(JavaConfig config, File tlauncherFolder) {
        JavaDownloadedElement java = getProperJavaElement(config);
        return new File(new File(tlauncherFolder, "jvms"), java.getJavaFolder());
    }

    public static JavaDownloadedElement getProperJavaElement(JavaConfig config) {
        if (useX64JavaInsteadX32Java()) {
            return config.getConfig().get(OS.CURRENT).get(OS.Arch.x64);
        }
        return config.getConfig().get(OS.CURRENT).get(OS.Arch.CURRENT);
    }

    public static boolean useX64JavaInsteadX32Java() {
        if (OS.is(OS.WINDOWS) && OS.Arch.CURRENT.equals(OS.Arch.x32)) {
            String s = OS.executeByTerminal("wmic os get osarchitecture");
            return s.contains("64");
        }
        return false;
    }

    public static void showCriticalProblem(String message) {
        Alert.showErrorHtml("A critical error has occurred, ask for help <br> <a href='https://vk.me/tlauncher'> https://vk.me/tlauncher </a> or by mail <b> support@tlauncher.org </b><br><br>" + message, 500);
    }

    public static void showCriticalProblem(Throwable e) {
        showCriticalProblem(getStringError(e));
        TLauncher.kill();
    }

    public static boolean hasCorrectJavaFX() {
        try {
            Class.forName("javafx.embed.swing.JFXPanel");
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public static void fillGPUInfo(Configuration con, boolean wait) {
        try {
            if (OS.is(OS.WINDOWS)) {
                Path dxdiag = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), "logs", "tlauncher", "dxdiag.txt");
                boolean dxdiagExist = Files.exists(dxdiag, new LinkOption[0]);
                if (!dxdiagExist || FileUtils.isFileOlder(dxdiag.toFile(), DateUtils.addDays(new Date(), -10))) {
                    String command = String.format("dxdiag /whql:off /t %s", dxdiag.toString());
                    OS.executeByTerminal(command);
                }
                AsyncThread.execute(() -> {
                    if (wait) {
                        U.sleepFor(15000L);
                    }
                    if (Files.exists(dxdiag, new LinkOption[0])) {
                        try {
                            String file = FileUtil.readFile(dxdiag.toFile(), Charset.defaultCharset().name());
                            String[] params = file.split(System.lineSeparator());
                            String name = (String) Arrays.stream(params).filter(e -> {
                                String e = e.toLowerCase();
                                return e.contains("card name:") || e.contains("chip type:") || e.contains("display memory:");
                            }).map(s -> {
                                return s.split(":")[1];
                            }).collect(Collectors.joining(","));
                            if (StringUtils.isNotBlank(name)) {
                                con.set("gpu.info.full", name);
                            }
                            List<String> names = (List) Arrays.stream(params).filter(e2 -> {
                                return e2.toLowerCase().contains("card name:");
                            }).map(s2 -> {
                                return s2.split(":")[1];
                            }).collect(Collectors.toList());
                            if (!names.isEmpty()) {
                                String gpu = names.get(names.size() - 1).trim();
                                if (!gpu.equalsIgnoreCase("Intel(R) HD Graphics")) {
                                    con.set("gpu.info", gpu);
                                }
                            }
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                });
            } else if (OS.is(OS.LINUX)) {
                String res = OS.executeByTerminal("lshw -C display");
                String[] params = res.split(System.lineSeparator());
                List<String> names = (List) Arrays.stream(params).filter(e -> {
                    return e.contains("product:");
                }).map(s -> {
                    return s.split(":")[1];
                }).collect(Collectors.toList());
                if (!names.isEmpty()) {
                    con.set("gpu.info", names.get(names.size() - 1).trim());
                    con.set("gpu.info.full", names.get(names.size() - 1).trim());
                }
            } else if (OS.is(OS.OSX)) {
                String res2 = OS.executeByTerminal("system_profiler SPDisplaysDataType");
                List<String> names2 = (List) Arrays.stream(res2.split(System.lineSeparator())).filter(e2 -> {
                    return e2.toLowerCase().contains("chipset model:");
                }).map(s2 -> {
                    return s2.split(":")[1];
                }).collect(Collectors.toList());
                if (!names2.isEmpty()) {
                    con.set("gpu.info", names2.get(names2.size() - 1).trim());
                    con.set("gpu.info.full", names2.get(names2.size() - 1).trim());
                }
            }
        } catch (Throwable e3) {
            U.log(e3);
        }
    }

    public static String defineCharsetString(String charset) {
        if (Charset.isSupported(charset)) {
            return charset;
        }
        return Charset.defaultCharset().name();
    }

    public static void createTimeStart() {
        try {
            File file = MinecraftUtil.getTLauncherFile(PROTECTED_DOUBLE_RUNNING_FILE);
            FileUtil.writeFile(file, CoreConstants.EMPTY_STRING + new Date().getTime());
        } catch (Throwable e) {
            U.log("can't delete file", e);
        }
    }

    public static void clearTimeLabel() {
        U.log("[Double running]", "clear time label");
        try {
            FileUtil.deleteFile(MinecraftUtil.getTLauncherFile(PROTECTED_DOUBLE_RUNNING_FILE));
        } catch (Throwable e) {
            U.log("can't delete file", e);
        }
    }

    public static void processRemoteVersionToSave(CompleteVersion complete, String remoteVersion, Gson gson) {
        ModifiedVersion modifiedVersion = complete.getModifiedVersion();
        if (Objects.isNull(complete.getInheritsFrom()) && notHasAnyAdditionalTLauncherField(modifiedVersion)) {
            modifiedVersion.setRemoteVersion(remoteVersion);
        }
    }

    public static boolean notHasAnyAdditionalTLauncherField(ModifiedVersion modifiedVersion) {
        return modifiedVersion.getJar() == null && modifiedVersion.getModpack() == null && modifiedVersion.getModsLibraries() == null && modifiedVersion.getAdditionalFiles() == null;
    }

    public static void addAuthHeaders(HttpRequestBase http) throws RequiredTLAccountException, SelectedAnyOneTLAccountException {
        TLauncher tl = TLauncher.getInstance();
        Account ac = tl.getProfileManager().findUniqueTlauncherAccount();
        http.addHeader("uuid", tl.getProfileManager().getClientToken().toString());
        http.addHeader("accessToken", ac.getAccessToken());
    }
}
