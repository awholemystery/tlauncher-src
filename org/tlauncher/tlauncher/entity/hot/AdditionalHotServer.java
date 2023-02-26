package org.tlauncher.tlauncher.entity.hot;

import java.awt.image.BufferedImage;
import java.util.Date;
import org.tlauncher.tlauncher.entity.ServerInfo;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/hot/AdditionalHotServer.class */
public class AdditionalHotServer extends ServerInfo {
    private String shortDescription;
    private String addDescription;
    private String versionDescription;
    private String tMonitoringLink;
    private BufferedImage image;
    private Integer online = -1;
    private Integer max = -1;
    private Date updated;
    private Integer imageNumber;
    private boolean active;

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setAddDescription(String addDescription) {
        this.addDescription = addDescription;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public void setTMonitoringLink(String tMonitoringLink) {
        this.tMonitoringLink = tMonitoringLink;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setImageNumber(Integer imageNumber) {
        this.imageNumber = imageNumber;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override // org.tlauncher.tlauncher.entity.ServerInfo
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AdditionalHotServer) {
            AdditionalHotServer other = (AdditionalHotServer) o;
            if (other.canEqual(this)) {
                Object this$shortDescription = getShortDescription();
                Object other$shortDescription = other.getShortDescription();
                if (this$shortDescription == null) {
                    if (other$shortDescription != null) {
                        return false;
                    }
                } else if (!this$shortDescription.equals(other$shortDescription)) {
                    return false;
                }
                Object this$addDescription = getAddDescription();
                Object other$addDescription = other.getAddDescription();
                if (this$addDescription == null) {
                    if (other$addDescription != null) {
                        return false;
                    }
                } else if (!this$addDescription.equals(other$addDescription)) {
                    return false;
                }
                Object this$versionDescription = getVersionDescription();
                Object other$versionDescription = other.getVersionDescription();
                if (this$versionDescription == null) {
                    if (other$versionDescription != null) {
                        return false;
                    }
                } else if (!this$versionDescription.equals(other$versionDescription)) {
                    return false;
                }
                Object this$tMonitoringLink = getTMonitoringLink();
                Object other$tMonitoringLink = other.getTMonitoringLink();
                if (this$tMonitoringLink == null) {
                    if (other$tMonitoringLink != null) {
                        return false;
                    }
                } else if (!this$tMonitoringLink.equals(other$tMonitoringLink)) {
                    return false;
                }
                Object this$image = getImage();
                Object other$image = other.getImage();
                if (this$image == null) {
                    if (other$image != null) {
                        return false;
                    }
                } else if (!this$image.equals(other$image)) {
                    return false;
                }
                Object this$online = getOnline();
                Object other$online = other.getOnline();
                if (this$online == null) {
                    if (other$online != null) {
                        return false;
                    }
                } else if (!this$online.equals(other$online)) {
                    return false;
                }
                Object this$max = getMax();
                Object other$max = other.getMax();
                if (this$max == null) {
                    if (other$max != null) {
                        return false;
                    }
                } else if (!this$max.equals(other$max)) {
                    return false;
                }
                Object this$updated = getUpdated();
                Object other$updated = other.getUpdated();
                if (this$updated == null) {
                    if (other$updated != null) {
                        return false;
                    }
                } else if (!this$updated.equals(other$updated)) {
                    return false;
                }
                Object this$imageNumber = getImageNumber();
                Object other$imageNumber = other.getImageNumber();
                if (this$imageNumber == null) {
                    if (other$imageNumber != null) {
                        return false;
                    }
                } else if (!this$imageNumber.equals(other$imageNumber)) {
                    return false;
                }
                return isActive() == other.isActive();
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.tlauncher.entity.ServerInfo
    protected boolean canEqual(Object other) {
        return other instanceof AdditionalHotServer;
    }

    @Override // org.tlauncher.tlauncher.entity.ServerInfo
    public int hashCode() {
        Object $shortDescription = getShortDescription();
        int result = (1 * 59) + ($shortDescription == null ? 43 : $shortDescription.hashCode());
        Object $addDescription = getAddDescription();
        int result2 = (result * 59) + ($addDescription == null ? 43 : $addDescription.hashCode());
        Object $versionDescription = getVersionDescription();
        int result3 = (result2 * 59) + ($versionDescription == null ? 43 : $versionDescription.hashCode());
        Object $tMonitoringLink = getTMonitoringLink();
        int result4 = (result3 * 59) + ($tMonitoringLink == null ? 43 : $tMonitoringLink.hashCode());
        Object $image = getImage();
        int result5 = (result4 * 59) + ($image == null ? 43 : $image.hashCode());
        Object $online = getOnline();
        int result6 = (result5 * 59) + ($online == null ? 43 : $online.hashCode());
        Object $max = getMax();
        int result7 = (result6 * 59) + ($max == null ? 43 : $max.hashCode());
        Object $updated = getUpdated();
        int result8 = (result7 * 59) + ($updated == null ? 43 : $updated.hashCode());
        Object $imageNumber = getImageNumber();
        return (((result8 * 59) + ($imageNumber == null ? 43 : $imageNumber.hashCode())) * 59) + (isActive() ? 79 : 97);
    }

    @Override // org.tlauncher.tlauncher.entity.ServerInfo
    public String toString() {
        return "AdditionalHotServer(super=" + super.toString() + ", shortDescription=" + getShortDescription() + ", addDescription=" + getAddDescription() + ", versionDescription=" + getVersionDescription() + ", tMonitoringLink=" + getTMonitoringLink() + ", image=" + getImage() + ", online=" + getOnline() + ", max=" + getMax() + ", updated=" + getUpdated() + ", imageNumber=" + getImageNumber() + ", active=" + isActive() + ")";
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getAddDescription() {
        return this.addDescription;
    }

    public String getVersionDescription() {
        return this.versionDescription;
    }

    public String getTMonitoringLink() {
        return this.tMonitoringLink;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public Integer getOnline() {
        return this.online;
    }

    public Integer getMax() {
        return this.max;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public Integer getImageNumber() {
        return this.imageNumber;
    }

    public boolean isActive() {
        return this.active;
    }
}
