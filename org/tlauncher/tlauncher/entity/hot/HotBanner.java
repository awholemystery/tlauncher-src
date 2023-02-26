package org.tlauncher.tlauncher.entity.hot;

import org.tlauncher.tlauncher.updater.client.Banner;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/hot/HotBanner.class */
public class HotBanner extends Banner {
    private String mouseOnImage;

    public void setMouseOnImage(String mouseOnImage) {
        this.mouseOnImage = mouseOnImage;
    }

    @Override // org.tlauncher.tlauncher.updater.client.Banner
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof HotBanner) {
            HotBanner other = (HotBanner) o;
            if (other.canEqual(this)) {
                Object this$mouseOnImage = getMouseOnImage();
                Object other$mouseOnImage = other.getMouseOnImage();
                return this$mouseOnImage == null ? other$mouseOnImage == null : this$mouseOnImage.equals(other$mouseOnImage);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.tlauncher.updater.client.Banner
    protected boolean canEqual(Object other) {
        return other instanceof HotBanner;
    }

    @Override // org.tlauncher.tlauncher.updater.client.Banner
    public int hashCode() {
        Object $mouseOnImage = getMouseOnImage();
        int result = (1 * 59) + ($mouseOnImage == null ? 43 : $mouseOnImage.hashCode());
        return result;
    }

    @Override // org.tlauncher.tlauncher.updater.client.Banner
    public String toString() {
        return "HotBanner(mouseOnImage=" + getMouseOnImage() + ")";
    }

    public String getMouseOnImage() {
        return this.mouseOnImage;
    }
}
