package org.tlauncher.tlauncher.updater.client;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/UpdateListener.class */
public interface UpdateListener {
    void onUpdateError(Update update, Throwable th);

    void onUpdateDownloading(Update update);

    void onUpdateDownloadError(Update update, Throwable th);

    void onUpdateReady(Update update);

    void onUpdateApplying(Update update);

    void onUpdateApplyError(Update update, Throwable th);
}
