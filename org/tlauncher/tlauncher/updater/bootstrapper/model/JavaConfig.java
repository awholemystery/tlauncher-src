package org.tlauncher.tlauncher.updater.bootstrapper.model;

import java.util.Map;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/model/JavaConfig.class */
public class JavaConfig {
    Map<OS, Map<OS.Arch, JavaDownloadedElement>> config;

    public void setConfig(Map<OS, Map<OS.Arch, JavaDownloadedElement>> config) {
        this.config = config;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof JavaConfig) {
            JavaConfig other = (JavaConfig) o;
            if (other.canEqual(this)) {
                Object this$config = getConfig();
                Object other$config = other.getConfig();
                return this$config == null ? other$config == null : this$config.equals(other$config);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof JavaConfig;
    }

    public int hashCode() {
        Object $config = getConfig();
        int result = (1 * 59) + ($config == null ? 43 : $config.hashCode());
        return result;
    }

    public String toString() {
        return "JavaConfig(config=" + getConfig() + ")";
    }

    public Map<OS, Map<OS.Arch, JavaDownloadedElement>> getConfig() {
        return this.config;
    }
}
