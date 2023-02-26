package org.tlauncher.util;

import java.awt.Color;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Function;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggingEvent;
import org.tlauncher.exceptions.CheckedFunction;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.rmo.Bootstrapper;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.async.ExtendedThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/U.class */
public class U {
    public static final String PROGRAM_PACKAGE = "org.tlauncher";
    public static final int CONNECTION_TIMEOUT = 30000;
    private static final int ST_TOTAL = 100;
    private static final int ST_PROGRAM = 10;
    private static Logger logField;
    private static FileAppender appender;
    private static ConsoleAppender console;
    public static final PatternLayout LOG_LAYOUT = new PatternLayout("%m%n");
    public static String FLUSH_MESSAGE = "flush now";

    private U() {
    }

    public static void log(Object... what) {
        hlog(null, what);
    }

    public static void plog(Object... what) {
        hlog(null, what);
    }

    private static void hlog(String prefix, Object[] append) {
        if (Objects.nonNull(logField)) {
            logField.info(toLog(prefix, append));
        } else {
            System.out.println(toLog(prefix, append));
        }
    }

    private static String toLog(String prefix, Object... append) {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        if (prefix != null) {
            b.append(prefix);
            first = false;
        }
        if (append != null) {
            for (Object e : append) {
                if (e != null) {
                    if (e.getClass().isArray()) {
                        if (!first) {
                            b.append(" ");
                        }
                        if (e instanceof Object[]) {
                            b.append(toLog((Object[]) e));
                        } else {
                            b.append(arrayToLog(e));
                        }
                    } else if (e instanceof Throwable) {
                        if (!first) {
                            b.append("\n");
                        }
                        b.append(stackTrace((Throwable) e));
                        b.append("\n");
                    } else if (e instanceof File) {
                        if (!first) {
                            b.append(" ");
                        }
                        File file = (File) e;
                        String absPath = file.getAbsolutePath();
                        b.append(absPath);
                        if (file.isDirectory() && !absPath.endsWith(File.separator)) {
                            b.append(File.separator);
                        }
                    } else if (e instanceof Iterator) {
                        Iterator<?> i = (Iterator) e;
                        while (i.hasNext()) {
                            b.append(" ");
                            b.append(toLog(i.next()));
                        }
                    } else if (e instanceof Enumeration) {
                        Enumeration<?> en = (Enumeration) e;
                        while (en.hasMoreElements()) {
                            b.append(" ");
                            b.append(toLog(en.nextElement()));
                        }
                    } else {
                        if (!first) {
                            b.append(" ");
                        }
                        b.append(e);
                    }
                } else {
                    if (!first) {
                        b.append(" ");
                    }
                    b.append(Configurator.NULL);
                }
                if (first) {
                    first = false;
                }
            }
        } else {
            b.append(Configurator.NULL);
        }
        return b.toString();
    }

    public static String toLog(Object... append) {
        return toLog(null, append);
    }

