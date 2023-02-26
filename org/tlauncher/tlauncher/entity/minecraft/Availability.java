package org.tlauncher.tlauncher.entity.minecraft;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/minecraft/Availability.class */
public class Availability {
    private Integer group;
    private Integer progress;

    public void setGroup(Integer group) {
        this.group = group;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Availability) {
            Availability other = (Availability) o;
            if (other.canEqual(this)) {
                Object this$group = getGroup();
                Object other$group = other.getGroup();
                if (this$group == null) {
                    if (other$group != null) {
                        return false;
                    }
                } else if (!this$group.equals(other$group)) {
                    return false;
                }
                Object this$progress = getProgress();
                Object other$progress = other.getProgress();
                return this$progress == null ? other$progress == null : this$progress.equals(other$progress);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Availability;
    }

    public int hashCode() {
        Object $group = getGroup();
        int result = (1 * 59) + ($group == null ? 43 : $group.hashCode());
        Object $progress = getProgress();
        return (result * 59) + ($progress == null ? 43 : $progress.hashCode());
    }

    public String toString() {
        return "Availability(group=" + getGroup() + ", progress=" + getProgress() + ")";
    }

    public Integer getGroup() {
        return this.group;
    }

    public Integer getProgress() {
        return this.progress;
    }
}
