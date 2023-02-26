package org.tlauncher.tlauncher.configuration;

import java.io.IOException;
import org.tlauncher.tlauncher.entity.ConfigEnum;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/AbstractConfiguration.class */
public interface AbstractConfiguration {
    String get(String str);

    int getInteger(String str);

    double getDouble(String str);

    float getFloat(String str);

    long getLong(String str);

    boolean getBoolean(String str);

    boolean getBoolean(ConfigEnum configEnum);

    String getDefault(String str);

    int getDefaultInteger(String str);

    double getDefaultDouble(String str);

    float getDefaultFloat(String str);

    long getDefaultLong(String str);

    boolean getDefaultBoolean(String str);

    void set(String str, Object obj);

    void set(ConfigEnum configEnum, Object obj);

    void clear();

    void save() throws IOException;
}
