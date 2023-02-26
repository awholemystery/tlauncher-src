package org.tlauncher.modpack.domain.client;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/CommentPositionDTO.class */
public class CommentPositionDTO {
    private Long id;
    private boolean position;

    public void setId(Long id) {
        this.id = id;
    }

    public void setPosition(boolean position) {
        this.position = position;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CommentPositionDTO) {
            CommentPositionDTO other = (CommentPositionDTO) o;
            if (other.canEqual(this) && isPosition() == other.isPosition()) {
                Object this$id = getId();
                Object other$id = other.getId();
                return this$id == null ? other$id == null : this$id.equals(other$id);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommentPositionDTO;
    }

    public int hashCode() {
        int result = (1 * 59) + (isPosition() ? 79 : 97);
        Object $id = getId();
        return (result * 59) + ($id == null ? 43 : $id.hashCode());
    }

    public String toString() {
        return "CommentPositionDTO(id=" + getId() + ", position=" + isPosition() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public boolean isPosition() {
        return this.position;
    }
}
