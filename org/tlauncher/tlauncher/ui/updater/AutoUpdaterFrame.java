package org.tlauncher.tlauncher.ui.updater;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.nio.file.Files;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.tlauncher.tlauncher.controller.UpdaterFormController;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.downloader.DownloaderListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.listener.UpdateUIListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.swing.ImagePanel;
import org.tlauncher.tlauncher.updater.client.AutoUpdater;
import org.tlauncher.tlauncher.updater.client.Update;
import org.tlauncher.tlauncher.updater.client.UpdateListener;
import org.tlauncher.tlauncher.updater.client.Updater;
import org.tlauncher.tlauncher.updater.client.UpdaterListener;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/updater/AutoUpdaterFrame.class */
public class AutoUpdaterFrame extends JFrame implements DownloaderListener, UpdaterListener, UpdateListener {
    private static final long serialVersionUID = -1184260781662212096L;
    private static final int ANIMATION_TICK = 1;
    private static final double OPACITY_STEP = 0.005d;
    private final JPanel titlepan;
    private final JPanel pan;
    private final LocalizableLabel label;
    private final ImagePanel hide;
    private final ImagePanel skip;
    private boolean closed;
    private boolean canSkip;
    private final Color border = new Color(255, 255, 255, 255);
    private final AutoUpdaterFrame instance = this;
    private final Object animationLock = new Object();

