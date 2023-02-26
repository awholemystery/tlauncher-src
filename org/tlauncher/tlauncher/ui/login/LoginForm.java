package org.tlauncher.tlauncher.ui.login;

import by.gdev.util.excepiton.NotAllowWriteFileOperation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.downloader.DownloaderListener;
import org.tlauncher.tlauncher.entity.server.RemoteServer;
import org.tlauncher.tlauncher.entity.server.SiteServer;
import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
import org.tlauncher.tlauncher.listeners.auth.ValidateAccountToken;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.managers.VersionManagerListener;
import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.MainPane;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.block.BlockablePanel;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.center.CenterPanel;
import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.login.buttons.ButtonPanel;
import org.tlauncher.tlauncher.ui.login.buttons.PlayButton;
import org.tlauncher.tlauncher.ui.scenes.DefaultScene;
import org.tlauncher.tlauncher.ui.settings.SettingsPanel;
import org.tlauncher.util.U;
import org.tlauncher.util.async.LoopedThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginForm.class */
public class LoginForm extends CenterPanel implements MinecraftListener, AuthenticatorListener, VersionManagerListener, DownloaderListener {
    public static final Dimension LOGIN_SIZE = new Dimension(1050, 70);
    public static final String LOGIN_BLOCK = "login";
    public static final String REFRESH_BLOCK = "refresh";
    public static final String LAUNCH_BLOCK = "launch";
    public static final String AUTH_BLOCK = "auth";
    public static final String UPDATER_BLOCK = "update";
    public static final String DOWNLOADER_BLOCK = "download";
    public final DefaultScene scene;
    public final MainPane pane;
    public final VersionComboBox versions;
    public final PlayButton play;
    public final BlockablePanel playPanel;
    public final ButtonPanel buttons;
    public final AccountComboBox accountComboBox;
    public final VersionPanel versionPanel;
    public final AccountPanel accountPanel;
    private final List<LoginStateListener> stateListeners;
    private final List<LoginProcessListener> processListeners;
    private final SettingsPanel settings;
    private final StartThread startThread;
    private final StopThread stopThread;
    private LoginState state;
    private RemoteServer server;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginForm$LoginState.class */
    public enum LoginState {
        LAUNCHING,
        STOPPING,
        STOPPED,
        LAUNCHED
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginForm$LoginStateListener.class */
    public interface LoginStateListener {
        void loginStateChanged(LoginState loginState);
    }

