package org.tlauncher.statistics;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/statistics/InstallVersionDTO.class */
public class InstallVersionDTO {
    private String installVersion;

    public void setInstallVersion(String installVersion) {
        this.installVersion = installVersion;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof InstallVersionDTO) {
            InstallVersionDTO other = (InstallVersionDTO) o;
            if (other.canEqual(this)) {
                Object this$installVersion = getInstallVersion();
                Object other$installVersion = other.getInstallVersion();
                return this$installVersion == null ? other$installVersion == null : this$installVersion.equals(other$installVersion);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstallVersionDTO;
    }

    public int hashCode() {
        Object $installVersion = getInstallVersion();
        int result = (1 * 59) + ($installVersion == null ? 43 : $installVersion.hashCode());
        return result;
    }

    public String toString() {
        return "InstallVersionDTO(installVersion=" + getInstallVersion() + ")";
    }

    public String getInstallVersion() {
        return this.installVersion;
    }
}
