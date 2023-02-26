package org.tlauncher.tlauncher.ui.model;

import org.tlauncher.modpack.domain.client.CommentDTO;
import org.tlauncher.tlauncher.ui.modpack.DiscussionPanel;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/model/InsertCommentDTO.class */
public class InsertCommentDTO extends CommentDTO {
    private DiscussionPanel.Comment parent;

    public void setParent(DiscussionPanel.Comment parent) {
        this.parent = parent;
    }

    @Override // org.tlauncher.modpack.domain.client.CommentDTO
    public String toString() {
        return "InsertCommentDTO(parent=" + getParent() + ")";
    }

    @Override // org.tlauncher.modpack.domain.client.CommentDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof InsertCommentDTO) {
            InsertCommentDTO other = (InsertCommentDTO) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$parent = getParent();
                Object other$parent = other.getParent();
                return this$parent == null ? other$parent == null : this$parent.equals(other$parent);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.CommentDTO
    protected boolean canEqual(Object other) {
        return other instanceof InsertCommentDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.CommentDTO
    public int hashCode() {
        int result = super.hashCode();
        Object $parent = getParent();
        return (result * 59) + ($parent == null ? 43 : $parent.hashCode());
    }

    public DiscussionPanel.Comment getParent() {
        return this.parent;
    }
}
