package org.tlauncher.tlauncher.downloader.mods;

import java.io.File;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.RetryDownloadException;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/mods/MapDownloader.class */
public class MapDownloader extends GameEntityDownloader {
    public MapDownloader(boolean forceDownload, MetadataDTO metadata) {
        super(ClientInstanceRepo.createModpackRepo(), forceDownload, metadata);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.downloader.Downloadable
    public void onComplete() throws RetryDownloadException {
        super.onComplete();
        try {
            File f = getMetadataDTO().getLocalDestination();
            FileUtil.unzipUniversal(f, f.getParentFile());
            FileUtil.deleteFile(f);
        } catch (Throwable t) {
            throw new RetryDownloadException("cannot unpack archive", t);
        }
    }
}
