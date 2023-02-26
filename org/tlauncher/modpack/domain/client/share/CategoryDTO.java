package org.tlauncher.modpack.domain.client.share;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/CategoryDTO.class */
public class CategoryDTO {
    private Long id;
    private String name;
    private String shortName;
    private int nesting;
    private List<CategoryDTO> subCategories;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setNesting(int nesting) {
        this.nesting = nesting;
    }

    public void setSubCategories(List<CategoryDTO> subCategories) {
        this.subCategories = subCategories;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CategoryDTO) {
            CategoryDTO other = (CategoryDTO) o;
            if (other.canEqual(this) && getNesting() == other.getNesting()) {
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
                Object this$shortName = getShortName();
                Object other$shortName = other.getShortName();
                if (this$shortName == null) {
                    if (other$shortName != null) {
                        return false;
                    }
                } else if (!this$shortName.equals(other$shortName)) {
                    return false;
                }
                Object this$subCategories = getSubCategories();
                Object other$subCategories = other.getSubCategories();
                return this$subCategories == null ? other$subCategories == null : this$subCategories.equals(other$subCategories);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CategoryDTO;
    }

    public int hashCode() {
        int result = (1 * 59) + getNesting();
        Object $id = getId();
        int result2 = (result * 59) + ($id == null ? 43 : $id.hashCode());
        Object $name = getName();
        int result3 = (result2 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $shortName = getShortName();
        int result4 = (result3 * 59) + ($shortName == null ? 43 : $shortName.hashCode());
        Object $subCategories = getSubCategories();
        return (result4 * 59) + ($subCategories == null ? 43 : $subCategories.hashCode());
    }

    public String toString() {
        return "CategoryDTO(id=" + getId() + ", name=" + getName() + ", shortName=" + getShortName() + ", nesting=" + getNesting() + ", subCategories=" + getSubCategories() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public int getNesting() {
        return this.nesting;
    }

    public List<CategoryDTO> getSubCategories() {
        return this.subCategories;
    }
}
