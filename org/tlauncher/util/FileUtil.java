package org.tlauncher.util;

import ch.qos.logback.core.CoreConstants;
import com.github.junrar.Archive;
import com.github.junrar.extract.ExtractArchive;
import com.github.junrar.rarfile.FileHeader;
import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.harmony.unpack200.IcTuple;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.tlauncher.exceptions.ParseModPackException;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.MapDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ResourcePackDTO;
import org.tlauncher.modpack.domain.client.ShaderpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.MapMetadataDTO;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/FileUtil.class */
public class FileUtil {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String NAME_ARCHIVE = "logOfFiles.zip";
    public static final Long SIZE_100 = 104857600L;
    public static final Long SIZE_200 = 209715200L;
    public static final Long SIZE_300 = 314572800L;
    public static Set<PosixFilePermission> PERMISSIONS = new HashSet<PosixFilePermission>() { // from class: org.tlauncher.util.FileUtil.1
        {
            add(PosixFilePermission.OWNER_READ);
            add(PosixFilePermission.OWNER_WRITE);
            add(PosixFilePermission.OWNER_EXECUTE);
            add(PosixFilePermission.OTHERS_READ);
            add(PosixFilePermission.OTHERS_EXECUTE);
            add(PosixFilePermission.GROUP_READ);
            add(PosixFilePermission.GROUP_EXECUTE);
        }
    };

