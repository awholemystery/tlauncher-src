package org.tlauncher.tlauncher.controller.java;

import java.util.List;
import java.util.Map;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/controller/java/JavaVersion.class */
public class JavaVersion {
    private Map<OS.Arch, Map<OS, List<String>>> map;

    public Map<OS.Arch, Map<OS, List<String>>> getMap() {
        return this.map;
    }

    public void setMap(Map<OS.Arch, Map<OS, List<String>>> map) {
        this.map = map;
    }

    public List<String> getProperUrl() throws NotIdentifiedSystem {
        Map<OS, List<String>> res = this.map.get(OS.Arch.CURRENT);
        if (res == null) {
            throw new NotIdentifiedSystem("coudn't find" + OS.Arch.CURRENT);
        }
        return res.get(OS.CURRENT);
    }
}
