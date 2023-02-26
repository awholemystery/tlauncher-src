package org.tlauncher.tlauncher.downloader.mods;

import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.downloader.DownloadableContainerHandler;
import org.tlauncher.tlauncher.downloader.RetryDownloadException;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/mods/GameEntityHandler.class */
public class GameEntityHandler implements DownloadableContainerHandler {
    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onAbort(DownloadableContainer c) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onError(DownloadableContainer c, Downloadable d, Throwable e) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
        if (d instanceof GameEntityDownloader) {
            GameEntityDownloader g = (GameEntityDownloader) d;
            String fileHash = FileUtil.getChecksum(d.getMetadataDTO().getLocalDestination(), "sha1");
            if (fileHash == null || fileHash.equals(g.getMetadataDTO().getSha1())) {
                return;
            }
            throw new RetryDownloadException("illegal library hash. got: " + fileHash + "; expected: " + g.getMetadataDTO().getSha1());
        }
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onFullComplete(DownloadableContainer c) {
    }
}
