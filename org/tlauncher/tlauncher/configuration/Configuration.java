package org.tlauncher.tlauncher.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import joptsimple.OptionSet;
import net.minecraft.launcher.updater.VersionFilter;
import net.minecraft.launcher.versions.ReleaseType;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.tlauncher.entity.ConfigEnum;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.settings.MinecraftSettings;
import org.tlauncher.util.Direction;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.IntegerArray;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.Reflect;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/Configuration.class */
public class Configuration extends SimpleConfiguration {
    private ConfigurationDefaults defaults;
    private Map<String, Object> constants;
    private List<Locale> defaultLocales;
    private List<Locale> supportedLocales;
    private boolean firstRun;

    private Configuration(URL url, OptionSet set) throws IOException {
        super(url);
        init(set);
    }

    private Configuration(File file, OptionSet set) {
        super(file);
        init(set);
    }

    public static Configuration createConfiguration(OptionSet set) throws IOException {
        File file;
        Object path = set != null ? set.valueOf("settings") : null;
        InnerConfiguration inner = TLauncher.getInnerSettings();
        if (path == null) {
            file = MinecraftUtil.getSystemRelatedDirectory(inner.get("settings.new"));
        } else {
            file = new File(path.toString());
        }
        boolean doesntExist = !file.isFile();
        if (doesntExist) {
            U.log("Creating configuration file...");
            FileUtil.createFile(file);
        }
        U.log("Loading configuration from file:", file);
        Configuration config = new Configuration(file, set);
        config.firstRun = doesntExist;
        return config;
    }

    public static Configuration createConfiguration() throws IOException {
        return createConfiguration(null);
    }

    public static List<Locale> getDefaultLocales(SimpleConfiguration simpleConfiguration) {
        List<Locale> l = new ArrayList<>();
        String[] languages = simpleConfiguration.get("languages").split(",");
        for (String string : languages) {
            l.add(getLocaleOf(string));
        }
        return l;
    }

    public boolean isFirstRun() {
        return this.firstRun;
    }

    public boolean isSaveable(String key) {
        return !this.constants.containsKey(key);
    }

    public Locale getLocale() {
        return getLocaleOf(get("locale"));
    }

    public Locale[] getLocales() {
        Locale[] locales = new Locale[this.supportedLocales.size()];
        return (Locale[]) this.supportedLocales.toArray(locales);
    }

    public ActionOnLaunch getActionOnLaunch() {
        return ActionOnLaunch.get(get("minecraft.onlaunch"));
    }

    public ConsoleType getConsoleType() {
        return ConsoleType.get(get("gui.console"));
    }

    public ConnectionQuality getConnectionQuality() {
        return ConnectionQuality.get(get("connection"));
    }

    public int[] getClientWindowSize() {
        String plainValue = get("minecraft.size");
        int[] value = new int[2];
        if (plainValue == null) {
            return new int[2];
        }
        try {
            IntegerArray arr = IntegerArray.parseIntegerArray(plainValue);
            value[0] = arr.get(0);
            value[1] = arr.get(1);
        } catch (Exception e) {
        }
        return value;
    }

    public int[] getLauncherWindowSize() {
        String plainValue = get("gui.size");
        int[] value = new int[2];
        if (plainValue == null) {
            return new int[2];
        }
        try {
            IntegerArray arr = IntegerArray.parseIntegerArray(plainValue);
            value[0] = arr.get(0);
            value[1] = arr.get(1);
        } catch (Exception e) {
        }
        return value;
    }

    public int[] getDefaultClientWindowSize() {
        String plainValue = getDefault("minecraft.size");
        return IntegerArray.parseIntegerArray(plainValue).toArray();
    }

    public int[] getDefaultLauncherWindowSize() {
        String plainValue = getDefault("gui.size");
        return IntegerArray.parseIntegerArray(plainValue).toArray();
    }

    public VersionFilter getVersionFilter() {
        ReleaseType.SubType[] values;
        VersionFilter filter = new VersionFilter();
        for (ReleaseType type : ReleaseType.getDefinable()) {
            boolean include = getBoolean("minecraft.versions." + type);
            if (!include) {
                filter.exclude(type);
            }
        }
        for (ReleaseType.SubType type2 : ReleaseType.SubType.values()) {
            boolean include2 = getBoolean("minecraft.versions.sub." + type2);
            if (!include2) {
                filter.exclude(type2);
            }
        }
        return filter;
    }

    public Direction getDirection(String key) {
        return (Direction) Reflect.parseEnum(Direction.class, get(key));
    }

    public Proxy getProxy() {
        return Proxy.NO_PROXY;
    }

