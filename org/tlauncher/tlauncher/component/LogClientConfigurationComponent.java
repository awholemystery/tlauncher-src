package org.tlauncher.tlauncher.component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.LogClient;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/component/LogClientConfigurationComponent.class */
public class LogClientConfigurationComponent {
    private LogClient.LogClientFile validateLogClientXmlResource(CompleteVersion version, File gameDir) {
        if (isNotNullLogClient(version)) {
            LogClient.LogClientFile l = getLogClient(version).getFile();
            if (!l.getSha1().equals(FileUtil.getChecksum(buildPathLogXmlConfiguration(gameDir, getLogClient(version)).toFile()))) {
                return l;
            }
            return null;
        }
        return null;
    }

    public DownloadableContainer getContainer(CompleteVersion version, File gameDir, boolean forceUpdate) {
        DownloadableContainer container = new DownloadableContainer();
        LogClient.LogClientFile l = validateLogClientXmlResource(version, gameDir);
        if (Objects.isNull(l)) {
            return container;
        }
        MetadataDTO m = new MetadataDTO();
        m.setLocalDestination(Paths.get(gameDir.toString(), "assets", PathAppUtil.LOG_CONFIGS, l.getId()).toFile());
        m.setSha1(l.getSha1());
        m.setUrl(l.getUrl());
        m.setSize(l.getSize());
        Downloadable d = new Downloadable(ClientInstanceRepo.EMPTY_REPO, m, forceUpdate);
        container.add(d);
        return container;
    }

    public Path buildPathLogXmlConfiguration(File gameDir, LogClient l) {
        return Paths.get(gameDir.getAbsolutePath(), "assets", PathAppUtil.LOG_CONFIGS, l.getFile().getId());
    }

    public LogClient getLogClient(CompleteVersion v) {
        if (Objects.nonNull(v.getLogging())) {
            LogClient c = v.getLogging().get("client");
            if (Objects.nonNull(c) && Objects.nonNull(c.getFile())) {
                return c;
            }
            return null;
        }
        return null;
    }

    public boolean isNotNullLogClient(CompleteVersion v) {
        return !Objects.isNull(getLogClient(v));
    }
}
