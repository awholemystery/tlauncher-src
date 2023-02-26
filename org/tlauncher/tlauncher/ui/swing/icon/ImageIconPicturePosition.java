package org.tlauncher.tlauncher.ui.swing.icon;

import javax.swing.ImageIcon;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/icon/ImageIconPicturePosition.class */
public class ImageIconPicturePosition extends ImageIcon {
    private static final long serialVersionUID = 4790815662595114089L;
    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public String toString() {
        return "ImageIconPicturePosition(position=" + getPosition() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ImageIconPicturePosition) {
            ImageIconPicturePosition other = (ImageIconPicturePosition) o;
            return other.canEqual(this) && super/*java.lang.Object*/.equals(o) && getPosition() == other.getPosition();
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ImageIconPicturePosition;
    }

    public int hashCode() {
        int result = super/*java.lang.Object*/.hashCode();
        return (result * 59) + getPosition();
    }

    public int getPosition() {
        return this.position;
    }

    public ImageIconPicturePosition(byte[] imageData, int position) {
        super(imageData);
        this.position = position;
    }
}
