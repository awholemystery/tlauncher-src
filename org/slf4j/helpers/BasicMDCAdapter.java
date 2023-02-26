package org.slf4j.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.spi.MDCAdapter;

/* loaded from: TLauncher-2.876.jar:org/slf4j/helpers/BasicMDCAdapter.class */
public class BasicMDCAdapter implements MDCAdapter {
    private InheritableThreadLocal inheritableThreadLocal = new InheritableThreadLocal();
    static boolean IS_JDK14 = isJDK14();

    static boolean isJDK14() {
        try {
            String javaVersion = System.getProperty("java.version");
            return javaVersion.startsWith("1.4");
        } catch (SecurityException e) {
            return false;
        }
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void put(String key, String val) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        Map map = (Map) this.inheritableThreadLocal.get();
        if (map == null) {
            map = Collections.synchronizedMap(new HashMap());
            this.inheritableThreadLocal.set(map);
        }
        map.put(key, val);
    }

    @Override // org.slf4j.spi.MDCAdapter
    public String get(String key) {
        Map Map = (Map) this.inheritableThreadLocal.get();
        if (Map != null && key != null) {
            return (String) Map.get(key);
        }
        return null;
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void remove(String key) {
        Map map = (Map) this.inheritableThreadLocal.get();
        if (map != null) {
            map.remove(key);
        }
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void clear() {
        Map map = (Map) this.inheritableThreadLocal.get();
        if (map != null) {
            map.clear();
            if (isJDK14()) {
                this.inheritableThreadLocal.set(null);
            } else {
                this.inheritableThreadLocal.remove();
            }
        }
    }

    public Set getKeys() {
        Map map = (Map) this.inheritableThreadLocal.get();
        if (map != null) {
            return map.keySet();
        }
        return null;
    }

    @Override // org.slf4j.spi.MDCAdapter
    public Map getCopyOfContextMap() {
        Map oldMap = (Map) this.inheritableThreadLocal.get();
        if (oldMap != null) {
            Map newMap = Collections.synchronizedMap(new HashMap());
            synchronized (oldMap) {
                newMap.putAll(oldMap);
            }
            return newMap;
        }
        return null;
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void setContextMap(Map contextMap) {
        Map map = Collections.synchronizedMap(new HashMap(contextMap));
        this.inheritableThreadLocal.set(map);
    }
}
