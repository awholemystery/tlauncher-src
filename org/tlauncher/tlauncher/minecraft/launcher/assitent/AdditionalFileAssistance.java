package org.tlauncher.tlauncher.minecraft.launcher.assitent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.DefaultDownloadableContainerHandler;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.downloader.mods.GameEntityDownloader;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/assitent/AdditionalFileAssistance.class */
public class AdditionalFileAssistance extends MinecraftLauncherAssistantWrapper {
    @Override // org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantWrapper, org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantInterface
    public void collectResources(MinecraftLauncher launcher) throws MinecraftException {
        List<MetadataDTO> list = launcher.getVersion().getAdditionalFiles();
        if (Objects.isNull(list)) {
            return;
        }
        DownloadableContainer c = new DownloadableContainer();
        try {
            for (MetadataDTO m : list) {
                m.setLocalDestination(new File(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), m.getPath()));
                if (notExistsOrCorrect(m) && !copyFromLocalRepo(m)) {
                    c.add(new AdditionalFileDownloader(m));
                }
            }
            if (!c.getList().isEmpty()) {
                c.addHandler(new DefaultDownloadableContainerHandler());
                launcher.getDownloader().add(c);
            }
        } catch (IOException e) {
            U.log(e);
            throw new MinecraftException(e.getMessage(), "download-jar", e);
        }
    }

    private boolean copyFromLocalRepo(MetadataDTO m) throws IOException {
        File source = new File(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), new URL(m.getUrl()).getPath());
        File target = new File(MinecraftUtil.getWorkingDirectory(), m.getPath());
        if (m.getSha1().equalsIgnoreCase(FileUtil.getChecksum(source))) {
            if (source.equals(target)) {
                return true;
            }
            FileUtil.copyFile(source, target, true);
            return true;
        }
        return false;
    }

    private boolean notExistsOrCorrect(MetadataDTO m) {
        File target = m.getLocalDestination();
        return (target.exists() && m.getSha1().equalsIgnoreCase(FileUtil.getChecksum(target))) ? false : true;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/assitent/AdditionalFileAssistance$AdditionalFileDownloader.class */
    private static class AdditionalFileDownloader extends GameEntityDownloader {
        AdditionalFileDownloader(MetadataDTO metadata) {
            super(ClientInstanceRepo.EMPTY_REPO, true, metadata);
        }
    }
}
