package org.tlauncher.tlauncher.ui.listener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/ManagerListerner.class */
public interface ManagerListerner<T> {
    void startedDownloading();

    void downloadedData(T t);

    void preparedGame();
}
