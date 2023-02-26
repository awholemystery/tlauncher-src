package org.tlauncher.tlauncher.ui.loc;

import ch.qos.logback.core.CoreConstants;
import java.awt.event.ItemListener;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import org.tlauncher.tlauncher.ui.TLauncherFrame;
import org.tlauncher.tlauncher.ui.images.ImageCache;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableCheckbox.class */
public class LocalizableCheckbox extends JCheckBox implements LocalizableComponent {
    private static final long serialVersionUID = 1;
    private String path;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableCheckbox$PANEL_TYPE.class */
    public enum PANEL_TYPE {
        SETTINGS,
        LOGIN
    }

    public LocalizableCheckbox(String path) {
        init(PANEL_TYPE.LOGIN);
        setLabel(path);
    }

    public LocalizableCheckbox(String path, boolean state) {
        super(CoreConstants.EMPTY_STRING, state);
        init(PANEL_TYPE.LOGIN);
        setText(path);
    }

    public LocalizableCheckbox(String path2, PANEL_TYPE settings) {
        setText(path2);
        init(settings);
    }

    @Deprecated
    public void setLabel(String path) {
        setText(path);
    }

    public void setText(String path) {
        this.path = path;
        super.setText(Localizable.get() == null ? path : Localizable.get().get(path));
    }

    public String getLangPath() {
        return this.path;
    }

    public boolean getState() {
        return super.getModel().isSelected();
    }

    public void setState(boolean state) {
        super.getModel().setSelected(state);
    }

    public void addListener(ItemListener l) {
        super.getModel().addItemListener(l);
    }

    public void removeListener(ItemListener l) {
        super.getModel().removeItemListener(l);
    }

    @Override // org.tlauncher.tlauncher.ui.loc.LocalizableComponent
    public void updateLocale() {
        setLabel(this.path);
    }

    protected void init(PANEL_TYPE panel) {
        Icon off = null;
        Icon on = null;
        switch (panel) {
            case SETTINGS:
                on = ImageCache.getNativeIcon("settings-check-box-on.png");
                off = ImageCache.getNativeIcon("settings-check-box-off.png");
                break;
            case LOGIN:
                on = ImageCache.getNativeIcon("checkbox-on.png");
                off = ImageCache.getNativeIcon("checkbox-off.png");
                break;
        }
        setFont(getFont().deriveFont(TLauncherFrame.fontSize));
        setOpaque(false);
        setIcon(off);
        setSelectedIcon(on);
        setDisabledIcon(off);
        setDisabledSelectedIcon(on);
        setPressedIcon(on);
    }
}
