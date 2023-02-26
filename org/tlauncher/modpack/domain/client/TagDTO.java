package org.tlauncher.modpack.domain.client;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/TagDTO.class */
public class TagDTO {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof TagDTO) {
            TagDTO other = (TagDTO) o;
            if (other.canEqual(this)) {
                Object this$name = getName();
                Object other$name = other.getName();
                return this$name == null ? other$name == null : this$name.equals(other$name);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TagDTO;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "TagDTO(name=" + getName() + ")";
    }

    public String getName() {
        return this.name;
    }
}
