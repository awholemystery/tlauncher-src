package org.tlauncher.tlauncher.updater.client;

import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/client/PointOffer.class */
public class PointOffer {
    private String name;
    private boolean active;
    private Map<String, String> texts;

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTexts(Map<String, String> texts) {
        this.texts = texts;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof PointOffer) {
            PointOffer other = (PointOffer) o;
            if (other.canEqual(this)) {
                Object this$name = getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }
                if (isActive() != other.isActive()) {
                    return false;
                }
                Object this$texts = getTexts();
                Object other$texts = other.getTexts();
                return this$texts == null ? other$texts == null : this$texts.equals(other$texts);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PointOffer;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        int result2 = (result * 59) + (isActive() ? 79 : 97);
        Object $texts = getTexts();
        return (result2 * 59) + ($texts == null ? 43 : $texts.hashCode());
    }

    public String toString() {
        return "PointOffer(name=" + getName() + ", active=" + isActive() + ", texts=" + getTexts() + ")";
    }

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }

    public Map<String, String> getTexts() {
        return this.texts;
    }
}
