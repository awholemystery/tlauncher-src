package org.tlauncher.tlauncher.updater.client;

import org.tlauncher.tlauncher.entity.ConfigEnum;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.updater.AutoUpdaterFrame;
import org.tlauncher.tlauncher.updater.client.Updater;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/AutoUpdater.class */
public class AutoUpdater extends Updater {
    private final TLauncher t;
    private final AutoUpdaterFrame frame = new AutoUpdaterFrame(this);
    private final UpdaterListener updaterL;
    private final UpdateListener updateL;

    public AutoUpdater(final TLauncher tlauncher) {
        this.t = tlauncher;
        addListener(new UpdaterAdapter() { // from class: org.tlauncher.tlauncher.updater.client.AutoUpdater.1
            @Override // org.tlauncher.tlauncher.updater.client.UpdaterAdapter, org.tlauncher.tlauncher.updater.client.UpdaterListener
            public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
                if (TLauncher.getVersion() < succeeded.response.getVersion()) {
                    tlauncher.getConfiguration().set(ConfigEnum.UPDATER_LAUNCHER, (Object) true);
                }
            }
        });
        addListener(this.frame);
        this.updaterL = new UpdaterAdapter() { // from class: org.tlauncher.tlauncher.updater.client.AutoUpdater.2
            @Override // org.tlauncher.tlauncher.updater.client.UpdaterAdapter, org.tlauncher.tlauncher.updater.client.UpdaterListener
            public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
                Update upd = succeeded.getResponse();
                if (upd.isApplicable()) {
                    upd.addListener(AutoUpdater.this.updateL);
                    upd.download();
                }
            }
        };
        this.updateL = new UpdateListener() { // from class: org.tlauncher.tlauncher.updater.client.AutoUpdater.3
            @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
            public void onUpdateError(Update u, Throwable e) {
            }

            @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
            public void onUpdateDownloading(Update u) {
            }

            @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
            public void onUpdateDownloadError(Update u, Throwable e) {
            }

            @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
            public void onUpdateReady(Update u) {
                u.apply();
            }

            @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
            public void onUpdateApplying(Update u) {
            }

            @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
            public void onUpdateApplyError(Update u, Throwable e) {
                System.exit(0);
            }
        };
        addListener(this.updaterL);
    }

    public TLauncher getLauncher() {
        return this.t;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.updater.client.Updater
    public Updater.SearchResult findUpdate0() {
        return super.findUpdate0();
    }

    public void cancelUpdate() {
        this.t.getDownloader().stopDownload();
    }

    public boolean isClosed() {
        return this.frame.isClosed();
    }
}
