package org.tlauncher.tlauncher.downloader;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/DownloadableContainerHandlerAdapter.class */
public abstract class DownloadableContainerHandlerAdapter implements DownloadableContainerHandler {
    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onAbort(DownloadableContainer c) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onError(DownloadableContainer c, Downloadable d, Throwable e) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
    public void onFullComplete(DownloadableContainer c) {
    }
}
