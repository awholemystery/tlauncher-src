package org.tlauncher.tlauncher.entity;

import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/VersionPeriod.class */
public class VersionPeriod {
    private String minVersion;
    private List<String> ignoreVersions;
    private List<String> includeVersions;

    public String getMinVersion() {
        return this.minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public List<String> getIgnoreVersions() {
        return this.ignoreVersions;
    }

    public void setIgnoreVersions(List<String> ignoreVersions) {
        this.ignoreVersions = ignoreVersions;
    }

    public List<String> getIncludeVersions() {
        return this.includeVersions;
    }

    public void setIncludeVersions(List<String> includeVersions) {
        this.includeVersions = includeVersions;
    }
}
