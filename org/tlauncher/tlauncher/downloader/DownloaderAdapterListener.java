package org.tlauncher.tlauncher.downloader;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/DownloaderAdapterListener.class */
public abstract class DownloaderAdapterListener implements DownloaderListener {
    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderStart(Downloader d, int files) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderAbort(Downloader d) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderProgress(Downloader d, double progress, double speed, double alreadyDownloaded) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderFileComplete(Downloader d, Downloadable file) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderComplete(Downloader d) {
    }
}
