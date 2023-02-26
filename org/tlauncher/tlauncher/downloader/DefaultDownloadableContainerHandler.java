package org.tlauncher.tlauncher.downloader;

import java.util.Objects;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.util.FileUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/DefaultDownloadableContainerHandler.class */
public class DefaultDownloadableContainerHandler extends DownloadableContainerHandlerAdapter {
    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
        MetadataDTO m = d.getMetadataDTO();
        if (m.getSha1() == null) {
            return;
        }
        String hash = FileUtil.getChecksum(m.getLocalDestination());
        if (Objects.isNull(hash) || hash.equals(m.getSha1())) {
            return;
        }
        throw new RetryDownloadException(String.format("illegal hash, got: %s; expected: %s", hash, m.getSha1()));
    }
}
