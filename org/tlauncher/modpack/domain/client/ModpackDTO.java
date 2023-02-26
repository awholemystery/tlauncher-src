package org.tlauncher.modpack.domain.client;

import java.util.ArrayList;
import java.util.Collection;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/ModpackDTO.class */
public class ModpackDTO extends GameEntityDTO {
    private boolean modpackMemory;
    private int memory;

    public boolean isModpackMemory() {
        return this.modpackMemory;
    }

    public int getMemory() {
        return this.memory;
    }

    public void setModpackMemory(boolean modpackMemory) {
        this.modpackMemory = modpackMemory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public String toString() {
        return "ModpackDTO(super=" + super.toString() + ", modpackMemory=" + isModpackMemory() + ", memory=" + getMemory() + ")";
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModpackDTO) {
            ModpackDTO other = (ModpackDTO) o;
            return other.canEqual(this) && super.equals(o) && isModpackMemory() == other.isModpackMemory() && getMemory() == other.getMemory();
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    protected boolean canEqual(Object other) {
        return other instanceof ModpackDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.GameEntityDTO
    public int hashCode() {
        int result = super.hashCode();
        return (((result * 59) + (isModpackMemory() ? 79 : 97)) * 59) + getMemory();
    }

    public void copy(ModpackDTO c) {
        c.setId(getId());
        c.setAuthor(getAuthor());
        c.setCategories(new CheckNullList<>(getCategories()));
        c.setDescription(getDescription());
        c.setInstalled(getInstalled());
        c.setDownloadMonth(getDownloadMonth());
        c.setMemory(this.memory);
        c.setModpackMemory(this.modpackMemory);
        c.setOfficialSite(getOfficialSite());
        c.setName(getName());
        c.setPicture(getPicture());
        c.setPictures(new CheckNullList<>(getPictures()));
        c.setPopulateStatus(isPopulateStatus());
        c.setTags(new CheckNullList<>(getTags()));
        c.setUserInstall(isUserInstall());
        ModpackVersionDTO version = (ModpackVersionDTO) getVersion();
        ModpackVersionDTO modpackVersionDTO = new ModpackVersionDTO();
        version.copy(modpackVersionDTO);
        c.setVersion(modpackVersionDTO);
        c.setVersions(new CheckNullList<>(getVersions()));
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/ModpackDTO$CheckNullList.class */
    public static class CheckNullList<E> extends ArrayList<E> {
        public CheckNullList(Collection<? extends E> c) {
            super(c == null ? new ArrayList<>() : c);
        }
    }
}
