package org.tlauncher.modpack.domain.client;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/ResourcePackDTO.class */
public class ResourcePackDTO extends SubModpackDTO {
    @Override // org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.GameEntityDTO
    public String toString() {
        return "ResourcePackDTO(super=" + super.toString() + ")";
    }

    @Override // org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.GameEntityDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ResourcePackDTO) {
            ResourcePackDTO other = (ResourcePackDTO) o;
            return other.canEqual(this) && super.equals(o);
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.GameEntityDTO
    protected boolean canEqual(Object other) {
        return other instanceof ResourcePackDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.GameEntityDTO
    public int hashCode() {
        int result = super.hashCode();
        return result;
    }
}
