package org.tlauncher.modpack.domain.client;

import java.util.HashSet;
import java.util.Set;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/ModDTO.class */
public class ModDTO extends SubModpackDTO {
    public static final Long OPTIFINE_ID = 9999999L;
    public static final Long TL_SKIN_ID = 10000014L;
    public static final Long TL_SKIN_CAPE_ID = 10000020L;
    public static final Long FABRIC_API_ID = 306612L;
    public static final Set<Long> SKIN_MODS = new HashSet<Long>() { // from class: org.tlauncher.modpack.domain.client.ModDTO.1
        private static final long serialVersionUID = 1;

        {
            add(ModDTO.TL_SKIN_ID);
            add(ModDTO.TL_SKIN_CAPE_ID);
        }
    };

    @Override // org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.GameEntityDTO
    public String toString() {
        return "ModDTO(super=" + super.toString() + ")";
    }

    @Override // org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.GameEntityDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModDTO) {
            ModDTO other = (ModDTO) o;
            return other.canEqual(this) && super.equals(o);
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.GameEntityDTO
    protected boolean canEqual(Object other) {
        return other instanceof ModDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.SubModpackDTO, org.tlauncher.modpack.domain.client.GameEntityDTO
    public int hashCode() {
        int result = super.hashCode();
        return result;
    }
}
