package org.tlauncher.tlauncher.configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.tlauncher.tlauncher.entity.ConfigEnum;
import org.tlauncher.util.StringUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/SimpleConfiguration.class */
public class SimpleConfiguration implements AbstractConfiguration {
    protected final Properties properties;
    protected Object input;
    protected String comments;

    public SimpleConfiguration() {
        this.properties = new Properties();
    }

    public SimpleConfiguration(InputStream stream) throws IOException {
        this();
        loadFromStream(this.properties, stream);
        this.input = stream;
    }

    public SimpleConfiguration(File file) {
        this();
        try {
            loadFromFile(this.properties, file);
        } catch (FileNotFoundException e) {
            log(file + " file not exists");
        } catch (Exception e2) {
            log("Error loading config from file:", e2);
        }
        this.input = file;
    }

    public SimpleConfiguration(URL url) throws IOException {
        this();
        loadFromURL(this.properties, url);
        this.input = url;
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public String get(String key) {
        return getStringOf(this.properties.getProperty(key));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getStringOf(Object obj) {
        String s;
        if (obj == null) {
            s = null;
        } else {
            s = obj.toString();
            if (s.isEmpty()) {
                s = null;
            }
        }
        return s;
    }

    public void set(String key, Object value, boolean flush) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (value == null) {
            this.properties.remove(key);
        } else {
            this.properties.setProperty(key, value.toString());
        }
        if (flush && isSaveable()) {
            store();
        }
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public void set(String key, Object value) {
        set(key, value, true);
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public void set(ConfigEnum key, Object value) {
        set(key.name(), value);
    }

    public void set(Map<String, Object> map, boolean flush) {
        for (Map.Entry<String, Object> en : map.entrySet()) {
            String key = en.getKey();
            Object value = en.getValue();
            if (value == null) {
                this.properties.remove(key);
            } else {
                this.properties.setProperty(key, value.toString());
            }
        }
        if (flush && isSaveable()) {
            store();
        }
    }

    public void set(Map<String, Object> map) {
        set(map, false);
    }

    public Set<String> getKeys() {
        Set<String> set = new HashSet<>();
        for (Object obj : this.properties.keySet()) {
            set.add(getStringOf(obj));
        }
        return Collections.unmodifiableSet(set);
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public String getDefault(String key) {
        return null;
    }

    public int getInteger(String key, int def) {
        return getIntegerOf(get(key), 0);
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public int getInteger(String key) {
        return getInteger(key, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getIntegerOf(Object obj, int def) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public double getDouble(String key) {
        return getDoubleOf(get(key), 0.0d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public double getDoubleOf(Object obj, double def) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public float getFloat(String key) {
        return getFloatOf(get(key), 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getFloatOf(Object obj, float def) {
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public long getLong(String key) {
        return getLongOf(get(key), 0L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getLongOf(Object obj, long def) {
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public boolean getBoolean(String key) {
        return getBooleanOf(get(key), false);
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public boolean getBoolean(ConfigEnum key) {
        return getBoolean(key.name());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean getBooleanOf(Object obj, boolean def) {
        try {
            return StringUtil.parseBoolean(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public int getDefaultInteger(String key) {
        return 0;
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public double getDefaultDouble(String key) {
        return 0.0d;
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public float getDefaultFloat(String key) {
        return 0.0f;
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public long getDefaultLong(String key) {
        return 0L;
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public boolean getDefaultBoolean(String key) {
        return false;
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public void save() throws IOException {
        if (!isSaveable()) {
            throw new UnsupportedOperationException();
        }
        File file = (File) this.input;
        this.properties.store(new FileOutputStream(file), this.comments);
    }

    public void store() {
        try {
            save();
        } catch (IOException e) {
            log("Cannot store values!", e);
        }
    }

    @Override // org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public void clear() {
        this.properties.clear();
    }

    public boolean isSaveable() {
        return this.input instanceof File;
    }

    private static void loadFromStream(Properties properties, InputStream stream) throws IOException {
        if (stream == null) {
            throw new NullPointerException();
        }
        Reader reader = new InputStreamReader(new BufferedInputStream(stream), Charset.forName("UTF-8"));
        properties.clear();
        properties.load(reader);
        reader.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Properties loadFromStream(InputStream stream) throws IOException {
        Properties properties = new Properties();
        loadFromStream(properties, stream);
        return properties;
    }

    private static void loadFromFile(Properties properties, File file) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }
        FileInputStream stream = new FileInputStream(file);
        loadFromStream(properties, stream);
    }

    protected static Properties loadFromFile(File file) throws IOException {
        Properties properties = new Properties();
        loadFromFile(properties, file);
        return properties;
    }

    private static void loadFromURL(Properties properties, URL url) throws IOException {
        if (url == null) {
            throw new NullPointerException();
        }
        InputStream connection = url.openStream();
        loadFromStream(properties, connection);
    }

    protected static Properties loadFromURL(URL url) throws IOException {
        Properties properties = new Properties();
        loadFromURL(properties, url);
        return properties;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void copyProperties(Properties src, Properties dest, boolean wipe) {
        if (src == null) {
            throw new NullPointerException("src is NULL");
        }
        if (dest == null) {
            throw new NullPointerException("dest is NULL");
        }
        if (wipe) {
            dest.clear();
        }
        for (Map.Entry<Object, Object> en : src.entrySet()) {
            String key = en.getKey() == null ? null : en.getKey().toString();
            String value = en.getKey() == null ? null : en.getValue().toString();
            dest.setProperty(key, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Properties copyProperties(Properties src) {
        Properties properties = new Properties();
        copyProperties(src, properties, false);
        return properties;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void log(Object... o) {
        U.log("[" + getClass().getSimpleName() + "]", o);
    }

    public boolean isUSSRLocale() {
        String locale = get("locale");
        return "ru_RU".equals(locale) || "uk_UA".equals(locale);
    }
}
