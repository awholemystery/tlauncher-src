package by.gdev.util.model;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/model/GPUDescription.class */
public class GPUDescription {
    String name;
    String chipType;
    String memory;

    public void setName(String name) {
        this.name = name;
    }

    public void setChipType(String chipType) {
        this.chipType = chipType;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof GPUDescription) {
            GPUDescription other = (GPUDescription) o;
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
                Object this$chipType = getChipType();
                Object other$chipType = other.getChipType();
                if (this$chipType == null) {
                    if (other$chipType != null) {
                        return false;
                    }
                } else if (!this$chipType.equals(other$chipType)) {
                    return false;
                }
                Object this$memory = getMemory();
                Object other$memory = other.getMemory();
                return this$memory == null ? other$memory == null : this$memory.equals(other$memory);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GPUDescription;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $chipType = getChipType();
        int result2 = (result * 59) + ($chipType == null ? 43 : $chipType.hashCode());
        Object $memory = getMemory();
        return (result2 * 59) + ($memory == null ? 43 : $memory.hashCode());
    }

    public String toString() {
        return "GPUDescription(name=" + getName() + ", chipType=" + getChipType() + ", memory=" + getMemory() + ")";
    }

    public String getName() {
        return this.name;
    }

    public String getChipType() {
        return this.chipType;
    }

    public String getMemory() {
        return this.memory;
    }
}