    public static Charset getCharset() {
        try {
            return Charset.forName("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeFile(File file, String text) throws IOException {
        createFile(file);
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        OutputStreamWriter ow = new OutputStreamWriter(os, "UTF-8");
        ow.write(text);
        ow.close();
        os.close();
    }

    public static void writeZipEntry(ZipOutputStream zip, File fileRead) throws IOException {
        try {
            FileInputStream in = new FileInputStream(fileRead);
            byte[] array = new byte[8192];
            while (in.read(array) != -1) {
                zip.write(array);
            }
            if (in != null) {
                if (0 != 0) {
                    in.close();
                } else {
                    in.close();
                }
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static String readStream(InputStream is, String charset) throws IOException {
        InputStreamReader reader = new InputStreamReader(new BufferedInputStream(is), charset);
        Throwable th = null;
        try {
            StringBuilder b = new StringBuilder();
            while (reader.ready()) {
                b.append((char) reader.read());
            }
            String sb = b.toString();
            if (reader != null) {
                if (0 != 0) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    reader.close();
                }
            }
            return sb;
        } finally {
        }
    }

    public static String readStream(InputStream is) throws IOException {
        return readStream(is, Charsets.UTF_8.displayName());
    }

    public static String getTextResource(URL url, String charset) throws IOException {
        return readStream(url.openStream(), charset);
    }

    public static String readFile(File file, String charset) throws IOException {
        return readStream(new FileInputStream(file), charset);
    }

    public static String readFile(File file) throws IOException {
        return readFile(file, "UTF-8");
    }

    public static String getFilename(String path) {
        String[] folders = path.split("/");
        int size = folders.length;
        if (size == 0) {
            return CoreConstants.EMPTY_STRING;
        }
        return folders[size - 1];
    }

    public static String getDigest(File file, String algorithm, int hashLength) {
        int read;
        DigestInputStream stream = null;
        try {
            stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance(algorithm));
            byte[] buffer = new byte[IcTuple.NESTED_CLASS_FLAG];
            do {
                read = stream.read(buffer);
            } while (read > 0);
            close(stream);
            return String.format("%1$0" + hashLength + "x", new BigInteger(1, stream.getMessageDigest().digest()));
        } catch (Exception e) {
            close(stream);
            return null;
        } catch (Throwable th) {
            close(stream);
            throw th;
        }
    }

    private static byte[] createChecksum(File file, String algorithm) {
        int numRead;
        InputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[8192];
            MessageDigest complete = MessageDigest.getInstance(algorithm);
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            byte[] digest = complete.digest();
            close(fis);
            return digest;
        } catch (Exception e) {
            close(fis);
            return null;
        } catch (Throwable th) {
            close(fis);
            throw th;
        }
    }

    public static String getChecksum(File file, String algorithm) {
        byte[] b;
        if (file == null || !file.exists() || (b = createChecksum(file, algorithm)) == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (byte cb : b) {
            result.append(Integer.toString((cb & 255) + 256, 16).substring(1));
        }
        return result.toString();
    }

    public static String getChecksum(File file) {
        return getChecksum(file, MessageDigestAlgorithms.SHA_1);
    }

    private static void close(Closeable a) {
        try {
            a.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getRunningJar() {
        try {
            return new File(URLDecoder.decode(FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Cannot get running file!", e);
        }
    }

    public static void copyFile(File source, File dest, boolean replace) throws IOException {
        if (!dest.isFile()) {
            createFile(dest);
        } else if (!replace) {
            return;
        }
        InputStream is = new BufferedInputStream(new FileInputStream(source));
        Throwable th = null;
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] buffer = new byte[1024];
            while (true) {
                int length = is.read(buffer);
                if (length <= 0) {
                    break;
                }
                os.write(buffer, 0, length);
            }
            if (os != null) {
                if (0 != 0) {
                    os.close();
                } else {
                    os.close();
                }
            }
            if (is != null) {
                if (0 == 0) {
                    is.close();
                    return;
                }
                try {
                    is.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (is != null) {
                    if (th3 != null) {
                        try {
                            is.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        is.close();
                    }
                }
                throw th4;
            }
        }
    }

    public static void deleteFile(File file) {
        File[] list;
        boolean onExit = !file.delete();
        if (onExit) {
            file.deleteOnExit();
            return;
        }
        File parent = file.getParentFile();
        if (parent == null || (list = parent.listFiles()) == null || list.length > 0) {
            return;
        }
        deleteFile(parent);
    }

    public static void deleteDirectory(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Specified path is not a directory: " + dir.getAbsolutePath());
        }
        File[] files = dir.listFiles();
        if (Objects.isNull(files)) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                deleteFile(file);
            }
        }
        deleteFile(dir);
    }

    public static File makeTemp(File file) throws IOException {
        createFile(file);
        file.deleteOnExit();
        return file;
    }

    public static boolean createFolder(File dir) throws IOException {
        if (dir == null) {
            throw new NullPointerException();
        }
        if (dir.isDirectory()) {
            return false;
        }
        if (!dir.mkdirs()) {
            throw new IOException("Cannot createScrollWrapper folders: " + dir.getAbsolutePath());
        }
        if (!dir.canWrite()) {
            throw new IOException("Created directory is not accessible: " + dir.getAbsolutePath());
        }
        return true;
    }

    public static void createFile(File file) throws IOException {
        if (file.isFile()) {
            return;
        }
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        if (!file.createNewFile()) {
            throw new IOException("Cannot createScrollWrapper file, or it was created during runtime: " + file.getAbsolutePath());
        }
    }

    public static void unTarGz(File zip, File folder, boolean replace, boolean deleteEmptyFile) throws IOException {
        createFolder(folder);
        TarArchiveInputStream zis = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(zip))));
        Throwable th = null;
        while (true) {
            try {
                TarArchiveEntry ze = (TarArchiveEntry) zis.getNextEntry();
                if (ze == null) {
                    break;
                }
                String fileName = ze.getName();
                if (!ze.isDirectory()) {
                    unZipAndTarGz(fileName, folder, replace, zis, deleteEmptyFile);
                }
            } catch (Throwable th2) {
                try {
                    throw th2;
                } catch (Throwable th3) {
                    if (zis != null) {
                        if (th2 != null) {
                            try {
                                zis.close();
                            } catch (Throwable th4) {
                                th2.addSuppressed(th4);
                            }
                        } else {
                            zis.close();
                        }
                    }
                    throw th3;
                }
            }
        }
        zis.close();
        if (zis != null) {
            if (0 != 0) {
                try {
                    zis.close();
                    return;
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                    return;
                }
            }
            zis.close();
        }
    }

    public static void unZip(File zip, File folder, boolean replace, boolean deleteEmptyFile) throws IOException {
        createFolder(folder);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)), StandardCharsets.UTF_8);
        Throwable th = null;
        while (true) {
            try {
                ZipEntry ze = zis.getNextEntry();
                if (ze == null) {
                    break;
                }
                String fileName = ze.getName();
                if (!ze.isDirectory()) {
                    unZipAndTarGz(fileName, folder, replace, zis, deleteEmptyFile);
                }
            } catch (Throwable th2) {
                try {
                    throw th2;
                } catch (Throwable th3) {
                    if (zis != null) {
                        if (th2 != null) {
                            try {
                                zis.close();
                            } catch (Throwable th4) {
                                th2.addSuppressed(th4);
                            }
                        } else {
                            zis.close();
                        }
                    }
                    throw th3;
                }
            }
        }
        zis.closeEntry();
        if (zis != null) {
            if (0 != 0) {
                try {
                    zis.close();
                    return;
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                    return;
                }
            }
            zis.close();
        }
    }

    private static void unZipAndTarGz(String fileName, File folder, boolean replace, InputStream zis, boolean deleteEmptyFile) throws IOException {
        byte[] buffer = new byte[1024];
        File newFile = new File(folder, fileName);
        if (!replace && newFile.isFile()) {
            return;
        }
        createFile(newFile);
        OutputStream fos = new BufferedOutputStream(new FileOutputStream(newFile));
        int count = 0;
        while (true) {
            int len = zis.read(buffer);
            if (len <= 0) {
                break;
            }
            count += len;
            fos.write(buffer, 0, len);
        }
        fos.close();
        if (deleteEmptyFile && count == 0) {
            Files.delete(newFile.toPath());
        }
    }

    public static void unZip(File zip, File folder, boolean replace) throws IOException {
        unZip(zip, folder, replace, true);
    }

    public static String getResource(URL resource, String charset) throws IOException {
        InputStream is = new BufferedInputStream(resource.openStream());
        InputStreamReader reader = new InputStreamReader(is, charset);
        StringBuilder b = new StringBuilder();
        while (reader.ready()) {
            b.append((char) reader.read());
        }
        reader.close();
        return b.toString();
    }

    public static String getResource(URL resource) throws IOException {
        return getResource(resource, "UTF-8");
    }

    public static String getFolder(URL url, String separator) {
        String[] folders = url.toString().split(separator);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < folders.length - 1; i++) {
            s.append(folders[i]).append(separator);
        }
        return s.toString();
    }

    public static String getExtension(File f) {
        if (!f.isFile() && f.isDirectory()) {
            return null;
        }
        String ext = CoreConstants.EMPTY_STRING;
        String s = f.getName();
        int i = s.lastIndexOf(46);
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static boolean checkFreeSpace(File file, long size) {
        try {
            FileStore store = Files.getFileStore(file.toPath().getRoot());
            long res = store.getUsableSpace();
            return res > size;
        } catch (IOException e) {
            return true;
        }
    }

    public static InputStream getResourceAppStream(String name) {
        return FileUtil.class.getResourceAsStream(name);
    }

    public static Path getRelative(String path) {
        return Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), path);
    }

    public static Path getRelativeConfig(String path) {
        return getRelative(TLauncher.getInnerSettings().get(path));
    }

    public static File getRelativeConfigFile(String path) {
        return getRelative(TLauncher.getInnerSettings().get(path)).toFile();
    }

    public static List<String> topFolders(Archive rar) {
        FileHeader fh = rar.nextFileHeader();
        Set<String> list = new HashSet<>();
        while (fh != null) {
            String line = Paths.get(fh.getFileNameW(), new String[0]).toString();
            if (line.indexOf(File.separator) > 0) {
                list.add(line.substring(0, line.indexOf(File.separator)));
            }
            fh = rar.nextFileHeader();
        }
        return new ArrayList(list);
    }

    public static List<String> topFolders(ZipFile zipFile) {
        Set<String> list = new HashSet<>();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            int index = name.indexOf("/");
            if (index > 0) {
                list.add(name.substring(0, index));
            }
        }
        IOUtils.closeQuietly(zipFile);
        return new ArrayList(list);
    }

    public static MetadataDTO createMetadata(File file, File rootFolder, Class<? extends GameEntityDTO> c) {
        MetadataDTO metadata;
        if (c == MapDTO.class) {
            MapMetadataDTO map = new MapMetadataDTO();
            map.setFolders(Lists.newArrayList(new String[]{file.getName()}));
            metadata = map;
        } else {
            metadata = new MetadataDTO();
            metadata.setSha1(getChecksum(file, MessageDigestAlgorithms.SHA_1));
            metadata.setSize(file.length());
        }
        metadata.setPath(rootFolder.toPath().relativize(file.toPath()).toString().replace("\\", "/"));
        metadata.setUrl(rootFolder.toPath().relativize(file.toPath()).toString().replace("\\", "/"));
        return metadata;
    }

    public static void zipFiles(List<File> files, Path root, File result) throws IOException {
        FileOutputStream fos = new FileOutputStream(result);
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(fos), StandardCharsets.UTF_8);
        for (File input : files) {
            FileInputStream fis = new FileInputStream(input);
            ZipEntry ze = new ZipEntry(root.relativize(input.toPath()).toString().replaceAll("\\\\", "/"));
            zipOut.putNextEntry(ze);
            byte[] tmp = new byte[CpioConstants.C_ISFIFO];
            while (true) {
                int size = fis.read(tmp);
                if (size != -1) {
                    zipOut.write(tmp, 0, size);
                }
            }
            zipOut.flush();
            fis.close();
        }
        zipOut.close();
    }

    public static void backupModpacks(Map<String, String> map, List<File> files, Path root, File result, List<String> modpacks) throws IOException {
        InputStream fis;
        FileOutputStream fos = new FileOutputStream(result);
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(fos), StandardCharsets.UTF_8);
        for (File input : files) {
            if (map.containsKey(input.getName())) {
                fis = new ByteArrayInputStream(map.get(input.getName()).getBytes(StandardCharsets.UTF_8));
            } else {
                fis = new FileInputStream(input);
            }
            ZipEntry ze = new ZipEntry(root.relativize(input.toPath()).toString().replaceAll("\\\\", "/"));
            U.log(ze.getName());
            zipOut.putNextEntry(ze);
            byte[] tmp = new byte[CpioConstants.C_ISFIFO];
            while (true) {
                int size = fis.read(tmp);
                if (size != -1) {
                    zipOut.write(tmp, 0, size);
                }
            }
            zipOut.flush();
            fis.close();
        }
        zipOut.close();
    }

    public static void unzipUniversal(File file, File folder) throws IOException, ParseModPackException {
        String ext = FilenameUtils.getExtension(file.getCanonicalPath());
        boolean z = true;
        switch (ext.hashCode()) {
            case 112675:
                if (ext.equals("rar")) {
                    z = false;
                    break;
                }
                break;
            case 120609:
                if (ext.equals(ArchiveStreamFactory.ZIP)) {
                    z = true;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                ExtractArchive extractArchive = new ExtractArchive();
                extractArchive.extractArchive(file, folder);
                return;
            case true:
                unZip(file, folder, true);
                return;
            default:
                throw new ParseModPackException("problem with format of the modpack");
        }
    }

    public static void zipFolder(File srcFolder, File destZipFile) throws Exception {
        List<File> files = (List) FileUtils.listFiles(srcFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        zipFiles(files, srcFolder.toPath().getParent(), destZipFile);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/util/FileUtil$GameEntityFolder.class */
    public enum GameEntityFolder {
        MODS,
        RESOURCEPACKS,
        MAPS,
        SAVES,
        RESOURCES,
        SHADERPACKS;

        @Override // java.lang.Enum
        public String toString() {
            return super.toString().toLowerCase();
        }

        public static String getPath(GameType type) {
            switch (type) {
                case MOD:
                    return MODS.toString().toLowerCase();
                case MAP:
                    return SAVES.toString().toLowerCase();
                case RESOURCEPACK:
                    return RESOURCEPACKS.toString().toLowerCase();
                case SHADERPACK:
                    return SHADERPACKS.toString().toLowerCase();
                default:
                    return CoreConstants.EMPTY_STRING;
            }
        }

        public static String getPath(Class<? extends GameEntityDTO> type, boolean folderSeparate) {
            String path = CoreConstants.EMPTY_STRING;
            if (type == ModDTO.class) {
                path = getPath(GameType.MOD);
            } else if (type == MapDTO.class) {
                path = getPath(GameType.MAP);
            } else if (type == ResourcePackDTO.class) {
                path = getPath(GameType.RESOURCEPACK);
            } else if (type == ShaderpackDTO.class) {
                path = getPath(GameType.SHADERPACK);
            }
            if (folderSeparate) {
                return path + "/";
            }
            return path;
        }
    }

    public static String readZippedFile(File file, String name) throws IOException {
        ZipFile f = new ZipFile(file);
        Throwable th = null;
        try {
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                ZipEntry entry = en.nextElement();
                if (entry.getName().endsWith(name)) {
                    String readStream = readStream(f.getInputStream(entry));
                    if (f != null) {
                        if (0 != 0) {
                            try {
                                f.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            f.close();
                        }
                    }
                    return readStream;
                }
            }
            if (f != null) {
                if (0 != 0) {
                    try {
                        f.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                } else {
                    f.close();
                }
            }
            throw new FileNotFoundException(file.toString());
        } finally {
        }
    }
}
