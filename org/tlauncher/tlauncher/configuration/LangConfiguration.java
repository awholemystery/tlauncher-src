package org.tlauncher.tlauncher.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/LangConfiguration.class */
public class LangConfiguration extends SimpleConfiguration {
    private final Locale[] locales;
    private final Properties[] prop;
    private int i;

    public LangConfiguration(Locale[] locales, Locale select, String folder) throws IOException {
        if (locales == null) {
            throw new NullPointerException();
        }
        int size = locales.length;
        this.locales = locales;
        this.prop = new Properties[size];
        for (int i = 0; i < size; i++) {
            Locale locale = locales[i];
            if (locale == null) {
                throw new NullPointerException("Locale at #" + i + " is NULL!");
            }
            String localeName = locale.toString();
            InputStream stream = getClass().getResourceAsStream(folder + localeName);
            if (stream == null) {
                throw new IOException("Cannot find locale file for: " + localeName);
            }
            this.prop[i] = loadFromStream(stream);
            if (localeName.equals("en_US")) {
                copyProperties(this.prop[i], this.properties, true);
            }
        }
        int defLocale = -1;
        int i2 = 0;
        while (true) {
            if (i2 < size) {
                if (!locales[i2].toString().equals("ru_RU")) {
                    i2++;
                } else {
                    defLocale = i2;
                    break;
                }
            } else {
                break;
            }
        }
        if (defLocale != -1) {
            for (Object key : this.prop[defLocale].keySet()) {
                for (int i3 = 0; i3 < size; i3++) {
                    if (i3 != defLocale && !this.prop[i3].containsKey(key) && !TLauncher.DEBUG) {
                        U.log("Locale", locales[i3], "doesn't contain key", key);
                    }
                }
            }
            for (int i4 = 0; i4 < size; i4++) {
                if (i4 != defLocale) {
                    int numberLine = 0;
                    for (Object key2 : this.prop[i4].keySet()) {
                        numberLine++;
                        if (!this.prop[defLocale].containsKey(key2)) {
                            U.log("Locale", locales[i4], "contains redundant key line is " + numberLine + ", key is " + key2);
                        }
                    }
                }
            }
        }
        setSelected(select);
    }

    public Locale[] getLocales() {
        return this.locales;
    }

    public Locale getSelected() {
        return this.locales[this.i];
    }

    public void setSelected(Locale select) {
        if (select == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < this.locales.length; i++) {
            if (this.locales[i].equals(select)) {
                this.i = i;
                return;
            }
        }
        throw new IllegalArgumentException("Cannot find Locale:" + select);
    }

    public String nget(String key) {
        if (key == null) {
            return null;
        }
        String value = this.prop[this.i].getProperty(key);
        if (value == null) {
            return getDefault(key);
        }
        return value;
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public String get(String key) {
        String value = nget(key);
        if (value == null) {
            return key;
        }
        return value;
    }

    public String nget(String key, Object... vars) {
        String value = get(key);
        if (value == null) {
            return null;
        }
        String[] variables = checkVariables(vars);
        for (int i = 0; i < variables.length; i++) {
            value = value.replace("%" + i, variables[i]);
        }
        return value;
    }

    public String ngetKeys(String key, Object... keys) {
        String value = get(key);
        if (value == null) {
            return null;
        }
        String[] variables = checkVariables(keys);
        for (int i = 0; i < variables.length; i++) {
            value = value.replace("%" + i, Localizable.get(variables[i]));
        }
        return value;
    }

    public String get(String key, Object... vars) {
        String value = nget(key, vars);
        if (value == null) {
            return key;
        }
        return value;
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public void set(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.tlauncher.tlauncher.configuration.SimpleConfiguration, org.tlauncher.tlauncher.configuration.AbstractConfiguration
    public String getDefault(String key) {
        return super.get(key);
    }

    private static String[] checkVariables(Object[] check) {
        if (check == null) {
            throw new NullPointerException();
        }
        if (check.length == 1 && check[0] == null) {
            return new String[0];
        }
        String[] string = new String[check.length];
        for (int i = 0; i < check.length; i++) {
            if (check[i] == null) {
                throw new NullPointerException("Variable at index " + i + " is NULL!");
            }
            string[i] = check[i].toString();
        }
        return string;
    }
}
