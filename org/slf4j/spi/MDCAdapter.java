package org.slf4j.spi;

import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/slf4j/spi/MDCAdapter.class */
public interface MDCAdapter {
    void put(String str, String str2);

    String get(String str);

    void remove(String str);

    void clear();

    Map getCopyOfContextMap();

    void setContextMap(Map map);
}
