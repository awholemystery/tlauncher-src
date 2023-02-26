package org.apache.commons.compress.archivers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/Lister.class */
public final class Lister {
    private static final ArchiveStreamFactory FACTORY = ArchiveStreamFactory.DEFAULT;

    public static void main(String[] args) throws ArchiveException, IOException {
        if (args.length == 0) {
            usage();
            return;
        }
        System.out.println("Analysing " + args[0]);
        File f = new File(args[0]);
        if (!f.isFile()) {
            System.err.println(f + " doesn't exist or is a directory");
        }
        String format = args.length > 1 ? args[1] : detectFormat(f);
        if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(format)) {
            list7z(f);
        } else if ("zipfile".equals(format)) {
            listZipUsingZipFile(f);
        } else if ("tarfile".equals(format)) {
            listZipUsingTarFile(f);
        } else {
            listStream(f, args);
        }
    }

    private static void listStream(File f, String[] args) throws ArchiveException, IOException {
        InputStream fis = new BufferedInputStream(Files.newInputStream(f.toPath(), new OpenOption[0]));
        Throwable th = null;
        try {
            ArchiveInputStream ais = createArchiveInputStream(args, fis);
            System.out.println("Created " + ais.toString());
            while (true) {
                ArchiveEntry ae = ais.getNextEntry();
                if (ae == null) {
                    break;
                }
                System.out.println(ae.getName());
            }
            if (ais != null) {
                if (0 != 0) {
                    ais.close();
                } else {
                    ais.close();
                }
            }
            if (fis != null) {
                if (0 == 0) {
                    fis.close();
                    return;
                }
                try {
                    fis.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (fis != null) {
                    if (th3 != null) {
                        try {
                            fis.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        fis.close();
                    }
                }
                throw th4;
            }
        }
    }

    private static ArchiveInputStream createArchiveInputStream(String[] args, InputStream fis) throws ArchiveException {
        if (args.length > 1) {
            return FACTORY.createArchiveInputStream(args[1], fis);
        }
        return FACTORY.createArchiveInputStream(fis);
    }

    private static String detectFormat(File f) throws ArchiveException, IOException {
        InputStream fis = new BufferedInputStream(Files.newInputStream(f.toPath(), new OpenOption[0]));
        Throwable th = null;
        try {
            String detect = ArchiveStreamFactory.detect(fis);
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
            return detect;
        } finally {
        }
    }

    private static void list7z(File f) throws IOException {
        SevenZFile z = new SevenZFile(f);
        Throwable th = null;
        try {
            System.out.println("Created " + z);
            while (true) {
                ArchiveEntry ae = z.getNextEntry();
                if (ae == null) {
                    break;
                }
                String name = ae.getName() == null ? z.getDefaultName() + " (entry name was null)" : ae.getName();
                System.out.println(name);
            }
            if (z != null) {
                if (0 != 0) {
                    try {
                        z.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                z.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (z != null) {
                    if (th3 != null) {
                        try {
                            z.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        z.close();
                    }
                }
                throw th4;
            }
        }
    }

    private static void listZipUsingZipFile(File f) throws IOException {
        ZipFile z = new ZipFile(f);
        Throwable th = null;
        try {
            System.out.println("Created " + z);
            Enumeration<ZipArchiveEntry> en = z.getEntries();
            while (en.hasMoreElements()) {
                System.out.println(en.nextElement().getName());
            }
            if (z != null) {
                if (0 != 0) {
                    try {
                        z.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                z.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (z != null) {
                    if (th3 != null) {
                        try {
                            z.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        z.close();
                    }
                }
                throw th4;
            }
        }
    }

    private static void listZipUsingTarFile(File f) throws IOException {
        TarFile t = new TarFile(f);
        Throwable th = null;
        try {
            System.out.println("Created " + t);
            t.getEntries().forEach(en -> {
                System.out.println(en.getName());
            });
            if (t != null) {
                if (0 != 0) {
                    try {
                        t.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                t.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (t != null) {
                    if (th3 != null) {
                        try {
                            t.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        t.close();
                    }
                }
                throw th4;
            }
        }
    }

    private static void usage() {
        System.out.println("Parameters: archive-name [archive-type]\n");
        System.out.println("The magic archive-type 'zipfile' prefers ZipFile over ZipArchiveInputStream");
        System.out.println("The magic archive-type 'tarfile' prefers TarFile over TarArchiveInputStream");
    }
}
