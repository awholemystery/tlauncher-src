package org.tlauncher.tlauncher.downloader;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/DownloaderListener.class */
public interface DownloaderListener {
    void onDownloaderStart(Downloader downloader, int i);

    void onDownloaderAbort(Downloader downloader);

    void onDownloaderProgress(Downloader downloader, double d, double d2, double d3);

    void onDownloaderFileComplete(Downloader downloader, Downloadable downloadable);

    void onDownloaderComplete(Downloader downloader);
}
