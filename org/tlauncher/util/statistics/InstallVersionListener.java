package org.tlauncher.util.statistics;

import com.google.common.collect.Maps;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.statistics.InstallVersionDTO;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.downloader.DownloaderAdapterListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/statistics/InstallVersionListener.class */
public class InstallVersionListener extends DownloaderAdapterListener {
    private String version;

    @Override // org.tlauncher.tlauncher.downloader.DownloaderAdapterListener, org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderStart(Downloader d, int files) {
        try {
            Version v = TLauncher.getInstance().getFrame().mp.defaultScene.loginForm.versions.getSelectedValue().getRemote();
            if (v == null) {
                v = TLauncher.getInstance().getFrame().mp.defaultScene.loginForm.versions.getSelectedValue().getLocal();
            }
            if (v == null) {
                this.version = "version_not_found";
            } else {
                this.version = v.getID();
            }
        } catch (NullPointerException exception) {
            U.log(exception);
            this.version = "version_not_found";
        }
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderAdapterListener, org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderComplete(Downloader d) {
        InstallVersionDTO v = new InstallVersionDTO();
        v.setInstallVersion(this.version);
        StatisticsUtil.startSending("save/install/version", v, Maps.newHashMap());
    }
}