    public AutoUpdaterFrame(AutoUpdater updater) {
        setPreferredSize(new Dimension(350, 60));
        setResizable(false);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(3);
        this.pan = new JPanel() { // from class: org.tlauncher.tlauncher.ui.updater.AutoUpdaterFrame.1
            private static final long serialVersionUID = -8469500310564854471L;
            protected final Insets insets = new Insets(5, 10, 10, 10);
            protected final Color background = new Color(255, 255, 255, 220);

            public void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0;
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(this.background);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g.setColor(AutoUpdaterFrame.this.border);
                for (int x = 1; x < 2; x++) {
                    g.drawRoundRect(x - 1, x - 1, (getWidth() - (2 * x)) + 1, (getHeight() - (2 * x)) + 1, 16, 16);
                }
                Color shadow = U.shiftAlpha(Color.gray, -200);
                int x2 = 2;
                while (true) {
                    shadow = U.shiftAlpha(shadow, -8);
                    if (shadow.getAlpha() != 0) {
                        g.setColor(shadow);
                        g.drawRoundRect(x2 - 1, x2 - 1, (getWidth() - (2 * x2)) + 1, (getHeight() - (2 * x2)) + 1, (16 - (2 * x2)) + 1, (16 - (2 * x2)) + 1);
                        x2++;
                    } else {
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                        super.paintComponent(g0);
                        return;
                    }
                }
            }

            public Insets getInsets() {
                return this.insets;
            }
        };
        this.pan.setOpaque(false);
        this.pan.setLayout(new BorderLayout());
        add(this.pan);
        this.titlepan = new JPanel();
        this.titlepan.setOpaque(false);
        this.titlepan.setLayout(new FlowLayout(2));
        this.hide = new ImagePanel("hide.png", 0.75f, 0.5f, false) { // from class: org.tlauncher.tlauncher.ui.updater.AutoUpdaterFrame.2
            private static final long serialVersionUID = 513294577418505533L;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.tlauncher.tlauncher.ui.swing.ImagePanel
            public boolean onClick() {
                if (super.onClick()) {
                    AutoUpdaterFrame.this.instance.setExtendedState(1);
                    return true;
                }
                return false;
            }
        };
        this.hide.setToolTipText(Localizable.get("autoupdater.buttons.hide"));
        this.titlepan.add(this.hide);
        this.skip = new ImagePanel("skip.png", 0.75f, 0.5f, false) { // from class: org.tlauncher.tlauncher.ui.updater.AutoUpdaterFrame.3
            private static final long serialVersionUID = 513294577418505533L;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.tlauncher.tlauncher.ui.swing.ImagePanel
            public boolean onClick() {
                if (super.onClick() && AutoUpdaterFrame.this.canSkip) {
                    AutoUpdaterFrame.this.handleClose();
                    return true;
                }
                return false;
            }
        };
        this.pan.add("East", this.titlepan);
        this.label = new LocalizableLabel("autoupdater.preparing");
        this.label.setOpaque(false);
        this.pan.add("West", this.label);
        setCanSkip(true);
        updater.getLauncher().getDownloader().addListener(this);
        pack();
        setLocationRelativeTo(null);
        requestFocusInWindow();
    }

    private void hideButtons() {
        this.skip.hide();
    }

    private void closeFrame() {
        if (this.closed) {
            dispose();
        } else {
            float opacity = 1.0f;
            synchronized (this.animationLock) {
                try {
                    setOpacity(1.0f);
                } catch (Throwable th) {
                }
                while (opacity > 0.0f) {
                    opacity = (float) (opacity - OPACITY_STEP);
                    if (opacity < 0.0f) {
                        opacity = 0.0f;
                    }
                    try {
                        setOpacity(opacity);
                    } catch (Throwable th2) {
                    }
                    U.sleepFor(1L);
                }
                dispose();
            }
        }
        this.closed = true;
    }

    public boolean canSkip() {
        return this.canSkip;
    }

    private void setCanSkip(boolean b) {
        this.canSkip = b;
        if (!b) {
            hideButtons();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleClose() {
        this.label.setText("autoupdater.opening", Double.valueOf(TLauncher.getVersion()));
        closeFrame();
    }

    public boolean isClosed() {
        return this.closed;
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateError(Update u, Throwable e) {
        if (this.closed) {
            return;
        }
        if (!Files.isWritable(FileUtil.getRunningJar().getParentFile().toPath())) {
            if (Alert.showErrorMessage("updater.error.title", "updater.access.denied.message", "updater.button.download.handle", "review.button.close") == 0) {
                OS.openLink(OS.is(OS.WINDOWS) ? u.getExeLinks().get(0) : u.getJarLinks().get(0));
            }
            TLauncher.kill();
        } else if (Alert.showLocQuestion("updater.error.title", "updater.download-error", e)) {
            UpdateUIListener.openUpdateLink(u.getlastDownloadedLink());
        }
        handleClose();
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateDownloading(Update u) {
        this.label.setText("autoupdater.downloading", Double.valueOf(u.getVersion()));
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateDownloadError(Update u, Throwable e) {
        onUpdateError(u, e);
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateReady(Update u) {
        setCanSkip(true);
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateApplying(Update u) {
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdateListener
    public void onUpdateApplyError(Update u, Throwable e) {
        if (Alert.showLocQuestion("updater.save-error", e)) {
            UpdateUIListener.openUpdateLink(u.getlastDownloadedLink());
        }
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterRequesting(Updater u) {
        this.label.setText("autoupdater.requesting");
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterErrored(Updater.SearchFailed failed) {
        handleClose();
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
        Update update = succeeded.getResponse();
        if (!FileUtil.checkFreeSpace(FileUtil.getRunningJar(), FileUtil.SIZE_100.longValue()) && update.isApplicable()) {
            showSpaceMessage(update);
            update.setFreeSpaceEnough(false);
        }
        if (update.isApplicable()) {
            UpdaterFormController controller = new UpdaterFormController(update, TLauncher.getInstance().getConfiguration());
            update.setUserChoose(controller.getResult());
        }
        if (update.isApplicable()) {
            update.addListener(this);
            return;
        }
        this.label.setText("autoupdater.opening");
        handleClose();
    }

    private void showSpaceMessage(Update update) {
        Alert.showMessage(Localizable.get("launcher.update.title"), Localizable.get("launcher.update.no.space").replace("disk", FileUtil.getRunningJar().toPath().getRoot().toString()));
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderStart(Downloader d, int files) {
        setCanSkip(false);
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderAbort(Downloader d) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderProgress(Downloader d, double progress, double speed, double alreadyDownloaded) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderFileComplete(Downloader d, Downloadable file) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderComplete(Downloader d) {
    }
}
