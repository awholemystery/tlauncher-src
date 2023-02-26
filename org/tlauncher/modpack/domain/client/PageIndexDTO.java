package org.tlauncher.modpack.domain.client;

import java.util.Date;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.site.PageIndexEnum;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/PageIndexDTO.class */
public class PageIndexDTO {
    private Long id;
    private String topic;
    private String uri;
    private boolean ready;
    private Long gameEntityId;
    private PageIndexEnum indexType;
    private GameType type;
    private Date updated;
    private Date updatedElement;
    private Long watched;
    private boolean engTranslation;

    public Long getId() {
        return this.id;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getUri() {
        return this.uri;
    }

    public boolean isReady() {
        return this.ready;
    }

    public Long getGameEntityId() {
        return this.gameEntityId;
    }

    public PageIndexEnum getIndexType() {
        return this.indexType;
    }

    public GameType getType() {
        return this.type;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public Date getUpdatedElement() {
        return this.updatedElement;
    }

    public Long getWatched() {
        return this.watched;
    }

    public boolean isEngTranslation() {
        return this.engTranslation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setGameEntityId(Long gameEntityId) {
        this.gameEntityId = gameEntityId;
    }

    public void setIndexType(PageIndexEnum indexType) {
        this.indexType = indexType;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setUpdatedElement(Date updatedElement) {
        this.updatedElement = updatedElement;
    }

    public void setWatched(Long watched) {
        this.watched = watched;
    }

    public void setEngTranslation(boolean engTranslation) {
        this.engTranslation = engTranslation;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof PageIndexDTO) {
            PageIndexDTO other = (PageIndexDTO) o;
            if (other.canEqual(this) && isReady() == other.isReady() && isEngTranslation() == other.isEngTranslation()) {
                Object this$id = getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }
                Object this$gameEntityId = getGameEntityId();
                Object other$gameEntityId = other.getGameEntityId();
                if (this$gameEntityId == null) {
                    if (other$gameEntityId != null) {
                        return false;
                    }
                } else if (!this$gameEntityId.equals(other$gameEntityId)) {
                    return false;
                }
                Object this$watched = getWatched();
                Object other$watched = other.getWatched();
                if (this$watched == null) {
                    if (other$watched != null) {
                        return false;
                    }
                } else if (!this$watched.equals(other$watched)) {
                    return false;
                }
                Object this$topic = getTopic();
                Object other$topic = other.getTopic();
                if (this$topic == null) {
                    if (other$topic != null) {
                        return false;
                    }
                } else if (!this$topic.equals(other$topic)) {
                    return false;
                }
                Object this$uri = getUri();
                Object other$uri = other.getUri();
                if (this$uri == null) {
                    if (other$uri != null) {
                        return false;
                    }
                } else if (!this$uri.equals(other$uri)) {
                    return false;
                }
                Object this$indexType = getIndexType();
                Object other$indexType = other.getIndexType();
                if (this$indexType == null) {
                    if (other$indexType != null) {
                        return false;
                    }
                } else if (!this$indexType.equals(other$indexType)) {
                    return false;
                }
                Object this$type = getType();
                Object other$type = other.getType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
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
                Object this$updatedElement = getUpdatedElement();
                Object other$updatedElement = other.getUpdatedElement();
                return this$updatedElement == null ? other$updatedElement == null : this$updatedElement.equals(other$updatedElement);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageIndexDTO;
    }

    public int hashCode() {
        int result = (1 * 59) + (isReady() ? 79 : 97);
        int result2 = (result * 59) + (isEngTranslation() ? 79 : 97);
        Object $id = getId();
        int result3 = (result2 * 59) + ($id == null ? 43 : $id.hashCode());
        Object $gameEntityId = getGameEntityId();
        int result4 = (result3 * 59) + ($gameEntityId == null ? 43 : $gameEntityId.hashCode());
        Object $watched = getWatched();
        int result5 = (result4 * 59) + ($watched == null ? 43 : $watched.hashCode());
        Object $topic = getTopic();
        int result6 = (result5 * 59) + ($topic == null ? 43 : $topic.hashCode());
        Object $uri = getUri();
        int result7 = (result6 * 59) + ($uri == null ? 43 : $uri.hashCode());
        Object $indexType = getIndexType();
        int result8 = (result7 * 59) + ($indexType == null ? 43 : $indexType.hashCode());
        Object $type = getType();
        int result9 = (result8 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $updated = getUpdated();
        int result10 = (result9 * 59) + ($updated == null ? 43 : $updated.hashCode());
        Object $updatedElement = getUpdatedElement();
        return (result10 * 59) + ($updatedElement == null ? 43 : $updatedElement.hashCode());
    }

    public String toString() {
        return "PageIndexDTO(id=" + getId() + ", topic=" + getTopic() + ", uri=" + getUri() + ", ready=" + isReady() + ", gameEntityId=" + getGameEntityId() + ", indexType=" + getIndexType() + ", type=" + getType() + ", updated=" + getUpdated() + ", updatedElement=" + getUpdatedElement() + ", watched=" + getWatched() + ", engTranslation=" + isEngTranslation() + ")";
    }
}
