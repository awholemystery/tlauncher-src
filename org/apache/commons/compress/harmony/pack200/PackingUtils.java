package org.apache.commons.compress.harmony.pack200;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.apache.commons.compress.harmony.pack200.Archive;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/PackingUtils.class */
public class PackingUtils {
    private static PackingLogger packingLogger = new PackingLogger("org.harmony.apache.pack200", null);

    static {
        LogManager.getLogManager().addLogger(packingLogger);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/PackingUtils$PackingLogger.class */
    public static class PackingLogger extends Logger {
        private boolean verbose;

        protected PackingLogger(String name, String resourceBundleName) {
            super(name, resourceBundleName);
            this.verbose = false;
        }

        @Override // java.util.logging.Logger
        public void log(LogRecord logRecord) {
            if (this.verbose) {
                super.log(logRecord);
            }
        }

        public void setVerbose(boolean isVerbose) {
            this.verbose = isVerbose;
        }
    }

    public static void config(PackingOptions options) throws IOException {
        String logFileName = options.getLogFile();
        if (logFileName != null) {
            FileHandler fileHandler = new FileHandler(logFileName, false);
            fileHandler.setFormatter(new SimpleFormatter());
            packingLogger.addHandler(fileHandler);
            packingLogger.setUseParentHandlers(false);
        }
        packingLogger.setVerbose(options.isVerbose());
    }

    public static void log(String message) {
        packingLogger.log(Level.INFO, message);
    }

    public static void copyThroughJar(JarInputStream jarInputStream, OutputStream outputStream) throws IOException {
        Manifest manifest = jarInputStream.getManifest();
        JarOutputStream jarOutputStream = new JarOutputStream(outputStream, manifest);
        Throwable th = null;
        try {
            jarOutputStream.setComment("PACK200");
            log("Packed META-INF/MANIFEST.MF");
            byte[] bytes = new byte[16384];
            while (true) {
                JarEntry jarEntry = jarInputStream.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                jarOutputStream.putNextEntry(jarEntry);
                while (true) {
                    int bytesRead = jarInputStream.read(bytes);
                    if (bytesRead != -1) {
                        jarOutputStream.write(bytes, 0, bytesRead);
                    }
                }
                log("Packed " + jarEntry.getName());
            }
            jarInputStream.close();
            if (jarOutputStream != null) {
                if (0 != 0) {
                    try {
                        jarOutputStream.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                jarOutputStream.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (jarOutputStream != null) {
                    if (th3 != null) {
                        try {
                            jarOutputStream.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        jarOutputStream.close();
                    }
                }
                throw th4;
            }
        }
    }

    public static void copyThroughJar(JarFile jarFile, OutputStream outputStream) throws IOException {
        JarOutputStream jarOutputStream = new JarOutputStream(outputStream);
        Throwable th = null;
        try {
            jarOutputStream.setComment("PACK200");
            byte[] bytes = new byte[16384];
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                jarOutputStream.putNextEntry(jarEntry);
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                while (true) {
                    int bytesRead = inputStream.read(bytes);
                    if (bytesRead == -1) {
                        break;
                    }
                    jarOutputStream.write(bytes, 0, bytesRead);
                }
                jarOutputStream.closeEntry();
                log("Packed " + jarEntry.getName());
                if (inputStream != null) {
                    if (0 != 0) {
                        inputStream.close();
                    } else {
                        inputStream.close();
                    }
                }
            }
            jarFile.close();
            if (jarOutputStream != null) {
                if (0 != 0) {
                    try {
                        jarOutputStream.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                jarOutputStream.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (jarOutputStream != null) {
                    if (th3 != null) {
                        try {
                            jarOutputStream.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        jarOutputStream.close();
                    }
                }
                throw th4;
            }
        }
    }

    public static List<Archive.PackingFile> getPackingFileListFromJar(JarInputStream jarInputStream, boolean keepFileOrder) throws IOException {
        List<Archive.PackingFile> packingFileList = new ArrayList<>();
        Manifest manifest = jarInputStream.getManifest();
        if (manifest != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            manifest.write(baos);
            packingFileList.add(new Archive.PackingFile("META-INF/MANIFEST.MF", baos.toByteArray(), 0L));
        }
        while (true) {
            JarEntry jarEntry = jarInputStream.getNextJarEntry();
            if (jarEntry == null) {
                break;
            }
            byte[] bytes = readJarEntry(jarEntry, new BufferedInputStream(jarInputStream));
            packingFileList.add(new Archive.PackingFile(bytes, jarEntry));
        }
        if (!keepFileOrder) {
            reorderPackingFiles(packingFileList);
        }
        return packingFileList;
    }

    public static List<Archive.PackingFile> getPackingFileListFromJar(JarFile jarFile, boolean keepFileOrder) throws IOException {
        List<Archive.PackingFile> packingFileList = new ArrayList<>();
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            InputStream inputStream = jarFile.getInputStream(jarEntry);
            Throwable th = null;
            try {
                byte[] bytes = readJarEntry(jarEntry, new BufferedInputStream(inputStream));
                packingFileList.add(new Archive.PackingFile(bytes, jarEntry));
                if (inputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        inputStream.close();
                    }
                }
            } finally {
            }
        }
        if (!keepFileOrder) {
            reorderPackingFiles(packingFileList);
        }
        return packingFileList;
    }

    private static byte[] readJarEntry(JarEntry jarEntry, InputStream inputStream) throws IOException {
        long size = jarEntry.getSize();
        if (size > 2147483647L) {
            throw new IllegalArgumentException("Large Class!");
        }
        if (size < 0) {
            size = 0;
        }
        byte[] bytes = new byte[(int) size];
        if (inputStream.read(bytes) != size) {
            throw new IllegalArgumentException("Error reading from stream");
        }
        return bytes;
    }

    private static void reorderPackingFiles(List<Archive.PackingFile> packingFileList) {
        Iterator<Archive.PackingFile> iterator = packingFileList.iterator();
        while (iterator.hasNext()) {
            Archive.PackingFile packingFile = iterator.next();
            if (packingFile.isDirectory()) {
                iterator.remove();
            }
        }
        packingFileList.sort(arg0, arg1 -> {
            String fileName0 = arg0.getName();
            String fileName1 = arg1.getName();
            if (fileName0.equals(fileName1)) {
                return 0;
            }
            if ("META-INF/MANIFEST.MF".equals(fileName0)) {
                return -1;
            }
            if ("META-INF/MANIFEST.MF".equals(fileName1)) {
                return 1;
            }
            return fileName0.compareTo(fileName1);
        });
    }
}
