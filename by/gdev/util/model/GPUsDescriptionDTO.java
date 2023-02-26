package by.gdev.util.model;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:by/gdev/util/model/GPUsDescriptionDTO.class */
public class GPUsDescriptionDTO {
    List<GPUDescription> gpus;
    String rawDescription;

    public void setGpus(List<GPUDescription> gpus) {
        this.gpus = gpus;
    }

    public void setRawDescription(String rawDescription) {
        this.rawDescription = rawDescription;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof GPUsDescriptionDTO) {
            GPUsDescriptionDTO other = (GPUsDescriptionDTO) o;
            if (other.canEqual(this)) {
                Object this$gpus = getGpus();
                Object other$gpus = other.getGpus();
                if (this$gpus == null) {
                    if (other$gpus != null) {
                        return false;
                    }
                } else if (!this$gpus.equals(other$gpus)) {
                    return false;
                }
                Object this$rawDescription = getRawDescription();
                Object other$rawDescription = other.getRawDescription();
                return this$rawDescription == null ? other$rawDescription == null : this$rawDescription.equals(other$rawDescription);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GPUsDescriptionDTO;
    }

    public int hashCode() {
        Object $gpus = getGpus();
        int result = (1 * 59) + ($gpus == null ? 43 : $gpus.hashCode());
        Object $rawDescription = getRawDescription();
        return (result * 59) + ($rawDescription == null ? 43 : $rawDescription.hashCode());
    }

    public String toString() {
        return "GPUsDescriptionDTO(gpus=" + getGpus() + ", rawDescription=" + getRawDescription() + ")";
    }

    public List<GPUDescription> getGpus() {
        return this.gpus;
    }

    public String getRawDescription() {
        return this.rawDescription;
    }
}
