package org.tlauncher.tlauncher.updater.client;

import org.tlauncher.tlauncher.updater.client.Updater;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/UpdaterAdapter.class */
public abstract class UpdaterAdapter implements UpdaterListener {
    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterRequesting(Updater u) {
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterErrored(Updater.SearchFailed failed) {
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
    }
}
