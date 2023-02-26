package org.slf4j.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.MDC;
import org.slf4j.spi.MDCAdapter;

/* loaded from: TLauncher-2.876.jar:org/slf4j/impl/Log4jMDCAdapter.class */
public class Log4jMDCAdapter implements MDCAdapter {
    @Override // org.slf4j.spi.MDCAdapter
    public void clear() {
        Map map = MDC.getContext();
        if (map != null) {
            map.clear();
        }
    }

    @Override // org.slf4j.spi.MDCAdapter
    public String get(String key) {
        return (String) MDC.get(key);
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void put(String key, String val) {
        MDC.put(key, val);
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void remove(String key) {
        MDC.remove(key);
    }

    @Override // org.slf4j.spi.MDCAdapter
    public Map getCopyOfContextMap() {
        Map old = MDC.getContext();
        if (old != null) {
            return new HashMap(old);
        }
        return null;
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void setContextMap(Map contextMap) {
        Map old = MDC.getContext();
        if (old == null) {
            for (Map.Entry mapEntry : contextMap.entrySet()) {
                MDC.put((String) mapEntry.getKey(), mapEntry.getValue());
            }
            return;
        }
        old.clear();
        old.putAll(contextMap);
    }
}
