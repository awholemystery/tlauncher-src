package org.apache.commons.compress.archivers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.Collections;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.Sets;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/ArchiveStreamFactory.class */
public class ArchiveStreamFactory implements ArchiveStreamProvider {
    private static final int TAR_HEADER_SIZE = 512;
    private static final int DUMP_SIGNATURE_SIZE = 32;
    private static final int SIGNATURE_SIZE = 12;
    public static final ArchiveStreamFactory DEFAULT = new ArchiveStreamFactory();
    public static final String APK = "apk";
    public static final String XAPK = "xapk";
    public static final String APKS = "apks";
    public static final String APKM = "apkm";
    public static final String AR = "ar";
    public static final String ARJ = "arj";
    public static final String CPIO = "cpio";
    public static final String DUMP = "dump";
    public static final String JAR = "jar";
    public static final String TAR = "tar";
    public static final String ZIP = "zip";
    public static final String SEVEN_Z = "7z";
    private final String encoding;
    private volatile String entryEncoding;
    private SortedMap<String, ArchiveStreamProvider> archiveInputStreamProviders;
    private SortedMap<String, ArchiveStreamProvider> archiveOutputStreamProviders;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putAll(Set<String> names, ArchiveStreamProvider provider, TreeMap<String, ArchiveStreamProvider> map) {
        names.forEach(name -> {
            ArchiveStreamProvider archiveStreamProvider = (ArchiveStreamProvider) map.put(toKey(name), provider);
        });
    }

    private static Iterable<ArchiveStreamProvider> archiveStreamProviderIterable() {
        return ServiceLoader.load(ArchiveStreamProvider.class, ClassLoader.getSystemClassLoader());
    }

    private static String toKey(String name) {
        return name.toUpperCase(Locale.ROOT);
    }

    public static SortedMap<String, ArchiveStreamProvider> findAvailableArchiveInputStreamProviders() {
        return (SortedMap) AccessController.doPrivileged(() -> {
            TreeMap<String, ArchiveStreamProvider> map = new TreeMap<>();
            putAll(DEFAULT.getInputStreamArchiveNames(), DEFAULT, map);
            archiveStreamProviderIterable().forEach(provider -> {
                putAll(provider.getInputStreamArchiveNames(), provider, map);
            });
            return map;
        });
    }

    public static SortedMap<String, ArchiveStreamProvider> findAvailableArchiveOutputStreamProviders() {
        return (SortedMap) AccessController.doPrivileged(() -> {
            TreeMap<String, ArchiveStreamProvider> map = new TreeMap<>();
            putAll(DEFAULT.getOutputStreamArchiveNames(), DEFAULT, map);
            archiveStreamProviderIterable().forEach(provider -> {
                putAll(provider.getOutputStreamArchiveNames(), provider, map);
            });
            return map;
        });
    }

    public ArchiveStreamFactory() {
        this(null);
    }

    public ArchiveStreamFactory(String encoding) {
        this.encoding = encoding;
        this.entryEncoding = encoding;
    }

    public String getEntryEncoding() {
        return this.entryEncoding;
    }

    @Deprecated
    public void setEntryEncoding(String entryEncoding) {
        if (this.encoding != null) {
            throw new IllegalStateException("Cannot overide encoding set by the constructor");
        }
        this.entryEncoding = entryEncoding;
    }

