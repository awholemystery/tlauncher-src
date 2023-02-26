package org.tlauncher.tlauncher.entity.minecraft;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/minecraft/JavaVersion.class */
public class JavaVersion {
    private String name;
    private String released;

    public void setName(String name) {
        this.name = name;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JavaVersion) {
            JavaVersion other = (JavaVersion) o;
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
                Object this$released = getReleased();
                Object other$released = other.getReleased();
                return this$released == null ? other$released == null : this$released.equals(other$released);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JavaVersion;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $released = getReleased();
        return (result * 59) + ($released == null ? 43 : $released.hashCode());
    }

    public String toString() {
        return "JavaVersion(name=" + getName() + ", released=" + getReleased() + ")";
    }

    public String getName() {
        return this.name;
    }

    public String getReleased() {
        return this.released;
    }
}
