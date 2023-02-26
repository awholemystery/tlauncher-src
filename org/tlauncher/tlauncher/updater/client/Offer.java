package org.tlauncher.tlauncher.updater.client;

import java.util.List;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/Offer.class */
public class Offer {
    private String client;
    private String offer;
    private String installer;
    private int startCheckboxSouth;
    private List<PointOffer> checkBoxes;
    private Map<String, String> topText;
    private Map<String, String> downText;
    private Map<String, String> args;

    public void setClient(String client) {
        this.client = client;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public void setInstaller(String installer) {
        this.installer = installer;
    }

    public void setStartCheckboxSouth(int startCheckboxSouth) {
        this.startCheckboxSouth = startCheckboxSouth;
    }

    public void setCheckBoxes(List<PointOffer> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public void setTopText(Map<String, String> topText) {
        this.topText = topText;
    }

    public void setDownText(Map<String, String> downText) {
        this.downText = downText;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Offer) {
            Offer other = (Offer) o;
            if (other.canEqual(this)) {
                Object this$client = getClient();
                Object other$client = other.getClient();
                if (this$client == null) {
                    if (other$client != null) {
                        return false;
                    }
                } else if (!this$client.equals(other$client)) {
                    return false;
                }
                Object this$offer = getOffer();
                Object other$offer = other.getOffer();
                if (this$offer == null) {
                    if (other$offer != null) {
                        return false;
                    }
                } else if (!this$offer.equals(other$offer)) {
                    return false;
                }
                Object this$installer = getInstaller();
                Object other$installer = other.getInstaller();
                if (this$installer == null) {
                    if (other$installer != null) {
                        return false;
                    }
                } else if (!this$installer.equals(other$installer)) {
                    return false;
                }
                if (getStartCheckboxSouth() != other.getStartCheckboxSouth()) {
                    return false;
                }
                Object this$checkBoxes = getCheckBoxes();
                Object other$checkBoxes = other.getCheckBoxes();
                if (this$checkBoxes == null) {
                    if (other$checkBoxes != null) {
                        return false;
                    }
                } else if (!this$checkBoxes.equals(other$checkBoxes)) {
                    return false;
                }
                Object this$topText = getTopText();
                Object other$topText = other.getTopText();
                if (this$topText == null) {
                    if (other$topText != null) {
                        return false;
                    }
                } else if (!this$topText.equals(other$topText)) {
                    return false;
                }
                Object this$downText = getDownText();
                Object other$downText = other.getDownText();
                if (this$downText == null) {
                    if (other$downText != null) {
                        return false;
                    }
                } else if (!this$downText.equals(other$downText)) {
                    return false;
                }
                Object this$args = getArgs();
                Object other$args = other.getArgs();
                return this$args == null ? other$args == null : this$args.equals(other$args);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Offer;
    }

    public int hashCode() {
        Object $client = getClient();
        int result = (1 * 59) + ($client == null ? 43 : $client.hashCode());
        Object $offer = getOffer();
        int result2 = (result * 59) + ($offer == null ? 43 : $offer.hashCode());
        Object $installer = getInstaller();
        int result3 = (((result2 * 59) + ($installer == null ? 43 : $installer.hashCode())) * 59) + getStartCheckboxSouth();
        Object $checkBoxes = getCheckBoxes();
        int result4 = (result3 * 59) + ($checkBoxes == null ? 43 : $checkBoxes.hashCode());
        Object $topText = getTopText();
        int result5 = (result4 * 59) + ($topText == null ? 43 : $topText.hashCode());
        Object $downText = getDownText();
        int result6 = (result5 * 59) + ($downText == null ? 43 : $downText.hashCode());
        Object $args = getArgs();
        return (result6 * 59) + ($args == null ? 43 : $args.hashCode());
    }

    public String toString() {
        return "Offer(client=" + getClient() + ", offer=" + getOffer() + ", installer=" + getInstaller() + ", startCheckboxSouth=" + getStartCheckboxSouth() + ", checkBoxes=" + getCheckBoxes() + ", topText=" + getTopText() + ", downText=" + getDownText() + ", args=" + getArgs() + ")";
    }

    public String getClient() {
        return this.client;
    }

    public String getOffer() {
        return this.offer;
    }

    public String getInstaller() {
        return this.installer;
    }

    public int getStartCheckboxSouth() {
        return this.startCheckboxSouth;
    }

    public List<PointOffer> getCheckBoxes() {
        return this.checkBoxes;
    }

    public Map<String, String> getTopText() {
        return this.topText;
    }

    public Map<String, String> getDownText() {
        return this.downText;
    }

    public Map<String, String> getArgs() {
        return this.args;
    }
}
