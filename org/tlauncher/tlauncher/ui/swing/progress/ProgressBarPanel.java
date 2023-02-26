package org.tlauncher.tlauncher.ui.swing.progress;

import ch.qos.logback.core.CoreConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.nio.file.Paths;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.downloader.DownloaderListener;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
import org.tlauncher.tlauncher.ui.login.LoginException;
import org.tlauncher.tlauncher.ui.progress.login.LauncherProgress;
import org.tlauncher.tlauncher.ui.progress.login.LauncherProgressListener;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.MinecraftUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/progress/ProgressBarPanel.class */
public class ProgressBarPanel extends JLayeredPane implements LocalizableComponent, DownloaderListener, MinecraftExtendedListener, LauncherProgressListener, LoginProcessListener {
    private static final long serialVersionUID = 4005627356304541408L;
    public static final Dimension SIZE = new Dimension(1050, 24);
    private final JLabel percentLabel;
    private final JLabel centerLabel;
    private final JLabel speedLabel;
    private final JLabel fileLabel;
    private final ExtendedPanel upperPanel;
    private JProgressBar bar;
    private boolean downloadingStart;

    public ProgressBarPanel(Icon speed, Icon file, LauncherProgress bar) {
        this.bar = bar;
        setVisible(false);
        this.upperPanel = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
        this.speedLabel = createLabel(speed);
        this.speedLabel.setHorizontalTextPosition(4);
        this.fileLabel = createLabel(file);
        this.fileLabel.setHorizontalTextPosition(4);
        this.percentLabel = createLabel(null);
        this.centerLabel = createLabel(null);
        this.centerLabel.setHorizontalAlignment(0);
        this.percentLabel.setFont(this.percentLabel.getFont().deriveFont(1));
        this.upperPanel.setPreferredSize(SIZE);
        this.upperPanel.setInsets(0, 20, 0, 20);
        setPreferredSize(SIZE);
        Component extendedPanel = new ExtendedPanel();
        extendedPanel.setLayout(new BoxLayout(extendedPanel, 0));
        extendedPanel.add(this.speedLabel);
        extendedPanel.add(Box.createHorizontalStrut(20));
        extendedPanel.add(this.fileLabel);
        this.upperPanel.add((Component) this.centerLabel, (Object) "Center");
        this.upperPanel.add((Component) this.percentLabel, (Object) "East");
        this.upperPanel.add(extendedPanel, "West");
        add(bar, 1);
        add(this.upperPanel, 0);
        bar.setBounds(0, 0, SIZE.width, SIZE.height);
        this.upperPanel.setBounds(0, 0, SIZE.width, SIZE.height);
    }

    private JLabel createLabel(Icon icon) {
        JLabel jLabel = new JLabel();
        jLabel.setFont(jLabel.getFont().deriveFont(0, 12.0f));
        if (icon != null) {
            jLabel.setIcon(icon);
        }
        jLabel.setHorizontalTextPosition(4);
        return jLabel;
    }

    private void startProgress() {
        clean();
        this.centerLabel.setText(Localizable.get("progress.bar.panel.init"));
        this.downloadingStart = true;
        setVisible(true);
    }

    private void stopProgress() {
        clean();
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderStart(Downloader d, int files) {
        startProgress();
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderAbort(Downloader d) {
        stopProgress();
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderProgress(Downloader d, double progress, double speed, double remainedData) {
        updateStateDownloading(d, progress, speed, remainedData);
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderFileComplete(Downloader d, Downloadable file) {
        updateStateDownloading(d, d.getProgress(), d.getSpeed(), d.getRemainedData());
    }

    private void updateStateDownloading(Downloader d, double progress, double speed, double remainedData) {
        int remainingTime = (int) (remainedData / speed);
        this.bar.setValue((int) progress);
        StringBuilder b = new StringBuilder();
        b.append(Localizable.get("progress.bar.panel.file.remained")).append(" ").append(d.getRemaining()).append(" ").append(Localizable.get("progress.bar.panel.file")).append(" ").append((int) Math.ceil(remainedData)).append(" ").append(Localizable.get("progress.bar.panel.size")).append(", ").append(Localizable.get("progress.bar.panel.remaining.time")).append(" ");
        if (remainingTime < 60) {
            b.append(Localizable.get("progress.bar.panel.remaining.time.less.than.minute"));
        } else {
            b.append((int) Math.ceil(remainingTime / 60.0d)).append(" ").append(Localizable.get("progress.bar.panel.remaining.time.minutes"));
        }
        this.fileLabel.setText(b.toString());
        b.setLength(0);
        b.append(Localizable.get("progress.bar.panel.speed"));
        if (speed < 1.0d) {
            b.append(" ").append(String.format("%.2f", Double.valueOf(speed * 1024.0d))).append(" ").append(Localizable.get("progress.bar.panel.speed.unity.KB"));
        } else {
            b.append(" ").append(String.format("%.2f", Double.valueOf(speed))).append(" ").append(Localizable.get("progress.bar.panel.speed.unity.MB"));
        }
        this.speedLabel.setText(b.toString());
        this.percentLabel.setText(((int) progress) + "%");
        if (this.downloadingStart) {
            this.centerLabel.setVisible(false);
            this.fileLabel.setVisible(true);
            this.speedLabel.setVisible(true);
            this.downloadingStart = false;
        }
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderComplete(Downloader d) {
        stopProgress();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftPrepare() {
        startProgress();
        this.centerLabel.setVisible(true);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftAbort() {
        stopProgress();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftClose() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftError(Throwable e) {
        stopProgress();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftKnownError(MinecraftException e) {
        stopProgress();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftCrash(Crash crash) {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onMinecraftCollecting() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onMinecraftComparingAssets() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onMinecraftDownloading() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onMinecraftReconstructingAssets() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onMinecraftUnpackingNatives() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onMinecraftDeletingEntries() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onMinecraftConstructing() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener, org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftLaunch() {
        stopProgress();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onMinecraftPostLaunch() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener
    public void onWorldBackup() {
        this.centerLabel.setText(Localizable.get("settings.backup.process").concat(" ").concat(String.valueOf(Paths.get(MinecraftUtil.getWorkingDirectory().toString(), PathAppUtil.BACKUP_DIRECTORY))));
    }

    private void clean() {
        this.speedLabel.setVisible(false);
        this.fileLabel.setVisible(false);
        this.speedLabel.setText(CoreConstants.EMPTY_STRING);
        this.fileLabel.setText(CoreConstants.EMPTY_STRING);
        this.percentLabel.setText(CoreConstants.EMPTY_STRING);
        this.bar.setValue(0);
        setVisible(false);
    }

    public JProgressBar getBar() {
        return this.bar;
    }

    public void setBar(JProgressBar bar) {
        this.bar = bar;
    }

    @Override // org.tlauncher.tlauncher.ui.progress.login.LauncherProgressListener
    public void repaintPanel() {
        this.upperPanel.repaint();
    }

    @Override // org.tlauncher.tlauncher.listeners.auth.LoginProcessListener
    public void preValidate() throws LoginException {
        SwingUtilities.invokeLater(() -> {
            clean();
            this.centerLabel.setText(Localizable.get("progress.bar.panel.validate.account.token"));
            this.downloadingStart = true;
            setVisible(true);
        });
    }

    @Override // org.tlauncher.tlauncher.listeners.auth.LoginProcessListener
    public void loginFailed() {
        SwingUtilities.invokeLater(() -> {
            stopProgress();
        });
    }
}
