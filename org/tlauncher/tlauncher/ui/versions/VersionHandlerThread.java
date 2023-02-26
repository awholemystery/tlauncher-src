package org.tlauncher.tlauncher.ui.versions;

import ch.qos.logback.core.CoreConstants;
import org.tlauncher.tlauncher.managers.ProfileManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.versions.VersionDownloadButton;
import org.tlauncher.util.U;
import org.tlauncher.util.async.LoopedThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionHandlerThread.class */
public class VersionHandlerThread {
    private final VersionHandler handler;
    public final String START_DOWNLOAD = VersionHandler.START_DOWNLOAD;
    public final String STOP_DOWNLOAD = VersionHandler.STOP_DOWNLOAD;
    public final String DELETE_BLOCK = VersionHandler.DELETE_BLOCK;
    final StartDownloadThread startThread = new StartDownloadThread(this);
    final StopDownloadThread stopThread = new StopDownloadThread(this);
    final VersionDeleteThread deleteThread = new VersionDeleteThread(this);

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionHandlerThread(VersionHandler handler) {
        this.handler = handler;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionHandlerThread$StartDownloadThread.class */
    class StartDownloadThread extends LoopedThread {
        private final VersionHandler handler;
        private final VersionDownloadButton button;

        StartDownloadThread(VersionHandlerThread parent) {
            super("StartDownloadThread");
            this.handler = parent.handler;
            this.button = this.handler.list.download;
            startAndWait();
        }

        @Override // org.tlauncher.util.async.LoopedThread
        protected void iterateOnce() {
            ProfileManager p = TLauncher.getInstance().getProfileManager();
            if (!p.hasSelectedAccount()) {
                Alert.showLocError(CoreConstants.EMPTY_STRING, "auth.error.nousername", null);
                return;
            }
            this.button.setState(VersionDownloadButton.ButtonState.STOP);
            Blocker.block(this.handler, VersionHandler.START_DOWNLOAD);
            this.button.startDownload();
            Blocker.unblock(this.handler, VersionHandler.START_DOWNLOAD);
            this.button.setState(VersionDownloadButton.ButtonState.DOWNLOAD);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionHandlerThread$StopDownloadThread.class */
    class StopDownloadThread extends LoopedThread {
        private final VersionHandler handler;
        private final VersionDownloadButton button;

        StopDownloadThread(VersionHandlerThread parent) {
            super("StopDownloadThread");
            this.handler = parent.handler;
            this.button = this.handler.list.download;
            startAndWait();
        }

        @Override // org.tlauncher.util.async.LoopedThread
        protected void iterateOnce() {
            Blocker.block(this.button.blockable, VersionHandler.STOP_DOWNLOAD);
            while (!this.handler.downloader.isThreadLocked()) {
                U.sleepFor(1000L);
            }
            this.button.stopDownload();
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/versions/VersionHandlerThread$VersionDeleteThread.class */
    class VersionDeleteThread extends LoopedThread {
        private final VersionHandler handler;
        private final VersionRemoveButton button;

        VersionDeleteThread(VersionHandlerThread parent) {
            super("VersionDeleteThread");
            this.handler = parent.handler;
            this.button = this.handler.list.remove;
            startAndWait();
        }

        @Override // org.tlauncher.util.async.LoopedThread
        protected void iterateOnce() {
            Blocker.block(this.handler, VersionHandler.DELETE_BLOCK);
            this.button.delete();
            Blocker.unblock(this.handler, VersionHandler.DELETE_BLOCK);
        }
    }
}
