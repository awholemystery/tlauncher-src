package org.tlauncher.tlauncher.modpack;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.extract.ExtractArchive;
import com.github.junrar.rarfile.FileHeader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.launcher.versions.CompleteVersion;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FilenameUtils;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/modpack/PreparedModIMpl.class */
public class PreparedModIMpl implements PreparedMod {
    @Override // org.tlauncher.tlauncher.modpack.PreparedMod
    public List<Path> prepare(CompleteVersion completeVersion) {
        return new ArrayList();
    }

    private void prepareModeFolders(Path source, Path dest, List<Path> paths) {
        File[] files = source.toFile().listFiles();
        for (File file : files) {
            paths.add(file.toPath());
        }
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            U.log(e);
        }
    }

    private void prepareGameFolders(Path source, Path dest, List<Path> paths) {
        File[] files = source.toFile().listFiles();
        ExtractArchive extractArchive = new ExtractArchive();
        for (int i = 0; i < files.length; i++) {
            String ext = FilenameUtils.getExtension(files[i].toString());
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
                    try {
                        paths.addAll(topFolders(new Archive(files[i])));
                    } catch (IOException | RarException e) {
                        U.log(e);
                    }
                    extractArchive.extractArchive(files[i], dest.toFile());
                    return;
                case true:
                    return;
                default:
                    U.log("don't find extension");
            }
        }
    }

    private List<Path> topFolders(Archive rar) throws IOException, RarException {
        FileHeader fh = rar.nextFileHeader();
        List<Path> list = new ArrayList<>();
        while (fh != null) {
            if (fh.isDirectory()) {
                list.add(Paths.get(fh.getFileNameString(), new String[0]));
            }
            fh = rar.nextFileHeader();
        }
        return list;
    }

    private List<Path> topFolders(ZipFile zipFile) {
        List<Path> list = new ArrayList<>();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                File file = new File(entry.getName());
                if (file.getParent() == null) {
                    list.add(Paths.get(file.getName(), new String[0]));
                }
            }
        }
        return list;
    }
}