    public LoginForm(DefaultScene scene) {
        super(noInsets);
        this.stateListeners = Collections.synchronizedList(new ArrayList());
        this.processListeners = Collections.synchronizedList(new ArrayList());
        this.state = LoginState.STOPPED;
        setSize(LOGIN_SIZE);
        setMaximumSize(LOGIN_SIZE);
        setOpaque(true);
        setBackground(new Color(113, 169, 76));
        this.scene = scene;
        this.pane = scene.getMainPane();
        this.settings = scene.settingsForm;
        this.startThread = new StartThread();
        this.stopThread = new StopThread();
        setLayout(new FlowLayout(0, 0, 0));
        add(Box.createHorizontalStrut(19));
        this.play = new PlayButton(this);
        this.accountPanel = new AccountPanel(this, this.global.getBoolean("chooser.type.account"));
        this.buttons = new ButtonPanel(this);
        this.playPanel = new BlockablePanel();
        this.versionPanel = new VersionPanel(this);
        this.accountPanel.setPreferredSize(new Dimension(246, 70));
        add((Component) this.accountPanel);
        add(Box.createHorizontalStrut(20));
        this.versionPanel.setPreferredSize(new Dimension(246, 70));
        add((Component) this.versionPanel);
        add(Box.createHorizontalStrut(20));
        this.playPanel.setLayout(new BorderLayout(0, 0));
        this.playPanel.setPreferredSize(new Dimension(240, 70));
        this.playPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));
        this.playPanel.add((Component) this.play);
        add((Component) this.playPanel);
        add(Box.createHorizontalStrut(19));
        this.buttons.setPreferredSize(new Dimension(240, 70));
        add((Component) this.buttons);
        this.versions = this.versionPanel.version;
        this.accountComboBox = this.accountPanel.accountComboBox;
        this.processListeners.add(this.settings);
        this.processListeners.add(this.versionPanel);
        this.processListeners.add(this.versions);
        this.processListeners.add(new ValidateAccountToken());
        this.stateListeners.add(this.play);
        this.tlauncher.getVersionManager().addListener(this);
        this.tlauncher.getDownloader().addListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runProcess() {
        LoginException error = null;
        boolean success = true;
        synchronized (this.processListeners) {
            this.processListeners.forEach(e -> {
                e.preValidate();
            });
        }
        synchronized (this.processListeners) {
            Iterator<LoginProcessListener> it = this.processListeners.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                LoginProcessListener listener = it.next();
                try {
                    listener.validatePreGameLaunch();
                } catch (LoginException loginError) {
                    log("Catched an error on a listener");
                    error = loginError;
                }
                if (error != null) {
                    log(error);
                    success = false;
                    break;
                }
            }
            if (success) {
                for (LoginProcessListener listener2 : this.processListeners) {
                    listener2.loginSucceed();
                }
            } else {
                ((HotServerManager) TLauncher.getInjector().getInstance(HotServerManager.class)).enablePopup();
                for (LoginProcessListener listener3 : this.processListeners) {
                    listener3.loginFailed();
                }
            }
        }
        if (error != null) {
            log("Login process has ended with an error.");
            return;
        }
        this.global.store();
        log("Login was OK. Trying to launch now.");
        boolean force = this.versionPanel.forceupdate.isSelected();
        changeState(LoginState.LAUNCHING);
        this.tlauncher.launch(this, this.server, force);
        this.server = null;
        this.versionPanel.forceupdate.setSelected(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopProcess() {
        while (!this.tlauncher.isLauncherWorking()) {
            log("waiting for launcher");
            U.sleepFor(500L);
        }
        changeState(LoginState.STOPPING);
        this.tlauncher.getLauncher().stop();
    }

    public void startLauncher() {
        if (Blocker.isBlocked(this)) {
            return;
        }
        this.startThread.iterate();
    }

    public void startLauncher(RemoteServer server) {
        this.server = server;
        startLauncher();
    }

    public void startLauncher(SiteServer server) {
        if (server == null) {
            log("version is null");
            return;
        }
        boolean find = false;
        int i = 0;
        while (true) {
            if (i >= this.versions.getModel().getSize()) {
                break;
            } else if (!((VersionSyncInfo) this.versions.getModel().getElementAt(i)).getID().equals(server.getVersion())) {
                i++;
            } else {
                this.versions.setSelectedValue((VersionComboBox) this.versions.getModel().getElementAt(i));
                find = true;
                break;
            }
        }
        if (!find) {
            Alert.showLocMessage("version.do.not.find");
            return;
        }
        this.server = server;
        startLauncher();
    }

    public void stopLauncher() {
        this.stopThread.iterate();
    }

    private void changeState(LoginState state) {
        if (state == null) {
            throw new NullPointerException();
        }
        if (this.state == state) {
            return;
        }
        this.state = state;
        for (LoginStateListener listener : this.stateListeners) {
            listener.loginStateChanged(state);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.BlockablePanel, org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        Blocker.block(reason, this.settings, this.play, this.versionPanel, this.accountPanel);
    }

    @Override // org.tlauncher.tlauncher.ui.block.BlockablePanel, org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        Blocker.unblock(reason, this.settings, this.play, this.versionPanel, this.accountPanel);
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderStart(Downloader d, int files) {
        Blocker.block(this, DOWNLOADER_BLOCK);
        if (this.play.getState() == PlayButton.PlayButtonState.CANCEL) {
            this.play.unblock("downloading finished");
        }
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderAbort(Downloader d) {
        Blocker.unblock(this, DOWNLOADER_BLOCK);
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderProgress(Downloader d, double progress, double speed, double alreadyDownloaded) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderFileComplete(Downloader d, Downloadable file) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderComplete(Downloader d) {
        Blocker.unblock(this, DOWNLOADER_BLOCK);
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshing(VersionManager manager) {
        Blocker.block(this, "refresh");
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshingFailed(VersionManager manager) {
        Blocker.unblock(this, "refresh");
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshed(VersionManager manager) {
        Blocker.unblock(this, "refresh");
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassing(Authenticator auth) {
        Blocker.block(this, AUTH_BLOCK);
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassingError(Authenticator auth, Exception e) {
        Blocker.unblock(this, AUTH_BLOCK);
        Throwable cause = e.getCause();
        if (cause != null && (e.getCause() instanceof IOException)) {
            return;
        }
        throw new LoginException("Cannot auth!");
    }

    @Override // org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener
    public void onAuthPassed(Authenticator auth) {
        Blocker.unblock(this, AUTH_BLOCK);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftPrepare() {
        Blocker.block(this, LAUNCH_BLOCK);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftAbort() {
        Blocker.unblock(this, LAUNCH_BLOCK);
        changeState(LoginState.STOPPED);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftLaunch() {
        changeState(LoginState.LAUNCHED);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftClose() {
        Blocker.unblock(this, LAUNCH_BLOCK);
        changeState(LoginState.STOPPED);
        this.tlauncher.getVersionManager().startRefresh(true);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftError(Throwable e) {
        Blocker.unblock(this, LAUNCH_BLOCK);
        changeState(LoginState.STOPPED);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftKnownError(MinecraftException e) {
        Blocker.unblock(this, LAUNCH_BLOCK);
        changeState(LoginState.STOPPED);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftCrash(Crash crash) {
        Blocker.unblock(this, LAUNCH_BLOCK);
        changeState(LoginState.STOPPED);
    }

    public void removeLoginProcessListener(LoginProcessListener listener) {
        this.processListeners.remove(listener);
    }

    public void addLoginProcessListener(LoginProcessListener listener) {
        this.processListeners.add(listener);
    }

    public void addLoginProcessListener(LoginProcessListener listener, int position) {
        this.processListeners.add(position, listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginForm$StartThread.class */
    public class StartThread extends LoopedThread {
        StartThread() {
            startAndWait();
        }

        @Override // org.tlauncher.util.async.LoopedThread
        protected void iterateOnce() {
            try {
                LoginForm.this.runProcess();
            } catch (Throwable t) {
                if (t instanceof NotAllowWriteFileOperation) {
                    Alert.showErrorHtml("auth.error.title", Localizable.get("auth.error.can.not.write", t.getMessage()));
                } else {
                    Alert.showError(t);
                }
            }
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/LoginForm$StopThread.class */
    class StopThread extends LoopedThread {
        StopThread() {
            startAndWait();
        }

        @Override // org.tlauncher.util.async.LoopedThread
        protected void iterateOnce() {
            try {
                LoginForm.this.stopProcess();
            } catch (Throwable t) {
                Alert.showError(t);
            }
        }
    }
}
