package org.tlauncher.tlauncher.ui.model;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/model/GameEntityComment.class */
public class GameEntityComment {
    private String description;
    private Long topicId;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof GameEntityComment) {
            GameEntityComment other = (GameEntityComment) o;
            if (other.canEqual(this)) {
                Object this$description = getDescription();
                Object other$description = other.getDescription();
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }
                Object this$topicId = getTopicId();
                Object other$topicId = other.getTopicId();
                return this$topicId == null ? other$topicId == null : this$topicId.equals(other$topicId);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameEntityComment;
    }

    public int hashCode() {
        Object $description = getDescription();
        int result = (1 * 59) + ($description == null ? 43 : $description.hashCode());
        Object $topicId = getTopicId();
        return (result * 59) + ($topicId == null ? 43 : $topicId.hashCode());
    }

    public String toString() {
        return "GameEntityComment(description=" + getDescription() + ", topicId=" + getTopicId() + ")";
    }

    public String getDescription() {
        return this.description;
    }

    public Long getTopicId() {
        return this.topicId;
    }
}
