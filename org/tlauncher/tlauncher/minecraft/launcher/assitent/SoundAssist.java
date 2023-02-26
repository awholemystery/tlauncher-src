package org.tlauncher.tlauncher.minecraft.launcher.assitent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.DefaultDownloadableContainerHandler;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.entity.AdditionalAsset;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/assitent/SoundAssist.class */
public class SoundAssist extends MinecraftLauncherAssistantWrapper {
    @Override // org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantWrapper, org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantInterface
    public void collectResources(MinecraftLauncher launcher) throws MinecraftException {
        List<AdditionalAsset> assets = TLauncher.getInstance().getAdditionalAssetsComponent().getAdditionalAssets();
        DownloadableContainer c = new DownloadableContainer();
        try {
            for (AdditionalAsset r : assets) {
                if (r.getVersions().contains(launcher.getVersion().getJar()) || r.getVersions().contains(launcher.getVersionName())) {
                    for (MetadataDTO m : r.getFiles()) {
                        if (notExistsOrCorrect(launcher.getRunningMinecraftDir(), m) && !copyFromLocalRepo(launcher.getRunningMinecraftDir(), m)) {
                            m.setLocalDestination(new File(MinecraftUtil.getTLauncherFile("repo"), m.getPath()));
                            c.add(new Downloadable(ClientInstanceRepo.EXTRA_VERSION_REPO, m, true));
                        }
                    }
                }
            }
            if (!c.getList().isEmpty()) {
                c.addHandler(new DefaultDownloadableContainerHandler());
                launcher.getDownloader().add(c);
            }
        } catch (IOException e) {
            throw new MinecraftException(e.getMessage(), "download-jar", e);
        }
    }

    private boolean notExistsOrCorrect(File file, MetadataDTO m) {
        File f = new File(file, m.getPath());
        return (f.exists() && m.getSha1().equalsIgnoreCase(FileUtil.getChecksum(f))) ? false : true;
    }

    private boolean copyFromLocalRepo(File file, MetadataDTO m) throws IOException {
        File source = MinecraftUtil.getTLauncherFile("repo/" + m.getPath());
        File target = new File(file, m.getPath());
        if (m.getSha1().equalsIgnoreCase(FileUtil.getChecksum(source))) {
            FileUtil.copyFile(source, target, true);
            return true;
        }
        return false;
    }
}
