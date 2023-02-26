package org.tlauncher.modpack.domain.client;

import org.tlauncher.modpack.domain.client.share.StateGameElement;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/SubModpackDTO.class */
public class SubModpackDTO extends GameEntityDTO {
    private StateGameElement stateGameElement = StateGameElement.ACTIVE;

    public void setStateGameElement(StateGameElement stateGameElement) {
        this.stateGameElement = stateGameElement;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public String toString() {
        return "SubModpackDTO(super=" + super.toString() + ", stateGameElement=" + getStateGameElement() + ")";
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof SubModpackDTO) {
            SubModpackDTO other = (SubModpackDTO) o;
            if (other.canEqual(this) && super.equals(o)) {
                Object this$stateGameElement = getStateGameElement();
                Object other$stateGameElement = other.getStateGameElement();
                return this$stateGameElement == null ? other$stateGameElement == null : this$stateGameElement.equals(other$stateGameElement);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    protected boolean canEqual(Object other) {
        return other instanceof SubModpackDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public int hashCode() {
        int result = super.hashCode();
        Object $stateGameElement = getStateGameElement();
        return (result * 59) + ($stateGameElement == null ? 43 : $stateGameElement.hashCode());
    }

    public StateGameElement getStateGameElement() {
        return this.stateGameElement;
    }
}
