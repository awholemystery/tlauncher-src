package org.tlauncher.tlauncher.ui.listener;

import java.net.URI;
import java.net.URISyntaxException;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.updater.client.Update;
import org.tlauncher.tlauncher.updater.client.UpdateListener;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/UpdateUIListener.class */
public class UpdateUIListener implements UpdateListener {
    private final TLauncher t;
    private final Update u;

    public UpdateUIListener(Update u) {
        if (u == null) {
            throw new NullPointerException();
        }
        this.t = TLauncher.getInstance();
        this.u = u;
        u.addListener(this);
    }

    public void push() {
        block();
        this.u.download(true);
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateError(Update u, Throwable e) {
        if (Alert.showLocQuestion("updater.error.title", "updater.download-error", e)) {
            openUpdateLink(u.getlastDownloadedLink());
        }
        unblock();
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateDownloading(Update u) {
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateDownloadError(Update u, Throwable e) {
        onUpdateError(u, e);
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateReady(Update u) {
        onUpdateReady(u, false);
    }

    private static void onUpdateReady(Update u, boolean showChangeLog) {
        Alert.showLocWarning("updater.downloaded", (Object) (showChangeLog ? u.getDescription() : null));
        u.apply();
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateApplying(Update u) {
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateApplyError(Update u, Throwable e) {
        if (Alert.showLocQuestion("updater.save-error", e)) {
            openUpdateLink(u.getlastDownloadedLink());
        }
        unblock();
    }

    public static boolean openUpdateLink(String link) {
        try {
            if (OS.openLink(new URI(link), false)) {
                return true;
            }
        } catch (URISyntaxException e) {
        }
        Alert.showLocError("updater.found.cannotopen", link);
        return false;
    }

    private void block() {
        Blocker.block(this.t.getFrame().mp, "updater");
    }

    private void unblock() {
        Blocker.unblock(this.t.getFrame().mp, "updater");
    }
}
