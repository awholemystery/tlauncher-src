package org.tlauncher.modpack.domain.client.site;

import org.tlauncher.modpack.domain.client.GameEntityDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/site/ModerationEntityDTO.class */
public class ModerationEntityDTO extends GameEntityDTO {
    private Status moderationStatus;
    private String changedAuthor;
    private String userMessage;
    private String adminMessage;

    public void setModerationStatus(Status moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public void setChangedAuthor(String changedAuthor) {
        this.changedAuthor = changedAuthor;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public String toString() {
        return "ModerationEntityDTO(super=" + super.toString() + ", moderationStatus=" + getModerationStatus() + ", changedAuthor=" + getChangedAuthor() + ", userMessage=" + getUserMessage() + ", adminMessage=" + getAdminMessage() + ")";
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModerationEntityDTO) {
            ModerationEntityDTO other = (ModerationEntityDTO) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$moderationStatus = getModerationStatus();
                Object other$moderationStatus = other.getModerationStatus();
                if (this$moderationStatus == null) {
                    if (other$moderationStatus != null) {
                        return false;
                    }
                } else if (!this$moderationStatus.equals(other$moderationStatus)) {
                    return false;
                }
                Object this$changedAuthor = getChangedAuthor();
                Object other$changedAuthor = other.getChangedAuthor();
                if (this$changedAuthor == null) {
                    if (other$changedAuthor != null) {
                        return false;
                    }
                } else if (!this$changedAuthor.equals(other$changedAuthor)) {
                    return false;
                }
                Object this$userMessage = getUserMessage();
                Object other$userMessage = other.getUserMessage();
                if (this$userMessage == null) {
                    if (other$userMessage != null) {
                        return false;
                    }
                } else if (!this$userMessage.equals(other$userMessage)) {
                    return false;
                }
                Object this$adminMessage = getAdminMessage();
                Object other$adminMessage = other.getAdminMessage();
                return this$adminMessage == null ? other$adminMessage == null : this$adminMessage.equals(other$adminMessage);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    protected boolean canEqual(Object other) {
        return other instanceof ModerationEntityDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public int hashCode() {
        int result = super.hashCode();
        Object $moderationStatus = getModerationStatus();
        int result2 = (result * 59) + ($moderationStatus == null ? 43 : $moderationStatus.hashCode());
        Object $changedAuthor = getChangedAuthor();
        int result3 = (result2 * 59) + ($changedAuthor == null ? 43 : $changedAuthor.hashCode());
        Object $userMessage = getUserMessage();
        int result4 = (result3 * 59) + ($userMessage == null ? 43 : $userMessage.hashCode());
        Object $adminMessage = getAdminMessage();
        return (result4 * 59) + ($adminMessage == null ? 43 : $adminMessage.hashCode());
    }

    public Status getModerationStatus() {
        return this.moderationStatus;
    }

    public String getChangedAuthor() {
        return this.changedAuthor;
    }

    public String getUserMessage() {
        return this.userMessage;
    }

    public String getAdminMessage() {
        return this.adminMessage;
    }
}
