package org.tlauncher.tlauncher.downloader.mods;

import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.repository.Repo;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/mods/GameEntityDownloader.class */
public abstract class GameEntityDownloader extends Downloadable {
    public GameEntityDownloader(Repo repo, boolean forceDownload, MetadataDTO metadataDTO) {
        super(repo, metadataDTO, forceDownload);
    }
}
