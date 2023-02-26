package org.tlauncher.tlauncher.ui;

import java.awt.Image;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.listener.UpdateUIListener;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.swing.ImagePanel;
import org.tlauncher.tlauncher.updater.client.Update;
import org.tlauncher.tlauncher.updater.client.Updater;
import org.tlauncher.tlauncher.updater.client.UpdaterListener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/SideNotifier.class */
public class SideNotifier extends ImagePanel implements UpdaterListener {
    private static final String LANG_PREFIX = "notifier.";
    private NotifierStatus status;
    private Update update;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SideNotifier() {
        super((Image) null, 1.0f, 0.75f, false);
        TLauncher.getInstance().getUpdater().addListener(this);
    }

    public NotifierStatus getStatus() {
        return this.status;
    }

    public void setStatus(NotifierStatus status) {
        if (status == null) {
            throw new NullPointerException();
        }
        this.status = status;
        setImage(status.getImage());
        if (status != NotifierStatus.NONE) {
            show();
        } else {
            hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.tlauncher.tlauncher.ui.swing.ImagePanel
    public boolean onClick() {
        boolean result = processClick();
        if (result) {
            hide();
        }
        return result;
    }

    private boolean processClick() {
        if (!super.onClick()) {
            return false;
        }
        switch (this.status) {
            case FAILED:
                Alert.showWarning(Localizable.get("notifier.failed.title"), Localizable.get("notifier.failed"));
                return true;
            case FOUND:
                if (this.update == null) {
                    throw new IllegalStateException("Update is NULL!");
                }
                String prefix = LANG_PREFIX + this.status.toString() + ".";
                String title = prefix + "title";
                String question = prefix + "question";
                boolean ask = Alert.showQuestion(Localizable.get(title), Localizable.get(question, Double.valueOf(this.update.getVersion())), this.update.getDescription());
                if (!ask) {
                    return false;
                }
                UpdateUIListener listener = new UpdateUIListener(this.update);
                listener.push();
                return true;
            case NONE:
                return true;
            default:
                throw new IllegalStateException("Unknown status: " + this.status);
        }
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterRequesting(Updater u) {
        setFoundUpdate(null);
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterErrored(Updater.SearchFailed failed) {
        setStatus(NotifierStatus.FAILED);
    }

    @Override // org.tlauncher.tlauncher.updater.client.UpdaterListener
    public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
        Update update = succeeded.getResponse();
        setFoundUpdate(update.isApplicable() ? update : null);
    }

    private void setFoundUpdate(Update upd) {
        this.update = upd;
        setStatus(upd == null ? NotifierStatus.NONE : NotifierStatus.FOUND);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/SideNotifier$NotifierStatus.class */
    public enum NotifierStatus {
        FAILED("warning.png"),
        FOUND("down32.png"),
        NONE;
        
        private final Image image;

        NotifierStatus(String imagePath) {
            this.image = imagePath == null ? null : ImageCache.getImage(imagePath);
        }

        NotifierStatus() {
            this(null);
        }

        public Image getImage() {
            return this.image;
        }

        @Override // java.lang.Enum
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
