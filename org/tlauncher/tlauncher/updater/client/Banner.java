package org.tlauncher.tlauncher.updater.client;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Banner.class */
public class Banner {
    private String image;
    private String clickLink;

    public void setImage(String image) {
        this.image = image;
    }

    public void setClickLink(String clickLink) {
        this.clickLink = clickLink;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Banner) {
            Banner other = (Banner) o;
            if (other.canEqual(this)) {
                Object this$image = getImage();
                Object other$image = other.getImage();
                if (this$image == null) {
                    if (other$image != null) {
                        return false;
                    }
                } else if (!this$image.equals(other$image)) {
                    return false;
                }
                Object this$clickLink = getClickLink();
                Object other$clickLink = other.getClickLink();
                return this$clickLink == null ? other$clickLink == null : this$clickLink.equals(other$clickLink);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Banner;
    }

    public int hashCode() {
        Object $image = getImage();
        int result = (1 * 59) + ($image == null ? 43 : $image.hashCode());
        Object $clickLink = getClickLink();
        return (result * 59) + ($clickLink == null ? 43 : $clickLink.hashCode());
    }

    public String toString() {
        return "Banner(image=" + getImage() + ", clickLink=" + getClickLink() + ")";
    }

    public String getImage() {
        return this.image;
    }

    public String getClickLink() {
        return this.clickLink;
    }
}
