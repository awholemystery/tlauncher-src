package org.tlauncher.tlauncher.ui.login.buttons;

import java.awt.Color;
import net.minecraft.launcher.updater.VersionSyncInfo;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.button.RoundImageButton;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.login.LoginForm;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/PlayButton.class */
public class PlayButton extends RoundImageButton implements Blockable, LoginForm.LoginStateListener {
    private static final long serialVersionUID = 6944074583143406549L;
    private PlayButtonState state;
    private final LoginForm loginForm;

    public PlayButton(LoginForm lf) {
        super(ImageCache.getImage("play.png"), ImageCache.getImage("play-active.png"));
        setForeground(Color.WHITE);
        this.loginForm = lf;
        addActionListener(e -> {
            switch (this.state) {
                case CANCEL:
                    this.loginForm.stopLauncher();
                    return;
                default:
                    this.loginForm.startLauncher();
                    return;
            }
        });
        setFont(getFont().deriveFont(1).deriveFont(18.0f));
        setState(PlayButtonState.PLAY);
    }

    public PlayButtonState getState() {
        return this.state;
    }

    public void setState(PlayButtonState state) {
        if (state == null) {
            throw new NullPointerException();
        }
        this.state = state;
        setText(state.getPath());
        if (state == PlayButtonState.CANCEL) {
            setEnabled(true);
        }
    }

    public void updateState() {
        VersionSyncInfo vs;
        if (this.loginForm.versions == null || (vs = this.loginForm.versions.getVersion()) == null) {
            return;
        }
        boolean installed = vs.isInstalled();
        boolean force = this.loginForm.versionPanel.forceupdate.getState();
        if (!installed) {
            setState(PlayButtonState.INSTALL);
        } else {
            setState(force ? PlayButtonState.REINSTALL : PlayButtonState.PLAY);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/PlayButton$PlayButtonState.class */
    public enum PlayButtonState {
        REINSTALL("loginform.enter.reinstall"),
        INSTALL("loginform.enter.install"),
        PLAY("loginform.enter"),
        CANCEL("loginform.enter.cancel");
        
        private final String path;

        PlayButtonState(String path) {
            this.path = path;
        }

        public String getPath() {
            return this.path;
        }
    }

    @Override // org.tlauncher.tlauncher.ui.login.LoginForm.LoginStateListener
    public void loginStateChanged(LoginForm.LoginState state) {
        if (state == LoginForm.LoginState.LAUNCHING) {
            setState(PlayButtonState.CANCEL);
            setEnabled(false);
            return;
        }
        updateState();
        setEnabled(!Blocker.isBlocked(this));
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        if (this.state != PlayButtonState.CANCEL) {
            setEnabled(false);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        setEnabled(true);
    }
}
