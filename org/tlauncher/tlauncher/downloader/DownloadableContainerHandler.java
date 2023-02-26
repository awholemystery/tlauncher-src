package org.tlauncher.tlauncher.downloader;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/DownloadableContainerHandler.class */
public interface DownloadableContainerHandler {
    void onAbort(DownloadableContainer downloadableContainer);

    void onError(DownloadableContainer downloadableContainer, Downloadable downloadable, Throwable th);

    void onComplete(DownloadableContainer downloadableContainer, Downloadable downloadable) throws RetryDownloadException;

    void onFullComplete(DownloadableContainer downloadableContainer);
}
