package org.tlauncher.tlauncher.ui.loc;

import java.awt.event.ItemListener;
import javax.swing.JRadioButton;
import org.tlauncher.tlauncher.ui.images.ImageCache;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/LocalizableRadioButton.class */
public class LocalizableRadioButton extends JRadioButton implements LocalizableComponent {
    private static final long serialVersionUID = 1;
    private String path;

    public LocalizableRadioButton() {
        init();
    }

    public LocalizableRadioButton(String path) {
        init();
        setLabel(path);
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

    private void init() {
        setOpaque(false);
        setIcon(ImageCache.getNativeIcon("radio-button-off.png"));
        setSelectedIcon(ImageCache.getNativeIcon("radio-button-on.png"));
        setDisabledIcon(ImageCache.getNativeIcon("radio-button-off.png"));
        setDisabledSelectedIcon(ImageCache.getNativeIcon("radio-button-on.png"));
        setPressedIcon(ImageCache.getNativeIcon("radio-button-on.png"));
    }
}