    private static String arrayToLog(Object e) {
        char[] cArr;
        short[] sArr;
        byte[] bArr;
        double[] dArr;
        float[] fArr;
        long[] jArr;
        boolean[] zArr;
        int[] iArr;
        Object[] objArr;
        if (!e.getClass().isArray()) {
            throw new IllegalArgumentException("Given object is not an array!");
        }
        StringBuilder b = new StringBuilder();
        boolean first = true;
        if (e instanceof Object[]) {
            for (Object i : (Object[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append(i);
            }
        } else if (e instanceof int[]) {
            for (int i2 : (int[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append(i2);
            }
        } else if (e instanceof boolean[]) {
            for (boolean i3 : (boolean[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append(i3);
            }
        } else if (e instanceof long[]) {
            for (long i4 : (long[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append(i4);
            }
        } else if (e instanceof float[]) {
            for (float i5 : (float[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append(i5);
            }
        } else if (e instanceof double[]) {
            for (double i6 : (double[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append(i6);
            }
        } else if (e instanceof byte[]) {
            for (byte i7 : (byte[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append((int) i7);
            }
        } else if (e instanceof short[]) {
            for (short i8 : (short[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append((int) i8);
            }
        } else if (e instanceof char[]) {
            for (char i9 : (char[]) e) {
                if (!first) {
                    b.append(" ");
                } else {
                    first = false;
                }
                b.append(i9);
            }
        }
        if (b.length() == 0) {
            throw new UnknownError("Unknown array type given.");
        }
        return b.toString();
    }

    public static void setLoadingStep(Bootstrapper.LoadingStep step) {
        if (step == null) {
            throw new NullPointerException();
        }
        plog(Bootstrapper.LoadingStep.LOADING_PREFIX, step.toString());
    }

    public static boolean ok(int d) {
        return new Random(System.currentTimeMillis()).nextInt(d) == 0;
    }

    public static double getAverage(double[] d) {
        double a = 0.0d;
        int k = 0;
        for (double curd : d) {
            if (curd != 0.0d) {
                a += curd;
                k++;
            }
        }
        if (k == 0) {
            return 0.0d;
        }
        return a / k;
    }

    public static double getAverage(double[] d, int max) {
        double a = 0.0d;
        int k = 0;
        for (double curd : d) {
            a += curd;
            k++;
            if (k == max) {
                break;
            }
        }
        if (k == 0) {
            return 0.0d;
        }
        return a / k;
    }

    public static double getSum(double[] d) {
        double a = 0.0d;
        for (double curd : d) {
            a += curd;
        }
        return a;
    }

    public static int getMaxMultiply(int i, int max) {
        if (i <= max) {
            return 1;
        }
        for (int x = max; x > 1; x--) {
            if (i % x == 0) {
                return x;
            }
        }
        return (int) Math.ceil(i / max);
    }

    private static String stackTrace(Throwable e) {
        StringBuilder trace = rawStackTrace(e);
        ExtendedThread currentAsExtended = (ExtendedThread) getAs(Thread.currentThread(), ExtendedThread.class);
        if (currentAsExtended != null) {
            trace.append("\nThread called by: ").append((CharSequence) rawStackTrace(currentAsExtended.getCaller()));
        }
        return trace.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x009c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.StringBuilder rawStackTrace(java.lang.Throwable r3) {
        /*
            r0 = r3
            if (r0 != 0) goto L6
            r0 = 0
            return r0
        L6:
            r0 = r3
            java.lang.StackTraceElement[] r0 = r0.getStackTrace()
            r4 = r0
            r0 = 0
            r5 = r0
            r0 = 0
            r6 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r7 = r0
            r0 = r7
            r1 = r3
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r4
            r8 = r0
            r0 = r8
            int r0 = r0.length
            r9 = r0
            r0 = 0
            r10 = r0
        L2d:
            r0 = r10
            r1 = r9
            if (r0 >= r1) goto L91
            r0 = r8
            r1 = r10
            r0 = r0[r1]
            r11 = r0
            int r6 = r6 + 1
            r0 = r11
            java.lang.String r0 = r0.toString()
            r12 = r0
            r0 = r12
            java.lang.String r1 = "org.tlauncher"
            boolean r0 = r0.startsWith(r1)
            if (r0 == 0) goto L52
            int r5 = r5 + 1
        L52:
            r0 = r7
            java.lang.String r1 = "\nat "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r12
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r6
            r1 = 100
            if (r0 == r1) goto L6b
            r0 = r5
            r1 = 10
            if (r0 != r1) goto L8b
        L6b:
            r0 = r4
            int r0 = r0.length
            r1 = r6
            int r0 = r0 - r1
            r13 = r0
            r0 = r13
            if (r0 == 0) goto L91
            r0 = r7
            java.lang.String r1 = "\n... and "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r13
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = " more"
            java.lang.StringBuilder r0 = r0.append(r1)
            goto L91
        L8b:
            int r10 = r10 + 1
            goto L2d
        L91:
            r0 = r3
            java.lang.Throwable r0 = r0.getCause()
            r8 = r0
            r0 = r8
            if (r0 == 0) goto Lac
            r0 = r7
            java.lang.String r1 = "\nCaused by: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r8
            java.lang.StringBuilder r1 = rawStackTrace(r1)
            java.lang.StringBuilder r0 = r0.append(r1)
        Lac:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.util.U.rawStackTrace(java.lang.Throwable):java.lang.StringBuilder");
    }

    public static long getUsingSpace() {
        return getTotalSpace() - getFreeSpace();
    }

    public static long getFreeSpace() {
        return Runtime.getRuntime().freeMemory() / 1048576;
    }

    public static long getTotalSpace() {
        return Runtime.getRuntime().totalMemory() / 1048576;
    }

    public static String memoryStatus() {
        return getUsingSpace() + " / " + getTotalSpace() + " MB";
    }

    public static void gc() {
        log("Starting garbage collector: " + memoryStatus());
        System.gc();
        log("Garbage collector completed: " + memoryStatus());
    }

    public static void sleepFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static URL makeURL(String p) {
        try {
            return new URL(p);
        } catch (Exception e) {
            log("Cannot make URL from string: " + p + ".", e);
            return null;
        }
    }

    public static URI makeURI(URL url) {
        try {
            return url.toURI();
        } catch (Exception e) {
            log("Cannot make URI from URL: " + url + ".", e);
            return null;
        }
    }

    public static URI makeURI(String p) {
        return makeURI(makeURL(p));
    }

    private static int fitInterval(int val, int min, int max) {
        if (val > max) {
            return max;
        }
        if (val < min) {
            return min;
        }
        return val;
    }

    public static long m() {
        return System.currentTimeMillis();
    }

    public static long n() {
        return System.nanoTime();
    }

    public static int getReadTimeout() {
        return getConnectionTimeout();
    }

    public static int getConnectionTimeout() {
        ConnectionQuality quality;
        TLauncher t = TLauncher.getInstance();
        if (t == null || (quality = t.getConfiguration().getConnectionQuality()) == null) {
            return 30000;
        }
        return quality.getTimeout();
    }

    public static Proxy getProxy() {
        return Proxy.NO_PROXY;
    }

    public static <K, E> LinkedHashMap<K, E> sortMap(Map<K, E> map, K[] sortedKeys) {
        if (map == null) {
            return null;
        }
        if (sortedKeys == null) {
            throw new NullPointerException("Keys cannot be NULL!");
        }
        LinkedHashMap<K, E> result = new LinkedHashMap<>();
        for (K key : sortedKeys) {
            Iterator<Map.Entry<K, E>> it = map.entrySet().iterator();
            while (true) {
                if (it.hasNext()) {
                    Map.Entry<K, E> entry = it.next();
                    K entryKey = entry.getKey();
                    E value = entry.getValue();
                    if (key == null && entryKey == null) {
                        result.put(null, value);
                        break;
                    } else if (key != null && key.equals(entryKey)) {
                        result.put(key, value);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static Color shiftColor(Color color, int bits) {
        if (color == null) {
            return null;
        }
        if (bits == 0) {
            return color;
        }
        int newRed = fitInterval(color.getRed() + bits, 0, 255);
        int newGreen = fitInterval(color.getGreen() + bits, 0, 255);
        int newBlue = fitInterval(color.getBlue() + bits, 0, 255);
        return new Color(newRed, newGreen, newBlue, color.getAlpha());
    }

    public static Color shiftAlpha(Color color, int bits) {
        if (color == null) {
            return null;
        }
        if (bits == 0) {
            return color;
        }
        int newAlpha = fitInterval(color.getAlpha() + bits, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), newAlpha);
    }

    @Deprecated
    public static <T> T getAs(Object o, Class<T> classOfT) {
        return (T) Reflect.cast(o, classOfT);
    }

    public static boolean equal(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a != null) {
            return a.equals(b);
        }
        return false;
    }

    public static void close(Closeable c) {
        try {
            c.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static <T> int find(T obj, T[] array) {
        if (obj == null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
            return -1;
        }
        for (int i2 = 0; i2 < array.length; i2++) {
            if (obj.equals(array[i2])) {
                return i2;
            }
        }
        return -1;
    }

    public static void initializeLoggerU(File minecraftFolder, String type) {
        if (minecraftFolder == null) {
            minecraftFolder = MinecraftUtil.getDefaultWorkingDirectory();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
        String separator = System.getProperty("file.separator");
        File path = new File(minecraftFolder, separator + "logs" + separator + "tlauncher" + separator + type + "_" + formatter.format(new Date()) + ".log");
        appender = new FileAppender() { // from class: org.tlauncher.util.U.1
            @Override // org.apache.log4j.WriterAppender
            protected boolean shouldFlush(LoggingEvent event) {
                if (!(event.getMessage() instanceof String)) {
                    return false;
                }
                return U.FLUSH_MESSAGE.equalsIgnoreCase((String) event.getMessage());
            }
        };
        appender.setName("fileAppender");
        appender.setLayout(LOG_LAYOUT);
        appender.setFile(path.getAbsolutePath());
        appender.setThreshold(Level.INFO);
        appender.activateOptions();
        appender.setBufferedIO(true);
        appender.setEncoding(TlauncherUtil.LOG_CHARSET);
        Logger.getRootLogger().addAppender(appender);
        logField = Logger.getLogger("main");
        console = new ConsoleAppender();
        console.setName("console");
        console.setLayout(new PatternLayout("%m%n"));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        console.setEncoding(TlauncherUtil.LOG_CHARSET);
        Logger.getRootLogger().addAppender(console);
        Logger.getRootLogger().setLevel(Level.INFO);
        if ("tlauncher".equalsIgnoreCase(type)) {
            try {
                Files.walk(Paths.get(minecraftFolder.getAbsolutePath(), "logs"), new FileVisitOption[0]).filter(x$0 -> {
                    return Files.isRegularFile(x$0, new LinkOption[0]);
                }).filter(p -> {
                    return ((Boolean) uncheckCall(() -> {
                        BasicFileAttributes b = Files.readAttributes(p, BasicFileAttributes.class, new LinkOption[0]);
                        FileTime t = b.creationTime();
                        return Boolean.valueOf(DateUtils.addDays(new Date(), -20).after(new Date(t.toMillis())));
                    })).booleanValue();
                }).forEach(p2 -> {
                    FileUtil.deleteFile(p2.toFile());
                });
            } catch (Throwable e) {
                log(e);
            }
        }
    }

    public static void debug(Object... ob) {
        if (!TLauncher.DEBUG) {
            return;
        }
        plog("[DEBUG] ----- ", ob);
    }

    public static URI fixInvallidLink(String link) {
        try {
            if (link.contains("|")) {
                debug("U", "replace |");
                return new URI(link.replace("|", "%7C"));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            if (link.contains("|")) {
                debug("U", "replace |");
                link = link.replace("|", "%7C");
            }
            if (link.contains("?")) {
                return new URI(link.substring(0, link.indexOf("?")));
            }
            return null;
        } catch (Exception e12) {
            e12.printStackTrace();
            return null;
        }
    }

    public static <T> void classNameLog(Class<T> cl, Object message) {
        log("[" + cl.getSimpleName() + "] ", message);
    }

    public static String readFileLog() {
        logField.info(FLUSH_MESSAGE);
        try {
            return FileUtil.readFile(new File(appender.getFile()), TlauncherUtil.LOG_CHARSET);
        } catch (IOException e) {
            logField.warn("can't read log file", e);
            return "can't read log file";
        }
    }

    public static void removeConsoleAppender() {
        Logger.getRootLogger().removeAppender(console);
    }

    static <T, R> Function<T, R> wrap(CheckedFunction<T, R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T> T uncheckCall(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
