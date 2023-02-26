package org.tlauncher.tlauncher.updater.client;

import org.tlauncher.tlauncher.updater.client.Updater;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/UpdaterListener.class */
public interface UpdaterListener {
    void onUpdaterRequesting(Updater updater);

    void onUpdaterErrored(Updater.SearchFailed searchFailed);

    void onUpdaterSucceeded(Updater.SearchSucceeded searchSucceeded);
}
