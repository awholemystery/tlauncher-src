package net.minecraft.launcher.versions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/ExtractRules.class */
public class ExtractRules {
    private List<String> exclude = new ArrayList();

    public ExtractRules() {
    }

    public ExtractRules(String[] exclude) {
        if (exclude != null) {
            Collections.addAll(this.exclude, exclude);
        }
    }

    public ExtractRules(ExtractRules rules) {
        for (String exclude : rules.exclude) {
            this.exclude.add(exclude);
        }
    }

    public List<String> getExcludes() {
        return this.exclude;
    }

    public boolean shouldExtract(String path) {
        if (this.exclude == null) {
            return true;
        }
        for (String rule : this.exclude) {
            if (path.startsWith(rule)) {
                return false;
            }
        }
        return true;
    }
}
