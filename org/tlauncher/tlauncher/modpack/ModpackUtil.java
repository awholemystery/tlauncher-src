package org.tlauncher.tlauncher.modpack;

import ch.qos.logback.core.CoreConstants;
import com.google.common.collect.Lists;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.Version;
import org.apache.commons.io.IOUtils;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameEntityDependencyDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.share.DependencyType;
import org.tlauncher.modpack.domain.client.share.ForgeStringComparator;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/modpack/ModpackUtil.class */
public class ModpackUtil {
    public static List<? extends VersionDTO> sortByDate(List<VersionDTO> versions) {
        List<VersionDTO> sortList = new ArrayList<>(versions);
        sortList.sort(o1, o2 -> {
            return o2.getUpdateDate().compareTo(o1.getUpdateDate());
        });
        return sortList;
    }

    public static Path getPathByVersion(Version version, String... paths) {
        StringBuilder builder = new StringBuilder();
        builder.append(PathAppUtil.VERSION_DIRECTORY).append("/").append(version.getID());
        for (String line : paths) {
            builder.append("/").append(line);
        }
        return FileUtil.getRelative(builder.toString());
    }

    public static Path getPathByVersion(CompleteVersion version) {
        return getPathByVersion(version, CoreConstants.EMPTY_STRING);
    }

    public static String getLatestGameVersion(Set<String> c) {
        if (c.isEmpty()) {
            return null;
        }
        ArrayList<String> list = Lists.newArrayList(c);
        list.sort(new ForgeStringComparator());
        return list.get(0);
    }

    public static void extractIncompatible(GameEntityDTO en, Set<Long> set) {
        if (en.getDependencies() == null) {
            return;
        }
        for (GameEntityDependencyDTO d : en.getDependencies()) {
            if (d.getDependencyType() == DependencyType.INCOMPATIBLE) {
                set.add(d.getGameEntityId());
            }
        }
    }

    public static Set<Long> getAllModpackIds(ModpackDTO m) {
        Set<Long> set = new HashSet<>();
        if (m.getId() != null) {
            set.add(m.getId());
        }
        for (GameType t : GameType.getSubEntities()) {
            List<? extends GameEntityDTO> list = ((ModpackVersionDTO) m.getVersion()).getByType(t);
            for (int i = 0; i < list.size(); i++) {
                if (Objects.nonNull(list.get(i).getId())) {
                    set.add(list.get(i).getId());
                }
            }
        }
        return set;
    }

    public static List<String> getPictureURL(Long id, String type) {
        return (List) Arrays.stream(TLauncher.getInnerSettings().getArray("file.server")).map(s -> {
            return s + "/pictures/" + type + "/" + id + ".png";
        }).collect(Collectors.toList());
    }

    public static StringBuilder buildMessage(List<GameEntityDTO> list) {
        StringBuilder b = new StringBuilder();
        for (GameEntityDTO dependency : list) {
            b.append(dependency.getName()).append("(").append(Localizable.get("modpack.button." + GameType.create((Class<? extends GameEntityDTO>) dependency.getClass()))).append(")").append(" ");
        }
        return b;
    }

    public static Path getPath(CompleteVersion v, GameType type) {
        switch (type) {
            case RESOURCEPACK:
                return getPathByVersion(v, "resourcepacks");
            case MOD:
                return getPathByVersion(v, "mods");
            case MAP:
                return getPathByVersion(v, PathAppUtil.DIRECTORY_WORLDS);
            case SHADERPACK:
                return getPathByVersion(v, "shaderpacks");
            default:
                throw new RuntimeException("not proper type");
        }
    }

    public static boolean useSkinMod(CompleteVersion version) {
        for (ModDTO m : ((ModpackVersionDTO) version.getModpack().getVersion()).getMods()) {
            if (ModDTO.TL_SKIN_CAPE_ID.equals(m.getId())) {
                return true;
            }
        }
        return false;
    }

    public static void addOrReplaceShaderConfig(CompleteVersion v, String field, String filename) throws IOException {
        Path p = getPathByVersion(v).resolve("optionsshaders.txt");
        if (Files.notExists(p, new LinkOption[0])) {
            Files.createFile(p, new FileAttribute[0]);
        }
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(p.toFile());
        properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        IOUtils.closeQuietly((InputStream) inputStream);
        properties.put(field, filename);
        FileWriter fileWriter = new FileWriter(p.toFile());
        properties.store(fileWriter, CoreConstants.EMPTY_STRING);
        IOUtils.closeQuietly((Writer) fileWriter);
    }

    public static String readShaderpackConfigField(CompleteVersion v, String name) throws IOException {
        Path p = getPathByVersion(v).resolve("optionsshaders.txt");
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(p.toFile());
        properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        IOUtils.closeQuietly((InputStream) inputStream);
        return properties.getProperty(name);
    }
}
