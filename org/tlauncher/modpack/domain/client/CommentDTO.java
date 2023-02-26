package org.tlauncher.modpack.domain.client;

import org.tlauncher.modpack.domain.client.site.CommonPage;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/CommentDTO.class */
public class CommentDTO {
    private Long id;
    private String description;
    private Long updated;
    private String author;
    private CommonPage<CommentDTO> subComments;
    private CommentPositionDTO authorPosition;
    private Long goodPosition;
    private Long badPosition;
    private boolean edited;
    private boolean removed;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSubComments(CommonPage<CommentDTO> subComments) {
        this.subComments = subComments;
    }

    public void setAuthorPosition(CommentPositionDTO authorPosition) {
        this.authorPosition = authorPosition;
    }

    public void setGoodPosition(Long goodPosition) {
        this.goodPosition = goodPosition;
    }

    public void setBadPosition(Long badPosition) {
        this.badPosition = badPosition;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CommentDTO) {
            CommentDTO other = (CommentDTO) o;
            if (other.canEqual(this) && isEdited() == other.isEdited() && isRemoved() == other.isRemoved()) {
                Object this$id = getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }
                Object this$updated = getUpdated();
                Object other$updated = other.getUpdated();
                if (this$updated == null) {
                    if (other$updated != null) {
                        return false;
                    }
                } else if (!this$updated.equals(other$updated)) {
                    return false;
                }
                Object this$goodPosition = getGoodPosition();
                Object other$goodPosition = other.getGoodPosition();
                if (this$goodPosition == null) {
                    if (other$goodPosition != null) {
                        return false;
                    }
                } else if (!this$goodPosition.equals(other$goodPosition)) {
                    return false;
                }
                Object this$badPosition = getBadPosition();
                Object other$badPosition = other.getBadPosition();
                if (this$badPosition == null) {
                    if (other$badPosition != null) {
                        return false;
                    }
                } else if (!this$badPosition.equals(other$badPosition)) {
                    return false;
                }
                Object this$description = getDescription();
                Object other$description = other.getDescription();
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }
                Object this$author = getAuthor();
                Object other$author = other.getAuthor();
                if (this$author == null) {
                    if (other$author != null) {
                        return false;
                    }
                } else if (!this$author.equals(other$author)) {
                    return false;
                }
                Object this$subComments = getSubComments();
                Object other$subComments = other.getSubComments();
                if (this$subComments == null) {
                    if (other$subComments != null) {
                        return false;
                    }
                } else if (!this$subComments.equals(other$subComments)) {
                    return false;
                }
                Object this$authorPosition = getAuthorPosition();
                Object other$authorPosition = other.getAuthorPosition();
                return this$authorPosition == null ? other$authorPosition == null : this$authorPosition.equals(other$authorPosition);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommentDTO;
    }

    public int hashCode() {
        int result = (1 * 59) + (isEdited() ? 79 : 97);
        int result2 = (result * 59) + (isRemoved() ? 79 : 97);
        Object $id = getId();
        int result3 = (result2 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $updated = getUpdated();
        int result4 = (result3 * 59) + ($updated == null ? 43 : $updated.hashCode());
        Object $goodPosition = getGoodPosition();
        int result5 = (result4 * 59) + ($goodPosition == null ? 43 : $goodPosition.hashCode());
        Object $badPosition = getBadPosition();
        int result6 = (result5 * 59) + ($badPosition == null ? 43 : $badPosition.hashCode());
        Object $description = getDescription();
        int result7 = (result6 * 59) + ($description == null ? 43 : $description.hashCode());
        Object $author = getAuthor();
        int result8 = (result7 * 59) + ($author == null ? 43 : $author.hashCode());
        Object $subComments = getSubComments();
        int result9 = (result8 * 59) + ($subComments == null ? 43 : $subComments.hashCode());
        Object $authorPosition = getAuthorPosition();
        return (result9 * 59) + ($authorPosition == null ? 43 : $authorPosition.hashCode());
    }

    public String toString() {
        return "CommentDTO(id=" + getId() + ", description=" + getDescription() + ", updated=" + getUpdated() + ", author=" + getAuthor() + ", subComments=" + getSubComments() + ", authorPosition=" + getAuthorPosition() + ", goodPosition=" + getGoodPosition() + ", badPosition=" + getBadPosition() + ", edited=" + isEdited() + ", removed=" + isRemoved() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getUpdated() {
        return this.updated;
    }

    public String getAuthor() {
        return this.author;
    }

    public CommonPage<CommentDTO> getSubComments() {
        return this.subComments;
    }

    public CommentPositionDTO getAuthorPosition() {
        return this.authorPosition;
    }

    public Long getGoodPosition() {
        return this.goodPosition;
    }

    public Long getBadPosition() {
        return this.badPosition;
    }

    public boolean isEdited() {
        return this.edited;
    }

    public boolean isRemoved() {
        return this.removed;
    }
}
