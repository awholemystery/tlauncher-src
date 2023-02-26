package org.tlauncher.tlauncher.downloader;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/DownloadableHandler.class */
public interface DownloadableHandler {
    void onStart(Downloadable downloadable);

    void onAbort(Downloadable downloadable);

    void onComplete(Downloadable downloadable) throws RetryDownloadException;

    void onError(Downloadable downloadable, Throwable th);
}
