package org.tlauncher.tlauncher.configuration;

import java.util.HashMap;
import java.util.Map;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.http.cookie.ClientCookie;
import org.tlauncher.tlauncher.ui.alert.Alert;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/ArgumentParser.class */
public class ArgumentParser {
    private static final Map<String, String> m = createLinkMap();
    private static final OptionParser parser = createParser();

    public static OptionParser getParser() {
        return parser;
    }

    public static OptionSet parseArgs(String[] args) {
        OptionSet set = null;
        try {
            set = parser.parse(args);
        } catch (OptionException e) {
            e.printStackTrace();
            Alert.showError((Object) e, false);
        }
        return set;
    }

    public static Map<String, Object> parse(OptionSet set) {
        Map<String, Object> r = new HashMap<>();
        if (set == null) {
            return r;
        }
        for (Map.Entry<String, String> a : m.entrySet()) {
            String key = a.getKey();
            Object value = null;
            if (key.startsWith("-")) {
                key = key.substring(1);
                value = true;
            }
            if (set.has(key)) {
                if (value == null) {
                    value = set.valueOf(key);
                }
                r.put(a.getValue(), value);
            }
        }
        return r;
    }

    private static Map<String, String> createLinkMap() {
        Map<String, String> r = new HashMap<>();
        r.put("directory", "minecraft.gamedir");
        r.put("java-directory", "minecraft.javadir");
        r.put(ClientCookie.VERSION_ATTR, "login.version.game");
        r.put("usertype", "login.account.type");
        r.put("javaargs", "minecraft.javaargs");
        r.put("margs", "minecraft.args");
        r.put("window", "minecraft.size");
        r.put("background", "gui.background");
        r.put("fullscreen", "minecraft.fullscreen");
        r.put("RunAllTLauncherVersions", "run.all.tlauncher.versions");
        r.put("RunAllOfficialVersions", "run.all.official.versions");
        return r;
    }

    private static OptionParser createParser() {
        OptionParser parser2 = new OptionParser();
        parser2.accepts("help", "Shows this help");
        parser2.accepts("nogui", "Starts minimal version");
        parser2.accepts("directory", "Specifies Minecraft directory").withRequiredArg();
        parser2.accepts("java-directory", "Specifies Java directory").withRequiredArg();
        parser2.accepts(ClientCookie.VERSION_ATTR, "Specifies version to run").withRequiredArg();
        parser2.accepts("javaargs", "Specifies JVM arguments").withRequiredArg();
        parser2.accepts("margs", "Specifies Minecraft arguments").withRequiredArg();
        parser2.accepts("window", "Specifies window size in format: width;height").withRequiredArg();
        parser2.accepts("settings", "Specifies path to settings file").withRequiredArg();
        parser2.accepts("background", "Specifies background image. URL links, JPEG and PNG formats are supported.").withRequiredArg();
        parser2.accepts("fullscreen", "Specifies whether fullscreen mode enabled or not").withRequiredArg();
        parser2.accepts("RunAllTLauncherVersions", "Run all tlauncher versions").withRequiredArg();
        parser2.accepts("RunAllOfficialVersions", "Run all official versions").withRequiredArg();
        return parser2;
    }
}