    public ArchiveInputStream createArchiveInputStream(String archiverName, InputStream in) throws ArchiveException {
        return createArchiveInputStream(archiverName, in, this.entryEncoding);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveStreamProvider
    public ArchiveInputStream createArchiveInputStream(String archiverName, InputStream in, String actualEncoding) throws ArchiveException {
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        if (in == null) {
            throw new IllegalArgumentException("InputStream must not be null.");
        }
        if (AR.equalsIgnoreCase(archiverName)) {
            return new ArArchiveInputStream(in);
        }
        if (ARJ.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new ArjArchiveInputStream(in, actualEncoding);
            }
            return new ArjArchiveInputStream(in);
        } else if (ZIP.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new ZipArchiveInputStream(in, actualEncoding);
            }
            return new ZipArchiveInputStream(in);
        } else if (TAR.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new TarArchiveInputStream(in, actualEncoding);
            }
            return new TarArchiveInputStream(in);
        } else if (JAR.equalsIgnoreCase(archiverName) || APK.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new JarArchiveInputStream(in, actualEncoding);
            }
            return new JarArchiveInputStream(in);
        } else if (CPIO.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new CpioArchiveInputStream(in, actualEncoding);
            }
            return new CpioArchiveInputStream(in);
        } else if (DUMP.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new DumpArchiveInputStream(in, actualEncoding);
            }
            return new DumpArchiveInputStream(in);
        } else if (SEVEN_Z.equalsIgnoreCase(archiverName)) {
            throw new StreamingNotSupportedException(SEVEN_Z);
        } else {
            ArchiveStreamProvider archiveStreamProvider = getArchiveInputStreamProviders().get(toKey(archiverName));
            if (archiveStreamProvider != null) {
                return archiveStreamProvider.createArchiveInputStream(archiverName, in, actualEncoding);
            }
            throw new ArchiveException("Archiver: " + archiverName + " not found.");
        }
    }

    public ArchiveOutputStream createArchiveOutputStream(String archiverName, OutputStream out) throws ArchiveException {
        return createArchiveOutputStream(archiverName, out, this.entryEncoding);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveStreamProvider
    public ArchiveOutputStream createArchiveOutputStream(String archiverName, OutputStream out, String actualEncoding) throws ArchiveException {
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        if (out == null) {
            throw new IllegalArgumentException("OutputStream must not be null.");
        }
        if (AR.equalsIgnoreCase(archiverName)) {
            return new ArArchiveOutputStream(out);
        }
        if (ZIP.equalsIgnoreCase(archiverName)) {
            ZipArchiveOutputStream zip = new ZipArchiveOutputStream(out);
            if (actualEncoding != null) {
                zip.setEncoding(actualEncoding);
            }
            return zip;
        } else if (TAR.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new TarArchiveOutputStream(out, actualEncoding);
            }
            return new TarArchiveOutputStream(out);
        } else if (JAR.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new JarArchiveOutputStream(out, actualEncoding);
            }
            return new JarArchiveOutputStream(out);
        } else if (CPIO.equalsIgnoreCase(archiverName)) {
            if (actualEncoding != null) {
                return new CpioArchiveOutputStream(out, actualEncoding);
            }
            return new CpioArchiveOutputStream(out);
        } else if (SEVEN_Z.equalsIgnoreCase(archiverName)) {
            throw new StreamingNotSupportedException(SEVEN_Z);
        } else {
            ArchiveStreamProvider archiveStreamProvider = getArchiveOutputStreamProviders().get(toKey(archiverName));
            if (archiveStreamProvider != null) {
                return archiveStreamProvider.createArchiveOutputStream(archiverName, out, actualEncoding);
            }
            throw new ArchiveException("Archiver: " + archiverName + " not found.");
        }
    }

    public ArchiveInputStream createArchiveInputStream(InputStream in) throws ArchiveException {
        return createArchiveInputStream(detect(in), in);
    }

    public static String detect(InputStream in) throws ArchiveException {
        if (in == null) {
            throw new IllegalArgumentException("Stream must not be null.");
        }
        if (in.markSupported()) {
            byte[] signature = new byte[12];
            in.mark(signature.length);
            try {
                int signatureLength = IOUtils.readFully(in, signature);
                in.reset();
                if (ZipArchiveInputStream.matches(signature, signatureLength)) {
                    return ZIP;
                }
                if (JarArchiveInputStream.matches(signature, signatureLength)) {
                    return JAR;
                }
                if (ArArchiveInputStream.matches(signature, signatureLength)) {
                    return AR;
                }
                if (CpioArchiveInputStream.matches(signature, signatureLength)) {
                    return CPIO;
                }
                if (ArjArchiveInputStream.matches(signature, signatureLength)) {
                    return ARJ;
                }
                if (SevenZFile.matches(signature, signatureLength)) {
                    return SEVEN_Z;
                }
                byte[] dumpsig = new byte[32];
                in.mark(dumpsig.length);
                try {
                    int signatureLength2 = IOUtils.readFully(in, dumpsig);
                    in.reset();
                    if (DumpArchiveInputStream.matches(dumpsig, signatureLength2)) {
                        return DUMP;
                    }
                    byte[] tarHeader = new byte[512];
                    in.mark(tarHeader.length);
                    try {
                        int signatureLength3 = IOUtils.readFully(in, tarHeader);
                        in.reset();
                        if (TarArchiveInputStream.matches(tarHeader, signatureLength3)) {
                            return TAR;
                        }
                        if (signatureLength3 >= 512) {
                            TarArchiveInputStream tais = null;
                            try {
                                tais = new TarArchiveInputStream(new ByteArrayInputStream(tarHeader));
                                if (tais.getNextTarEntry().isCheckSumOK()) {
                                    IOUtils.closeQuietly(tais);
                                    return TAR;
                                }
                                IOUtils.closeQuietly(tais);
                            } catch (Exception e) {
                                IOUtils.closeQuietly(tais);
                            } catch (Throwable th) {
                                IOUtils.closeQuietly(tais);
                                throw th;
                            }
                        }
                        throw new ArchiveException("No Archiver found for the stream signature");
                    } catch (IOException e2) {
                        throw new ArchiveException("IOException while reading tar signature", e2);
                    }
                } catch (IOException e3) {
                    throw new ArchiveException("IOException while reading dump signature", e3);
                }
            } catch (IOException e4) {
                throw new ArchiveException("IOException while reading signature.", e4);
            }
        }
        throw new IllegalArgumentException("Mark is not supported.");
    }

    public SortedMap<String, ArchiveStreamProvider> getArchiveInputStreamProviders() {
        if (this.archiveInputStreamProviders == null) {
            this.archiveInputStreamProviders = Collections.unmodifiableSortedMap(findAvailableArchiveInputStreamProviders());
        }
        return this.archiveInputStreamProviders;
    }

    public SortedMap<String, ArchiveStreamProvider> getArchiveOutputStreamProviders() {
        if (this.archiveOutputStreamProviders == null) {
            this.archiveOutputStreamProviders = Collections.unmodifiableSortedMap(findAvailableArchiveOutputStreamProviders());
        }
        return this.archiveOutputStreamProviders;
    }

    @Override // org.apache.commons.compress.archivers.ArchiveStreamProvider
    public Set<String> getInputStreamArchiveNames() {
        return Sets.newHashSet(AR, ARJ, ZIP, TAR, JAR, CPIO, DUMP, SEVEN_Z);
    }

    @Override // org.apache.commons.compress.archivers.ArchiveStreamProvider
    public Set<String> getOutputStreamArchiveNames() {
        return Sets.newHashSet(AR, ZIP, TAR, JAR, CPIO, SEVEN_Z);
    }
}
