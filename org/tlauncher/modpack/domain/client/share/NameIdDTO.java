package org.tlauncher.modpack.domain.client.share;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/NameIdDTO.class */
public class NameIdDTO {
    private Long id;
    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof NameIdDTO) {
            NameIdDTO other = (NameIdDTO) o;
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
                return this$name == null ? other$name == null : this$name.equals(other$name);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof NameIdDTO;
    }

    public int hashCode() {
        Object $id = getId();
        int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $name = getName();
        return (result * 59) + ($name == null ? 43 : $name.hashCode());
    }

    public String toString() {
        return "NameIdDTO(id=" + getId() + ", name=" + getName() + ")";
    }

    public NameIdDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public NameIdDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
