package net.minecraft.launcher.versions;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.tlauncher.util.OS;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/Rule.class */
public class Rule {
    private Action action;
    private OSRestriction os;
    private Map<String, Object> features;

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/Rule$Action.class */
    public enum Action {
        ALLOW,
        DISALLOW
    }

    public Rule() {
        this.action = Action.ALLOW;
    }

    public Rule(Rule rule) {
        this.action = Action.ALLOW;
        this.action = rule.action;
        if (rule.os != null) {
            this.os = new OSRestriction(rule.os);
        }
    }

    public Action getAppliedAction() {
        if (this.features != null && this.features.get("is_demo_user") != null) {
            return Action.DISALLOW;
        }
        if (this.os != null && !this.os.isCurrentOperatingSystem()) {
            return null;
        }
        return this.action;
    }

    public String toString() {
        return "Rule{action=" + this.action + ", os=" + this.os + ", features=" + this.features + '}';
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/Rule$OSRestriction.class */
    public class OSRestriction {
        private OS name;
        private String version;

        public OSRestriction(OSRestriction osRestriction) {
            this.name = osRestriction.name;
            this.version = osRestriction.version;
        }

        public boolean isCurrentOperatingSystem() {
            if (this.name != null && this.name != OS.CURRENT) {
                return false;
            }
            if (this.version != null) {
                try {
                    Pattern pattern = Pattern.compile(this.version);
                    Matcher matcher = pattern.matcher(System.getProperty("os.version"));
                    if (!matcher.matches()) {
                        return false;
                    }
                    return true;
                } catch (Throwable th) {
                    return true;
                }
            }
            return true;
        }

        public String toString() {
            return "OSRestriction{name=" + this.name + ", version='" + this.version + "'}";
        }
    }
}
