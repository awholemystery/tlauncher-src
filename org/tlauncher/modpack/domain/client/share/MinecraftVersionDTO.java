package org.tlauncher.modpack.domain.client.share;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/MinecraftVersionDTO.class */
public class MinecraftVersionDTO {
    private Long id;
    private String name;
    private String value;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof MinecraftVersionDTO) {
            MinecraftVersionDTO other = (MinecraftVersionDTO) o;
            if (other.canEqual(this)) {
                Object this$id = getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }
                Object this$name = getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }
                Object this$value = getValue();
                Object other$value = other.getValue();
                return this$value == null ? other$value == null : this$value.equals(other$value);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MinecraftVersionDTO;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $value = getValue();
        return (result2 * 59) + ($value == null ? 43 : $value.hashCode());
    }

    public String toString() {
        return "MinecraftVersionDTO(id=" + getId() + ", name=" + getName() + ", value=" + getValue() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
