package org.tlauncher.tlauncher.ui.login.buttons;

import java.awt.Image;
import org.tlauncher.tlauncher.managers.ComponentManager;
import org.tlauncher.tlauncher.managers.ComponentManagerListener;
import org.tlauncher.tlauncher.managers.ComponentManagerListenerHelper;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.block.Blockable;
import org.tlauncher.tlauncher.ui.block.Blocker;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.tlauncher.updater.client.Updater;
import org.tlauncher.tlauncher.updater.client.UpdaterListener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/login/buttons/RefreshButton.class */
public class RefreshButton extends MainImageButton implements Blockable, ComponentManagerListener, UpdaterListener {
    private static final long serialVersionUID = -1334187593288746348L;
    private static final int TYPE_REFRESH = 0;
    private static final int TYPE_CANCEL = 1;
    private LoginForm lf;
    private int type;
    private final Image refresh;
    private final Image cancel;
    private Updater updaterFlag;

    private RefreshButton(LoginForm loginform, int type) {
        super(DARK_GREEN_COLOR, "refresh-mouse-under.png", "refresh-gray.png");
        this.refresh = ImageCache.getImage("refresh-mouse-under.png");
        this.cancel = ImageCache.getImage("cancel.png");
        this.image = this.refresh;
        this.lf = loginform;
        setType(type, false);
        addActionListener(e -> {
            onPressButton();
        });
        ((ComponentManagerListenerHelper) TLauncher.getInstance().getManager().getComponent(ComponentManagerListenerHelper.class)).addListener(this);
        TLauncher.getInstance().getUpdater().addListener(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RefreshButton(LoginForm loginform) {
        this(loginform, 0);
    }

    private void onPressButton() {
        switch (this.type) {
            case 0:
                if (this.updaterFlag != null) {
                    this.updaterFlag.asyncFindUpdate();
                }
                TLauncher.getInstance().getManager().startAsyncRefresh();
                break;
            case 1:
                TLauncher.getInstance().getManager().stopRefresh();
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + this.type + ". Use RefreshButton.TYPE_* constants.");
        }
        this.lf.defocus();
    }

    void setType(int type) {
        setType(type, true);
    }

    private void setType(int type, boolean repaint) {
        Image image;
        switch (type) {
            case 0:
                image = this.refresh;
                break;
            case 1:
                image = this.cancel;
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type + ". Use RefreshButton.TYPE_* constants.");
        }
        this.type = type;
        this.image = image;
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterRequesting(Updater u) {
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterErrored(Updater.SearchFailed failed) {
        this.updaterFlag = failed.getUpdater();
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
        this.updaterFlag = null;
    }

    @Override // org.tlauncher.tlauncher.managers.ComponentManagerListener
    public void onComponentsRefreshing(ComponentManager manager) {
        Blocker.block(this, "refresh");
    }

    @Override // org.tlauncher.tlauncher.managers.ComponentManagerListener
    public void onComponentsRefreshed(ComponentManager manager) {
        Blocker.unblock(this, "refresh");
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void block(Object reason) {
        if (reason.equals("refresh")) {
            setType(1);
        } else {
            setEnabled(false);
        }
    }

    @Override // org.tlauncher.tlauncher.ui.block.Blockable
    public void unblock(Object reason) {
        if (reason.equals("refresh")) {
            setType(0);
        }
        setEnabled(true);
    }
}