    public UUID getClient() {
        try {
            return UUID.fromString(get("client"));
        } catch (Exception e) {
            return null;
        }
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public String getDefault(String key) {
        return getStringOf(this.defaults.get(key));
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public int getDefaultInteger(String key) {
        return getIntegerOf(this.defaults.get(key), 0);
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public double getDefaultDouble(String key) {
        return getDoubleOf(this.defaults.get(key), 0.0d);
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public float getDefaultFloat(String key) {
        return getFloatOf(this.defaults.get(key), 0.0f);
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public long getDefaultLong(String key) {
        return getLongOf(this.defaults.get(key), 0L);
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public boolean getDefaultBoolean(String key) {
        return getBooleanOf(this.defaults.get(key), false);
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration
    public void set(String key, Object value, boolean flush) {
        if (this.constants.containsKey(key)) {
            return;
        }
        super.set(key, value, flush);
    }

    public void setForcefully(String key, Object value, boolean flush) {
        super.set(key, value, flush);
    }

    public void setForcefully(String key, Object value) {
        setForcefully(key, value, true);
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public void save() throws IOException {
        if (!isSaveable()) {
            throw new UnsupportedOperationException();
        }
        Properties temp = copyProperties(this.properties);
        for (String key : this.constants.keySet()) {
            temp.remove(key);
        }
        File file = (File) this.input;
        temp.store(new FileOutputStream(file), this.comments);
    }

    public File getFile() {
        if (!isSaveable()) {
            return null;
        }
        return (File) this.input;
    }

    private void init(OptionSet set) {
        this.comments = " TLauncher configuration file\n Version " + TLauncher.getVersion();
        InnerConfiguration inner = TLauncher.getInnerSettings();
        this.defaults = new ConfigurationDefaults(inner);
        this.constants = ArgumentParser.parse(set);
        set(this.constants, false);
        log("Constant values:", this.constants);
        int version = ConfigurationDefaults.getVersion();
        if (getDouble("settings.version") != version) {
            clear();
        }
        set("settings.version", Integer.valueOf(version), false);
        for (Map.Entry<String, Object> curen : this.defaults.getMap().entrySet()) {
            String key = curen.getKey();
            if (this.constants.containsKey(key)) {
                log("Key \"" + key + "\" is unsaveable!");
            } else {
                String value = get(key);
                Object defvalue = curen.getValue();
                if (defvalue != null) {
                    try {
                        PlainParser.parse(value, defvalue);
                    } catch (Exception e) {
                        log("Key \"" + key + "\" is invalid!");
                        set(key, defvalue, false);
                    }
                }
            }
        }
        this.defaultLocales = getDefaultLocales(inner);
        this.supportedLocales = this.defaultLocales;
        Locale selected = getLocaleOf(get("locale"));
        if (selected == null) {
            log("Selected locale is not supported, rolling back to default one");
            selected = Locale.getDefault();
            if (selected == getLocaleOf("uk_UA")) {
                selected = getLocaleOf("ru_RU");
            }
        }
        set("locale", findSuitableLocale(selected, this.supportedLocales), false);
        if (get("chooser.type.account") == null) {
            set("chooser.type.account", false, false);
        }
        if (get("skin.status.checkbox.state") == null) {
            set("skin.status.checkbox.state", true, false);
        }
        if (getInteger(MinecraftSettings.MINECRAFT_SETTING_RAM) > OS.Arch.MAX_MEMORY) {
            U.log(String.format("decreased memory from  %s to %s", Integer.valueOf(getInteger(MinecraftSettings.MINECRAFT_SETTING_RAM)), Integer.valueOf(OS.Arch.MAX_MEMORY)));
            set(MinecraftSettings.MINECRAFT_SETTING_RAM, Integer.valueOf(OS.Arch.MAX_MEMORY));
        }
        if (getInteger(MinecraftSettings.MINECRAFT_SETTING_RAM) < 512) {
            set(MinecraftSettings.MINECRAFT_SETTING_RAM, (Object) 512);
        }
        set(ConfigEnum.UPDATER_LAUNCHER, (Object) false);
        if (isSaveable()) {
            try {
                save();
            } catch (IOException e2) {
                log("Cannot save value!", e2);
            }
        }
    }

    public static Locale getLocaleOf(String locale) {
        Locale[] availableLocales;
        if (locale == null) {
            return null;
        }
        for (Locale cur : Locale.getAvailableLocales()) {
            if (cur.toString().equals(locale)) {
                return cur;
            }
        }
        return null;
    }

    public static Locale findSuitableLocale(Locale selected, List<Locale> supportedLocales) {
        for (Locale l : supportedLocales) {
            if (l.getLanguage().equals(selected.getLanguage())) {
                return l;
            }
        }
        U.log("Default locale is not supported, rolling back to global default one");
        return Locale.US;
    }

    public boolean isExist(String key) {
        return get(key) != null;
    }
}
