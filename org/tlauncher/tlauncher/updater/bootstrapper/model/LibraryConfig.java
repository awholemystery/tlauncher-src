package org.tlauncher.tlauncher.updater.bootstrapper.model;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/model/LibraryConfig.class */
public class LibraryConfig {
    private double versionClient;
    private List<DownloadedElement> libraries;

    public void setVersionClient(double versionClient) {
        this.versionClient = versionClient;
    }

    public void setLibraries(List<DownloadedElement> libraries) {
        this.libraries = libraries;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof LibraryConfig) {
            LibraryConfig other = (LibraryConfig) o;
            if (other.canEqual(this) && Double.compare(getVersionClient(), other.getVersionClient()) == 0) {
                Object this$libraries = getLibraries();
                Object other$libraries = other.getLibraries();
                return this$libraries == null ? other$libraries == null : this$libraries.equals(other$libraries);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LibraryConfig;
    }

    public int hashCode() {
        long $versionClient = Double.doubleToLongBits(getVersionClient());
        int result = (1 * 59) + ((int) (($versionClient >>> 32) ^ $versionClient));
        Object $libraries = getLibraries();
        return (result * 59) + ($libraries == null ? 43 : $libraries.hashCode());
    }

    public String toString() {
        return "LibraryConfig(versionClient=" + getVersionClient() + ", libraries=" + getLibraries() + ")";
    }

    public double getVersionClient() {
        return this.versionClient;
    }

    public List<DownloadedElement> getLibraries() {
        return this.libraries;
    }
}
