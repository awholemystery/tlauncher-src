package org.tlauncher.statistics;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/statistics/UniqueClientDTO.class */
public class UniqueClientDTO {
    private String os;
    private String javaVersion;
    private String resolution;
    private double clientVersion;
    private String osVersion;
    private String uuid;
    private String cpu;
    private String gpu;
    private int ramGpu;
    private int ram;

    public void setOs(String os) {
        this.os = os;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setClientVersion(double clientVersion) {
        this.clientVersion = clientVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public void setRamGpu(int ramGpu) {
        this.ramGpu = ramGpu;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof UniqueClientDTO) {
            UniqueClientDTO other = (UniqueClientDTO) o;
            if (other.canEqual(this)) {
                Object this$os = getOs();
                Object other$os = other.getOs();
                if (this$os == null) {
                    if (other$os != null) {
                        return false;
                    }
                } else if (!this$os.equals(other$os)) {
                    return false;
                }
                Object this$javaVersion = getJavaVersion();
                Object other$javaVersion = other.getJavaVersion();
                if (this$javaVersion == null) {
                    if (other$javaVersion != null) {
                        return false;
                    }
                } else if (!this$javaVersion.equals(other$javaVersion)) {
                    return false;
                }
                Object this$resolution = getResolution();
                Object other$resolution = other.getResolution();
                if (this$resolution == null) {
                    if (other$resolution != null) {
                        return false;
                    }
                } else if (!this$resolution.equals(other$resolution)) {
                    return false;
                }
                if (Double.compare(getClientVersion(), other.getClientVersion()) != 0) {
                    return false;
                }
                Object this$osVersion = getOsVersion();
                Object other$osVersion = other.getOsVersion();
                if (this$osVersion == null) {
                    if (other$osVersion != null) {
                        return false;
                    }
                } else if (!this$osVersion.equals(other$osVersion)) {
                    return false;
                }
                Object this$uuid = getUuid();
                Object other$uuid = other.getUuid();
                if (this$uuid == null) {
                    if (other$uuid != null) {
                        return false;
                    }
                } else if (!this$uuid.equals(other$uuid)) {
                    return false;
                }
                Object this$cpu = getCpu();
                Object other$cpu = other.getCpu();
                if (this$cpu == null) {
                    if (other$cpu != null) {
                        return false;
                    }
                } else if (!this$cpu.equals(other$cpu)) {
                    return false;
                }
                Object this$gpu = getGpu();
                Object other$gpu = other.getGpu();
                if (this$gpu == null) {
                    if (other$gpu != null) {
                        return false;
                    }
                } else if (!this$gpu.equals(other$gpu)) {
                    return false;
                }
                return getRamGpu() == other.getRamGpu() && getRam() == other.getRam();
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UniqueClientDTO;
    }

    public int hashCode() {
        Object $os = getOs();
        int result = (1 * 59) + ($os == null ? 43 : $os.hashCode());
        Object $javaVersion = getJavaVersion();
        int result2 = (result * 59) + ($javaVersion == null ? 43 : $javaVersion.hashCode());
        Object $resolution = getResolution();
        int result3 = (result2 * 59) + ($resolution == null ? 43 : $resolution.hashCode());
        long $clientVersion = Double.doubleToLongBits(getClientVersion());
        int result4 = (result3 * 59) + ((int) (($clientVersion >>> 32) ^ $clientVersion));
        Object $osVersion = getOsVersion();
        int result5 = (result4 * 59) + ($osVersion == null ? 43 : $osVersion.hashCode());
        Object $uuid = getUuid();
        int result6 = (result5 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $cpu = getCpu();
        int result7 = (result6 * 59) + ($cpu == null ? 43 : $cpu.hashCode());
        Object $gpu = getGpu();
        return (((((result7 * 59) + ($gpu == null ? 43 : $gpu.hashCode())) * 59) + getRamGpu()) * 59) + getRam();
    }

    public String toString() {
        return "UniqueClientDTO(os=" + getOs() + ", javaVersion=" + getJavaVersion() + ", resolution=" + getResolution() + ", clientVersion=" + getClientVersion() + ", osVersion=" + getOsVersion() + ", uuid=" + getUuid() + ", cpu=" + getCpu() + ", gpu=" + getGpu() + ", ramGpu=" + getRamGpu() + ", ram=" + getRam() + ")";
    }

    public String getOs() {
        return this.os;
    }

    public String getJavaVersion() {
        return this.javaVersion;
    }

    public String getResolution() {
        return this.resolution;
    }

    public double getClientVersion() {
        return this.clientVersion;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getCpu() {
        return this.cpu;
    }

    public String getGpu() {
        return this.gpu;
    }

    public int getRamGpu() {
        return this.ramGpu;
    }

    public int getRam() {
        return this.ram;
    }
}
