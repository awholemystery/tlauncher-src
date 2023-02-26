package org.tlauncher.tlauncher.managers;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ManagerListerner.class */
public interface ManagerListerner<T> {
    void startedDownloading();

    void downloadedData(T t);

    void preparedGame();
}
